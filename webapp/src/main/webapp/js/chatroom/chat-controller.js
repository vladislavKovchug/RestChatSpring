function ChatController(eventBus, tokenContainer) {

    var joinedChatRooms = [];
    var handleErrors = false;

    var chatService = new ChatService();
    var chatContentReaderService = new ChatContentReaderService(eventBus, joinedChatRooms);

    eventBus.registerConsumer(EventBusMessages.CHAT_LOADED, onViewLoaded);

    eventBus.registerConsumer(EventBusMessages.JOIN_CHAT_ROOM, function (chatRoomId) {
        chatService.joinChatRoom(chatRoomId, tokenContainer.token, joinChatRoom,
            onJoinCharRoomError, onChatError);
    });

    eventBus.registerConsumer(EventBusMessages.SEND_MESSAGE, function (sendMessageDTO) {
        chatService.postMessage(sendMessageDTO.message, sendMessageDTO.users, sendMessageDTO.chatId, tokenContainer.token, function () {}, onChatError, onChatError);
    });

    eventBus.registerConsumer(EventBusMessages.LEAVE_CHAT_ROOM, function(chatRoomId){
        chatService.leaveChatRoom(chatRoomId, tokenContainer.token, function(){
            eventBus.sendMessage(EventBusMessages.CHAT_ROOM_LEFT, chatRoomId);
        }, onChatError, onChatError);
    });

    eventBus.registerConsumer(EventBusMessages.UPDATE_CHAT_ROOM_MESSAGES, function(updateMessagesDTO){
        chatService.readChatMessages(updateMessagesDTO.chatRoomId, updateMessagesDTO.time, tokenContainer.token, function (items) {
            eventBus.sendMessage(EventBusMessages.CHAT_ROOM_MESSAGES_UPDATED, {
                "chatRoomId" : updateMessagesDTO.chatRoomId,
                "items" : items
            });
        }, onChatError, onChatError);
    });

    eventBus.registerConsumer(EventBusMessages.UPDATE_CHAT_ROOM_USER_LIST, function(chatRoomId){
        chatService.readChatRoomUserList(chatRoomId, tokenContainer.token, function (items) {
            eventBus.sendMessage(EventBusMessages.CHAT_ROOM_USER_LIST_UPDATED, {
                "chatRoomId" : chatRoomId,
                "items" : items
            });
        }, onChatError, onChatError);
    });

    eventBus.registerConsumer(EventBusMessages.LOGOUT_USER, logout);

    eventBus.registerConsumer(EventBusMessages.CHAT_ERROR_MESSAGE_SHOWED, logout);

    function onViewLoaded() {
        chatService.readUserProfile(tokenContainer.token.userId, tokenContainer.token, function (userProfile) {
                eventBus.sendMessage(EventBusMessages.USER_PROFILE_UPDATED, userProfile);
            },
            onChatError, onChatError);

        chatService.readJoinedChatList(tokenContainer.token, function (data) {
            eventBus.sendMessage(EventBusMessages.JOINED_CHAT_ROOM_LIST_UPDATED, data);
        }, onChatError, onChatError);

        chatService.readChatRooms(tokenContainer.token, function (data) {
            eventBus.sendMessage(EventBusMessages.CHAT_ROOM_LIST_UPDATED, data);
        }, onChatError);
    }

    function joinChatRoom(chatRoom) {
        eventBus.sendMessage(EventBusMessages.JOINED_TO_CHAT_ROOM, chatRoom);
    }

    function onChatError(errorMessage) {
        if(handleErrors){
            handleErrors = false;
            eventBus.sendMessage(EventBusMessages.SHOW_CHAT_ERROR_MESSAGE, errorMessage);
        }
    }

    function onJoinCharRoomError(errorMessage){
        eventBus.sendMessage(EventBusMessages.SHOW_JOIN_CHAT_ROOM_ERROR_MESSAGE, errorMessage);
    }

    function logout() {
        handleErrors = false;
        chatService.logout(tokenContainer.token, function(){}, function(){}, function(){});
        chatContentReaderService.stopTimer();
        eventBus.sendMessage(EventBusMessages.USER_LOGGED_OUT);
    }

    function init(){
        joinedChatRooms = [];
        handleErrors = true;
        chatContentReaderService.startTimer();
    }

    function destroy() {
        chatContentReaderService.stopTimer();
    }

    return {
        "init": init,
        "destroy": destroy
    };
}
