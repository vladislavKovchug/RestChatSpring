function RestService() {

    var _get = function (url, callback, error, authenticationError) {
        $.ajax({
            url: url,
            type: 'GET',
            statusCode: buildStatusCode(error, authenticationError),
            success: callback
        });
    };

    var _post = function (url, data, callback, error, authenticationError) {
        $.ajax({
            url: url,
            type: 'POST',
            data: JSON.stringify(data),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            dataType: 'json',
            statusCode: buildStatusCode(error, authenticationError),
            success: callback
        });
    };

    var _put = function (url, data, callback, error, authenticationError) {
        $.ajax({
            url: url,
            type: 'PUT',
            data: JSON.stringify(data),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            dataType: 'json',
            statusCode: buildStatusCode(error, authenticationError),
            success: callback
        });
    };

    var _delete = function (url, callback, error, authenticationError) {
        $.ajax({
            url: url,
            type: 'DELETE',
            statusCode: buildStatusCode(error, authenticationError),
            success: callback
        });
    };

    function buildStatusCode(errorHandler, authenticationErrorHandler){
        return {
            403: function (data) {
                var errorMessage = JSON.parse(data.responseText).errorMessage;
                if(authenticationErrorHandler){
                    authenticationErrorHandler(errorMessage);
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