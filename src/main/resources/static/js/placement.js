var PlacementButtons = function() {
	mainLeft.addEventListener("click", function(event) {
      network.ask("rotate",  {"rotate": "left"}, function(responseObject) {
        var currTile = responseObject.currTile;
        var validMoves = responseObject.validMoves;
        var players = responseObject.players;

        renderer.currTile = currTile;
        renderer.validMoves = validMoves;
        renderer.players = players;

        renderer.render();
      });
    });

  mainRight.addEventListener("click", function(event) {
      network.ask("rotate", {"rotate": "right"}, function(responseObject) {
        var currTile = responseObject.currTile;
        var validMoves = responseObject.validMoves;
        var players = responseObject.players;

        renderer.currTile = currTile;
        renderer.validMoves = validMoves;
        renderer.players = players;

        renderer.render();
      });
    });

  mainPlace.addEventListener("click", function(event) {
      if (!renderer.selectedMove) {
        return;
      }

      var postParameters = {move: renderer.selectedMove, meeple: renderer.selectedMeeple};

      network.ask("place", postParameters, function(responseObject) {
        var currTile = network.get("currTile");
        var board = network.get("board");
        var validMoves = network.get("validMoves");
        var players = network.get("players");

        renderer.currTile = currTile;
        renderer.validMoves = validMoves;
        renderer.board = board.board;
        renderer.players = players;

        renderer.render();
      });
    });
}
