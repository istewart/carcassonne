TILE_SIZE = 60;
CANVAS_SIZE = 1000;

MOVES_COLOR = "red";
LINE_WIDTH = 3;

var colors = ["maroon", "blue", "purple", "#003300" /* forest green */]; // player colors by id

// TODO

// get better tiles and graphics
// README
// DEBUG

// Core rendering object which takes a variety of components of the game, an offset, and a scale
// and renders the game board onto the canvas.
function Renderer(board, currTile, players, validMoves, validMeeples, scale, xt, yt) {
  this.board = board;
  this.currTile = currTile;
  this.players = players;
  this.validMoves = validMoves;
  this.validMeeples = validMeeples;

  this.scale = scale;
  this.xt = xt;
  this.yt = yt;

  this.mouseOver = false;
  this.tileMouseOver = false;

  console.log(this);
}

// Clears and re-renders the entire page.
Renderer.prototype.render = function() {
  console.log(this);

  var canvas = document.getElementById("mainCanvas");
  var ctx = canvas.getContext("2d");

  // Store the current transformation matrix
  ctx.save();

  // Use the identity matrix while clearing the canvas
  ctx.setTransform(1, 0, 0, 1, 0, 0);
  ctx.beginPath();
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  ctx.stroke();
  ctx.closePath();

  // Restore the transform
  ctx.restore();

  this.renderBoard();
  this.renderTile();
  this.renderMoves();
  this.renderPlayers();
  this.shadeMove();
};

// Renders the player list.
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
    obj.value = (network.get("connected")[playersList[i].id] ? 
      "" + currPlayer.name + ": " + currPlayer.score + " | Meeples: " + currPlayer.numMeeples 
      : "" + currPlayer.name + ": (disconnected)");

    if (network.get("currentPlayer") != null && (currPlayer.id === network.get("currentPlayer").id)) {
      obj.style.backgroundColor = "#222";
      obj.style.color = "#e8e8e8";
    } else {
      obj.style.backgroundColor = "#e8e8e8";
      obj.style.color = colors[currPlayer.id - 1];
    }
  });
};

// Renders the current tile, meeple slots, and selected meeple in the tile canvas.
Renderer.prototype.renderTile = function() {
  var tilesRemaining = network.get("tilesLeft");
  $("#turnCount")[0].value = "Tiles Remaining: " + tilesRemaining;

  var tileCanvas = document.getElementById("tileCanvas");
  var ctx = tileCanvas.getContext("2d");

  var w = tileCanvas.width;
  var h = tileCanvas.height;

  if (!this.currTile) { // tile is undefined or null
    ctx.fillStyle = "black";
    ctx.fillRect(0, 0, w, h);
    return;
  }

  var currImage = document.getElementById(this.currTile.pngID);
  var r = this.currTile.rotation * Math.PI / 180;

  if (r) { // fancy way of drawing an image rotated about it's center
    ctx.translate((w / 2), (h / 2));
    ctx.rotate(r);
    ctx.drawImage(currImage, -(w / 2), -(h / 2), w, h);
    ctx.rotate(-r);
    ctx.translate(-(w / 2), -(h / 2));
  } else { // tile not rotated
    ctx.drawImage(currImage, 0, 0, w, h);
  }

  var spots = this.validMeeples;
  var radius = w / 10;

  if (!spots || !myTurn) {
    return;
  }

  for (var i = 0; i < spots.length; i++) {
    var x;
    var y;

    switch(spots[i]) {
      case "UP": x = .5 * w; y = .2 * w; break;
      case "DOWN": x = .5 * w; y = .8 * w; break;
      case "RIGHT": x = .8 * w; y = .5 * w; break;
      case "LEFT": x =.2 * w; y = .5 * w; break;
      case "CENTER": x = .5 * w; y = .5 * w; break;
      default: alert("Meeple switch failed!")
    }

    if (this.selectedMeeple && spots[i] === this.selectedMeeple) {
      ctx.beginPath();
      ctx.lineWidth = 5 * LINE_WIDTH;
      ctx.arc(x, y, radius, 0, 2 * Math.PI);
      ctx.closePath();

      ctx.fillStyle = colors[network.get("currentPlayer").id - 1];
      ctx.strokeStyle = null;
      ctx.fill();
    } else if (showHints) {
      ctx.beginPath();
      ctx.lineWidth = 5 * LINE_WIDTH;
      ctx.arc(x, y, radius, 0, 2 * Math.PI);
      ctx.closePath();

      ctx.fillStyle = null;
      ctx.strokeStyle = colors[network.get("currentPlayer").id - 1];
      ctx.stroke();
    }
  }
};

