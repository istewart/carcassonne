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

      // Simple Tests

      var players = [{color: "red", id: "1", name: "Ian", score: "314", numMeeples: 8}, 
                   {color: "blue", id: "2", name: "Scott", score: "32", numMeeples: 4}];
      var currTile = {center: null, top: null, bottom: null, left: null, right: null, id: "1", rotation: 90, shield: false};
      var validMoves = [{x: 0, y: 0}, {x: 1, y: 0}, {x: 0, y: -2}, {x: 5, y: 5}];
      var board = [{tile: {id: "2", rotation: -90}, pos: {x: -1, y: 0}, meeple: {meeplePlacement: "CENTER", player: {color: "blue", id: "2", name: "Scott", score: "32", numMeeples: 4}}}, 
                   {tile: {id: "3"}, pos: {x: -1, y: 1}}, 
                   {tile: {id: "4"}, pos: {x: 0, y: -1}}];
      var validMeeples = ["UP", "CENTER", "RIGHT"];

      renderer = new Renderer(board, currTile, players, validMoves, validMeeples, 1, 0, 0);
      renderer.render();

      network.say("newPlayer", $("#joinName").val());
      network.ask("gameStart", "gameStart", function(responseObject) {
        var currTile = responseObject.currTile;
        var board = responseObject.board;
        var validMoves = responseObject.validMoves;
        var validMeeples = responseObject.validMoves;
        var players = currTile.players;

        renderer = new Renderer(board, currTile, players, validMoves, validMeeples, 1, 0, 0);

        renderer.render();
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