// TIMELINE

Timeline.load(function() {
    var HOUR = 60 * 60 * 1000;
    var now = Date.now();
    var startDate = new Date(now - (HOUR / 2));
    var endDate = new Date(now + (HOUR * 4));

    var options = {
        hAxis: {
            minValue: startDate,
            maxValue: endDate,
            format: 'HH:mm'
        },
        timeline: {
            singleColor: '#384259'
        }
    };

    var tl = new Timeline('#timeline', ROOMS, options);
    tl.draw();
});



// FULLSCREEN
(function() {
    var b = document.body;
    var d = document;
    var rfs = b.requestFullscreen || b.requestFullScreen || b.mozRequestFullscreen || b.mozRequestFullScreen || b.webkitRequestFullscreen || b.webkitRequestFullScreen;
    var efs = d.exitFullscreen || d.exitFullScreen || d.cancelFullscreen || d.cancelFullScreen || d.mozExitFullscreen || d.mozExitFullScreen || d.mozCancelFullscreen || d.mozCancelFullScreen || d.webkitExitFullscreen || d.webkitExitFullScreen || d.webkitCancelFullscreen || d.webkitCancelFullScreen;

    function hasFse() {
        return d.fullScreenElement || d.fullscreenElement || d.mozFullScreenElement || d.mozFullscreenElement || d.webkitFullScreenElement || d.webkitFullscreenElement;
    }

    document.addEventListener('keydown', function (e) {
        if (e.keyCode === 70) {
            if(hasFse()) {
                efs.apply(d);
            }
            else {
                rfs.apply(b);
            }
        }
    });
})();



// REFRESH
(function() {
    setTimeout(function() {
        location.reload();
    }, 2 * 60 * 1000);
})();