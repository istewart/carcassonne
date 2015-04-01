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
    <!-- // <script src="js/main.js"></script> -->
    
    <title>${title}</title>
  </head>
  
  <body>
    
    <div id="main_wrap">

      <div id="sidebar">
        <canvas id="tileCanvas"></canvas>
        <div>
          <form action="/left" class="placeform">
            <input type="submit" value="Left" class="placebutton">
          </form>
          <form action="/place" class="placeform">
            <input type="submit" value="Place" class="placebutton">
          </form>
          <form action="/right" class="placeform">
            <input type="submit" value="Right" class="placebutton">
          </form>
        </div>

        <form class="players">
          <textarea id="playerOne" class="playerbox" readonly></textarea></br>
          <textarea id="playerTwo" class="playerbox" readonly></textarea></br>
          <textarea id="playerThree" class="playerbox" readonly></textarea></br>
          <textarea id="playerFour" class="playerbox" readonly></textarea></br>
        </form>

      </div>

      <div id="content">
        <canvas id="mainCanvas"></canvas>
      </div>
    </div>

    <div id="menudiv">
      <h1>Carcassonne</h1>

      <form action="/host">
        <input type="submit" value="Host" class="menubutton">
      </form>
      <form action="/join">
        <input type="submit" value="Join" class="menubutton">
      </form>
      <form action="/settings">
        <input type="submit" value="Settings" class="menubutton">
      </form>
      <form action="/help">
        <input type="submit" value="Help" class="menubutton">
      </form>

    </div>

    <div id="joindiv">
      <h1>Join</h1>

      <form action="/connect">
        <textarea id="name" class="hostfield" rows="1" placeholder="Your Name"></textarea></br>
        <textarea id="ip" class="hostfield" rows="1" placeholder="Host I.P. Address"></textarea></br>
        <input type="submit" value="Join" class="hoststart">
      </form>

    </div>

    <div id="hostdiv">
      <h1>Host</h1>

      <form action="/launch">
        <textarea id="name" class="hostfield" rows="1" placeholder="Game Name"></textarea></br>
        <textarea id="ip" class="hostfield" rows="1" placeholder="Your I.P. Address"></textarea></br>
        <input type="submit" value="Start" class="hoststart">
      </form>
    
    </div>

  </body>
</html>