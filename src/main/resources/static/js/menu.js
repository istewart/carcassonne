var Menu = function() {
	  var hostButton = document.getElementById("hostButton");
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
      $("#hostDiv").hide();
      $("#settingsDiv").hide();
      $("#instructionsDiv").hide();
    }

    hostButton.addEventListener("click", function(event) {
      hideAll();
      $("#hostDiv").show();
    });

    joinButton.addEventListener("click", function(event) {
      hideAll();
      $("#joinDiv").show();
    });

    joinJoin.addEventListener("click", function(event) {
      hideAll();
      $("#mainDiv").show();

      // Simple Tests

      var players = [{color: "red", id: "1", name: "Ian", score: "314", numMeeples: 8}, 
                   {color: "blue", id: "2", name: "Scott", score: "32", numMeeples: 4}];
      var currTile = {center: null, top: null, bottom: null, left: null, right: null, id: "test", rotation: 90, shield: false};
      var validMoves = [{x: 0, y: 0}, {x: 1, y: 0}, {x: 0, y: -2}, {x: 5, y: 5}];
      var board = [{tile: {id: "test", rotation: -90}, pos: {x: -1, y: 0}, meeple: {meeplePlacement: "CENTER", player: {color: "blue", id: "2", name: "Scott", score: "32", numMeeples: 4}}}, 
                   {tile: {id: "test"}, pos: {x: -1, y: 1}}, 
                   {tile: {id: "test"}, pos: {x: 0, y: -1}}];
      var validMeeples = ["UP", "CENTER", "RIGHT"];

      renderer = new Renderer(board, currTile, players, validMoves, validMeeples, 1, 0, 0);
      renderer.render();
    });

    hostStart.addEventListener("click", function(event) {
      hideAll();
      $("#menuDiv").show();
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