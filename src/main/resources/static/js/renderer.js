TILE_SIZE = 60;
CANVAS_SIZE = 1000;

// can clean things up with global context and canvas
// take a look at clearing and tile spacing
// do click and drag better
// add a better background to the board

function Renderer(board, currTile, players, validMoves, scale, xt, yt) {
  this.board = board;
  this.currTile = currTile;
  this.players = players;
  this.validMoves = validMoves;

  this.scale = scale;
  this.xt = xt;
  this.yt = yt;

  console.log(this);
}

Renderer.prototype.renderPlayers = function() {
  var playersList = this.players;

  if (playersList == null || playersList.length == 0) {
    $('.playerBox').each(function(i, obj) {
      obj.innerHTML = "";
    });

    return;
  }

  $('.playerBox').each(function(i, obj) {
    if (i >= playersList.length) {
      obj.innerHTML = "";
      return true; // equivalent to continue;
    }

    var currPlayer = playersList[i];
    obj.value = "" + currPlayer.name + ": " + currPlayer.score + " Meeples: " + currPlayer.numMeeples;
    obj.style.color = currPlayer.color;
  });
};

Renderer.prototype.renderTile = function() {
  var currImage = document.getElementById(this.currTile.id);
  var tileImage = document.getElementById("tileImage");
  tileImage.src = currImage.src;

  $("#tileImage").rotate(this.currTile.rotation);
  $("#tileImage").show();
};

// var c = document.getElementById("myCanvas");
// var ctx = c.getContext("2d");
// ctx.drawImage(tileImage, 0, 0);

Renderer.prototype.renderBoard = function() { // still very much a work in progress
  var mainCanvas = document.getElementById("mainCanvas");
  var ctx = mainCanvas.getContext("2d");

  var tileMap = this.board;

  for (var i = 0; i < tileMap.length; i++) {
    var currTile = tileMap[i];

    var targetPlacement = this.posToCanvas(currTile.pos);
    var tileObj = currTile.tile;

    var targetImg = document.getElementById(tileObj.id);
    var targetRadians = tileObj.rotation * Math.PI / 180;

    if (targetRadians) { // fancy way of drawing an image rotated about it's center
      ctx.translate(targetPlacement.x + (targetPlacement.s / 2), targetPlacement.y + (targetPlacement.s / 2));
      ctx.rotate(targetRadians);
      ctx.drawImage(targetImg, -(targetPlacement.s / 2), -(targetPlacement.s / 2), targetPlacement.s, targetPlacement.s);
      ctx.rotate(-targetRadians);
      ctx.translate(-targetPlacement.x - (targetPlacement.s / 2), -targetPlacement.y - (targetPlacement.s / 2));
    } else { // tile not rotated
      ctx.drawImage(targetImg, targetPlacement.x, targetPlacement.y, targetPlacement.s, targetPlacement.s);
    }
    
  }

  return;
 };

Renderer.prototype.renderMoves = function() {
  var mainCanvas = document.getElementById("mainCanvas");
  var ctx = mainCanvas.getContext("2d");

  var moves = this.validMoves;

  for (var i = 0; i < moves.length; i++) {
    var pos = moves[i];
    var targetPlacement = this.posToCanvas(pos);

    ctx.strokeStyle = "red";
    ctx.rect(targetPlacement.x, targetPlacement.y, 
             targetPlacement.s, targetPlacement.s);
    ctx.stroke();
  }

  return;
};

Renderer.prototype.render = function() {
  var canvas = document.getElementById("mainCanvas");
  var ctx = canvas.getContext("2d");

  // Store the current transformation matrix
  ctx.save();

  // Use the identity matrix while clearing the canvas
  ctx.setTransform(1, 0, 0, 1, 0, 0);
  ctx.beginPath();
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  ctx.stroke();

  // Restore the transform
  ctx.restore();

  this.renderBoard();
  this.renderTile();
  this.renderMoves();
  this.renderPlayers();
};

// takes a pos {x: x, y: y}
// returns a pos mapped to the canvas space with {x: x, y: y, s: size}
Renderer.prototype.posToCanvas = function(pos) {
  var canvasHeight = document.getElementById("mainCanvas").height;
  var canvasWidth = document.getElementById("mainCanvas").width;

  var targetSize = TILE_SIZE * this.scale;

  var centerX = (canvasWidth / 2) - (targetSize / 2) + (this.xt * targetSize);
  var centerY = (canvasHeight / 2) - (targetSize / 2) + (this.yt * targetSize);

  var targetX = centerX + (pos.x * targetSize);
  var targetY = centerY + (-pos.y * targetSize);

  return {x: targetX, y: targetY, s: targetSize};
};

Renderer.prototype.pixelsToCanvas = function(pixPos) {
  var canvasUnitHeight = document.getElementById("mainCanvas").height; // canvas units
  var canvasPixHeight = $("#mainCanvas").height(); // canvas pix

  var canvasUnitWidth = document.getElementById("mainCanvas").width; // canvas units
  var canvasPixWidth = $("#mainCanvas").width(); // canvas pix

  var canvasX = pixPos.x * (canvasUnitWidth / canvasPixWidth);
  var canvasY = pixPos.y * (canvasUnitHeight / canvasPixHeight);

  return {x: canvasX, y: canvasY};
}

Renderer.prototype.canvasDiffToPosDiff = function(canvasPos) {
  var canvasHeight = document.getElementById("mainCanvas").height;
  var canvasWidth = document.getElementById("mainCanvas").width;

  var targetSize = TILE_SIZE * this.scale;

  var posX = (canvasPos.x) / targetSize;
  var posY = (canvasPos.y) / targetSize;

  return {x: posX, y: posY};
}

Renderer.prototype.canvasToPos = function(canvasPos) {
  var canvasHeight = document.getElementById("mainCanvas").height;
  var canvasWidth = document.getElementById("mainCanvas").width;

  var targetSize = TILE_SIZE * this.scale;

  var centerX = (canvasWidth / 2) + (this.xt * targetSize);
  var centerY = (canvasHeight / 2) + (this.yt * targetSize);

  var posX = (canvasPos.x - centerX) / targetSize;
  var posY = -((canvasPos.y - centerY) / targetSize);

  return {x: posX, y: posY};
}

Renderer.prototype.shadeMove = function(pos) {
  var mainCanvas = document.getElementById("mainCanvas");
  var ctx = mainCanvas.getContext("2d");

  var targetPlacement = this.posToCanvas(pos);

  ctx.fillStyle = "red";
  ctx.fillRect(targetPlacement.x, targetPlacement.y, 
           targetPlacement.s, targetPlacement.s);

  return;
};
