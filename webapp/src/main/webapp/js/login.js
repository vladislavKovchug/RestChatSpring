function LoginService(eventBus) {

    var restService = new RestService(eventBus);

    eventBus.registerConsumer(EventBusMessages.LOGIN_USER, function (loginDto) {
        restService.post('/chat/login', loginDto, onLoginSuccess, onLoginError);
    });

    function onLoginSuccess(data) {
        ChatRoomStorage.isLoggedIn = true;
        ChatRoomStorage.token = data;
        eventBus.sendMessage(EventBusMessages.USER_LOGGED_IN);
    }

    function onLoginError(errorMessage){
        alert(errorMessage);
    }

}

function LoginView(eventBus, element) {

    var loginInput = element.find('#login-input');
    var passwordInput = element.find('#password-input');

    element.find('#login-btn').click(onLoginBtnClick);

    function onLoginBtnClick() {
        eventBus.sendMessage(EventBusMessages.LOGIN_USER, {"login": loginInput.val(), "password": passwordInput.val()});
    }
}