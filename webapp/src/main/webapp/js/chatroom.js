function ChatController(eventBus, tokenContainer) {

    var repeatTimer = false;
    var activeChatRoom = null;
    var lastMessageTime = -1;
    var chatService = new ChatService();

    eventBus.registerConsumer(EventBusMessages.CHAT_LOADED, onViewLoaded);

    eventBus.registerConsumer(EventBusMessages.JOIN_CHAT_ROOM, function (chatRoomId) {
        chatService.joinChatRoom(chatRoomId, tokenContainer.token, joinChatRoom,
            onAuthenticationError, onAuthenticationError);
    });

    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_SELECTED, function (chatRoomId) {
        if (!repeatTimer) {
            repeatTimer = true;
            setTimeout(readChatRoomContent, ChatConstants.CHAT_READER_DELAY);
        }
        activeChatRoom = chatRoomId;
        lastMessageTime = -1;
        readChatRoomContent(true);
    });

    eventBus.registerConsumer(EventBusMessages.SEND_MESSAGE, function (message) {
        if (activeChatRoom) {
            chatService.postMessage(message.message, message.users, activeChatRoom, tokenContainer.token, function (data) {
                readChatRoomContent(true);
            }, onAuthenticationError, onAuthenticationError);
        }
    });

    eventBus.registerConsumer(EventBusMessages.LEAVE_CHAT_ROOM, function(chatRoomId){
        chatService.leaveChatRoom(chatRoomId, tokenContainer.token, function(){

            if(activeChatRoom == chatRoomId){
                repeatTimer = false;
            }
            eventBus.sendMessage(EventBusMessages.CHAT_ROOM_LEFT, chatRoomId);

        }, onAuthenticationError, onAuthenticationError);
    });

    function onViewLoaded() {
        if (!tokenContainer.token.userId || !tokenContainer.token.token) {
            logout();
            return;
        }
        chatService.readChatRooms(tokenContainer.token, function (data) {
            eventBus.sendMessage(EventBusMessages.CHAT_ROOM_LIST_UPDATED, data);
        }, onAuthenticationError);
    }

    function joinChatRoom(data) {
        eventBus.sendMessage(EventBusMessages.JOINED_TO_CHAT_ROOM, data);
    }

    function onAuthenticationError() {
        logout();
    }

    function logout() {
        repeatTimer = false;
        eventBus.sendMessage(EventBusMessages.USER_LOGGED_OUT);
    }

    function destroy() {
        logout();
    }

    function init() {
        if (!tokenContainer.token.userId || !tokenContainer.token.token) {
            logout();
            return;
        }

        chatService.readUserProfile(tokenContainer.token.userId, tokenContainer.token, function () {},
            onAuthenticationError, onAuthenticationError);
    }

    function readChatRoomContent(notInTimer) {

        console.log("there should be messages load for chatroom: " + activeChatRoom);

        chatService.readChatRoomUserList(activeChatRoom, tokenContainer.token, function (data) {
            eventBus.sendMessage(EventBusMessages.CHAT_ROOM_USER_LIST_UPDATED, data);
        }, onAuthenticationError, onAuthenticationError);

        chatService.readChatMessages(activeChatRoom, lastMessageTime, tokenContainer.token, function (messages) {
            if(messages.length > 0){
                lastMessageTime = messages[messages.length-1].date;
            }
            eventBus.sendMessage(EventBusMessages.CHAT_ROOM_MESSAGES_UPDATED, messages);
        }, onAuthenticationError, onAuthenticationError);

        if (!notInTimer && repeatTimer) {
            setTimeout(readChatRoomContent, ChatConstants.CHAT_READER_DELAY);
        }
    }

    return {
        "init": init,
        "destroy": destroy
    };
}

