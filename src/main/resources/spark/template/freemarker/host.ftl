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
    <h1>Host</h1>

    <form action="/launch">
      <textarea id="name" class="hostfield" rows="1" placeholder="Game Name"></textarea></br>
      <textarea id="ip" class="hostfield" rows="1" placeholder="I.P. Address"></textarea></br>
      <input type="submit" value="Start" class="hoststart">
    </form>

  </body>
</html>