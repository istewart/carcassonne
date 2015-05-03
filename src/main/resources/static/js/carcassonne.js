var renderer; // global rendering object
var isPlaced = false; // if a tile has been placed but not meepled
var showHints = true; // if meeple and tile hints should be displayed
var myTurn = false; // if its my turn

function hideAll() {
  $("#menuDiv").hide();
  $("#joinDiv").hide();
  $("#mainDiv").hide();
  $("#settingsDiv").hide();
  $("#instructionsDiv").hide();
  $("#lobbyDiv").hide();
  $("#turnDiv").hide();
  resize();
}

// main function to configure the web page
var handler = {
  connect: function() {
    if (network.get("gameStart")) {
      handler.gameStart();
    } else {
      hideAll();
      $("#menuDiv").show();
      var players = network.get("players");
      for (key in  players) {
        if (players[key].id == network.id) {
          $("#joinButton").html(" Back to Lobby ");

          Menu.joined = true;
        }
      }
    }


    Menu();
    PlacementButtons();
    Canvas();
  },

  board: function() {
    renderer.render();
  },

  players: function(playersList) {

    if (playersList == null || playersList.length == 0) {
      $('.lobbyBox').each(function(i, obj) {
        obj.innerHTML = "";
      });

      return;
    } else {
      $('.lobbyBox').each(function(i, obj) {
        if (i >= playersList.length) {
          obj.innerHTML = "";
          return true; // equivalent to continue;
        }

        var currPlayer = playersList[i];
        obj.value = "" + currPlayer.name;
        obj.style.color = currPlayer.color;
      });
    }

    renderer.players = playersList;

  },

  gameStart: function(state) {
    if (state == true) {
      var n = network;

      hideAll();
      $("#mainDiv").show();

      if (network.get("currentPlayer").id != network.id) {
        $("#mainLeft").hide();
        $("#mainRight").hide();
        $("#mainPlace").hide();
        $("#mainMeeple").hide();
        $("#mainSkip").hide();
      }

      var currTile = n.get("currTile");
      var board = n.get("board");
      var validMoves = n.get("validMoves");
      var validMeeples = n.get("validMeeples");
      var players = n.get("players");

      renderer = new Renderer(board.board, currTile, players, validMoves, validMeeples, 1, 0, 0);
      renderer.render();
    }
  },

  currentPlayer: function(player) {
    if (player.id == network.id) {
      myTurn = true;

      renderer.render();
      $("yourTurnDiv").show();
      $("yourTurnDiv").hide(300);
      $("#mainLeft").show();
      $("#mainRight").show();
      $("#mainPlace").show();
      $("#turnDiv").show();
      setInterval(function() {$("#turnDiv").hide();}, 5000);
    } else {
      myTurn = false;
      renderer.render();
      $("#mainLeft").hide();
      $("#mainRight").hide();
      $("#mainPlace").hide();
      $("#mainMeeple").hide();
      $("#mainSkip").hide();
    }
    console.log("MyTurn? Player id from ian:" + player.id + " Player id from network: " + network.id);
  },

  currTile: function(tile) {
    renderer.currTile = tile;
    renderer.render();
  },

  board: function(board) {
    renderer.board = board.board;
    renderer.render();
  },

  validMoves: function(validMoves) {
    renderer.validMoves = validMoves;
    renderer.render();
  },

  validMeeples: function(validMeeples) {
    console.log("meeple");
    renderer.validMeeples = validMeeples;
    renderer.render();
  },

  disconnect: function() {
    grayOut(true);
    alert("You appear to have disconnected. The game will return when you reconnect.");
  },

  reconnect: function() {
    grayOut(false);
  },

  fatal: function() {
    grayOut(true);
  },

  gameOver: function(val) {
    if (val == true) {
      alert("Game Over!");
      renderer.players = network.get("players");
      renderer.board = network.get("board");
      renderer.validMoves = network.get("validMoves");
      renderer.validMeeples = network.get("validMeeples");
      renderer.currTile = null;
      renderer.selectedMeeple = null;
      renderer.selectedTile = null;
      renderer.render();
    }
  },

  connected: function() {
    renderer.render();
  },
}

function grayOut(vis, options) {
  // Pass true to gray out screen, false to ungray
  // options are optional.  This is a JSON object with the following (optional) properties
  // opacity:0-100         // Lower number = less grayout higher = more of a blackout 
  // zindex: #             // HTML elements with a higher zindex appear on top of the gray out
  // bgcolor: (#xxxxxx)    // Standard RGB Hex color code
  // grayOut(true, {'zindex':'50', 'bgcolor':'#0000FF', 'opacity':'70'});
  // Because options is JSON opacity/zindex/bgcolor are all optional and can appear
  // in any order.  Pass only the properties you need to set.
  var options = options || {}; 
  var zindex = options.zindex || 50;
  var opacity = options.opacity || 70;
  var opaque = (opacity / 100);
  var bgcolor = options.bgcolor || '#000000';
  var dark=document.getElementById('darkenScreenObject');
  if (!dark) {
    // The dark layer doesn't exist, it's never been created.  So we'll
    // create it here and apply some basic styles.
    var tbody = document.getElementsByTagName("body")[0];
    var tnode = document.createElement('div');           // Create the layer.
        tnode.style.position='absolute';                 // Position absolutely
        tnode.style.top='0px';                           // In the top
        tnode.style.left='0px';                          // Left corner of the page
        tnode.style.overflow='hidden';                   // Try to avoid making scroll bars            
        tnode.style.display='none';                      // Start out Hidden
        tnode.id='darkenScreenObject';                   // Name it so we can find it later
    tbody.appendChild(tnode);                            // Add it to the web page
    dark=document.getElementById('darkenScreenObject');  // Get the object.
  }
  if (vis) {
    // Calculate the page width and height 
    if( document.body && ( document.body.scrollWidth || document.body.scrollHeight ) ) {
        var pageWidth = document.body.scrollWidth+'px';
        var pageHeight = document.body.scrollHeight+'px';
    } else if( document.body.offsetWidth ) {
      var pageWidth = document.body.offsetWidth+'px';
      var pageHeight = document.body.offsetHeight+'px';
    } else {
       var pageWidth='100%';
       var pageHeight='100%';
    }   
    //set the shader to cover the entire page and make it visible.
    dark.style.opacity=opaque;                      
    dark.style.MozOpacity=opaque;                   
    dark.style.filter='alpha(opacity='+opacity+')'; 
    dark.style.zIndex=zindex;        
    dark.style.backgroundColor=bgcolor;  
    dark.style.width= pageWidth;
    dark.style.height= pageHeight;
    dark.style.display='block';        
  } else {
     dark.style.display='none';
  }
}
