// We use an "Immediate Function" to initialize the application to avoid leaving anything behind in the global scope
(function () {

    /*
    BabyGrowthStorage =  {
        getImmunizationData: function(){
            var b = [
                {"is_completed":true,"user_immunization_id":1,"title":"BCG","immunization_id":1,"recommended_age":134,"user":2},
                {"recommended_age":123,"title":"Hepatitis B1","immunization_id":2},
                {"recommended_age":0,"title":"OPV1","immunization_id":3},
                {"recommended_age":128,"title":"Pneumococcal Booster","immunization_id":4}
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
        }
    };
    */





    var homeTpl = Handlebars.compile($("#baby-growth-home-tpl").html());
    var immunizationTpl = Handlebars.compile($("#immunization-tpl").html());
    var immunizationLiTpl = Handlebars.compile($("#immunization-li-tpl").html());

    var slider = new PageSlider($('body'));

    var detailsURL = /^#immunization_list/;

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
        }
    }
}());