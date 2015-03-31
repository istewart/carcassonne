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


  </body>
</html>