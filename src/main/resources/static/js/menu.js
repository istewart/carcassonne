var Menu = function () {
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

      // Simple Tests

      var players = [{color: "red", id: "1", name: "Ian", score: "314", numMeeples: 8}, 
                   {color: "blue", id: "2", name: "Scott", score: "32", numMeeples: 4}];
      var currTile = {center: null, top: null, bottom: null, left: null, right: null, id: "test", rotation: 90, shield: false};
      var validMoves = [{x: 0, y: 0}, {x: 1, y: 0}, {x: 0, y: -2}, {x: 5, y: 5}];
      var board = [{tile: {id: "test", rotation: -90}, pos: {x: -1, y: 0}}, {tile: {id: "test"}, pos: {x: -1, y: 1}}, {tile: {id: "test"}, pos: {x: 0, y: -1}}];

      var r = new Renderer(board, currTile, players, validMoves, 1, 0, 0);
      r.renderTile();
      r.renderPlayers();
      r.renderMoves();
      r.renderBoard();

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
}