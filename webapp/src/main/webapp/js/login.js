function LoginController(eventBus, tokenContainer) {

    var loginService = new LoginService();

    eventBus.registerConsumer(EventBusMessages.LOGIN_USER, function (loginDto) {
        loginService.login(loginDto, onLoginSuccess, onLoginError);
    });

    function onLoginSuccess(data) {
        tokenContainer.token = data;
        eventBus.sendMessage(EventBusMessages.USER_LOGGED_IN);
    }

    function onLoginError(errorMessage){
        alert(errorMessage);
    }

    return {
        "init" : function(){},
        "destroy" : function(){}
    };
}

function LoginView(eventBus, element) {

    var loginInput = element.find('#login-input');
    var passwordInput = element.find('#password-input');

    element.find('#login-btn').click(onLoginBtnClick);

    function onLoginBtnClick() {
        eventBus.sendMessage(EventBusMessages.LOGIN_USER, {"login": loginInput.val(), "password": passwordInput.val()});
    }
}

function LoginService(){

    var restService = new RestService();

    function _login(loginData, callback, errorHandler){
        restService.post('/chat/login', loginData, callback, errorHandler, errorHandler);
    }

    return {
        "login" : _login
    }
}