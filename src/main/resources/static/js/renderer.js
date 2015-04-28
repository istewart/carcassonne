TILE_SIZE = 60;
CANVAS_SIZE = 1000;

// TODO

// can clean things up with global context and canvas
// take a look at clearing meeples and tiles and tile spacing
// do click and drag better
// add a better background to the board
// get better tiles
// integrate with back end
// fix settings and instructions

function Renderer(board, currTile, players, validMoves, validMeeples, scale, xt, yt) {
  this.board = board;
  this.currTile = currTile;
  this.players = players;
  this.validMoves = validMoves;
  this.validMeeples = validMeeples;

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
  var tileCanvas = document.getElementById("tileCanvas");
  var ctx = tileCanvas.getContext("2d");

  var w = tileCanvas.width;
  var h = tileCanvas.height;
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
  var radius = w / 8;

  for (var i = 0; i < spots.length; i++) {
    var x;
    var y;

    switch(spots[i]) {
      case "UP": x = w / 2; y = h / 4; break;
      case "DOWN": x = w / 2; y = 3 * h / 4; break;
      case "RIGHT": x = 3 * w / 4; y = h / 2; break;
      case "LEFT": x = w / 4; y = h / 2; break;
      case "CENTER": x = w / 2; y = h / 2; break;
      default: alert("Meeple switch failed!")
    }

    ctx.beginPath();
    ctx.arc(x, y, radius, 0, 2 * Math.PI);
    ctx.closePath();

    if (this.selectedMeeple && spots[i] === this.selectedMeeple) {
      ctx.fillStyle = "red";
      ctx.strokeStyle = null;
      ctx.fill();
    } else {
      ctx.fillStyle = null;
      ctx.strokeStyle = "red";
      ctx.lineWidth = 4;
      ctx.stroke();
    }
  }
};

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

    var meeple = currTile.meeple;

    if (meeple) {
      var w = targetPlacement.s;
      var h = targetPlacement.s;
      var radius = targetPlacement.s / 10;

      var x = targetPlacement.x;
      var y = targetPlacement.y;

      var meeplePlacement = meeple.meeplePlacement;

      switch(meeplePlacement) {
        case "UP": x += w / 2; y += h / 4; break;
        case "DOWN": x += w / 2; y += 3 * h / 4; break;
        case "RIGHT": x += 3 * w / 4; y += h / 2; break;
        case "LEFT": x += w / 4; y += h / 2; break;
        case "CENTER": x += w / 2; y += h / 2; break;
        default: alert("Meeple switch failed!")
      }

      ctx.beginPath();
      ctx.fillStyle = meeple.player.color;
      ctx.arc(x, y, radius, 0, 2 * Math.PI);
      ctx.fill();
      ctx.closePath();
    }
  }

  return;
 };

Renderer.prototype.renderMoves = function() {
  var mainCanvas = document.getElementById("mainCanvas");
  var ctx = mainCanvas.getContext("2d");
  ctx.beginPath();

  var moves = this.validMoves;

  for (var i = 0; i < moves.length; i++) {
    var pos = moves[i];
    var targetPlacement = this.posToCanvas(pos);

    ctx.strokeStyle = "red";
    ctx.rect(targetPlacement.x, targetPlacement.y, 
             targetPlacement.s, targetPlacement.s);
    ctx.stroke();
  }

  ctx.closePath();
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
  ctx.closePath();

  // Restore the transform
  ctx.restore();

  this.renderBoard();
  this.renderTile();
  this.renderMoves();
  this.renderPlayers();
  this.shadeMove();
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

Renderer.prototype.shadeMove = function() {
  if (!this.selectedTile) {
    return;
  }

  var mainCanvas = document.getElementById("mainCanvas");
  var ctx = mainCanvas.getContext("2d");

  var targetPlacement = this.posToCanvas(this.selectedTile);

  ctx.fillStyle = "red";
  ctx.fillRect(targetPlacement.x, targetPlacement.y, 
           targetPlacement.s, targetPlacement.s);

  return;
};

Renderer.prototype.pixelsToTile = function(pixPos) {
  var canvasUnitHeight = document.getElementById("tileCanvas").height; // canvas units
  var canvasPixHeight = $("#tileCanvas").height(); // canvas pix

  var canvasUnitWidth = document.getElementById("tileCanvas").width; // canvas units
  var canvasPixWidth = $("#tileCanvas").width(); // canvas pix

  var canvasX = pixPos.x * (canvasUnitWidth / canvasPixWidth);
  var canvasY = pixPos.y * (canvasUnitHeight / canvasPixHeight);

  return {x: canvasX, y: canvasY};
}

Renderer.prototype.containsMove = function(pos) {
  for (var i = 0; i < this.validMoves.length; i++) {
    var curr = this.validMoves[i];

    if (pos.x == curr.x && pos.y == curr.y) {
      return true;
    }
  }

  return false;
}
