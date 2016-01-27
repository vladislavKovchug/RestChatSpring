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
        eventBus.sendMessage(EventBusMessages.SHOW_LOGIN_ERROR_MESSAGE, errorMessage);
    }

    return {
        "init" : function(){},
        "destroy" : function(){}
    };
}

function LoginView(eventBus, element) {

    var loginInput = element.find('#login-input');
    var passwordInput = element.find('#password-input');
    var errorMessage = element.find('#error-message');

    errorMessage.hide();

    eventBus.registerConsumer(EventBusMessages.SHOW_LOGIN_ERROR_MESSAGE, function(message){
        errorMessage.html(message);
        errorMessage.alert();
        errorMessage.fadeTo(2000, 500).slideUp(500, function(){
            errorMessage.hide();
        });
    });

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