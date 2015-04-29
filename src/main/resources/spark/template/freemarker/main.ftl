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
    
    <title>${title}</title>
  </head>
  
  <body onload="network.onLoad()">
    
    <div id="mainDiv">

      <div id="sidebar">
        <canvas id="tileCanvas" height="1000" width="1000"></canvas>
        <!-- <img src="" id="tileImage"> -->
        <div>
            <button id="mainLeft" class="placeButton"> Left </button>
            <button id="mainPlace" class="placeButton"> Place </button>
            <button id="mainRight" class="placeButton"> Right </button>
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

      <button class="menubutton" id="joinButton"> Join </button></br>
      <button class="menubutton" id="settingsButton"> Settings </button></br>
      <button class="menubutton" id="instructionsButton"> Instructions </button></br>
    </div>

    <div id="joinDiv">
      <h1>Join</h1>

      <textarea id="joinName" class="hostField" rows="1" placeholder="Your Name"></textarea></br>
      <button class="submitButton" id="joinJoin"> Start </button><br>
      <button class="backButton" id="joinBack"> Back </button>
    </div>
      
    <div id="settingsDiv">
      <h1>Settings</h1>

      <input type="checkbox" name="hints" value="True" checked> Show Hints<br>
      <button class="submitButton" id="settingsSave"> Save </button><br>
      <button class="backButton" id="settingsBack"> Back </button>
    </div>

    <div id="instructionsDiv">
      <h1>Instructions</h1>

      <p> Something explaining rules here. </p>

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
    </div>

  </body>
</html>