function ChatView(eventBus, element) {
    eventBus.sendMessage(EventBusMessages.CHAT_LOADED);

    var userNameContainer = element.find('#user-name-span');

    var chatRoomSelector = element.find('#chat-room-selector');
    var chatRoomSelectorPattern = chatRoomSelector.html();
    chatRoomSelector.empty();

    var logoutBtn = element.find('#logout-btn');
    var joinChatRoomBtn = element.find('#join-chat-room-btn');
    var messageTextArea = element.find('#message-text');
    var sendMessageButton = element.find('#send-message-btn');

    var tabsContainer = element.find("#tabs-container");
    var tabsContainerPattern = tabsContainer.html();
    tabsContainer.empty();

    var _userList = element.find('#user-list');
    var userListPattern = _userList.html();
    _userList.empty();

    var _messageList = element.find('#message-list');
    var messageListPattern = _messageList.html();
    _messageList.empty();

    var chatRoomContainer = element.find('#chat-room-container');
    var chatRoomPattern = chatRoomContainer.html();
    chatRoomContainer.empty();

    eventBus.registerConsumer(EventBusMessages.USER_PROFILE_UPDATED, function(userProfile){
        userNameContainer.html(userProfile.name);
    })

    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_LIST_UPDATED, function (chatRooms) {
        chatRoomSelector.empty();
        for (var i = 0; i < chatRooms.length; i++) {
            var newOption = createElementFromTemplate(chatRoomSelectorPattern, chatRooms[i]);
            chatRoomSelector.append(newOption);
        }
    });

    eventBus.registerConsumer(EventBusMessages.JOINED_CHAT_ROOM_LIST_UPDATED, function(chatRooms){
        tabsContainer.empty();
        chatRoomContainer.empty();
        for(var i = 0; i < chatRooms.length; i++){
            addChatRoom(chatRooms[i]);
        }
    });

    eventBus.registerConsumer(EventBusMessages.JOINED_TO_CHAT_ROOM, addChatRoom);

    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_LEFT, function(chatId){
        $(tabsContainer.find('*[chat-id="' + chatId + '"]').get(0)).detach();
        chatRoomContainer.find('#chat_' + chatId).detach();
    });

    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_USER_LIST_UPDATED, function (users) {
        var selectedTab = tabsContainer.find("li.active");
        var chatId = selectedTab.attr("chat-id");

        var userIdList = [];
        chatRoomContainer.('#chat_' + chatId + ' #user-list input').each(function (el) {
            if ($(this).val()) {
                userIdList.push($(this).val());
            }
        });


        //update, add new users
        for (var i = 0; i < users.length; i++) {
            var newElement = userListPattern;
            for (var key in users[i]) {
                newElement = newElement.replace(new RegExp('{{' + key + '}}', 'g'), users[i][key]);
            }
            if (!chatRoomContainer.('#chat_' + chatId + ' #user-list #user_' + users[i].id).length) {
                chatRoomContainer.('#chat_' + chatId + ' #user-list').append(newElement);
            }
        }

        //remove users not in list
        for(var i=0; i<userIdList.length; i++){
            var userExists = false;
            for(var j = 0; j < users.length; j++){
                if(userIdList[i] == users[j].id){
                    userExists = true; break;
                }
            }
            if(!userExists){
                chatRoomContainer.('#chat_' + chatId + ' #user-list #user_' + userIdList[i]).detach();
            }
        }
    });

    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_MESSAGES_UPDATED, function (messages) {
        var selectedTab = tabsContainer.find("li.active");
        var chatId = selectedTab.attr("chat-id");

        for (var i = 0; i < messages.length; i++) {
            var date = new Date(messages[i].date);
            messages[i].formatedTime = date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
            messages[i].formatedToUserName = '<b>' + messages[i].toUserName + '</b> ,';

            var newElement = createElementFromTemplate(messageListPattern, messages[i]);
            messageList.append(newElement);
        }
    });

    logoutBtn.click(function(){
        eventBus.sendMessage(EventBusMessages.LOGOUT_USER);
    });

    joinChatRoomBtn.click(function () {
        var chatRoomId = chatRoomSelector.val();
        eventBus.sendMessage(EventBusMessages.JOIN_CHAT_ROOM, chatRoomId);
    });

    sendMessageButton.click(function () {
        var selectedTab = tabsContainer.find("li.active");
        if(selectedTab.length <= 0){
            return;
        }
        var chatId = selectedTab.attr("chat-id");
        var selectedUsers = [];
        chatRoomContainer.('#chat_' + chatId + ' #user-list input:checked').each(function (el) {
            if ($(this).val()) {
                selectedUsers.push($(this).val());
            }
        });

        var messageText = messageTextArea.val();
        messageTextArea.val('');
        eventBus.sendMessage(EventBusMessages.SEND_MESSAGE, {
            chatId : chatId,
            message: messageText,
            users: selectedUsers
        });
    });

    function onLeaveChatRoom(event) {
        var chatId = $(event.currentTarget).attr("chat-id");
        eventBus.sendMessage(EventBusMessages.LEAVE_CHAT_ROOM, chatId);
        event.stopPropagation();
        return false;
    }

    function addChatRoom(chatRoom){
        var tab = createElementFromTemplate(tabsContainerPattern, chatRoom);
        var chatElement = createElementFromTemplate(chatRoomPattern, chatRoom);

        tab.find("#leave-chat-room-btn").click(onLeaveChatRoom);

        tabsContainer.append(tab);
        chatRoomContainer.append(chatElement);
    }

    function createElementFromTemplate(template, data){
        var newElement = template;
        for (var key in data) {
            newElement = newElement.replace(new RegExp('{{' + key + '}}', 'g'), data[key]);
        }
        return $(newElement);
    }

}