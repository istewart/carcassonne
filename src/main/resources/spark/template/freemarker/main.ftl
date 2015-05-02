<!DOCTYPE html>
<html>
  <head>
   
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/main.css">
    <!-- <link rel="stylesheet" href="css/menu.css"> -->
    <link href="http://fonts.googleapis.com/css?family=Merienda+One" rel="stylesheet" type="text/css">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="js/network.js"></script>
    <script src="js/renderer.js"></script>
    <script src="js/menu.js"></script>
    <script src="js/placement.js"></script>
    <script src="js/canvas.js"></script>
    <script src="js/carcassonne.js"></script>
    
    
    <title> Carcassonne </title>
  </head>
  
  <body onload="network.onLoad()">
    
    <div id="mainDiv">

      <div id="sidebar">
        <canvas id="tileCanvas" height="1000" width="1000"></canvas>
        
        <div>
            <button id="mainLeft" class="placeButton"> &#x27f2 </button>
            <button id="mainPlace" class="placeButton"> Place Tile </button>
            <button id="mainRight" class="placeButton"> &#x27F3 </button>
            <button id="mainMeeple" class="placeButton"> Place Meeple </button>
            <button id="mainSkip" class="placeButton"> Skip Meeple </button>
        </div>

        <form class="players">
          <textarea id="playerOne" class="playerBox" rows="1" readonly></textarea></br>
          <textarea id="playerTwo" class="playerBox" rows="1" readonly></textarea></br>
          <textarea id="playerThree" class="playerBox" rows="1" readonly></textarea></br>
          <textarea id="playerFour" class="playerBox" rows="1" readonly></textarea></br>
        </form>

      </div>

      <div id="contentDiv">
        <canvas id="mainCanvas" height="1000" width ="1000"></canvas>
      </div>
    </div>


    <div id="menuDiv">
      <h1>Carcassonne</h1>
      <p>Tell your friends to join this game by visiting <a href=${url}>${url}</a> while on the same LAN.</p>

      <button class="menubutton" id="joinButton"> Join </button></br>
      <button class="menubutton" id="settingsButton"> Settings </button></br>
      <button class="menubutton" id="instructionsButton"> Instructions </button></br>
    </div>

    <div id="joinDiv">
      <h1>Join</h1>

      <textarea id="joinName" class="hostField" rows="1" placeholder="Your Name"></textarea></br>
      <button class="submitButton" id="joinJoin"> Enter Lobby </button><br>
      <button class="backButton" id="joinBack"> Back </button>
    </div>

    <div id="lobbyDiv">
      <h1>Lobby</h1>
      <h2>Current Players</h2>
      <form class="players">
        <textarea id="playerOne" class="lobbyBox" rows="1" readonly></textarea></br>
        <textarea id="playerTwo" class="lobbyBox" rows="1" readonly></textarea></br>
        <textarea id="playerThree" class="lobbyBox" rows="1" readonly></textarea></br>
        <textarea id="playerFour" class="lobbyBox" rows="1" readonly></textarea></br>
      </form><br>
      <button class="submitButton" id="startGame"> Start </button><br>
      <button class="backButton" id="lobbyBack"> Back </button>
    </div>

      
    <div id="settingsDiv">
      <h1>Settings</h1>

      <input type="checkbox" name="hints" value="True" id="settingsHints" checked> Show Hints<br>
      <button class="submitButton" id="settingsSave"> Save </button><br>
      <button class="backButton" id="settingsBack"> Back </button>
    </div>

    <div id="instructionsDiv">
      <h1>Instructions</h1>
	  <h2><u>Game Overview</u></h2>
      <p>Carcassonne is a board game in which you build the board as you play. Every turn, players place a tile,
      creating cities and roads as the game progresses. To score points, one must place a meeple on tile. Scoring
      happens after this placement happens each turn, throughout the game. As such, each turn consists of three 
      actions: place, meeple, and score. </p> 
      
      <h2><u>Tile Placement</u></h2>
      <p>At the start of their turn, a player first draws a tile, which is displayed in the top right of the screen.
      This tile is the next tile that they must play somewhere on the board. Tiles may only be placed so that they
      are adjacent to some tile already on the board. This placement must "fit" with the pieces previously placed
      on the board. The images below show good and bad tile placement. The first tiles in the deck are always the 
      river tiles. River placement operates the same way, except for the rule that you can never place two bends in
      the same direction one after the other</p>
      
      tile placement images here

	  <h2><u>Meeple Placement</u></h2>
      <p>After placing a tile, a player has the opportunity to place a meeple. Meeples are Carcassonne's scoring
      mechanism - to get points, you have to place meeples. You cannot place a meeple on a feature which connects
      to a previously placed meeple. However, you can connect to distinct features and have multiple meeples there.
      The details of these more complicated scenarios are found below.</p> 

	  meeple placement images here
	  
	  <h2><u>Scoring</u></h2>
      <p> </p> 

      <p> Scoring happens after meeples are placed, so if a feature is
      completed by placing a tile, one could place a meeple there, and it would be scored on the same
      turn. Games end once all tiles are placed, and the remaining meeples on the board are scored. Monasteries and roads
      are scored the same way as during the game, except they do not have to be completed.
      Cities are scored too, but each tile is now worth one point, rather than the two awarded for in game completion. </p> 

      <p> The player with the most points at the end of the game wins! </p>

      <button class="backButton" id="instructionsBack"> Back </button>
    </div>

    <div id="tileDiv">
      <img src="images/tiles/test.png" id="test">

      <img src="images/tiles/1.png" id="1">
      <img src="images/tiles/2.png" id="2">
      <img src="images/tiles/3.png" id="3">
      <img src="images/tiles/4.png" id="4">
      <img src="images/tiles/5.png" id="5">
      <img src="images/tiles/6.png" id="6">
      <img src="images/tiles/7.png" id="7">
      <img src="images/tiles/8.png" id="8">
      <img src="images/tiles/9.png" id="9">
      <img src="images/tiles/10.png" id="10">
      <img src="images/tiles/11.png" id="11">
      <img src="images/tiles/12.png" id="12">
      <img src="images/tiles/13.png" id="13">
      <img src="images/tiles/14.png" id="14">
      <img src="images/tiles/15.png" id="15">
      <img src="images/tiles/16.png" id="16">
      <img src="images/tiles/17.png" id="17">
      <img src="images/tiles/18.png" id="18">
      <img src="images/tiles/19.png" id="19">
      <img src="images/tiles/20.png" id="20">
      <img src="images/tiles/21.png" id="21">
      <img src="images/tiles/22.png" id="22">
      <img src="images/tiles/23.png" id="23">
      <img src="images/tiles/24.png" id="24">
      <img src="images/tiles/25.png" id="25">
      <img src="images/tiles/26.png" id="26">
      <img src="images/tiles/27.png" id="27">
      <img src="images/tiles/28.png" id="28">
      <img src="images/tiles/29.png" id="29">
      <img src="images/tiles/30.png" id="30">
      <img src="images/tiles/31.png" id="31">
      <img src="images/tiles/32.png" id="32">
      <img src="images/tiles/33.png" id="33">
      <img src="images/tiles/34.png" id="34">
      <img src="images/tiles/35.png" id="35">
    </div>

  </body>
</html>