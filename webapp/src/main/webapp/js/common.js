function TokenContainer(){
    this.token = {
        "token" : '',
        "userId" : ''
    };
}

var EventBusMessages = {
    "UPDATE_APPLICATION_VIEW": 'UPDATE_APPLICATION_VIEW',
    "CHAT_LOADED": 'CHAT_LOADED',
    "USER_LOGGED_OUT": 'USER_LOGGED_OUT',

    "USER_LOGGED_IN" : 'USER_LOGGED_IN',
    "LOGIN_USER": 'LOGIN_USER',

    "CHAT_ROOM_LIST_UPDATED" : 'CHAT_ROOM_LIST_UPDATED',
    "JOIN_CHAT_ROOM" : 'JOIN_CHAT_ROOM',
    "CHAT_ROOM_USER_LIST_UPDATED" : 'CHAT_ROOM_USER_LIST_UPDATED',
    "UPDATE_JOINED_CHAT_ROOM_LIST" : 'UPDATE_JOINED_CHAT_ROOM_LIST',
    "SEND_MESSAGE" : 'SEND_MESSAGE',

    "AUTHENTICATION_ERROR": 'AUTHENTICATION_ERROR'
}

var ChatRoomStorage = function () {

    this.messages = [];
    this.selectedChatRoomId = null;
    this.joinedChatRooms = [];
    this.token = {};
    this.isLoggedIn = false;

}