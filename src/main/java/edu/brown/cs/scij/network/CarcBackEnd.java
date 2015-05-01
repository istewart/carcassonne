package edu.brown.cs.scij.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import com.google.gson.Gson;

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
  private static final Gson GSON = new Gson();

  public CarcBackEnd(Referee r) {
    this.r = r;
  }

  @Override
  public synchronized Object answer(int player, String field,
      Map<String, String> val) {
    System.out.println("field: " + field);
    List<Posn> validMoves;
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
          validMoves = r.getBoard().validMoves(t);
          toReturn.put("validMoves", validMoves);
          toReturn.put("players", r.getPlayers());
          return toReturn;
        }
        break;
      case "newPlayer":
        String name = val.get("name");
        r.newPlayer(new Player(player, name));
        List<Player> players2 = r.getPlayers();
        s.putField("players", players2);
        return ImmutableMap.of("success", "success", "players", players2);
      case "gameStart":
        s.seal();

        t = r.drawTile();
        Board b = r.getBoard();
        List<Player> players = r.getPlayers();
        validMoves = b.validMoves(t);

        // List<Direction> validMeeples = /*t.validMeeples()*/
        // r.validMeeples(p);
        s.putField("currTile", t);
        toReturn.put("currTile", t);
        s.putField("board", b);
        toReturn.put("board", b);
        s.putField("players", players);
        toReturn.put("players", players);
        s.putField("validMoves", validMoves);
        toReturn.put("validMoves", validMoves);
        Player curPlayer = r.nextPlayer();
        s.putField("currentPlayer", curPlayer);
        toReturn.put("currentPlayer", curPlayer);
        // putField() current board, current tile, list of players,
        // current player, valid moves
        return toReturn;
      case "placeTile":
        // receiving: posn
        String posn = val.get("move");
        String[] xy = posn.split(",");
        Posn p = new Posn(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
        try {
          r.getBoard().place(p, r.getCurTile());
          r.setCurPosn(p);
        } catch (PosnTakenException e) {
          // TODO Send back a message saying it's the same player's turn with
          // the
          // same tile and such
        }

        // putField: board, current player, list of all players, valid meeples,
        // current tile

        if (r.getCurPlayer().getNumMeeples() > 0) {
          try {
            List<Direction> validMeeples = r.getBoard().validMeeples(p);
            s.putField("validMeeples", validMeeples);
            toReturn.put("validMeeples", validMeeples);
          } catch (NullTileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        } else {
          s.putField("validMeeples", new ArrayList<>());
          toReturn.put("validMeeples", new ArrayList<>());
        }

        // TODO shouldn't send this here, erase
        /*
         * s.putField("gameover", r.isGameOver());
         * toReturn.put("gameover", r.isGameOver());
         */
        return toReturn;
      case "placeMeeple":
        // TODO receiving: direction
        // returning: currtile, board, validmoves, players, currplayer
        String dir = val.get("meeple");
        Direction d = null;
        Tile curTile = r.getCurTile();
        if (dir != null) {
          try {
            switch (dir) {
              case "UP":
                d = Direction.UP;
                break;
              case "LEFT":
                d = Direction.LEFT;
                break;
              case "DOWN":
                d = Direction.DOWN;
                break;
              case "RIGHT":
                d = Direction.RIGHT;
                break;
              case "CENTER":
                d = Direction.CENTER;
                break;
            }
            r.placeMeeple(r.getCurPosn(), r.getCurPlayer(), d);
          } catch (UnMeeplableException e) {
            e.printStackTrace();
          } catch (NullTileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } catch (OutOfMeeplesException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }

        System.out.println("current posn: " + r.getCurPosn());
        r.score(r.getCurPosn());

        if (r.isGameOver()) {
          s.putField("gameover", r.isGameOver());
          toReturn.put("gameover", r.isGameOver());
          s.putField("players", r.getPlayers());
          toReturn.put("players", r.getPlayers());

          // TODO send only a gameover message with the list of players
        } else {
          s.putField("board", r.getBoard());
          toReturn.put("board", r.getBoard());
          s.putField("currentPlayer", r.getCurPlayer());
          toReturn.put("currentPlayer", r.getCurPlayer());
          s.putField("players", r.getPlayers());
          toReturn.put("players", r.getPlayers());

          curTile = r.drawTile();
          validMoves = r.getBoard().validMoves(curTile);
          while (validMoves.isEmpty()) {
            validMoves = checkValidMoves(r.getBoard(), curTile);
          }

          // check current orientation

          s.putField("currTile", r.getCurTile());
          toReturn.put("currTile", r.getCurTile());
          s.putField("validMoves", validMoves);
          toReturn.put("validMoves", validMoves);
          s.putField("gameover", r.isGameOver());
          toReturn.put("gameover", r.isGameOver());
          return toReturn;
        }

        break;
      default:
        System.out.println("GOT TO DEWFAULT BIZNATCHES");
        // TODO not sure what to do when it's none of these
    }
    // return only what the person requesting should know
    return toReturn;
  }

  private List<Posn> checkValidMoves(Board b, Tile curTile) {
    List<Posn> validMoves;
    curTile.rotateLeft();
    // check rotation 270
    validMoves = r.getBoard().validMoves(curTile);
    if (validMoves.isEmpty()) {
      curTile.rotateLeft();
      // check rotation 180
      validMoves = r.getBoard().validMoves(curTile);
      if (validMoves.isEmpty()) {
        curTile.rotateLeft();
        // check rotation 90
        validMoves = r.getBoard().validMoves(curTile);
        if (validMoves.isEmpty()) {
          r.getDeck().getTiles().add(curTile);
          Collections.shuffle(r.getDeck().getTiles());
          curTile = r.drawTile();
        }
      }
    }
    return validMoves;
  }

  @Override
  public BackEnd setServer(Server s) {
    this.s = s;
    return this;
  }

}
