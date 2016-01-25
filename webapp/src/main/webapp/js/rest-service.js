function RestService(eventBus) {

    var _get = function (url, callback, error) {
        $.ajax({
            url: url,
            type: 'GET',
            statusCode: buildStatusCode(error),
            success: callback
        });
    };

    var _post = function (url, data, callback, error) {
        $.ajax({
            url: url,
            type: 'POST',
            data: JSON.stringify(data),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            dataType: 'json',
            statusCode: buildStatusCode(error),
            success: callback
        });
    };

    var _put = function (url, data, callback, error) {
        $.ajax({
            url: url,
            type: 'PUT',
            data: JSON.stringify(data),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            dataType: 'json',
            statusCode: buildStatusCode(error),
            success: callback
        });
    };

    var _delete = function (url, callback, error) {
        $.ajax({
            url: url,
            type: 'DELETE',
            statusCode: buildStatusCode(error),
            success: callback
        });
    };

    function buildStatusCode(errorHandler){
        return {
            403: function (data) {
                var errorMessage = JSON.parse(data.responseText).errorMessage;
                eventBus.sendMessage(EventBusMessages.AUTHENTICATION_ERROR, errorMessage);
                if(errorHandler){
                    errorHandler(errorMessage);
                }
            },
            500: function (data) {
                var errorMessage = JSON.parse(data.responseText).errorMessage;
                if(errorHandler){
                    errorHandler(errorMessage);
                }
            }
        };
    }

    return {
        "get": _get,
        "post": _post,
        "put": _put,
        "delete": _delete
    };
}