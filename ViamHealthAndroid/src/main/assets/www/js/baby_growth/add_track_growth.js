var AddTrackGrowthView = function (adapter, template) {
    this.initialize = function () {
        // Define a div wrapper for the view. The div wrapper is used to attach events.
        this.el = $('<div/>');
        //this.el.on('blur', '.track-growth-form-date', this.validate_date_widget);
        this.el.on('click', '.save-growth-form', this.save_growth_form);

    };
    this.render = function() {
        this.el.html(template());
        //this.load_date_widget();
        return this;
    };

    this.save_growth_form = function(event){

        //event.preventDefault();
        var clform = true;
        var p = $("#track-growth-form");
        var height = $(p).find('.track-growth-form-height');
        var weight = $(p).find('.track-growth-form-weight');
        var ddate = $(p).find('.track-growth-form-date');

        if ($(height).val() == undefined || $(height).val().trim().length ==  0){
            clform = false;
            $(p).find('.height_error').show().html('Please check height entered');
        }
        if ($(weight).val() == undefined || $(weight).val().trim().length ==  0){
            clform = false;
            $(p).find('.weight_error').show().html('Please check weight entered');
        }
        if(clform == false) return;

        adapter.updateUserTrackData($(ddate).val(),$(height).val().trim(),$(weight).val().trim());

        $( "#track-growth-form-container" ).slideUp( "slow", function() {
            $("#growth-form-result").slideDown("slow", function(){
            });
          });

    };

    /*this.validate_date_widget = function(event){
        var p = $("#track-growth-form");
        var input = $(p).find(".track-growth-form-date");
        var date;
        if (input.val().length > 0) {
            date = Date.parse(input.val());
            if (date !== null){
                input.val(date.toString("dd, MMMM yyyy"));
                input.removeAttr("pattern","");
            }
            else{
                input.attr("pattern","not-fail");
                adapter.showToast("Please enter the date in different format.");
            }
        }
    }*/
    /*this.load_date_widget = function(){
        //event.preventDefault();

        var input = $(this.el).find(".track-growth-form-date");

        var date;
        if (input.val().length > 0) {
            date = Date.parse(input.val());
            if (date !== null){
                input.val(date.toString("dd, MMMM yyyy"));
                input.removeAttr("pattern","");
            }
            else{
                input.attr("pattern","not-fail");
                adapter.showToast("Please enter the date in different format.");
            }
        }
    };*/


    this.initialize();
}