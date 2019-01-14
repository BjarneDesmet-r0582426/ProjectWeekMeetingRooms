var PADDING_LEFT = 80;
var PADDING_RIGHT = 0;

var TIME_START = 8;
var TIME_END = 21;

getReservations();

var sprites = new Image();
sprites.onload = drawRivers;
sprites.src = 'secret/sprites.png';

function drawRivers() {
	if (!sprites.complete) return;

	drawTimeline();

	var rivers = document.getElementsByClassName('river');
	var riversMap = {};
	for (var i = 0; i < rivers.length; i++) {
		drawRiver(rivers[i]);
		riversMap[rivers[i].title] = rivers[i];
	}

	var _RESERVATIONS = getReservations();
	for (var i = 0; i < _RESERVATIONS.length; i++) {
		var reservation = _RESERVATIONS[i];
		if (reservation[0] in riversMap) {
			drawReservation(riversMap[reservation[0]], reservation[1], reservation[2]);
		}
	}
}

function drawRiver(canvas) {
	canvas.width = canvas.offsetWidth;
	canvas.height = canvas.offsetHeight;
	var context = canvas.getContext('2d');
	context.clearRect(0, 0, canvas.width, canvas.height);

	var max = Math.ceil(canvas.offsetWidth / 32);
	for (var i = 0; i <= max; i++) {
		drawSprite(context, Sprite.BORDER2, i * 32, 0, 32, 32);
		drawSprite(context, Sprite.BORDER8, i * 32, 32, 32, 32);
	}

	if (!isReservedAt(canvas.title, new Date())) {
		drawTimeIndicator(canvas, new Date());
	}

	context.font = "20px arial bold";
	context.fillStyle = "white";
	context.fillText(canvas.title, 10, 39);
}

function drawTimeIndicator(canvas,date) {
	drawSprite(canvas.getContext('2d'), Sprite.DUCK, getXFromTime(canvas, date) - 16, 10, 32, 32);
}

function drawReservation(canvas, start, end) {
	drawObstacles(canvas.getContext('2d'), getXFromTime(canvas, start), getXFromTime(canvas, end));
}

function drawObstacles(context, startx, endx) {
	for (var x = startx; x < endx; x += 32) {
		var sprite = Math.random() <= 0.5 ? Sprite.ROCK1 : Sprite.ROCK2;
		drawSprite(context, sprite, x, 15, Math.min(32, endx - x), 32);
	}
}

function drawTimeline() {
	var canvas = document.getElementById('timeline');
	canvas.width = canvas.offsetWidth;
	canvas.height = canvas.offsetHeight;
	var context = canvas.getContext('2d');
	context.clearRect(0, 0, canvas.width, canvas.height);

	context.font = "20px arial bold";
	context.fillStyle = "white";
	context.textAlign = "center";

	for (var i = TIME_START; i < TIME_END; i++) {
		context.fillText(i, getXFromTime(canvas, new Date(0,0,0,i,0,0)), 20);	
	}
}

function getXFromTime(canvas, date) {
	hours = date.getHours() + (date.getMinutes() / 60);
	return (hours - TIME_START) * getStepXPerHour(canvas) + PADDING_LEFT;
}

function getStepXPerHour(canvas) {
	return (canvas.width - (PADDING_LEFT + PADDING_RIGHT)) / (TIME_END - TIME_START);
}


var Sprite = {
	BORDER1: [0, 0],
	BORDER2: [1, 0],
	BORDER3: [2, 0],
	BORDER4: [0, 1],
	BORDER5: [1, 1],
	BORDER6: [2, 1],
	BORDER7: [0, 2],
	BORDER8: [1, 2],
	BORDER9: [2, 2],

	DUCK: [3, 0],

	ROCK1: [0, 3],
	ROCK2: [1, 3],
}

function drawSprite(context, sprite, x, y, dx, dy) {
	context.drawImage(sprites, sprite[0] * 32, sprite[1] * 32, 32, 32, x, y, dx, dy);
}

function isReservedAt(room, date) {
    var _RESERVATIONS = getReservations();
	for (var i = 0; i < _RESERVATIONS.length; i++) {
		var reservation = _RESERVATIONS[i];
		if (reservation[0] === room) {
			var start = reservation[1].getHours() + (reservation[1].getMinutes() / 60);
			var end = reservation[2].getHours() + (reservation[2].getMinutes() / 60);
			var current = date.getHours() + (date.getMinutes() / 60);
			if (start <= current && current <= end) return true;
		}
	}
	return false;
}

function getReservations() {
	var reservations = [];

	ROOMS.forEach(function(room) {
		room.reservations.forEach(function(res) {
			reservations.push([room.number, new Date(res.start), new Date(res.end)]);
		});
	});

	return reservations;
}

window.onresize = drawRivers;
setInterval(drawRivers, 30000);