// Renders the tiles on the game board as well as any meeples on them.
Renderer.prototype.renderBoard = function() { // still very much a work in progress
  var mainCanvas = document.getElementById("mainCanvas");
  var ctx = mainCanvas.getContext("2d");

  var tileMap = this.board;

  for (var posn in tileMap) {
    var posArray = posn.replace("(", "").replace(")", "").split(", ");
    var pos = {x: posArray[0], y: posArray[1]};

    var targetPlacement = this.posToCanvas(pos);
    var tileObj = tileMap[posn];

    var targetImg = document.getElementById(tileObj.pngID);
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

    var w = targetPlacement.s;
    var h = targetPlacement.s;
    var radius = targetPlacement.s / 10;

    var x = targetPlacement.x;
    var y = targetPlacement.y;

    var meeplePlayerId;

    if (tileObj.top.meeple) {
      x += w / 2;
      y += h / 4; 
      meeplePlayerId = tileObj.top.meeple.player.id;
    } else if (tileObj.bottom.meeple) {
      x += w / 2;
      y += 3 * h / 4;
      meeplePlayerId = tileObj.bottom.meeple.player.id;
    } else if (tileObj.left.meeple) {
      x += w / 4;
      y += h / 2;
      meeplePlayerId = tileObj.left.meeple.player.id;
    } else if (tileObj.right.meeple) {
      x += 3 * w / 4;
      y += h / 2;
      meeplePlayerId = tileObj.right.meeple.player.id;
    } else if (tileObj.centers[0].meeple) {
      x += w / 2;
      y += h / 2;
      meeplePlayerId = tileObj.centers[0].meeple.player.id;
    } else {
      continue;
    }

    ctx.beginPath();
    ctx.fillStyle =  colors[meeplePlayerId - 1];
    ctx.strokeStyle = null;
    ctx.arc(x, y, radius, 0, 2 * Math.PI);
    ctx.closePath();
    ctx.fill();
  }

  return;
 };

// Renders the valid moves based on the current tile and tile orientation.
Renderer.prototype.renderMoves = function() {
  var mainCanvas = document.getElementById("mainCanvas");
  var ctx = mainCanvas.getContext("2d");

  if (isPlaced || (!showHints && !$.isEmptyObject(this.board)) || !myTurn) { // have a move selected or hints are hidden
    return;
  }

  ctx.beginPath();

  var moves = this.validMoves;

  for (var i = 0; i < moves.length; i++) {
    var pos = moves[i];
    var targetPlacement = this.posToCanvas(pos);

    ctx.lineWidth = LINE_WIDTH;
    ctx.strokeStyle = MOVES_COLOR;
    ctx.rect(targetPlacement.x, targetPlacement.y, 
             targetPlacement.s, targetPlacement.s);
    ctx.stroke();
  }

  ctx.closePath();
  return;
};

// Takes a pos {x: x, y: y}
// Returns a pos mapped to the canvas space with {x: x, y: y, s: size}
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

// Takes a pixel value from the main canvas {x, y}
// Returns that position mapped to the canvas space {x, y}
Renderer.prototype.pixelsToCanvas = function(pixPos) {
  var canvasUnitHeight = document.getElementById("mainCanvas").height; // canvas units
  var canvasPixHeight = $("#mainCanvas").height(); // canvas pix

  var canvasUnitWidth = document.getElementById("mainCanvas").width; // canvas units
  var canvasPixWidth = $("#mainCanvas").width(); // canvas pix

  var canvasX = pixPos.x * (canvasUnitWidth / canvasPixWidth);
  var canvasY = pixPos.y * (canvasUnitHeight / canvasPixHeight);

  return {x: canvasX, y: canvasY};
}


