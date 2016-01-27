function RegisterController(eventBus) {

    var registerService = new RegisterService();

    eventBus.registerConsumer(EventBusMessages.REGISTER_USER, onRegister);

    eventBus.registerConsumer(EventBusMessages.REGISTER_SUCCESS_MESSAGE_SHOWED, function(){
        eventBus.sendMessage(EventBusMessages.USER_REGISTERED);
    });

    function onRegister(registerData){
        registerService.registerUser(registerData, function(){
            eventBus.sendMessage(EventBusMessages.SHOW_REGISTER_SUCCESS_MESSAGE, 'User registered.');
        }, onError);
    }

    function onError(errorMessage){
        eventBus.sendMessage(EventBusMessages.SHOW_REGISTER_ERROR_MESSAGE, errorMessage);
    }

    return {
        "init": function () {
        },
        "destroy": function () {
        }
    };
}

function RegisterView(eventBus, element) {
    var date = element.find('#datetimepicker');
    var errorMessage = element.find('#error-message');
    var successMessage = element.find('#success-message');

    element.find('#register-btn').click(onRegister);
    errorMessage.hide();
    successMessage.hide();

    var datetimepicker = date.datetimepicker({
        format: 'MM/DD/YYYY'
    });

    eventBus.registerConsumer(EventBusMessages.SHOW_REGISTER_ERROR_MESSAGE, function(message){
        errorMessage.html(message);
        errorMessage.alert();
        errorMessage.fadeTo(2000, 500).slideUp(500, function(){
            errorMessage.hide();
        });
    });

    eventBus.registerConsumer(EventBusMessages.SHOW_REGISTER_SUCCESS_MESSAGE, function(message){
        successMessage.html(message);
        successMessage.alert();
        successMessage.fadeTo(2000, 500).slideUp(500, function(){
            successMessage.hide();
            eventBus.sendMessage(EventBusMessages.REGISTER_SUCCESS_MESSAGE_SHOWED);
        });
    });

    function onRegister() {
        var login = element.find('#login-input').val();
        var password = element.find('#password-input').val();
        var birthday = new Date($("#date-input").val()).getTime();
        eventBus.sendMessage(EventBusMessages.REGISTER_USER, {
            "login": login,
            "password": password,
            "birthday": birthday
        });
    }
}

function RegisterService() {
    var restService = new RestService();

    function _registerUser(data, success, error) {
        restService.post('/chat/register', data, success, error, error);
    }

    return {
        "registerUser": _registerUser
    }
}