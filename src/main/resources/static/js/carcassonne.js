// put main game board in a different file
// hard code in tiles and rendering
// change help to instructions

 $(document).ready(function() {

    console.log("Script started!");

    var hostDiv = document.getElementById("hostDiv");
    var joinDiv = document.getElementById("joinDiv");
    var mainDiv = document.getElementById("mainDiv");
    var menuDiv = document.getElementById("menuDiv");

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