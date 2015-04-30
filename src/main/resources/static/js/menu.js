var Menu = function() {
    var joinButton = document.getElementById("joinButton");
    var settingsButton = document.getElementById("settingsButton");
    var instructionsButton = document.getElementById("instructionsButton");

    var joinJoin = document.getElementById("joinJoin");
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
      console.log('hidden');
    }

    joinButton.addEventListener("click", function(event) {
      hideAll();
      $("#joinDiv").show();
    });

    gameStart.addEventListener("click", function(event) {
      network.ask("gameStart", {"message": "gameStart"}, function(responseObject) {
          // responseObject is null

          var currTile = responseObject.currTile;
          var board = responseObject.board;
          var validMoves = responseObject.validMoves;
          var validMeeples = responseObject.validMeeples;
          var players = responseObject.players;

          renderer = new Renderer(board.board, currTile, players, validMoves, validMeeples, 1, 0, 0);
          renderer.render();
        });
    })

    joinJoin.addEventListener("click", function(event) {
      hideAll();
      $("#mainDiv").show();

      network.ask("newPlayer", {"name": $("#joinName").val()}, function(responseObject) {
        console.log(responseObject);
      });
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