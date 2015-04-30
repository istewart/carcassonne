package edu.brown.cs.scij.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.scij.game.Board;
import edu.brown.cs.scij.game.Player;
import edu.brown.cs.scij.game.Posn;
import edu.brown.cs.scij.game.PosnTakenException;
import edu.brown.cs.scij.game.Referee;
import edu.brown.cs.scij.tile.Tile;

public class CarcBackEnd implements BackEnd {
  private Referee r;
  private Server s;

  public CarcBackEnd(Referee r) {
    this.r = r;
  }

  @Override
  public synchronized Object answer(int player, String field,
      Map<String, String> val) {
    Tile t;
    assert (player == r.getCurPlayer().getId());
    Map<String, Object> toReturn = new HashMap<>();
    switch (field) {
      case "rotate":
        // receive left or right
        // return tile, valid moves, valid meeples
        t = r.getCurTile();
        if (t != null) {
          if (val.get("rotate").equals("left")) {
            t.rotateLeft();
          } else {
            t.rotateRight();
          }
          toReturn.put("currTile", t);
          toReturn.put("validMoves", r.getBoard().validMoves(t));
        }
        /*
         * if (r.getCurPlayer().getNumMeeples() > 0) {
         * toReturn.put("validMeeples", t.validMeeples()); } else {
         * toReturn.put("validMeeples", new ArrayList<Direction>()); }
         */
        break;
      case "newPlayer":
        /* Map<String, String> newPlayer = (HashMap<String, Object>) val; */
        String name = val.get("name");
        r.newPlayer(new Player(player, name));
        return ImmutableMap.of("success", "success");
      case "gameStart":
        s.seal();
        t = r.drawTile();
        Board b = r.getBoard();
        List<Player> players = r.getPlayers();
        List<Posn> validMoves = b.validMoves(t);

        // List<Direction> validMeeples = /*t.validMeeples()*/
        // r.validMeeples(p);
        s.putField("currTile", t);
        s.putField("board", b);
        s.putField("players", players);
        s.putField("validMoves", validMoves);
        // s.putField("validMeeples", validMeeples);
        s.putField("currentPlayer", r.nextPlayer());
        // putField() current board, current tile, list of players,
        // current player, valid moves, valid meeples
        return null;
      case "placeTile":
        // receiving: posn
        // Map<String, String> place = (HashMap<String, String>) val;
        String posn = val.get("posn");
        String[] xy = posn.split(",");
        Posn p = new Posn(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
        try {
          r.getBoard().place(p, r.getCurTile());
        } catch (PosnTakenException e) {
          // TODO Send back a message saying it's the same player's turn with
          // the
          // same tile and such
        }

        // putField: board, next player, list of all players, next tile, valid
        // meeples
        s.putField("board", r.getBoard());
        s.putField("currentPlayer", r.nextPlayer());
        s.putField("players", r.getPlayers());
        Tile currTile = r.drawTile();
        s.putField("currTile", currTile);
        s.putField("validMoves", r.getBoard().validMoves(currTile));
        /*
         * if (r.getCurPlayer().getNumMeeples() > 0) {
         * s.putField("validMeeples", currTile.validMeeples()); } else {
         * s.putField("validMeeples", new ArrayList<Direction>()); }
         */
        s.putField("gameover", r.isGameOver());
        break;
      case "placeMeeple":
        // TODO
        break;
      default:
        // TODO not sure what to do when it's none of these
    }
    // return only what the person requesting should know
    return toReturn;
  }

  @Override
  public BackEnd setServer(Server s) {
    this.s = s;
    return this;
  }

}
