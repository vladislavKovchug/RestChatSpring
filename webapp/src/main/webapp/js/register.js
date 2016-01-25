function RegisterController(eventBus){

    //not implemented yet
    return {
        "init" : function(){},
        "destroy" : function(){}
    };
}

function RegisterView(eventBus, element){
    var date = element.find('#datetimepicker1');
    date.datetimepicker();
}