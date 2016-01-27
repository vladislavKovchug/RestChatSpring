function ChatContentReaderService(eventBus, joinedChatRooms){

    var repeatTimer = false;

    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_MESSAGES_UPDATED, function(messages){
        var chatRoomId = messages.chatRoomId;
        if(messages.items.length > 0){
            joinedChatRooms[chatRoomId].lastMessageTime = messages.items[messages.items.length - 1].date;
        }
    });

    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_LEFT, function(chatId){
        _removeChatRoom(chatId);
    });

    eventBus.registerConsumer(EventBusMessages.JOINED_CHAT_ROOM_LIST_UPDATED, function(chatRooms){
        for(var i=0; i<chatRooms.length; i++){
            _addChatRoom(chatRooms[i].id);
        }
    });

    eventBus.registerConsumer(EventBusMessages.JOINED_TO_CHAT_ROOM, function(chatRoom){
        _addChatRoom(chatRoom.id);
    });


    function timer(notInTimer){

        for(var chatRoomId in joinedChatRooms){
            if(!joinedChatRooms.hasOwnProperty(chatRoomId)){
                continue;
            }

            eventBus.sendMessage(EventBusMessages.UPDATE_CHAT_ROOM_MESSAGES, {
                "chatRoomId" : chatRoomId,
                "time" : joinedChatRooms[chatRoomId].lastMessageTime
            });

            eventBus.sendMessage(EventBusMessages.UPDATE_CHAT_ROOM_USER_LIST, chatRoomId);
        }

        if (!notInTimer && repeatTimer) {
            setTimeout(timer, ChatConstants.CHAT_READER_DELAY);
        }
    }

    function _addChatRoom(chatId){
        joinedChatRooms[chatId] = {
            "lastMessageTime" : -1
        }
    }

    function _removeChatRoom(chatId){
        delete this[chatId];
    }

    function _startTimer(){
        repeatTimer = true;
        setTimeout(timer, ChatConstants.CHAT_READER_DELAY);
    }

    function _stopTimer(){
        repeatTimer = false;
    }

    function _doTimerStep(){
        timer(true);
    }

    return{
        "startTimer" : _startTimer,
        "stopTimer" : _stopTimer,
        "doTimerStep" : _doTimerStep
    }
}