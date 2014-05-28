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

    this.getGrowthChartData = function(){
        var deferred = $.Deferred(),
            data = JSON.parse(BabyGrowthStorage.getGrowthChartData());
        deferred.resolve(data);
        return deferred.promise();
    }

    this.updateUserTrackData =  function(d,h,w){
         BabyGrowthStorage.updateUserTrackData(d,h,w);
     }
    this.getPercentileData = function(d,h,w){
        var deferred = $.Deferred(),
            data = JSON.parse(BabyGrowthStorage.getPercentileData(d,h,w));
        deferred.resolve(data);
        return deferred.promise();
    }
}