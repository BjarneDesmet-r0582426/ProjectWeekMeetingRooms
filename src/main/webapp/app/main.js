var $back = document.getElementById('timeline-back');
var $date = document.getElementById('timeline-date');
var $forward = document.getElementById('timeline-forward');
var DAY = 24 * 60 * 60 * 1000;
var date = new Date();
var index = 0;

setDate();


Timeline.load(function() {
    var options = {
        hAxis: getTimeframe(date),
        timeline: {
            singleColor: '#003469'
        }
    };

    var tl = new Timeline('#timeline', ROOMS, options);
    tl.draw();

    $back.addEventListener('click', function() {
        if(!disabled($back)) {
            date = new Date((+date) - DAY);
            index--;
            update(tl);
        }
    });

    $forward.addEventListener('click', function() {
        if(!disabled($forward)) {
            date = new Date((+date) + DAY);
            index++;
            update(tl);
        }
    });
});

function update(tl) {
    disable($back);
    disable($forward);

    tl.options.hAxis = getTimeframe(date);

    var url = 'app?action=json&date=' + date.getFullYear() + '-' + pad(date.getMonth() + 1) + '-' + pad(date.getDate());
    fetch(url)
        .then(function(res){ return res.json(); })
        .then(function(data) {
            tl.setData(data);
            tl.draw();
            autoDisable();
            setDate();
        });
}

function disable($elem) {
    $elem.setAttribute('disabled', 'disabled');
}

function enable($elem) {
    $elem.removeAttribute('disabled');
}

function disabled($elem) {
    return $elem.hasAttribute('disabled');
}

function autoDisable() {
    if(index <= -7) {
        disable($back);
    }
    else {
        enable($back);
    }

    if(index >= 14) {
        disable($forward);
    }
    else {
        enable($forward);
    }
}

function setDate() {
    $date.textContent = pad(date.getDate()) + '/' + pad(date.getMonth() + 1) + '/' + date.getFullYear();
}

function pad(num) {
    return (num < 10 ? '0' : '') + num;
}

function getTimeframe(date) {
    var startDate = new Date(date.getFullYear(), date.getMonth(), date.getDate(), 9, 0, 0);
    var endDate = new Date(date.getFullYear(), date.getMonth(), date.getDate(), 22, 0, 0, 0);

    return {
        minValue: startDate,
        maxValue: endDate,
        format: 'HH:mm'
    };
}