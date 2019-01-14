// CLOCK
(function() {
    var $currentTime = document.getElementById('currentTime');

    if ($currentTime) {
        setInterval(updateTime, 1000);
        updateTime();
    }

    function updateTime() {
        var currentdate = new Date();
        $currentTime.innerHTML = padTime(currentdate.getHours()) + ":" + padTime(currentdate.getMinutes());
    }

    function padTime(num) {
        return (num < 10 ? '0' : '') + num;
    }
})();



//BLUEPRINT
(function() {
    var $canvas = document.getElementById('blueprint');

    if($canvas) {
        drawBlueprint();
    }

    function drawBlueprint() {
        var context = $canvas.getContext('2d');

        drawImage(context, 'Base');

        ROOMS.forEach(function (room) {
            drawImage(context, room.number + (isRoomReserved(room) ? 'R' : ''));
        });
    }

    function drawImage(context, name) {
        var image = new Image();
        image.onload = function () {
            context.drawImage(image, 0, 0);
        };
        image.src = 'LokalenHd/' + name + '.png';
    }

    function isRoomReserved(room) {
        var now = Date.now();

        return !!room.reservations.find(function (res) {
            if (res.start <= now && res.end >= now) {
                return true;
            }
        });
    }
})();



// SECRET

(function() {
    var $logo = document.getElementById('logo');

    if ($logo) {
        var clickCounter = 0;
        $logo.addEventListener('click', function () {
            clickCounter++;
            if (clickCounter === 3) {
                window.location = "live?secret=1";
            }
        });
    }
})();