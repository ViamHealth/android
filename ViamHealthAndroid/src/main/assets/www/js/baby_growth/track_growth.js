var TrackGrowthView = function (adapter, template) {
    this.initialize = function () {
        // Define a div wrapper for the view. The div wrapper is used to attach events.
        this.el = $('<div/>');

        this.el.on('click', '.take_to_add_track_growth', this.loadAddTrackGrowth);
        this.el.on('click', '.take_to_graph_track_growth', this.loadTrackGrowthGraph);

    };
    this.render = function() {
        this.el.html(template());
        //this.load_date_widget();
        //this.load_graph();
        return this;
    };

    this.loadAddTrackGrowth = function(event){
            event.preventDefault();
            location.hash = '#' + 'add_track_growth';
        };
        this.loadTrackGrowthGraph = function(event){
            event.preventDefault();
            location.hash = '#' + 'track_growth_graph';
        };

    this.initialize();
}