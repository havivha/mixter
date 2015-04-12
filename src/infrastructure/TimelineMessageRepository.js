var _ = require('lodash');

var TimelineMessageRepository = function TimelineMessageRepository(){
    var self = this;

    var projections = [];

    self.save = function(projection){
        projections.push(projection);
    };

    self.getMessageOfUser = function(userId) {
        return _.filter(projections, function(projection){
            return projection.ownerId.equals(userId);
        });
    };
};

exports.create = function(){
    return new TimelineMessageRepository();
};