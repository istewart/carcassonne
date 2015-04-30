var PlacementButtons = function() {
  var isPlaced = false;
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
    if (isPlaced) {
      if (!renderer.selectedTile) {
        return;
      }

      var postParameters = {"meeple": renderer.selectedMeeple};

      network.ask("placeMeeple", postParameters, function(responseObject) {
        var currTile = responseObject.currTile;
        var board = responseObject.board;
        var validMoves = responseObject.validMoves;
        var players = responseObject.players;

        renderer.currTile = currTile;
        renderer.validMoves = validMoves;
        renderer.board = board.board;
        renderer.players = players;

        renderer.render();
      });
      isPlaced = false;
      $("#mainPlace").html("Place Meeple");
    } else {

      var move = "" + renderer.selectedTile.x + "," + renderer.selectedTile.y;
        var postParameters = {"move": move};

      network.ask("placeTile", postParameters, function(responseObject) {
        var validMeeples = responseObject.validMeeples;
        renderer.validMeeples = validMeeples;

        renderer.render();
      });
      isPlaced = true;
      $("#mainPlace").html("Place Tile");
    }
  });
}
