 TILE_SIZE = 50;

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

    Renderer.prototype.renderPlayers = function() {
      if (players == null || players.length == 0) {
        $('.playerBox').each(function(i, obj) {
          obj.innerHTML = "";
        });

        return;
      }

      $('.playerBox').each(function(i, obj) {
        if (i >= players.length) {
          obj.innerHTML = "";
          return true; // equivalent to continue;
        }

        console.log(i);
        var currPlayer = players[i];
        obj.value = "" + currPlayer.name + ": " + currPlayer.score + " Meeples: " + currPlayer.numMeeples;
        obj.style.color = currPlayer.color;
      });
    };

    Renderer.prototype.renderTile = function() {
      var currImage = document.getElementById(this.currTile.id);
      var tileImage = document.getElementById("tileImage");

      tileImage.src = currImage.src;
      $("#tileImage").rotate(this.currTile.rotation);
      $("#tileImage").show();

      // var c = document.getElementById("myCanvas");
      // var ctx = c.getContext("2d");

      // ctx.drawImage(tileImage, 0, 0);
    };

    Renderer.prototype.renderBoard = function(scale, xt, yt) { // still very much a work in progress
      var mainCanvas = document.getElementById("mainCanvas");
      var ctx = mainCanvas.getContext("2d");

      var tileMap = board.board;

      var canvasHeight = $("#mainCanvas").height();
      var canvasWidth = $("#mainCanvas").width();
      var centerY = (canvasHeight / 2) + yt * TILE_SIZE * scale;
      var centerX = (canvasWidth / 2) + xt * TILE_SIZE * scale;

      for (var i = 0; i < tileMap.length; i++) {
        var currPair = tileMap[i];

        var xCord = currPair.x;
        var yCord = currPair.y;

        var targetY = centerY + yCord * TILE_SIZE * scale;
        var targetX = centerY + yCord * TILE_SIZE * scale;

        var targetSize = TILE_SIZE * scale;

        var tileObj = currPair.tile;
        var targetImg = document.getElementById(tileObj.id);

        var targetRadians = tileObj.rotation * Math.PI / 180;

        ctx.rotate(targetRadians);
        ctx.drawImage(targetImg, targetX, targetY, targetSize, targetSize);
        ctx.rotate(-targetRadians);
      }
    };

    Renderer.prototype.renderMoves = function() {
      // body...
    };

    Renderer.prototype.render = function() {
      renderBoard();
      renderTile();
      renderMoves();
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

    var players = [{color: "red", id: "1", name: "Ian", score: "314", numMeeples: 8}, 
                   {color: "blue", id: "2", name: "Scott", score: "32", numMeeples: 4}];
    var currTile = {center: null, top: null, bottom: null, left: null, right: null, id: "test", rotation: 90, shield: false};

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