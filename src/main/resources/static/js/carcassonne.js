var renderer; // global rendering object
var isPlaced = false; // if a tile has been placed but not meepled
var showHints = true; // if meeple and tile hints should be displayed
var myTurn = false;

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

    renderer.players = playersList;

  },

  gameStart: function(state) {
    console.log("gameStart = ")
    console.log(state);
    if (state == true) {
      var n = network;

      $("#menuDiv").hide();
      $("#joinDiv").hide();
      $("#mainDiv").hide();
      $("#settingsDiv").hide();
      $("#instructionsDiv").hide();
      $("#lobbyDiv").hide();
      $("#mainDiv").show();

      if (network.get("currentPlayer").id != network.id) {
        $("#mainLeft").hide();
        $("#mainRight").hide();
        $("#mainPlace").hide();
        $("#mainMeeple").hide();
        $("#mainSkip").hide();
      }

      var currTile = n.get("currTile");
      var board = n.get("board");
      var validMoves = n.get("validMoves");
      var validMeeples = n.get("validMeeples");
      var players = n.get("players");

      renderer = new Renderer(board.board, currTile, players, validMoves, validMeeples, 1, 0, 0);
      renderer.render();
    }
  },

  currentPlayer: function(player) {
    if (player.id == network.id) {
      myTurn = true;
      renderer.render();
      $("#mainLeft").show();
      $("#mainRight").show();
      $("#mainPlace").show();
    } else {
      myTurn = false;
      renderer.render();
      $("#mainLeft").hide();
      $("#mainRight").hide();
      $("#mainPlace").hide();
      $("#mainMeeple").hide();
      $("#mainSkip").hide();
    }
    console.log("MyTurn? " + myTurn);
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
    console.log("meeple");
    renderer.validMeeples = validMeeples;
    renderer.render();
  },
}
