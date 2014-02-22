var ImmunizationView = function (adapter, template, listItemTemplate) {

    this.initialize = function () {
        // Define a div wrapper for the view. The div wrapper is used to attach events.
        this.el = $('<div/>');
    };

    this.render = function() {
        this.el.html(template());
        var that = this;
        
        adapter.getImmunizationData().done(function(immunizations) {
           $(that.el).find('.immunization_list_id').html(listItemTemplate(immunizations));
        });
        return this;
    };
    this.initialize();

}