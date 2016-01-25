function EventBus(eventBusName){
    this.name = eventBusName;
    this.consumers = {};
}

EventBus.prototype.registerConsumer = function(type, consumer){
    if(!this.consumers[type]){
        this.consumers[type] = []
    }
    this.consumers[type].push(consumer);
}

EventBus.prototype.sendMessage = function(type, message){
	if(this.consumers[type]){
		for(var i=0; i<this.consumers[type].length; i++){
			setTimeout(function(consumer){
				return function(){
					consumer(message);
				}
			}(this.consumers[type][i]), 100);
		}
	}
}