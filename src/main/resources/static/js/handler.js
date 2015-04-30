var handler = {
	board: function() {
		renderer.render();
	}

	players: function() {
		console.log("hellO");
		renderer.renderPlayers();
		var playersList = this.players;

	      if (playersList == null || playersList.length == 0) {
	        $('.playerBox2').each(function(i, obj) {
	          obj.innerHTML = "";
	        });

	        return;
	      }

	      $('.playerBox2').each(function(i, obj) {
        if (i >= playersList.length) {
          obj.innerHTML = "";
          return true; // equivalent to continue;
        }

        var currPlayer = playersList[i];
        obj.value = "" + currPlayer.name + ": " + currPlayer.score + " Meeples: " + currPlayer.numMeeples;
        obj.style.color = currPlayer.color;
	}
}
