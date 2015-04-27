var PlacementButtons = function() {
	mainLeft.addEventListener("click", function(event) {

      network.ask("rotate", "left", function(responseJSON) {
        responseObject = JSON.parse(responseJSON);
        var currTile = responseObject.currTile;
        var validMoves = responseObject.validMoves;

        renderer.currTile = currTile;
        renderer.validMoves = validMoves;

        renderer.render();
      });
    });

    mainRight.addEventListener("click", function(event) {
      
      network.ask("rotate", "right", function(responseJSON) {
        responseObject = JSON.parse(responseJSON);
        var currTile = responseObject.currTile;
        var validMoves = responseObject.validMoves;

        renderer.currTile = currTile;
        renderer.validMoves = validMoves;

        renderer.render();
      });
    });

    mainLeft.addEventListener("click", function(event) {
      var postParameters = "TODO";

      network.ask("place", postParameters, function(responseJSON) {
        responseObject = JSON.parse(responseJSON);
        var currTile = responseObject.currTile;
        var board = responseObject.board;
        var validMoves = responseObject.validMoves;
        var players = currTile.players;

        renderer.currTile = currTile;
        renderer.validMoves = validMoves;
        renderer.board = board;
        renderer.players = players;

        renderer.render();
      });
    });
}