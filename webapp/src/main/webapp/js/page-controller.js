function PageController(eventBus) {
    var pages = [];
    var defaultPage = null;
    var lastPageHash = null;

    $(window).bind('hashchange', _viewPage);

    pageController = {
        "setDefault": _setDefault,
        "setPage": _setPage,
        "view": _viewPage
    };

    function _viewPage() {
        var hash = window.location.hash;
        var page = defaultPage;
        if (pages[hash]) {
            page = pages[hash];
        }

        if (!page) {
            throw new Error('can not get view for page ' + hash + '. No default page set.');
        }

        $.get(page.template, function (data) {
            var content = $($.parseHTML(data));
            page.view(eventBus, content);
            eventBus.sendMessage(EventBusMessages.PAGE_CLOSED, hash);
            eventBus.sendMessage(EventBusMessages.UPDATE_APPLICATION_VIEW, content);
        });

        console.log(hash);
    }

    function _setDefault(view, template) {
        defaultPage = {
            "view": view,
            "template": template
        };
        return pageController;
    }

    function _setPage(path, view, template) {
        pages[path] = {
            "view": view,
            "template": template
        };
        return pageController;
    }

    return pageController;
}