var AddTrackGrowthView = function (adapter, template) {
    this.initialize = function () {
        // Define a div wrapper for the view. The div wrapper is used to attach events.
        this.el = $('<div/>');
        //this.el.on('blur', '.track-growth-form-date', this.validate_date_widget);
        this.el.on('click', '.save-growth-form', this.save_growth_form);

    };
    this.render = function() {
        this.el.html(template());
        var now = new Date();
        var day = ("0" + now.getDate()).slice(-2);
        var month = ("0" + (now.getMonth() + 1)).slice(-2);
        var today = now.getFullYear()+"-"+(month)+"-"+(day) ;
        $(this.el).find('.track-growth-form-date').val(today);
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

        $( "#track-growth-form-container" ).slideUp( "slow", function() {
            $("#growth-form-result").slideDown("slow", function(){
            });
        });

        adapter.getPercentileData($(ddate).val(),$(height).val().trim(),$(weight).val().trim()).done(function(res){
            var j = $("#growth-form-result");
            if(res.error == ""){
                $(j).find(".child_weight_percentile").html(res.weightPercentile);
                $(j).find(".child_height_percentile").html(res.heightPercentile);
                $(j).find(".child_weight").html(res.weight);
                $(j).find(".child_height").html(res.height);
                $(j).find(".loading_message").hide();
                $(j).find(".message").show();
            } else {
                adapter.showToast(res.error);
            }
        });
        adapter.updateUserTrackData($(ddate).val(),$(height).val().trim(),$(weight).val().trim());

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