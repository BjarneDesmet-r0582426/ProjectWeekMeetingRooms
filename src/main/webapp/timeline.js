(function() {
    var _loaded = false;
    var _loading = false;
    var _onLoad = [];
    var _timelines = [];

    // Redraw timelines when the window resizes
    window.addEventListener('resize', function() {
        _timelines.forEach(function(tl) {
            tl.resize();
        });
    });

    window.Timeline = function Timeline($container, data, options) {
        this.$container = typeof $container === 'string' ? document.querySelector($container) : $container;
        this.options = options || {};
        this.data = prepare(data, this);
        this.chart = new google.visualization.Timeline(this.$container);
        this.draws = 0;

        _timelines.push(this);

        google.visualization.events.addListener(this.chart, 'onmouseover', this.onMouse.bind(this));
        google.visualization.events.addListener(this.chart, 'onmouseout', this.onMouse.bind(this));
    }

    Timeline.prototype.setData = function(data) {
        this.data = prepare(data, this);
    }

    Timeline.prototype.draw = function() {
        this.chart.draw(this.data, this.options);
        this.draws++;

        if(this.draws === 1) {
            this.fixHeight();
        }

        this.removeEmpty();
        this.fixScroll();

        if(this.hasNow) {
            this.removeExtraRow();
            this.showNow();
        }
    }

    // On the first draw, a default height is used
    // Then we read out the actual height, and redraw with that
    // Otherwise, the default height stays, and the viewport is scrolled...
    Timeline.prototype.fixHeight = function() {
        this.options.height = this.getHeight();
        this.draw();
    }

    // Removes very short appointments (usually because they are cut off by the time frame)
    Timeline.prototype.removeEmpty = function() {
        var $rects = Array.from(this.$container.querySelectorAll('svg rect'));
        $rects.forEach(function($rect) {
            if(+$rect.getAttribute('width') < 5) {
                $rect.style.display = 'none';
                if($rect.nextElementSibling) {
                    $rect.nextElementSibling.style.display = 'none';
                }
            }
            else {
                $rect.style.display = "";
                if($rect.nextElementSibling) {
                    $rect.nextElementSibling.style.display = "";
                }
            }
        });
    }

    // For some reason, the SVG gets slightly too high, causing the viewport to scroll
    Timeline.prototype.fixScroll = function() {
        var $wrapper = this.$container.querySelector('div[style*=overflow-y]');

        if($wrapper) {
            $wrapper.style.overflowY = 'hidden';
        }
    }

    // The reference line is added by adding a reservation at the current time
    // This creates an extra row, however, which is removed here
    Timeline.prototype.removeExtraRow = function() {
        var $row = this.$container.querySelector('div > div > div > div > div > svg > g:nth-of-type(1) > rect:first-child');
        var $wrapper = this.$container.querySelector('div > div > div > div');
        $wrapper.style.marginTop = '-' + svgHeight($row) + 'px';
        $wrapper.parentNode.style.overflow = 'hidden';
    }

    // Timeline#removeEmpty() removes all (unusually) short appointments
    // This includes the reference line, which added again here
    Timeline.prototype.showNow = function() {
        var $svg = this.$container.querySelector('div > div > div > div > div > svg');
        var $rect = $svg.querySelector('g:nth-of-type(3) > rect:first-child');

        $rect.style.display = '';
        $rect.setAttribute('height', svgHeight($svg));
        $rect.setAttribute('width', '1');
        $rect.setAttribute('y', '0');
    }

    // Called when the window resizes
    Timeline.prototype.resize = function() {
        if(this.options.onResize) {
            this.options.onResize(this);
        }
        this.draw();
    }

    // Get the required height for the timeline
    Timeline.prototype.getHeight = function() {
        var $scaleSvg = this.$container.querySelector('div > div > div > svg');
        var $wrapper = $scaleSvg.nextElementSibling;
        var $mainSvg = $wrapper.firstElementChild;
        var baseHeight = svgHeight($mainSvg);
        var extra = svgHeight($scaleSvg) - $wrapper.clientHeight;

        return baseHeight + extra;
    }

    // Prevents the removal of the reference line when the mouse hovers over it
    Timeline.prototype.onMouse = function() {
        if(this.hasNow) {
            this.showNow();
        }
    }


    // Load the Google Charts library, and call 'cb' when done
    Timeline.load = function load(cb) {
        if(_loaded) {
            cb();
        }
        else {
            _onLoad.push(cb);

            if(!_loading) {
                _loading = true;
                google.charts.load('43', {packages: ['timeline']});
                google.charts.setOnLoadCallback(function () {
                    _loaded = true;
                    _onLoad.forEach(function (cb) {
                        cb();
                    })
                });
            }
        }
    }


    // Retrieves the height attribute of an SVG element, as a number
    function svgHeight($svgH) {
        return +($svgH.getAttribute('height'));
    }


    // Adds reservations to a DataTable (sorted),
    // removing or cutting off reservations outside the time frame
    // and adding a reference line for the current time (if applicable)
    function prepare(rooms, tl) {
        var reservations = new google.visualization.DataTable();
        var roomNotEmpty = {};

        reservations.addColumn({type: 'string', id: 'Room'});
        reservations.addColumn({type: 'string', id: 'Subject'});
        reservations.addColumn({type: 'date', id: 'Start'});
        reservations.addColumn({type: 'date', id: 'End'});

        if(tl.options.hAxis && tl.options.hAxis.minValue && tl.options.hAxis.maxValue) {
            var now = new Date();

            if(tl.options.hAxis.minValue < now && tl.options.hAxis.maxValue > now) {
                tl.hasNow = true;
                reservations.addRow(['', '', new Date(), new Date]);
            }
            else {
                tl.hasNow = false;
            }
        }
        else {
            tl.hasNow = false;
        }

        rooms.forEach(function(room) {
            room.reservations.forEach(function(res) {
                var start = res.start, end = res.end;

                if(tl.options.hAxis && tl.options.hAxis.minValue) {
                    var minValue = +tl.options.hAxis.minValue;

                    if(end < minValue) {
                        return;
                    }
                    else if(start < minValue) {
                        start = minValue;
                    }
                }

                if(tl.options.hAxis && tl.options.hAxis.maxValue) {
                    var maxValue = +tl.options.hAxis.maxValue;

                    if(start > maxValue) {
                        return;
                    }
                    else if(end > maxValue) {
                        end = maxValue;
                    }
                }

                roomNotEmpty[room.number] = true;

                reservations.addRow([
                    room.number,
                    res.registrar,
                    new Date(start),
                    new Date(end)
                ]);
            });
        });

        rooms.forEach(function(room) {
            if(!roomNotEmpty[room.number]) {
                var time = tl.options.hAxis ? tl.options.hAxis.minValue || tl.options.hAxis.maxValue || new Date() : new Date();
                reservations.addRow([room.number, "", time, time]);
            }
        });

        reservations.sort({column: 0});

        return reservations;
    }

})();