// Takes a set distance between two points on the canvas {delta x, delta y}.
// Returns that distance in positon units {delta x, delta y}.
Renderer.prototype.canvasDiffToPosDiff = function(canvasPos) {
  var canvasHeight = document.getElementById("mainCanvas").height;
  var canvasWidth = document.getElementById("mainCanvas").width;

  var targetSize = TILE_SIZE * this.scale;

  var posX = (canvasPos.x) / targetSize;
  var posY = (canvasPos.y) / targetSize;

  return {x: posX, y: posY};
}

// Takes a point on the canvas {x, y}.
// Returns that point positon units {x, y}.
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

// Shades the move currently selected by the player.
Renderer.prototype.shadeMove = function() {
  if (!this.selectedTile) {
    return;
  }

  var mainCanvas = document.getElementById("mainCanvas");
  var ctx = mainCanvas.getContext("2d");

  var targetPlacement = this.posToCanvas(this.selectedTile);

  var tileObj = this.currTile;
  var targetImg = document.getElementById(tileObj.pngID);
  var targetRadians = tileObj.rotation * Math.PI / 180;

  if (this.mouseOver) { // currently mousing over a slot
    ctx.globalAlpha = .5;
  }

  if (targetRadians) { // fancy way of drawing an image rotated about it's center
    ctx.translate(targetPlacement.x + (targetPlacement.s / 2), targetPlacement.y + (targetPlacement.s / 2));
    ctx.rotate(targetRadians);
    ctx.drawImage(targetImg, -(targetPlacement.s / 2), -(targetPlacement.s / 2), targetPlacement.s, targetPlacement.s);
    ctx.rotate(-targetRadians);
    ctx.translate(-targetPlacement.x - (targetPlacement.s / 2), -targetPlacement.y - (targetPlacement.s / 2));
  } else { // tile not rotated
    ctx.drawImage(targetImg, targetPlacement.x, targetPlacement.y, targetPlacement.s, targetPlacement.s);
  }

  ctx.globalAlpha = 1;

  ctx.beginPath();
  ctx.lineWidth = LINE_WIDTH;
  ctx.strokeStyle = MOVES_COLOR;
  ctx.rect(targetPlacement.x, targetPlacement.y, 
           targetPlacement.s, targetPlacement.s);
  ctx.stroke();
  ctx.closePath();

  var meeple = this.selectedMeeple;

  if (meeple) {
    var w = targetPlacement.s;
    var h = targetPlacement.s;
    var radius = targetPlacement.s / 10;

    var x = targetPlacement.x;
    var y = targetPlacement.y;

    switch(meeple) {
      case "UP": x += w / 2; y += h / 4; break;
      case "DOWN": x += w / 2; y += 3 * h / 4; break;
      case "RIGHT": x += 3 * w / 4; y += h / 2; break;
      case "LEFT": x += w / 4; y += h / 2; break;
      case "CENTER": x += w / 2; y += h / 2; break;
      default: alert("Meeple switch failed!")
    }

    ctx.beginPath();
    ctx.fillStyle = colors[network.get("currentPlayer").id - 1];
    ctx.arc(x, y, radius, 0, 2 * Math.PI);
    ctx.fill();
    ctx.closePath();
  }

  return;
};

// Takes a click value on the tile canvas {x, y}.
// Returns the canvas value of that click on the tile canvas {x, y}.
Renderer.prototype.pixelsToTile = function(pixPos) {
  var canvasUnitHeight = document.getElementById("tileCanvas").height; // canvas units
  var canvasPixHeight = $("#tileCanvas").height(); // canvas pix

  var canvasUnitWidth = document.getElementById("tileCanvas").width; // canvas units
  var canvasPixWidth = $("#tileCanvas").width(); // canvas pix

  var canvasX = pixPos.x * (canvasUnitWidth / canvasPixWidth);
  var canvasY = pixPos.y * (canvasUnitHeight / canvasPixHeight);

  return {x: canvasX, y: canvasY};
}

// Takes the position of a potential move.
// Returns true if that move is valid and false otherwise.
Renderer.prototype.containsMove = function(pos) {
  if (!this.validMoves) {
    return false;
  }

  for (var i = 0; i < this.validMoves.length; i++) {
    var curr = this.validMoves[i];

    if (pos.x == curr.x && pos.y == curr.y) {
      return true;
    }
  }

  return false;
}

