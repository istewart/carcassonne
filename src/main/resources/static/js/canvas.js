var Canvas = function() {
	// TODO: Set up scrolling, click, and drag
	
	$("#mainCanvas").bind('mousewheel', function(e) {
		if (e.originalEvent.wheelDelta > 0) {
			renderer.scale = .9 * renderer.scale;
		} else {
			renderer.scale = 10.0 * renderer.scale / 9.0;
		}

		renderer.render();
	});
	
	$("#map").bind('mousedown', function(e) {
		$(this).data('p0', {x : e.pageX, y: e.pageY});
	}).on('mouseup', function(e) {
		var p0 = $(this).data('p0'),
		p1 = {x : e.pageX, y: e.pageY},
		d = Math.sqrt(Math.pow(p1.x - p0.x, 2) + Math.pow(p1.y - p0.y, 2));
		
		if (d > 5) {
			//set rendere.xt and yt based on posToCanvas. need to write CanvasToPos
		} else {
			//click
		}
	});
}