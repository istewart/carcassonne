var renderer; // global rendering object
var isPlaced = false; // if a tile has been placed but not meepled
var showHints = true; // if meeple and tile hints should be displayed

// main function to configure the web page
var handler = {
  connect: function() {
    Menu();
    PlacementButtons();
    Canvas();
  },
}
