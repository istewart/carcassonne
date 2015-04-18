TILE_SIZE = 50;
CANVAS_SIZE = 1000;

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
    console.log(targetRadians);

    if (targetRadians) {
      ctx.translate(targetPlacement.x + (TILE_SIZE / 2), targetPlacement.y + (TILE_SIZE / 2));
      ctx.rotate(targetRadians);
      ctx.drawImage(targetImg, -(TILE_SIZE / 2), -(TILE_SIZE / 2), targetPlacement.s, targetPlacement.s);
      ctx.rotate(-targetRadians);
      ctx.translate(-targetPlacement.x - (TILE_SIZE / 2), -targetPlacement.y - (TILE_SIZE / 2));
    } else {
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
  renderBoard();
  renderTile();
  renderMoves();
};

// takes a pos {x: x, y: y}
// returns a pos mapped to the canvas space with {x: x, y: y, s: size}
Renderer.prototype.posToCanvas = function(pos) {
  var canvasHeight = document.getElementById("mainCanvas").height;
  var canvasWidth = document.getElementById("mainCanvas").width;

  var centerX = (canvasWidth / 2) ; //+ (this.xt * TILE_SIZE * this.scale);
  var centerY = (canvasHeight / 2)  ; //+ (this.yt * TILE_SIZE * this.scale);

  var targetX = centerX + (pos.x * TILE_SIZE * this.scale);
  var targetY = centerY + (-pos.y * TILE_SIZE * this.scale);
  var targetSize = TILE_SIZE * this.scale;

  return {x: targetX, y: targetY, s: targetSize};
};