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
        <p id = "tileTitle"> Current Tile </p>
        <canvas id="tileCanvas" height="1000" width="1000"></canvas>
        	
        
        <div>
            <button id="mainLeft" class="placeButton"> &#x27f2 </button>
            <button id="mainPlace" class="placeButton"> Place Tile </button>
            <button id="mainRight" class="placeButton"> &#x27F3 </button>
            <button id="mainMeeple" class="placeButton"> Place Meeple </button>
            <button id="mainSkip" class="placeButton"> Skip Meeple </button>
        </div>

        <form class="players">
          <textarea id="playerOne" class="playerBox" rows="1" disabled></textarea></br>
          <textarea id="playerTwo" class="playerBox" rows="1" disabled></textarea></br>
          <textarea id="playerThree" class="playerBox" rows="1" disabled></textarea></br>
          <textarea id="playerFour" class="playerBox" rows="1" disabled></textarea></br>
        </form>

        <textarea id="turnCount" rows="1" disabled></textarea>

      </div>
      
      <div id="contentDiv">
        <canvas id="mainCanvas" height="1000" width ="1000"></canvas>
        <div id="yourTurnDiv!">
          "It is now your turn!"
        </div>
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
        <textarea id="playerOne" class="lobbyBox" rows="1" disabled></textarea></br>
        <textarea id="playerTwo" class="lobbyBox" rows="1" disabled></textarea></br>
        <textarea id="playerThree" class="lobbyBox" rows="1" disabled></textarea></br>
        <textarea id="playerFour" class="lobbyBox" rows="1" disabled></textarea></br>
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

	  <h2>Game Overview</h2>

      <p>Carcassonne is a board game in which you build up the board as you play. Each turn, the current player places a tile,
      creating huge cities and winding roads as the game progresses. To score points, players must place a meeple on a tile and complete the feature (road, city, or monastary) that the meeple corresponds to. Scoring
      happens after placement each turn. As such, each turn consists of three 
      phases: place, meeple, and score. The player with the most points at the end of the game wins!</p>

    <h2>Tile Placement</h2>

      <p>At the start of their turn, a player draws a tile, which is displayed in the top right corner of 
      the screen. They must then place this tile somewhere on the board. Tiles may only be placed such that they
      are adjacent to some tile already on the board. Any feature of the new tile must match up with those of the surrounding tiles, much like in a puzzle. </p>
      
      <p class="imageBlock"><img src="images/instructions/roadroad.png" id="rr">
      <img src="images/instructions/citycity.png" id="cc">
      <img src="images/instructions/cityroad.png" id="cr"></p>

      <p class="imageBlock">Valid placement of a tile with a road, a tile with a city, and a tile with both a city and a road.</p>
      
	  <h2>Meeple Placement</h2>

      <p>After placing a tile, a player may place a meeple. Meeples are Carcassonne's scoring
      mechanism -- to get points, you have to place meeples. Meeples can become one of three professions based on the tile on which they are placed: thief (on a road), knight (in a city), and monk (in a monastery). 
      Scoring is different for each of these cases. You 
      cannot place a meeple on a feature which connects to a previously placed meeple. However, you can connect 
      two distinct features and subsequently have multiple meeples there. The details of these more complicated
      scenarios are explained further below. A player may have a maximum of 7 meeples on the board at any time.</p> 

      <p class="imageBlock"><img src="images/instructions/meepleroad.png" id="mr">
      <img src="images/instructions/meeplecity.png" id="mc">
      <img src="images/instructions/meeplemonastery.png" id="mm"></p>

      <p class="imageBlock">Valid placement of a meeple on a road, a city, and a monastery.</p>
	  
	  <h2>Scoring Roads</h2>

      <p>If a meepled road is completed, either by forming a circle or touching an endpoint, the player who meepled
      the road gains one point for each tile touched by that completed road.</p> 
      
      <p class="imageBlock"><img src="images/instructions/roadCircle.png" id="rc">
      <img src="images/instructions/roadEndpoint.png" id="re"></p>

      <p class="imageBlock">One circular finished road and one finished road with endpoints.</p>

	  <h2>Scoring Cities</h2>

      <p>If a meepled city is completed, the player who meepled the city gains two point for each tile touched by 
      that completed city. Additionally, if any tile contains a shield image, that tile is worth twice as much.</p> 
      
      <p class="imageBlock"><img src="images/instructions/city.png" id="c">
      <img src="images/instructions/cityshield.png" id="cs"></p>

      <p class="imageBlock">Two finished cities, one with shields.</p>
      
    <h2>Scoring Monasteries</h2>

      <p>If a meepled monastery is surrounded by played tiles, 9 points are awarded to the player who meepled that
      monastery (one for each tile, including the tile with the monastery).</p>
      
      <p class="imageBlock"><img src="images/instructions/monastery.png" id="m"></p>

      <p class="imageBlock">A finished monastery.</p>
      
    <h2>Multiple Meeples</h2>

      <p>Even though you cant place a meeple on a feature that already has a touching meeple, it is possible to
      connect features which have distinct meeples. If two players have a meeple on a feature, and that feature is
      completed, both players receive the points. In the scenario where there are three meeples on a feature, and one
      player's outnumber anothers, the player with more meeples receives all the points. If a player has two meeples
      on a feature which is completed, they do not receive double points for the completion of that feature. </p>
      
      <p class="imageBlock"><img src="images/instructions/citytwoplayers.png" id="c2p">
      <img src="images/instructions/citytwoplayersthreemeeples.png" id="c2p3">
      <img src="images/instructions/cityoneplayertwomeeples.png" id="c1p"></p>

      <p class="imageBlock">Scenario where both players get the points, the player with more meeples gets the points, and the player
      gets the points only once.</p>
      
    <h2>Endgame Scoring</h2>

      <p>At the end of the game, once all tiles have been played, the meeples which remain on the board are scored.
      For monasteries and roads, scoring is done the same way as in game, except here features need not be completed
      to be scored. For cities however, everything which is not completed is worth half as much (shields included).
      Finish your cities! </p>
      
      <p class="imageBlock"><img src="images/instructions/roadUnfinished.png" id="ru">
      <img src="images/instructions/monasteryUnfinished.png" id="mu">
      <img src="images/instructions/cityUnfinished.png" id="cu"></p>

	    <p class="imageBlock">An unfinished road, monastery, and city, to be scored at the end of a game.</p>

      <p class="imageBlock"><button class="backButton" id="instructionsBack"> Back </button></p>
    </div>

    <div id="tileDiv">
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