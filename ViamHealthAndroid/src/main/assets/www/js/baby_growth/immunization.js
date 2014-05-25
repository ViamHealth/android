var ImmunizationView = function (adapter, template, listItemTemplate) {

    this.initialize = function () {
        // Define a div wrapper for the view. The div wrapper is used to attach events.
        this.el = $('<div/>');
        this.el.on('click', '.immunization-is-completed', this.updateIsCompleted);
        this.el.on('click', '.immunization-current-tab', this.manageTabContents1);
        this.el.on('click', '.immunization-past-tab', this.manageTabContents2);
    };

    this.manageTabContents1= function(event) {
        $("#immunization-future").show();
        $("#immunization-past").hide();
    };

    this.manageTabContents2= function(event) {
            $("#immunization-future").hide();
            $("#immunization-past").show();
        };

    this.render = function() {
        this.el.html(template());
        var that = this;

        adapter.getUserAgeInMonths().done(function(age) {
            var age = parseInt(age);

            adapter.getImmunizationData().done(function(immunizations) {
                var length_immu = immunizations.length,
                    immu_past = [],
                    immu_future = [];


                var header_string = "";
                for(var i =0; i < length_immu ; i++){
                    if(header_string == immunizations[i].header_string){
                        immunizations[i].show_header = false;
                    } else {
                        immunizations[i].show_header = true;
                        header_string = immunizations[i].header_string
                    }
                    immunizations[i].show_checkbox = true;
                    if(immunizations[i].list_item_type =="1"){
                        //Past
                        immunizations[i].show_right_string = false;
                        immu_past.push(immunizations[i]);
                    } else if(immunizations[i].list_item_type =="2"){
                        immunizations[i].show_right_string = true;
                        immu_future.push(immunizations[i]);

                    }
                }

                $(that.el).find('#immunization-past').html(listItemTemplate(immu_past));
                $(that.el).find('#immunization-future').html(listItemTemplate(immu_future));
                if(immu_future.length == 0){
                    $(that.el).find(".immunization-current-tab").hide();
                    $(that.el).find("#immunization-future").hide();
                    $(that.el).find("#immunization-past").show();
                }
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