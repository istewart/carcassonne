var Canvas = function() {
	// TODO: Set up scrolling, click, and drag
	
	$("#mainCanvas").bind('mousewheel', function(e) {
		if (e.originalEvent.wheelDelta > 0) { // scroll out
			renderer.scale = .9 * renderer.scale;
		} else { // scroll in
			renderer.scale = 10.0 * renderer.scale / 9.0;
		}

		renderer.render();
	});
	
	$("#mainCanvas").bind('mousedown', function(e) {
		$(this).data('p0', {x : e.pageX, y: e.pageY});
	}).on('mouseup', function(e) {
		var start = $(this).data('p0'),
		end = {x : e.pageX, y: e.pageY},
		d = Math.sqrt(Math.pow((start.x - end.x), 2) + Math.pow((start.y - end.y), 2)); // distance in pixels
																					// maybe using squared distance
		
		if (d > 5) {
			var pixDiff = {x: (end.x - start.x), y: (end.y - start.y)};
			console.log(pixDiff);
			var canvasDiff = renderer.pixelsToCanvas(pixDiff);
			console.log(canvasDiff);
			var posDiff = renderer.canvasToPos(canvasDiff);
			console.log(posDiff);

			renderer.xt += posDiff.x;
			renderer.yt += posDiff.y;
		} else {
			//click
		}

		renderer.render();
	});
}