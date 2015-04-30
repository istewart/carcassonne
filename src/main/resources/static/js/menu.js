var Menu = function() {
  var joined = false;
  var joinButton = document.getElementById("joinButton");
  var settingsButton = document.getElementById("settingsButton");
  var instructionsButton = document.getElementById("instructionsButton");

  var joinJoin = document.getElementById("joinJoin");
  var lobbyBack = document.getElementById("lobbyBack");
  var startGame = document.getElementById("startGame");


  var mainLeft = document.getElementById("mainLeft");
  var mainPlace = document.getElementById("mainPlace");
  var mainRight = document.getElementById("mainRight");

  var hideAll = function() {
    $("#menuDiv").hide();
    $("#joinDiv").hide();
    $("#mainDiv").hide();
    $("#settingsDiv").hide();
    $("#instructionsDiv").hide();
    $("#lobbyDiv").hide();
  }

  joinButton.addEventListener("click", function(event) {
    hideAll();

    if (Menu.joined) {
      $("#lobbyDiv").show();
    } else {
      $("#joinDiv").show();
    }
  });

  startGame.addEventListener("click", function(event) {
    network.ask("gameStart", {"message": "gameStart"}, function(responseObject) {
        // responseObject is null

        var currTile = responseObject.currTile;
        var board = responseObject.board;
        var validMoves = responseObject.validMoves;
        var validMeeples = responseObject.validMeeples;
        var players = responseObject.players;

        renderer = new Renderer(board.board, currTile, players, validMoves, validMeeples, 1, 0, 0);
        renderer.render();

        hideAll();
        $("#mainDiv").show();
      });
  })

  joinJoin.addEventListener("click", function(event) {
    hideAll();
    $("#lobbyDiv").show();
    $("#joinButton").html(" Back to Lobby ");

    Menu.joined = true;
    network.ask("newPlayer", {"name": $("#joinName").val()}, function(responseObject) {
      console.log(responseObject);
    });
  });

  lobbyBack.addEventListener("click", function(event) {
    hideAll();
    $("#joinDiv").show();
  });

  settingsButton.addEventListener("click", function(event) {
    hideAll();
    $("#settingsDiv").show();
  });

  instructionsButton.addEventListener("click", function(event) {
    hideAll();
    $("#instructionsDiv").show();
  });

  var goBack = function() {
    hideAll();
    $("#menuDiv").show();
  }

  var backButtons = document.getElementsByClassName("backButton");
  for (var i = 0; i < backButtons.length; i++) {
    backButtons[i].addEventListener('click', goBack, false);
  };
}

