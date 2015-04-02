 $(document).ready(function() {

    console.log("Script started!");

    var hostDiv = document.getElementById("hostDiv");
    var joinDiv = document.getElementById("joinDiv");
    var mainDiv = document.getElementById("mainDiv");
    var menuDiv = document.getElementById("menuDiv");

    function Renderer(board, currTile, players, validMoves) {
      this.board = board;
      this.currTile = currTile;
      this.players = players;
      this.validMoves = validMoves;

      console.log(this);
    }

    Renderer.prototype.renderPlayers = function(players) {
      if (players == null || players.length == 0) {
        $('.playerBox').each(function(i, obj) {
          obj.innerHTML = "";
        });

        return;
      }

      $('.playerBox').each(function(i, obj) {
        if (i >= players.length) {
          obj.innerHTML = "";
          return true; // continues in loop
        }

        var currPlayer = players[i];
        obj.value = "" + currPlayer.name + ": " + currPlayer.score + " Meeples: " + currPlayer.numMeeples;
        obj.style.color = currPlayer.color;
      });
    };

    Renderer.prototype.renderTile = function(currTile) {
      var currImage = document.getElementById(currTile.id);
      var tileImage = document.getElementById("tileImage");

      tileImage.src = currImage.src;
      tileImage.show();

      // var c = document.getElementById("myCanvas");
      // var ctx = c.getContext("2d");

      // ctx.drawImage(tileImage, 0, 0);
    };

    Renderer.prototype.renderBoard = function(board) {
      // body...
    };

    Renderer.prototype.renderMoves = function(validMoves) {
      // body...
    };

    Renderer.prototype.render = function() {
      renderBoard(this.board);
      renderTile(this.currTile);
      renderMoves(this.validMoves);
      console.log(this);
    };

    var hostButton = document.getElementById("hostButton");
    var joinButton = document.getElementById("joinButton");
    var settingsButton = document.getElementById("settingsButton");
    var helpButton = document.getElementById("helpButton");

    var joinJoin = document.getElementById("joinJoin");

    var mainLeft = document.getElementById("mainLeft");
    var mainPlace = document.getElementById("mainPlace");
    var mainRight = document.getElementById("mainRight");

    hostButton.addEventListener("click", function(event) {
      $("#menuDiv").hide();
      $("#joinDiv").hide();
      $("#mainDiv").hide();
      $("#hostDiv").show();
    });

    joinButton.addEventListener("click", function(event) {
      $("#menuDiv").hide();
      $("#joinDiv").show();
      $("#mainDiv").hide();
      $("#hostDiv").hide();
    });

    joinJoin.addEventListener("click", function(event) {
      $("#menuDiv").hide();
      $("#joinDiv").hide();
      $("#mainDiv").show();
      $("#hostDiv").hide();
    });

    hostStart.addEventListener("click", function(event) {
      $("#menuDiv").show();
      $("#joinDiv").hide();
      $("#mainDiv").hide();
      $("#hostDiv").hide();
    });

    hostStart.addEventListener("click", function(event) {
      $("#menuDiv").show();
      $("#joinDiv").hide();
      $("#mainDiv").hide();
      $("#hostDiv").hide();
    });

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

    // Simple Tests

    var players = [{color: "red", id: "1", name: "Ian", score: "314", numMeeples: 8}];
    var currTile = {center: null, top: null, bottom: null, left: null, right: null, id: "test", rotation: 0, shield: false};

    var r = new Renderer(null, currTile, players, null);
    r.renderTile();
    r.renderPlayers();

    // sb.addEventListener("keyup", function(event) {
    //   var text = document.getElementById("startbox").value;

    //   var postParameters = {inputStart: JSON.stringify(text)};
    //   console.log(postParameters);

    //   $.post("/suggestions", postParameters, function(responseJSON) {
    //     console.log("Received suggestions: " + responseJSON);
    //     responseObject = JSON.parse(responseJSON);
    //     var suggestionsList = responseObject.startSuggestions;

    //     displayStart(suggestionsList);
    //   });
    // });

  });