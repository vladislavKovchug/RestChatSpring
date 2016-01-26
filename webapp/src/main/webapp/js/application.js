$(function () {

    var tokenContainer = new TokenContainer();
    var eventBus = new EventBus('chatEventBus');

    var chatController = new ChatController(eventBus, tokenContainer);
    var loginController = new LoginController(eventBus, tokenContainer);
    var registerController = new RegisterController(eventBus);

    new PageManager(eventBus)
        .setPage('#login', LoginView, loginController, 'login.html')
        .setPage('#register', RegisterView, registerController, 'register.html')
        .setPage('#chat', ChatView, chatController,'chat.html')
        .setDefault(LoginView, loginController, 'login.html')
        .view();

    ApplicationView(eventBus, $('#application-container'));

    console.log('Application initialized');
});

function ApplicationView(eventBus, element) {
    eventBus.registerConsumer(EventBusMessages.UPDATE_APPLICATION_VIEW, function (page) {
        element.empty();
        element.append(page);
    });

    eventBus.registerConsumer(EventBusMessages.USER_LOGGED_OUT, function () {
        window.location = '#login';
    });

    eventBus.registerConsumer(EventBusMessages.USER_LOGGED_IN, function () {
        window.location = '#chat';
    });

    eventBus.registerConsumer(EventBusMessages.USER_REGISTERED, function () {
        window.location = '#login';
    });

}