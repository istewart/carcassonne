var renderer; // global rendering object
var isPlaced = false; // if a tile has been placed but not meepled
var showHints = true; // if meeple and tile hints should be displayed

// main function to configure the web page
var handler = {
  connect: function() {
    console.log("connect");
    Menu();
    PlacementButtons();
    Canvas();
  },

  board: function() {
    renderer.render();
  },

  players: function(playersList) {
    console.log("hello00");
    console.log(playersList);

    if (playersList == null || playersList.length == 0) {
      $('.lobbyBox').each(function(i, obj) {
        obj.innerHTML = "";
      });

      return;
    } else {
      $('.lobbyBox').each(function(i, obj) {
        if (i >= playersList.length) {
          obj.innerHTML = "";
          return true; // equivalent to continue;
        }

        var currPlayer = playersList[i];
        obj.value = "" + currPlayer.name;
        obj.style.color = currPlayer.color;
      });
    }
  },

  gameStart: function(state) {
    console.log("gameStart = ")
    console.log(state);
    if (state == true) {
      var n = network;

      var currTile = n.get("currTile");
      var board = n.get("board");
      var validMoves = n.get("validMoves");
      var validMeeples = n.get("validMeeples");
      var players = n.get("players");

      renderer = new Renderer(board.board, currTile, players, validMoves, validMeeples, 1, 0, 0);
      renderer.render();
      
      $("#menuDiv").hide();
      $("#joinDiv").hide();
      $("#mainDiv").hide();
      $("#settingsDiv").hide();
      $("#instructionsDiv").hide();
      $("#lobbyDiv").hide();
      $("#mainDiv").show();
    }
  },

  currPlayer: function(player) {
    console.log(player);
  },

  currTile: function(tile) {
    renderer.currTile = tile;
    renderer.render();
  },

  board: function(board) {
    renderer.board = board.board;
    renderer.render();
  },

  validMoves: function(validMoves) {
    renderer.validMoves = validMoves;
    renderer.render();
  },

  validMeeples: function(validMeeples) {
    renderer.validMeeples = validMeeples;
    renderer.render();
  },
}
