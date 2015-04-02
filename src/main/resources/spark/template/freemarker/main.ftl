<!DOCTYPE html>
<html>
  <head>
   
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../../../static/css/normalize.css">
    <link rel="stylesheet" href="../../../static/css/main.css">
    <!-- <link rel="stylesheet" href="../../../static/css/menu.css"> -->
    <link href="http://fonts.googleapis.com/css?family=Merienda+One" rel="stylesheet" type="text/css">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="../../../static/js/carcassonne.js"></script>
    
    <title>${title}</title>
  </head>
  
  <body>
    
    <div id="mainDiv">

      <div id="sidebar">
        <!-- <canvas id="tileCanvas"></canvas> -->
        <img src="" id="tileImage">
        <div>
            <button id="mainLeft" class="placeButton"> Left </button>
            <button id="mainPlace" class="placeButton"> Place </button>
            <button id="mainRight" class="placeButton"> Right </button>
        </div>

        <form class="players">
          <textarea id="playerOne" class="playerBox" readonly></textarea></br>
          <textarea id="playerTwo" class="playerBox" readonly></textarea></br>
          <textarea id="playerThree" class="playerBox" readonly></textarea></br>
          <textarea id="playerFour" class="playerBox" readonly></textarea></br>
        </form>

      </div>

      <div id="content">
        <canvas id="mainCanvas"></canvas>
      </div>
    </div>

    <div id="menuDiv">
      <h1>Carcassonne</h1>

      <button class="menubutton" id="hostButton"> Host </button></br>
      <button class="menubutton" id="joinButton"> Join </button></br>
      <button class="menubutton" id="settingsButton"> Settings </button></br>
      <button class="menubutton" id="helpButton"> Help </button></br>

    </div>

    <div id="hostDiv">
      <h1>Host</h1>

      <textarea id="hostName" class="hostField" rows="1" placeholder="Game Name"></textarea></br>
      <textarea id="hostIP" class="hostField" rows="1" placeholder="Your I.P. Address"></textarea></br>
      <button class="submitButton" id="hostStart"> Start </button>
    
    </div>

    <div id="joinDiv">
      <h1>Join</h1>

      <textarea id="joinName" class="hostField" rows="1" placeholder="Your Name"></textarea></br>
      <textarea id="joinIP" class="hostField" rows="1" placeholder="Host I.P. Address"></textarea></br>
      <button class="submitButton" id="joinJoin"> Start </button>

    </div>

    <div id = "tileDiv">
      <img src="../../../static/images/tiles/test.png" id="test">
    </div>

  </body>
</html>