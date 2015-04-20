<html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/html5bp.css">
    <link rel="stylesheet" href="css/networkmain.css">
  </head>
  <body onload="network.onLoad()">
    ${body} <br>
    Connect to this page by visiting ${url}.
    <div id="status">
      Loading...
    </div>

    <div id="connected">
      <!-- players statuses -->
    </div>

    <div id="chat">
      <!-- chat log -->
    </div>

    <script src="js/jquery-2.1.1.js"></script>
    <script src="js/network.js"></script>
    <script src="js/example.js"></script>
  </body>
</html>
