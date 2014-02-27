var BabyGrowthData = function () {

    this.initialize = function() {
        var deferred = $.Deferred();
        deferred.resolve();
        return deferred.promise();
    }

    this.getImmunizationData = function(){
        var deferred = $.Deferred(),
            immunizations = JSON.parse(BabyGrowthStorage.getImmunizationData());
        deferred.resolve(immunizations);
        return deferred.promise();
    }

    this.updateIsCompleted = function(immunization_id, is_complete){
        var deferred = $.Deferred(),
            success = JSON.parse(BabyGrowthStorage.updateIsCompleted(immunization_id, is_complete));
        deferred.resolve(success);
        return deferred.promise();
    }

    this.getUserAgeInMonths = function(){
        var deferred = $.Deferred(),
            age = BabyGrowthStorage.getUserAgeInMonths();
        deferred.resolve(age);
        return deferred.promise();
    }

    this.showToast = function(message){
        BabyGrowthStorage.showToast(message);
    }
}