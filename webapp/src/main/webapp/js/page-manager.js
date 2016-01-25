function PageManager(eventBus) {
    var pages = [];
    var defaultPage = null;
    var currentPageController = null;

    $(window).bind('hashchange', _viewPage);

    pageManager = {
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
            var content = $(data);
            if(currentPageController){
                currentPageController.destroy();
            }
            currentPageController = page.controller;
            page.controller.init();
            page.view(eventBus, content);
            eventBus.sendMessage(EventBusMessages.UPDATE_APPLICATION_VIEW, content);
        });

        console.log(hash);
    }

    function _setDefault(view, controller, template) {
        defaultPage = {
            "view": view,
            "controller" : controller,
            "template": template
        };
        return pageManager;
    }

    function _setPage(path, view, controller, template) {
        pages[path] = {
            "view": view,
            "controller" : controller,
            "template": template
        };
        return pageManager;
    }

    return pageManager;
}