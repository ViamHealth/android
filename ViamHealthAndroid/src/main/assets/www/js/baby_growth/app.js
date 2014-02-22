// We use an "Immediate Function" to initialize the application to avoid leaving anything behind in the global scope
(function () {
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