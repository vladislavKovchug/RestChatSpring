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

    function _readJoinedChatList(token, success, error, authenticationError){
        restService.get('/chat/users/chats/' + token.userId + '?token=' + token.token,
            success, error, authenticationError);
    }

    function _postMessage(message, users, chatId, token, success, error, authenticationError) {
        var messageDTO = {"token": token.token, "message": message};
        if (users.length > 0) {
            var responseList = [];
            var requestsCount = users.length;
            for (var i = 0; i < requestsCount; i++) {
                restService.post('/chat/messages/' + chatId + '/' + token.userId + '/' + users[i], messageDTO,
                    _multiplePostResponse(responseList, requestsCount, success), error, authenticationError);
            }
        } else {
            restService.post('/chat/messages/' + chatId + '/' + token.userId, messageDTO,
                success, error, authenticationError);
        }
    }

    function _leaveChatRoom(chatId, token, success, error, authenticationError){
        restService.delete('/chat/chats/' + chatId + '/' + token.userId + '?token=' + token.token,
            success, error, authenticationError);
    }

    function _logout(token, success, error, authenticationError){
        restService.delete('/chat/logout/' + token.userId + '?token=' + token.token,
            success, error, authenticationError);
    }

    function _multiplePostResponse(responseList, requestsCount, onSuccess){
        return function(responseData){
            responseList.push(responseData);
            if(responseList.length >= requestsCount){
                onSuccess(responseList);
            }
        }
    }

    return {
        "readUserProfile": _readUserProfile,
        "readChatRooms": _readChatRooms,
        "joinChatRoom": _joinChatRoom,
        "readChatRoomUserList": _readChatRoomUserList,
        "readChatMessages": _readChatMessages,
        "readJoinedChatList": _readJoinedChatList,
        "postMessage": _postMessage,
        "leaveChatRoom" : _leaveChatRoom,
        "logout" : _logout
    }
}