function ChatView(eventBus, element) {
    eventBus.sendMessage(EventBusMessages.CHAT_LOADED);

    var chatRoomSelector = element.find('#chat-room-selector');
    var chatRoomSelectorPattern = chatRoomSelector.html();
    chatRoomSelector.empty();

    var joinChatRoomBtn = element.find('#join-chat-room-btn');

    var messageTextArea = element.find('#message-text');
    var sendMessageButton = element.find('#send-message-btn');

    var tabsContainer = element.find("#tabs-container");
    var tabsContainerPattern = tabsContainer.html();
    tabsContainer.empty();
    var selectedTab = null;

    var userList = element.find('#user-list');
    var userListPattern = userList.html();
    userList.empty();

    var messageList = element.find('#message-list');
    var messageListPattern = messageList.html();
    messageList.empty();

    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_LIST_UPDATED, function (chatRooms) {
        chatRoomSelector.empty();
        for (var i = 0; i < chatRooms.length; i++) {
            var newElement = chatRoomSelectorPattern;
            for (var key in chatRooms[i]) {
                newElement = newElement.replace(new RegExp('{{' + key + '}}', 'g'), chatRooms[i][key]);
            }
            chatRoomSelector.append(newElement);
        }
    });

    eventBus.registerConsumer(EventBusMessages.JOINED_TO_CHAT_ROOM, function (chatRoom) {
        var newElement = tabsContainerPattern;
        for (var key in chatRoom) {
            newElement = newElement.replace(new RegExp('{{' + key + '}}', 'g'), chatRoom[key]);
        }
        var tab = $(newElement);
        tab.click(function (event) {
            onSelectChatRoom(tab);
            event.stopPropagation();
        });
        onSelectChatRoom(tab);
        tab.find("#leave-chat-room-btn").click(onLeaveChatRoom);
        tabsContainer.append(tab);
    });

    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_USER_LIST_UPDATED, function (users) {
        for (var i = 0; i < users.length; i++) {
            var newElement = userListPattern;
            for (var key in users[i]) {
                newElement = newElement.replace(new RegExp('{{' + key + '}}', 'g'), users[i][key]);
            }
            if (!userList.find("#user_" + users[i].id).length) {
                userList.append(newElement);
            }
        }
    });

    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_LEFT, function(chatId){
        var tab = $(tabsContainer.find('*[chat-id="1"]').get(0));
        tab.detach();

        if (selectedTab && selectedTab.attr("chat-id") == tab.attr('chat-id')) {
            userList.empty();
            messageList.empty();
        }
    });

    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_MESSAGES_UPDATED, function (messages) {
        for (var i = 0; i < messages.length; i++) {
            var newElement = messageListPattern;
            for (var key in messages[i]) {
                newElement = newElement.replace(new RegExp('{{' + key + '}}', 'g'), messages[i][key]);
            }
            var date = new Date(messages[i].date);
            var formattedTime = date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
            newElement = newElement.replace(new RegExp('{{time}}', 'g'), formattedTime);
            var toUser = '';
            if(messages[i].privateMessage){
                toUser = '<b>' + messages[i].toUserName + '</b> ,';
            }
            newElement = newElement.replace(new RegExp('{{toUser}}', 'g'), toUser);
            messageList.append(newElement);
        }
    });

    joinChatRoomBtn.click(function () {
        var chatRoomId = chatRoomSelector.val();
        eventBus.sendMessage(EventBusMessages.JOIN_CHAT_ROOM, chatRoomId);
    });

    sendMessageButton.click(function () {
        var selectedUsers = [];
        userList.find('input:checked').each(function (el) {
            if ($(this).val()) {
                selectedUsers.push($(this).val());
            }
        });
        var messageText = messageTextArea.val();
        messageTextArea.html('');
        eventBus.sendMessage(EventBusMessages.SEND_MESSAGE, {message: messageText, users: selectedUsers});
    });

    function onLeaveChatRoom(event) {
        var chatId = $(event.currentTarget).attr("chat-id");
        eventBus.sendMessage(EventBusMessages.LEAVE_CHAT_ROOM, chatId);
        event.stopPropagation();
    }

    function onSelectChatRoom(tab) {
        if (selectedTab && selectedTab.attr("chat-id") == tab.attr('chat-id')) {
            return;
        }
        if (selectedTab) {
            selectedTab.removeClass("active");
        }
        messageList.empty();
        tab.addClass("active");
        selectedTab = tab;
        eventBus.sendMessage(EventBusMessages.CHAT_ROOM_SELECTED, selectedTab.attr("chat-id"));
    }

}

function ChatService() {
    var restService = new RestService();

    function _readUserProfile(userId, token, success, error, authenticationError) {
        restService.get('/chat/users/' + token.userId + '/' + userId + '?token=' + token.token,
            success, error, authenticationError);
    }

    function _readChatRooms(token, success, authenticationError) {
        restService.get('/chat/chats/' + token.userId + '?token=' + token.token,
            success, authenticationError, authenticationError);
    }

    function _joinChatRoom(chatId, token, success, error, authenticationError) {
        restService.put('/chat/chats/' + chatId + '/' + token.userId,
            {"token": token.token}, success, error, authenticationError);
    }

    function _readChatRoomUserList(chatId, token, success, error, authenticationError) {
        restService.get('/chat/chats/' + chatId + '/' + token.userId + '?token=' + token.token,
            success, error, authenticationError);
    }

    function _readChatMessages(chatId, time, token, success, error, authenticationError) {
        restService.get('/chat/messages/' + chatId + '/' + token.userId + '/' + time + '?token=' + token.token,
            success, error, authenticationError);
    }

    function _postMessage(message, users, chatId, token, success, error, authenticationError) {
        var messageDTO = {"token": token.token, "message": message};
        if (users.length > 0) {
            for (var i = 0; i < users.length; i++) {
                restService.post('/chat/messages/' + chatId + '/' + token.userId + '/' + users[i], messageDTO,
                    success, error, authenticationError);
            }
        } else {
            restService.post('/chat/messages/' + chatId + '/' + token.userId, messageDTO,
                success, error, authenticationError);
        }
    }

    function _leaveChatRoom(chatId, token, success, error, authenticationError){
        restService.delete('/chat/chats/' + chatId + '/' + token.userId + 'token?=' + token.token,
            success, error, authenticationError);
    }

    function _logout(token, success, error, authenticationError){
        restService.delete('/chat/logout/' + token.userId + 'token?=' + token.token,
        success, error, authenticationError);
    }

    return {
        "readUserProfile": _readUserProfile,
        "readChatRooms": _readChatRooms,
        "joinChatRoom": _joinChatRoom,
        "readChatRoomUserList": _readChatRoomUserList,
        "readChatMessages": _readChatMessages,
        "postMessage": _postMessage,
        "leaveChatRoom" : _leaveChatRoom,
        "logout" : _logout
    }
}