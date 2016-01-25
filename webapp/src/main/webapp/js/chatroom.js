function ChatController(eventBus, tokenContainer) {

    var repeatTimer = false;
    var chatService = new ChatService(eventBus);

    eventBus.registerConsumer(EventBusMessages.CHAT_LOADED, onViewLoaded);

    eventBus.registerConsumer(EventBusMessages.JOIN_CHAT_ROOM, function (chatRoomId) {
        restService.put('/chats/' + chatRoomId + '/' + ChatRoomStorage.token.userId,
            {"token": ChatRoomStorage.token.token}, joinChatRoom);
    });

    function onViewLoaded() {
        if (!ChatRoomStorage.isLoggedIn) {
            return;
        }

        restService.get('/chat/chats/' + ChatRoomStorage.token.userId + '?token=' + ChatRoomStorage.token.token, function (data) {
            eventBus.sendMessage(EventBusMessages.CHAT_ROOM_LIST_UPDATED, data);
        });
    }

    function joinChatRoom(data) {
        if(ChatRoomStorage.selectedChatRoomId){
            ChatRoomStorage.joinedChatRooms[ChatRoomStorage.selectedChatRoomId].active = false;
        }

        ChatRoomStorage.joinedChatRooms[data.id] = {
            "chatRoomId": data.id,
            "name": data.name,
            "messages" : [],
            "users" : [],
            "active": true
        };
        eventBus.sendMessage(EventBusMessages.UPDATE_JOINED_CHAT_ROOM_LIST, ChatRoomStorage.joinedChatRooms);
    }

    function onAuthenticationError(){
        eventBus.sendMessage(EventBusMessages.USER_LOGGED_OUT);
    }

    function destroy() {
        repeatTimer = false;
    }

    function init(){
        chatService.readUserProfile(tokenContainer.token.userId, tokenContainer.token, function(){},
            onAuthenticationError, onAuthenticationError)
    }

    return {
        "init" : init(),
        "destroy" : destroy()
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

    var userList = element.find('#user-list');
    var userListPattern = userList.html();
    userList.empty();

    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_LIST_UPDATED, function (chatRooms) {
        chatRoomSelector.empty();
        for (var i = 0; i < chatRooms.length; i++) {
            var newElement = chatRoomSelectorPattern;
            for (var key in chatRooms[i]) {
                newElement = newElement.replace('{{' + key + '}}', chatRooms[i][key]);
            }
            chatRoomSelector.append(newElement);
        }
    });

    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_USER_LIST_UPDATED, function (users) {
        userList.empty();
        for (var i = 0; i < chatRooms.length; i++) {
            var newElement = userListPattern;
            for (var key in chatRooms[i]) {
                newElement = newElement.replace('{{' + key + '}}', users[i][key]);
            }
            userList.append(newElement);
        }
    });

    joinChatRoomBtn.click(function () {
        var chatRoomId = chatRoomSelector.val();
        eventBus.sendMessage(EventBusMessages.JOIN_CHAT_ROOM, chatRoomId);
    });

    sendMessageButton.click(function () {
        var messageText = messageTextArea.val();
        eventBus.sendMessage(EventBusMessages.SEND_MESSAGE, messageText);
    });
}

function ChatService(eventBus){
    var restService = new RestService(eventBus);

    function _readUserProfile(userId, token, success, error, authenticationError){
        restService.get('/chat/users/' + token.userId + '/' + userId + '?token=' + token.token,
            success, error, authenticationError);
    }

    return {
        "readUserProfile" : _readUserProfile
    }
}