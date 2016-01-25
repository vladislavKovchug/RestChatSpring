$(function () {

    var eventBus = new EventBus('chatEventBus');
    var chatService = new ChatService(eventBus);
    var loginService = new LoginService(eventBus);

    new PageController(eventBus)
        .setPage('#login', LoginView, 'login.html')
        .setPage('#register', RegisterView, 'register.html')
        .setPage('#chat', ChatView, 'chat.html')
        .setDefault(LoginView, 'login.html')
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

}