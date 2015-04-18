var PlacementButtons = function() {
	mainLeft.addEventListener("click", function(event) {
      var postParameters = "TODO";

      $.post("/left", postParameters, function(responseJSON) {
        responseObject = JSON.parse(responseJSON);
        var currTile = responseObject.currTile;
        var board = responseObject.board;
        var validMoves = responseObject.validMoves;
        var players = currTile.players;

        var r = new Renderer(board, currTile, players, validMoves);
        r.render();
      });
    });

    mainRight.addEventListener("click", function(event) {
      var postParameters = "TODO";

      $.post("/right", postParameters, function(responseJSON) {
        responseObject = JSON.parse(responseJSON);
        var currTile = responseObject.currTile;
        var board = responseObject.board;
        var validMoves = responseObject.validMoves;
        var players = currTile.players;

        var r = new Renderer(board, currTile, players, validMoves);
        r.render();
      });
    });

    mainLeft.addEventListener("click", function(event) {
      var postParameters = "TODO";

      $.post("/place", postParameters, function(responseJSON) {
        responseObject = JSON.parse(responseJSON);
        var currTile = responseObject.currTile;
        var board = responseObject.board;
        var validMoves = responseObject.validMoves;
        var players = currTile.players;

        var r = new Renderer(board, currTile, players, validMoves);
        r.render();
      });
    });
}