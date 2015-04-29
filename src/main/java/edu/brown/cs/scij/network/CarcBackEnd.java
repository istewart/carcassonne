package edu.brown.cs.scij.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.scij.game.Board;
import edu.brown.cs.scij.game.NullTileException;
import edu.brown.cs.scij.game.Player;
import edu.brown.cs.scij.game.Posn;
import edu.brown.cs.scij.game.PosnTakenException;
import edu.brown.cs.scij.game.Referee;
import edu.brown.cs.scij.tile.Direction;
import edu.brown.cs.scij.tile.OutOfMeeplesException;
import edu.brown.cs.scij.tile.Tile;
import edu.brown.cs.scij.tile.UnMeeplableException;

public class CarcBackEnd implements BackEnd {
  private Referee r;
  private Server s;

  public CarcBackEnd(Referee r) {
    this.r = r;
  }

  @Override
  public synchronized Object answer(int player, String field, Object val) {
    Tile t;
    Map<String, String> data = (Map<String, String>) val;
    assert (player == r.getCurPlayer().getId());
    switch (field) {
      case "rotate":
        // receive left or right
        // return tile, valid moves, valid meeples
        t = r.getCurTile();
        if (t != null) {
          if (data.get("rotate").equals("left")) {
            t.rotateLeft();
          } else {
            t.rotateRight();
          }
          Map<String, Object> toReturn = new HashMap<>();
          toReturn.put("currTile", t);
          toReturn.put("validMoves", r.getBoard().validMoves(t));
        }
        /*
         * if (r.getCurPlayer().getNumMeeples() > 0) {
         * toReturn.put("validMeeples", t.validMeeples());
         * } else {
         * toReturn.put("validMeeples", new ArrayList<Direction>());
         * }
         */
        break;
      case "newPlayer":
        /* Map<String, String> newPlayer = (HashMap<String, Object>) val; */
        String name = data.get("name");
        r.newPlayer(new Player(player, name));
        break;
      case "gameStart":
        s.seal();
        t = r.getCurTile();
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
        // receiving: posn, meeple placement (UP RIGHT DOWN LEFT CENTER)
        // Map<String, String> place = (HashMap<String, String>) val;
        String posn = data.get("posn");
        String[] xy = posn.split(",");
        Posn p = new Posn(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));

        String meeple = data.get("meeple");
        try {
          r.getBoard().place(p, r.getCurTile());
        } catch (PosnTakenException e) {
          // TODO Send back a message saying it's the same player's turn with
          // the
          // same tile and such
        }

        if (meeple != null) {
          Direction d = null;
          switch (meeple) {
            case "DOWN":
              d = Direction.DOWN;
              break;
            case "UP":
              d = Direction.UP;
              break;
            case "RIGHT":
              d = Direction.RIGHT;
              break;
            case "LEFT":
              d = Direction.LEFT;
              break;
            case "CENTER":
              d = Direction.CENTER;
              break;
            default:
              break;
          }
          try {
            r.placeMeeple(p, r.getCurPlayer(), d);
            r.score(p);
          } catch (NullTileException e) {
            // TODO shouldn't get here
            e.printStackTrace();
          } catch (OutOfMeeplesException e) {
            // TODO don't send anything, just nothing gets added
          } catch (UnMeeplableException e) {
            // TODO should send back a message saying can't meeple there, have
            // them redo whole move
          }

        }

        // putField: board, next player, list of all players, next tile, valid
        // moves, valid meeples
        s.putField("board", r.getBoard());
        s.putField("currentPlayer", r.nextPlayer());
        s.putField("players", r.getPlayers());
        Tile currTile = r.drawTile();
        s.putField("currTile", currTile);
        s.putField("validMoves", r.getBoard().validMoves(currTile));
        /*
         * if (r.getCurPlayer().getNumMeeples() > 0) {
         * s.putField("validMeeples", currTile.validMeeples());
         * } else {
         * s.putField("validMeeples", new ArrayList<Direction>());
         * }
         */
        s.putField("gameover", r.isGameOver());
        break;
      default:
        // TODO not sure what to do when it's none of these
    }
    // return only what the person requesting should know
    return ImmutableMap.of();
  }

  @Override
  public BackEnd setServer(Server s) {
    this.s = s;
    return this;
  }

}
