MIN_DRAG_DISTANCE = 5;
MIN_SCALE = .6;
MAX_SCALE = 3;
// var mouseStart;
// var dragging;

// Generates handlers for user interaction with the main canvas and the tile canvas.
var Canvas = function() {
	var canvasOffsetX;
	var canvasOffsetY;
	
	// Scrolls in or out (with caps on the available level of zoom) based on the mouse wheel.
	$("#mainCanvas").bind('mousewheel', function(e) {
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

		renderer.render();
	});
	
	// Selects a valid move on click, or allows board movement on click and drag.
	$("#mainCanvas").bind('mousedown', function(e) {
		var bodyOffsetX = document.body.getBoundingClientRect().left;
		var divOffsetX = $("#contentDiv")[0].getBoundingClientRect().left;
		canvasOffsetX = divOffsetX - bodyOffsetX;

		var bodyOffsetY = document.body.getBoundingClientRect().top;
		var divOffsetY = $("#contentDiv")[0].getBoundingClientRect().top;
		canvasOffsetY = divOffsetY - bodyOffsetY;

		$(this).data('p0', {x : e.pageX - canvasOffsetX, y: e.pageY - canvasOffsetY});
	}).on('mouseup', function(e) {
		var start = $(this).data('p0'),
		end = {x: e.pageX - canvasOffsetX, y: e.pageY - canvasOffsetY},
		d = Math.sqrt(Math.pow((start.x - end.x), 2) + Math.pow((start.y - end.y), 2)); // distance in pixels
		
		if (d > MIN_DRAG_DISTANCE) { // treat like a click and drag
			var pixDiff = {x: (end.x - start.x), y: (end.y - start.y)};
			var canvasDiff = renderer.pixelsToCanvas(pixDiff);
			var posDiff = renderer.canvasDiffToPosDiff(canvasDiff);

			renderer.xt += posDiff.x;
			renderer.yt += posDiff.y;

			renderer.render();
		} else { // treat like a regular click
			var pixPos = start;
			var canvasPos = renderer.pixelsToCanvas(pixPos);
			var cordPos = renderer.canvasToPos(canvasPos);

			var roundedPos = {x: Math.round(cordPos.x), y: Math.round(cordPos.y)};

			if (!renderer.containsMove(roundedPos)) { // not a valid move
				return;
			}

			renderer.selectedTile = roundedPos;
			renderer.render();
		}
	});

	// Handles meeple selection on the tile canvas by displaying valid and selected meeples.
	$("#tileCanvas").bind('click', function(e) {
		var tileCanvas = document.getElementById("tileCanvas");
  		var ctx = tileCanvas.getContext("2d");

  		var w = tileCanvas.width;
  		var h = tileCanvas.height;

		var bodyOffsetX = document.body.getBoundingClientRect().left;
		var divOffsetX = $("#sidebar")[0].getBoundingClientRect().left;
		canvasOffsetX = divOffsetX - bodyOffsetX;

		var bodyOffsetY = document.body.getBoundingClientRect().top;
		var divOffsetY = $("#contentDiv")[0].getBoundingClientRect().top;
		canvasOffsetY = divOffsetY - bodyOffsetY;

		var pixelClick = {x : e.pageX - canvasOffsetX, y: e.pageY - canvasOffsetY};
		var canvasClick = renderer.pixelsToTile(pixelClick);

		console.log(canvasClick);

		if (canvasClick.y > .125 * h && canvasClick.y < .375 * h && canvasClick.x > .375 * w && canvasClick.x < .625 * w) {
			renderer.selectedMeeple = "UP";
		} else if (canvasClick.y > .375 * h && canvasClick.y < .625 * h) {
			if (canvasClick.x > .125 * h && canvasClick.x < .375 * h) {
				renderer.selectedMeeple = "LEFT";
			} else if (canvasClick.x > .375 * h && canvasClick.x < .625 * h) {
				renderer.selectedMeeple = "CENTER";
			} else if (canvasClick.x > .625 * h && canvasClick.x < .875 * h) {
				renderer.selectedMeeple = "RIGHT";
			}
		} else if (canvasClick.y > .625 * h && canvasClick.y < .875 * h && canvasClick.x > .375 * w && canvasClick.x < .625 * w) {
			renderer.selectedMeeple = "DOWN";
		}

		renderer.render();
	});
}