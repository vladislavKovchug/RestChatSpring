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

    var joinChatRoomErrorMessage = element.find('#join-chat-room-error-message');
    joinChatRoomErrorMessage.hide();

    var chatErrorModalDialog = element.find('#chat-error-modal-dialog');

    eventBus.registerConsumer(EventBusMessages.USER_PROFILE_UPDATED, function(userProfile){
        userNameContainer.html(userProfile.name);
    });

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
        var chatId = users.chatRoomId;
        var items = users.items;

        var userIdList = [];
        chatRoomContainer.find('#chat_' + chatId + ' #user-list input').each(function (el) {
            if ($(this).val()) {
                userIdList.push($(this).val());
            }
        });

        //update, add new users
        for (var i = 0; i < items.length; i++) {
            var newElement = createElementFromTemplate(userListPattern, items[i]);
            if (!chatRoomContainer.find('#chat_' + chatId + ' #user-list #user_' + items[i].id).length) {
                chatRoomContainer.find('#chat_' + chatId + ' #user-list').append(newElement);
            }
        }

        //remove users not in list
        for(var i=0; i<userIdList.length; i++){
            var userExists = false;
            for(var j = 0; j < items.length; j++){
                if(userIdList[i] == items[j].id){
                    userExists = true; break;
                }
            }
            if(!userExists){
                chatRoomContainer.find('#chat_' + chatId + ' #user-list #user_' + userIdList[i]).detach();
            }
        }
    });

    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_MESSAGES_UPDATED, function (messages) {
        var chatId = messages.chatRoomId;
        var items = messages.items;

        for (var i = 0; i < items.length; i++) {
            var date = new Date(items[i].date);
            items[i].formatedTime = date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
            items[i].formatedToUserName = items[i].privateMessage ? '<b>' + items[i].toUserName + '</b> ,' : '';

            var newElement = createElementFromTemplate(messageListPattern, items[i]);
            chatRoomContainer.find('#chat_' + chatId + ' #message-list').append(newElement);
        }
    });

    eventBus.registerConsumer(EventBusMessages.SHOW_JOIN_CHAT_ROOM_ERROR_MESSAGE, function(message){
        joinChatRoomErrorMessage.html(message);
        joinChatRoomErrorMessage.alert();
        joinChatRoomErrorMessage.fadeTo(2000, 500).slideUp(500, function(){
            joinChatRoomErrorMessage.hide();
        });
    });

    eventBus.registerConsumer(EventBusMessages.SHOW_CHAT_ERROR_MESSAGE, function(message){
        chatErrorModalDialog.find('#modal-body').html(message);
        chatErrorModalDialog.modal('show');
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
        chatRoomContainer.find('#chat_' + chatId + ' #user-list input:checked').each(function (el) {
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

    chatErrorModalDialog.on('hidden.bs.modal', function () {
        eventBus.sendMessage(EventBusMessages.CHAT_ERROR_MESSAGE_SHOWED);
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
            if(!data.hasOwnProperty(key)) continue;
            newElement = newElement.replace(new RegExp('{{' + key + '}}', 'g'), data[key]);
        }
        return $(newElement);
    }

}