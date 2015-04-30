var Menu = function() {
    var joinButton = document.getElementById("joinButton");
    var settingsButton = document.getElementById("settingsButton");
    var instructionsButton = document.getElementById("instructionsButton");

    var joinJoin = document.getElementById("joinJoin");

    var mainLeft = document.getElementById("mainLeft");
    var mainPlace = document.getElementById("mainPlace");
    var mainRight = document.getElementById("mainRight");

    var hideAll = function() {
      $("#menuDiv").hide();
      $("#joinDiv").hide();
      $("#mainDiv").hide();
      $("#settingsDiv").hide();
      $("#instructionsDiv").hide();
    }

    joinButton.addEventListener("click", function(event) {
      hideAll();
      $("#joinDiv").show();
    });

    joinJoin.addEventListener("click", function(event) {
      hideAll();
      $("#mainDiv").show();

      network.ask("newPlayer", {"name": $("#joinName").val()}, function(responseObject) {
        console.log(responseObject);

        network.ask("gameStart", {"message": "gameStart"}, function(responseObject) {
          // responseObject is null

          var currTile = network.get("currTile");
          var board = network.get("board");
          var validMoves = network.get("validMoves");
          var validMeeples = network.get("validMeeples");
          var players = network.get("players");

          renderer = new Renderer(board.board, currTile, players, validMoves, validMeeples, 1, 0, 0);
          renderer.render();
        });
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