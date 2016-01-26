function ChatController(eventBus, tokenContainer) {

    var repeatTimer = false;
    var joinedChatRooms = [];

    var activeChatRoom = null;
    var lastMessageTime = -1;

    var chatService = new ChatService();

    eventBus.registerConsumer(EventBusMessages.CHAT_LOADED, onViewLoaded);

    eventBus.registerConsumer(EventBusMessages.JOIN_CHAT_ROOM, function (chatRoomId) {
        chatService.joinChatRoom(chatRoomId, tokenContainer.token, joinChatRoom,
            onChatError, onChatError);
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
            }, onChatError, onChatError);
        }
    });

    eventBus.registerConsumer(EventBusMessages.LEAVE_CHAT_ROOM, function(chatRoomId){
        chatService.leaveChatRoom(chatRoomId, tokenContainer.token, function(){

            if(activeChatRoom == chatRoomId){
                repeatTimer = false;
            }
            eventBus.sendMessage(EventBusMessages.CHAT_ROOM_LEFT, chatRoomId);

        }, onChatError, onChatError);
    });

    eventBus.registerConsumer(EventBusMessages.LOGOUT_USER, logout);

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
        joinedChatRooms[chatRoom.id] = {
            id : chatRoom.id,
            lastMessageTime : -1
        };
        eventBus.sendMessage(EventBusMessages.JOINED_TO_CHAT_ROOM, chatRoom);
    }

    function onChatError(errorMessage) {
        repeatTimer = false;
        alert(errorMessage);
        logout();
    }

    function logout() {
        chatService.logout(token, function(){}, function(){}, function(){});
        repeatTimer = false;
        eventBus.sendMessage(EventBusMessages.USER_LOGGED_OUT);
    }

    function init(){

    }

    function destroy() {
        logout();
    }

    function readChatRoomContent(notInTimer) {

        console.log("messages load" + activeChatRoom);

        chatService.readChatRoomUserList(activeChatRoom, tokenContainer.token, function (data) {
            eventBus.sendMessage(EventBusMessages.CHAT_ROOM_USER_LIST_UPDATED, data);
        }, onChatError, onChatError);

        chatService.readChatMessages(activeChatRoom, lastMessageTime, tokenContainer.token, function (messages) {
            if(messages.length > 0){
                lastMessageTime = messages[messages.length-1].date;
            }
            eventBus.sendMessage(EventBusMessages.CHAT_ROOM_MESSAGES_UPDATED, messages);
        }, onChatError, onChatError);

        if (!notInTimer && repeatTimer) {
            setTimeout(readChatRoomContent, ChatConstants.CHAT_READER_DELAY);
        }
    }

    return {
        "init": init,
        "destroy": destroy
    };
}
