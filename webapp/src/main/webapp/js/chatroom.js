function ChatService(eventBus) {

    var repeatTimer = false;

    var restService = new RestService(eventBus);

    eventBus.registerConsumer(EventBusMessages.CHAT_LOADED, init);

    eventBus.registerConsumer(EventBusMessages.PAGE_CLOSED, function (hash) {
        if (hash == '#chat') {
            close();
        }
    });

    function init() {
        if (!ChatRoomStorage.isLoggedIn) {
            eventBus.sendMessage(EventBusMessages.USER_LOGGED_OUT);
            return;
        }

        restService.get('/chat/chats/' + ChatRoomStorage.token.userId + '?token=' + ChatRoomStorage.token.token, function(data){
            eventBus.sendMessage(EventBusMessages.CHAT_ROOM_LIST_UPDATED, data);
        });

    }

    function close() {
        repeatTimer = false;
    }
}

function ChatView(eventBus, element) {
    eventBus.sendMessage(EventBusMessages.CHAT_LOADED);

    var chatRoomSelector = element.find('#chat-room-selector');
    var chatRoomSelectorPattern = chatRoomSelector.html();


    eventBus.registerConsumer(EventBusMessages.CHAT_ROOM_LIST_UPDATED, function(chatRooms){
        chatRoomSelector.empty();
        for(var i=0; i<chatRooms.length; i++){
            var newElement = chatRoomSelectorPattern;
            for(var key in chatRooms[i]){
                newElement = newElement.replace('{{' + key + '}}', chatRooms[i][key]);
            }
            chatRoomSelector.append(newElement);
        }
    });

}