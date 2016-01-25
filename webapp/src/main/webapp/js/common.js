function ChatMessage(sender, text) {
    this.sender = sender;
    this.text = text;
}

var EventBusMessages = {
    "UPDATE_APPLICATION_VIEW": 'UPDATE_APPLICATION_VIEW',
    "PAGE_CLOSED": 'PAGE_CLOSED',
    "CHAT_LOADED": 'CHAT_LOADED',
    "USER_LOGGED_OUT": 'USER_LOGGED_OUT',

    "USER_LOGGED_IN" : 'USER_LOGGED_IN',
    "LOGIN_USER": 'LOGIN_USER',

    "CHAT_ROOM_LIST_UPDATED" : "CHAT_ROOM_LIST_UPDATED",

    "AUTHENTICATION_ERROR": 'AUTHENTICATION_ERROR'
}

var ChatRoomStorage = function () {

    this.messages = [];
    this.chatRooms = [];
    this.token = {};
    this.isLoggedIn = false;

}