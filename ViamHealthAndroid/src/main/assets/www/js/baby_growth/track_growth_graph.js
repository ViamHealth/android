var TrackGrowthGraphView = function (adapter, template) {
    this.initialize = function () {
        // Define a div wrapper for the view. The div wrapper is used to attach events.
        this.el = $('<div/>');
    };
    this.render = function() {
    console.log('here');
        this.el.html(template());
        this.load_graph();
        return this;
    };


    this.load_graph = function(){
        var that = this;

        adapter.getGrowthChartData().done(function(data){
            var track_growth_length = data.track_growth.length;
            var user_track_growth_length = data.user_track_growth.length;

            var tg_h_data=[];
            var tg_w_data=[];
            var utg_h_data = [];
            var utg_w_data = [];

            for(var i=0;i< track_growth_length;i++){
                if(data.track_growth[i].height)
                    tg_h_data.push([ data.track_growth[i].age, data.track_growth[i].height ]);
                if(data.track_growth[i].weight)
                    tg_w_data.push([ data.track_growth[i].age, data.track_growth[i].weight ]);
            }

            for(var i=0;i< user_track_growth_length;i++){
                if(data.user_track_growth[i].height)
                    utg_h_data.push([ data.user_track_growth[i].age, data.user_track_growth[i].height ]);
                if(data.user_track_growth[i].weight)
                    utg_w_data.push([ data.user_track_growth[i].age, data.user_track_growth[i].weight ]);
            }

            $(that.el).find('#growth-chart').highcharts({
                chart: {type: 'spline'},
                xAxis: {
                    type: 'linear',
                    min: 0,
                    max:  6420,

                },
                yAxis: [{ // Primary yAxis
                   labels: {
                       formatter: function() {
                           return this.value +' Kg';
                       },
                       style: {
                           color: '#89A54E'
                       }
                   },
                   title: {
                       text: 'Weight',
                       style: {
                           color: '#89A54E'
                       }
                   },
                   opposite: true

                }, { // Secondary yAxis
                   gridLineWidth: 0,
                   title: {
                       text: 'Height',
                       style: {
                           color: '#4572A7'
                       }
                   },
                   labels: {
                       formatter: function() {
                           return this.value +' cm';
                       },
                       style: {
                           color: '#4572A7'
                       }
                   }

                }],
                series: [{
                    name: 'Average Height',
                    data: tg_h_data,
                },{
                    name: 'Average Weight',
                    data: tg_w_data,
                },{
                    name: 'Height',
                    data: utg_h_data,
                },{
                    name: 'Weight',
                    data: utg_w_data,
                }],
            });
        });

    };

    this.initialize();
}