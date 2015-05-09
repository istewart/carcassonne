var PlacementButtons = function() {
  // rotates the tile left on click
	mainLeft.addEventListener("click", function(event) {
      network.ask("rotate",  {"rotate": "left"}, function(responseObject) {
        var currTile = responseObject.currTile;
        var validMoves = responseObject.validMoves;
        var players = responseObject.players;

        renderer.currTile = currTile;
        renderer.validMoves = validMoves;
        renderer.players = players;
        renderer.selectedTile = null;

        renderer.render();
      });
    });

  // rotates the tile right on click
  mainRight.addEventListener("click", function(event) {
      network.ask("rotate", {"rotate": "right"}, function(responseObject) {
        var currTile = responseObject.currTile;
        var validMoves = responseObject.validMoves;
        var players = responseObject.players;

        renderer.currTile = currTile;
        renderer.validMoves = validMoves;
        renderer.players = players;
        renderer.selectedTile = null;

        renderer.render();
      });
    });

  // places the tile on click
  mainPlace.addEventListener("click", function(event) {
      if (!renderer.selectedTile) { // no tile selected
        return;
      }

      var move = "" + renderer.selectedTile.x + "," + renderer.selectedTile.y;
      var postParameters = {"move": move};

      network.ask("placeTile", postParameters, function(responseObject) {
        var error = responseObject.error;
        if (error != null) {
          return;
        }
        var validMeeples = responseObject.validMeeples;
        renderer.validMeeples = validMeeples;

        isPlaced = true;

        $("#mainPlace").hide();
        $("#mainLeft").hide();
        $("#mainRight").hide();

        if (myTurn) {
          if (renderer.validMeeples.length > 0) {
            $("#mainMeeple").show();
          }
          $("#mainSkip").show();
        }

        renderer.render();
      });
  });

  // locks in the selected meeple placement
  mainMeeple.addEventListener("click", function(event) {
      if (!renderer.selectedMeeple) {
        alert("You don't have a meeple selected!");
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
        renderer.validMeeples = null;
        renderer.selectedTile = null;
        renderer.selectedMeeple = null;

        isPlaced = false;

        $("#mainMeeple").hide();
        $("#mainSkip").hide();

        if (myTurn) {
          $("#mainPlace").show();
          $("#mainLeft").show();
          $("#mainRight").show();
        }

        renderer.render();
        myTurn = false;
      });
  });

  // skips meeple placement by sending a null meeple to the back end
  mainSkip.addEventListener("click", function(event) {
      var postParameters = {"meeple": null};

      network.ask("placeMeeple", postParameters, function(responseObject) {
        var currTile = responseObject.currTile;
        var board = responseObject.board;
        var validMoves = responseObject.validMoves;
        var players = responseObject.players;

        renderer.currTile = currTile;
        renderer.validMoves = validMoves;
        renderer.board = board.board;
        renderer.players = players;
        renderer.validMeeples = null;
        renderer.selectedTile = null;
        renderer.selectedMeeple = null;

        isPlaced = false;

        $("#mainMeeple").hide();
        $("#mainSkip").hide();

        if (myTurn) {
          $("#mainPlace").show();
          $("#mainLeft").show();
          $("#mainRight").show();
        }

        renderer.render();
      });
  });
}
