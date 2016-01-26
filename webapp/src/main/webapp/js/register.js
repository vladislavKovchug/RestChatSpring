function RegisterController(eventBus) {

    var registerService = new RegisterService();

    eventBus.registerConsumer(EventBusMessages.REGISTER_USER, onRegister);

    function onRegister(registerData){
        registerService.registerUser(registerData, function(){
            alert('User registered.');
            eventBus.sendMessage(EventBusMessages.USER_REGISTERED);
        }, onError);
    }

    function onError(errorMessage){
        alert(errorMessage);
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
    var datepicker = date.datetimepicker({
        format: 'MM/DD/YYYY'
    });

    element.find('#register-btn').click(onRegister);

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