var ImmunizationView = function (adapter, template, listItemTemplate) {

    this.initialize = function () {
        // Define a div wrapper for the view. The div wrapper is used to attach events.
        this.el = $('<div/>');
        this.el.on('click', '.immunization-is-completed', this.updateIsCompleted);
    };

    this.render = function() {
        this.el.html(template());
        var that = this;

        adapter.getUserAgeInMonths().done(function(age) {
            var age = parseInt(age);

            adapter.getImmunizationData().done(function(immunizations) {
                var length_immu = immunizations.length;
                var immu_to_do = [],
                    immu_past = [],
                    immu_future = [];

                var age_start = age - 6;
                var age_end = age + 6;

                for(var i =0; i < length_immu ; i++){
                    if(immunizations[i].recommended_age < age_start ){
                        immu_past.push(immunizations[i]);
                    }else if(immunizations[i].recommended_age >= age_start && immunizations[i].recommended_age <= age_end ){
                        immu_to_do.push(immunizations[i]);
                    } else if(immunizations[i].recommended_age > age_end  ){
                        immu_future.push(immunizations[i]);
                    }
                }

                $(that.el).find('.immunization_list_to_do').html(listItemTemplate(immu_to_do));
                $(that.el).find('.immunization_list_past').html(listItemTemplate(immu_past));
                $(that.el).find('.immunization_list_future').html(listItemTemplate(immu_future));
            });

        });


        return this;
    };

    this.updateIsCompleted= function(event) {
        //Checkbox gets unchecked/checked by now !
        var is_checked = false;
        var immunization_id = $(this).attr("data-immunization-id");
        if($(this).is(':checked')){
            var is_checked = true;
        }

        adapter.updateIsCompleted(immunization_id, is_checked).done(function(is_complete){
            console.log("did we complete?"+is_complete);
        });
    }
    this.initialize();

}