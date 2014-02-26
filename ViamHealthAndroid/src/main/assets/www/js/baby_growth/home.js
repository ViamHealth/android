var HomeView = function (adapter, template) {
    this.initialize = function () {
        // Define a div wrapper for the view. The div wrapper is used to attach events.
        this.el = $('<div/>');
        this.el.on('click', '.take_to_immunization_list', this.loadImmunizationList);
        this.el.on('click', '.take_to_track_growth', this.loadTrackGrowth);

    };
    this.render = function() {
        this.el.html(template());
        return this;
    };
    this.loadImmunizationList = function(event){
        event.preventDefault();
        location.hash = '#' + 'immunization_list';
    };
    this.loadTrackGrowth = function(event){
        event.preventDefault();
        console.log('in here');
        adapter.showToast("Coming soon");
        //alert('coming soon');
        //location.hash = '#' + 'track_growth';
    }
    this.initialize();
}