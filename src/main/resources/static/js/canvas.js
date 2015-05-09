MIN_DRAG_DISTANCE = 5; // distance to count as click and drag
MIN_SCALE = .4;
MAX_SCALE = 2;

var mouseStart; // start location of click
var dragging; // true if clicking and dragging else false

var startXT; // start transformation of x
var startYT; // start transformation of y

// Generates handlers for user interaction with the main canvas and the tile canvas.
var Canvas = function() {
	var canvasOffsetX;
	var canvasOffsetY;

	// Scrolls in or out (with caps on the available level of zoom) based on the mouse wheel.
	var zoom = function(e) {
		if (e.originalEvent.wheelDelta > 0) { // scroll out
			if (renderer.scale < MIN_SCALE) {
				return; // do nothing
			}

			renderer.scale = .9 * renderer.scale;
		} else { // scroll in
			if (renderer.scale > MAX_SCALE) {
				return; // do nothing
			}

			renderer.scale = 10.0 * renderer.scale / 9.0;
		}

		$("#mainCanvas").unbind('mousewheel'); // slow down scrolling by preventing actions in quick succesion
		setTimeout(function() {
			$("#mainCanvas").bind('mousewheel', zoom);
		}, 50);


		renderer.render();
	};
	
	$("#mainCanvas").bind('mousewheel', zoom);

	// Selects a valid move on click, or allows board movement on click and drag.
	$("#mainCanvas").bind('mousedown', function(e) {
		var bodyOffsetX = document.body.getBoundingClientRect().left;
		var divOffsetX = $("#contentDiv")[0].getBoundingClientRect().left;
		canvasOffsetX = divOffsetX - bodyOffsetX;

		var bodyOffsetY = document.body.getBoundingClientRect().top;
		var divOffsetY = $("#contentDiv")[0].getBoundingClientRect().top;
		canvasOffsetY = divOffsetY - bodyOffsetY;

		startXT = renderer.xt;
		startYT = renderer.yt;

		mouseStart = {x : e.pageX - canvasOffsetX, y: e.pageY - canvasOffsetY};
		dragging = true;
	}).on('mousemove', function(e) {
		var start = mouseStart;
		if (!start) { // hasn't been a click
			return;
		}

		end = {x: e.pageX - canvasOffsetX, y: e.pageY - canvasOffsetY},
		d = Math.sqrt(Math.pow((start.x - end.x), 2) + Math.pow((start.y - end.y), 2)); // distance in pixels
		
		if (d > MIN_DRAG_DISTANCE && dragging) { // treat like a click and drag
			var pixDiff = {x: (end.x - start.x), y: (end.y - start.y)};
			var canvasDiff = renderer.pixelsToCanvas(pixDiff);
			var posDiff = renderer.canvasDiffToPosDiff(canvasDiff);

			renderer.xt = startXT + posDiff.x;
			renderer.yt = startYT + posDiff.y;

			renderer.render();
		} else if (!dragging && !renderer.selectedTile && myTurn) { // mousing over a spot with no tile selected
			var end = {x: e.pageX - canvasOffsetX, y: e.pageY - canvasOffsetY};
			var pixPos = end;
			var canvasPos = renderer.pixelsToCanvas(pixPos);
			var cordPos = renderer.canvasToPos(canvasPos);

			var roundedPos = {x: Math.round(cordPos.x), y: Math.round(cordPos.y)};

			if (!renderer.containsMove(roundedPos)) { // not a valid move
				renderer.selectedTile = null;
				renderer.mouseOver = false;
			} else {
				renderer.selectedTile = roundedPos;
				renderer.mouseOver = true;
			}

			renderer.render();
			renderer.selectedTile = null;
			renderer.mouseOver = false;
		}
	}).on('mouseup', function(e) {
		var start = mouseStart;
		dragging = false;
		var end = {x: e.pageX - canvasOffsetX, y: e.pageY - canvasOffsetY};
		d = Math.sqrt(Math.pow((start.x - end.x), 2) + Math.pow((start.y - end.y), 2)); // distance in pixels
		
		if (d <= MIN_DRAG_DISTANCE && !isPlaced && myTurn) { // treat like a regular click
			var pixPos = start;
			var canvasPos = renderer.pixelsToCanvas(pixPos);
			var cordPos = renderer.canvasToPos(canvasPos);

			var roundedPos = {x: Math.round(cordPos.x), y: Math.round(cordPos.y)};

			if (!renderer.containsMove(roundedPos)) { // not a valid move
				renderer.selectedTile = null;
			} else {
				renderer.selectedTile = roundedPos;
				renderer.mouseOver = false;
			}

			renderer.render();
		}
	}).on('mouseleave', function(e) {
		dragging = false;
	});

	// Handles meeple selection on the tile canvas by displaying valid and selected meeples.
	$("#tileCanvas").bind('click', function(e) { // select meeple on click
		if (!myTurn) {
			return;
		}

		var tileCanvas = document.getElementById("tileCanvas");
  		var ctx = tileCanvas.getContext("2d");

  		var w = tileCanvas.width;
  		var h = tileCanvas.height;

  		var positionOffset = $("#tileCanvas").offset();

		var pixelClick = {x : e.pageX - positionOffset.left, y: e.pageY - positionOffset.top};
		var canvasClick = renderer.pixelsToTile(pixelClick);

		// checking where on the tile canvas the user clicked
		if (canvasClick.y > .1 * h && canvasClick.y < .3 * h && canvasClick.x > .4 * w && canvasClick.x < .6 * w) {
			renderer.selectedMeeple = "UP";
		} else if (canvasClick.y > .4 * h && canvasClick.y < .6 * h) {
			if (canvasClick.x > .1 * h && canvasClick.x < .3 * h) {
				renderer.selectedMeeple = "LEFT";
			} else if (canvasClick.x > .4 * h && canvasClick.x < .6 * h) {
				renderer.selectedMeeple = "CENTER";
			} else if (canvasClick.x > .7 * h && canvasClick.x < .9 * h) {
				renderer.selectedMeeple = "RIGHT";
			}
		} else if (canvasClick.y > .7 * h && canvasClick.y < .9 * h && canvasClick.x > .4 * w && canvasClick.x < .6 * w) {
			renderer.selectedMeeple = "DOWN";
		} else {
			renderer.selectedMeeple = null;
		}

		if (renderer.selectedMeeple != null && 
			$.inArray(renderer.selectedMeeple, renderer.validMeeples) == -1) { // selected meeple is invalid

			renderer.selectedMeeple = null;
		}

		renderer.render();
	}).on('mousemove', function(e) { // display meeples on mouseover
		if (!renderer.selectedMeeple && myTurn) { // mousing over a spot with no tile selected
			var tileCanvas = document.getElementById("tileCanvas");
  			var ctx = tileCanvas.getContext("2d");

  			var w = tileCanvas.width;
  			var h = tileCanvas.height;	

			var positionOffset = $("#tileCanvas").offset();
			var pixelClick = {x : e.pageX - positionOffset.left, y: e.pageY - positionOffset.top};
			var canvasClick = renderer.pixelsToTile(pixelClick);

		// checking where on the page the user is mousing over
		if (canvasClick.y > .1 * h && canvasClick.y < .3 * h && canvasClick.x > .4 * w && canvasClick.x < .6 * w) {
			renderer.selectedMeeple = "UP";
		} else if (canvasClick.y > .4 * h && canvasClick.y < .6 * h) {
			if (canvasClick.x > .1 * h && canvasClick.x < .3 * h) {
				renderer.selectedMeeple = "LEFT";
			} else if (canvasClick.x > .4 * h && canvasClick.x < .6 * h) {
				renderer.selectedMeeple = "CENTER";
			} else if (canvasClick.x > .7 * h && canvasClick.x < .9 * h) {
				renderer.selectedMeeple = "RIGHT";
			}
		} else if (canvasClick.y > .7 * h && canvasClick.y < .9 * h && canvasClick.x > .4 * w && canvasClick.x < .6 * w) {
			renderer.selectedMeeple = "DOWN";
		} else {
			renderer.selectedMeeple = null;
		}

		if (renderer.selectedMeeple != null && 
			$.inArray(renderer.selectedMeeple, renderer.validMeeples) == -1) { // selected meeple is invalid

			renderer.selectedMeeple = null;
			renderer.tileMouseOver = false;
		} else {
			renderer.tileMouseOver = false;
		}

			renderer.render();
			renderer.selectedMeeple = null;
			renderer.tileMouseOver = false;
		}
	});
}

$(window).resize(setResizeTimer);

var resizeTimer;

function setResizeTimer() {
	clearTimeout(resizeTimer);
	resizeTimer = setTimeout(resize, 100);
}

// maintain div size when user resizes window
function resize() {
	var height = $(window).height();
	var width = $(window).width() - 300;

	$("#contentDiv").css("width", width + "px");
	$("#contentDiv").css("height", height);
	$("#sidebarDiv").css("height", height);
	$("#mainDiv").css("height", height);
	if (renderer != undefined) {
		renderer.render();
	}
}
