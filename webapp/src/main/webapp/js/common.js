function TokenContainer(){
    this.token = {
        "token" : '',
        "userId" : ''
    };
}

var ChatConstants = {
    "CHAT_READER_DELAY" : 5000
}

var EventBusMessages = {
    "UPDATE_APPLICATION_VIEW": 'UPDATE_APPLICATION_VIEW',
    "CHAT_LOADED": 'CHAT_LOADED',
    "USER_LOGGED_OUT": 'USER_LOGGED_OUT',

    "USER_LOGGED_IN" : 'USER_LOGGED_IN',
    "LOGIN_USER": 'LOGIN_USER',

    "CHAT_ROOM_LIST_UPDATED" : 'CHAT_ROOM_LIST_UPDATED',
    "JOIN_CHAT_ROOM" : 'JOIN_CHAT_ROOM',
    "JOINED_TO_CHAT_ROOM" : 'JOINED_TO_CHAT_ROOM',
    "CHAT_ROOM_SELECTED" : 'CHAT_ROOM_SELECTED',

    "CHAT_ROOM_USER_LIST_UPDATED" : 'CHAT_ROOM_USER_LIST_UPDATED',
    "CHAT_ROOM_MESSAGES_UPDATED" : 'CHAT_ROOM_MESSAGES_UPDATED',
    "SEND_MESSAGE" : 'SEND_MESSAGE',

    "LEAVE_CHAT_ROOM": 'LEAVE_CHAT_ROOM',
    "CHAT_ROOM_LEFT": 'CHAT_ROOM_LEFT',

    "REGISTER_USER" : 'REGISTER_USER',
    "USER_REGISTERED" : 'USER_REGISTERED'

}