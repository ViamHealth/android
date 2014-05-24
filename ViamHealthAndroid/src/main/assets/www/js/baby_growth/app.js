// We use an "Immediate Function" to initialize the application to avoid leaving anything behind in the global scope
(function () {

/*
    BabyGrowthStorage =  {
        getImmunizationData: function(){
            var b = [
                {"is_completed":true,"user_immunization_id":1,"title":"BCG","immunization_id":1,"recommended_age":134,"user":2,"schedule_date_string":"ababbaba","header_string":""},
                {"recommended_age":123,"title":"Hepatitis B1","immunization_id":2,"schedule_date_string":"ababbaba","header_string":""},
                {"recommended_age":0,"title":"OPV1","immunization_id":3,"header_string":"9090909"},
                {"recommended_age":128,"title":"Pneumococcal Booster","immunization_id":4,"header_string":"7878"}
                ];
            return JSON.stringify(b);
        },
        updateIsCompleted: function(immunization_id, is_complete){
                return false;
        },
        getUserAgeInMonths: function(){
            return 124;
        },
        showToast: function(message){
            alert(message);
        },
        getGrowthChartData: function(){

        var b=
'{"user_track_growth": [{"user": 1, "age": 1037, "height": 111.0, "id": 1, "entry_date": "2014-01-10"}, {"weight": 35.0, "age": 1095, "height": 110.0, "user": 1, "id": 2, "entry_date": "2014-03-09"}], "track_growth": [{"age": 0, "label": "Birth", "weight": 89.0, "height": 45.0}, {"age": 90, "label": "3 months", "weight": 45.0, "height": 112.0}, {"age": 356, "label": "1 year", "weight": 23.0, "height": 112.0}]}';
        return b;
        }
    };

*/




    var homeTpl = Handlebars.compile($("#baby-growth-home-tpl").html());
    var immunizationTpl = Handlebars.compile($("#immunization-tpl-last-minute").html());
    var immunizationLiTpl = Handlebars.compile($("#immunization-li-tpl-last-minute").html());
    var trackGrowthTpl = Handlebars.compile($("#track-growth-tpl").html());
    var addTrackGrowthTpl = Handlebars.compile($("#add-track-growth-tpl").html());
    var trackGrowthGraphTpl = Handlebars.compile($("#track-growth-graph-tpl").html());

    var slider = new PageSlider($('body'));

    var detailsURL = /^#immunization_list/;
    var trackGrowthUrl = /^#track_growth$/;
    var addTrackGrowthUrl = /^#add_track_growth$/;
    var trackGrowthGraphUrl = /^#track_growth_graph$/;

    $(window).on('hashchange', route);



    var adapter = new BabyGrowthData();

    adapter.initialize().done(function () {
        route();
    });

    /*document.addEventListener('deviceready', function () {
        console.log("readyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
        route();
        if (navigator.notification) { // Override default HTML alert with native dialog
            window.alert = function (message) {
                navigator.notification.alert(
                    message,    // message
                    null,       // callback
                    "Workshop", // title
                    'OK'        // buttonName
                );
            };
        }
    }, false);*/
    /* ---------------------------------- Local Functions ---------------------------------- */
    function route() {
        var hash = window.location.hash;
        if (!hash) {
            slider.slidePage(new HomeView(adapter, homeTpl).render().el);
            return;
        }
        var match = hash.match(detailsURL);
        if (match) {
            slider.slidePage(new ImmunizationView(adapter, immunizationTpl, immunizationLiTpl).render().el);
        } else if (hash.match(trackGrowthUrl)) {
            slider.slidePage(new TrackGrowthView(adapter, trackGrowthTpl).render().el);
        } else if (hash.match(addTrackGrowthUrl)) {
            slider.slidePage(new AddTrackGrowthView(adapter, addTrackGrowthTpl).render().el);
        }else if (hash.match(trackGrowthGraphUrl)) {
            console.log('here');
            slider.slidePage(new TrackGrowthGraphView(adapter, trackGrowthGraphTpl).render().el);
        }
        return;
    }

}());