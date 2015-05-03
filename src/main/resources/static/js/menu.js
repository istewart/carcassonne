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

  var mainMeeple = document.getElementById("mainMeeple");
  var mainSkip = document.getElementById("mainSkip");

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
    var name = $("#joinName").val();
    if (name.length > 12) {
      alert("Plaese limit your name to 12 characters");
    } else {
      hideAll();
      $("#lobbyDiv").show();
      $("#joinButton").html(" Back to Lobby ");
      Menu.joined = true;
      network.ask("newPlayer", {"name": $("#joinName").val()}, function(responseObject) {
        console.log(responseObject);
      });
    }
    
  });

  lobbyBack.addEventListener("click", function(event) {
    hideAll();
    $("#joinDiv").show();
  });

  settingsButton.addEventListener("click", function(event) {
    hideAll();
    $("#settingsDiv").show();
    console.log(showHints);
    console.log($("#settingsHints")[0].checked);
    $("#settingsHints")[0].checked = showHints;
  });

  $("#settingsSave")[0].addEventListener("click", function(event) {
    showHints = $("#settingsHints")[0].checked;
    console.log(showHints);

    hideAll();
    $("#menuDiv").show();
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

