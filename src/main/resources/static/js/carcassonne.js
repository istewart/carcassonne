var renderer; // global rendering object
var network; // global networking object for realtime communication

// main function to configure the web page
 $(document).ready(function() {
    Menu();
    PlacementButtons();
    Canvas();

    network = new Network(); // TODO
  });