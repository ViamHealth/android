
var BabyGrowthData = function () {

    this.initialize = function() {
        var deferred = $.Deferred();
        deferred.resolve();
        return deferred.promise();
    }

    this.getImmunizationData = function(){
        /*if(undefined == BabyGrowthStorage ){
            //For testing UI on browser
            var BabyGrowthStorage =  {
                getImmunizationData: function(){
                    var b = [
                        {'id':'1','title':'one'},
                        {'id':'2','title':'two'},
                        ];
                    return JSON.stringify(b);
                }
            }
        }*/
        var deferred = $.Deferred(),
            immunizations = JSON.parse(BabyGrowthStorage.getImmunizationData());

        deferred.resolve(immunizations);
        return deferred.promise();
    }
}