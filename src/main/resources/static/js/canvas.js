MIN_DRAG_DISTANCE = 5;

var Canvas = function() {
	// TODO: Set up scrolling, click, and drag
	var canvasOffsetX;
	var canvasOffsetY;
	
	$("#mainCanvas").bind('mousewheel', function(e) {
		if (e.originalEvent.wheelDelta > 0) { // scroll out
			renderer.scale = .9 * renderer.scale;
		} else { // scroll in
			renderer.scale = 10.0 * renderer.scale / 9.0;
		}

		renderer.render();
	});
	
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
		end = {x : e.pageX - canvasOffsetX, y: e.pageY - canvasOffsetY},
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

			var roundedPos = {x: Math.round(cordPos.x), y: Math.round(cordPos.y)}

			renderer.render();
			renderer.shadeMove(roundedPos);

			// TODO send to front end
		}
	});
}