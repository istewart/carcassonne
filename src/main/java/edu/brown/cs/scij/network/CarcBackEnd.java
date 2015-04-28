package edu.brown.cs.scij.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.scij.game.Board;
import edu.brown.cs.scij.game.Player;
import edu.brown.cs.scij.game.Posn;
import edu.brown.cs.scij.game.Referee;
import edu.brown.cs.scij.tile.Direction;
import edu.brown.cs.scij.tile.Tile;

public class CarcBackEnd implements BackEnd {
  private Referee r;
  private Server s;

  public CarcBackEnd(Referee r) {
    this.r = r;
  }

  @Override
  public synchronized Object answer(int player, String field, Object val) {
    switch (field) {
      case "rotate":
        // receive left or right
        // return tile, valid moves, valid meeples
        if (val.equals("left")) {

        } else if (val.equals("right")) {

        } else {
          // bad
        }
        break;
      case "new player":
        Map<String, String> newPlayer = (HashMap<String, String>) val;
        String id = newPlayer.get("id");
        String name = newPlayer.get("name");
        r.newPlayer(new Player(id, name));
        break;
      case "game start":
        Tile t = r.getCurTile();
        Board b = r.getBoard();
        List<Player> players = r.getPlayers();
        List<Posn> validMoves = b.validMoves(t);
        List<Direction> validMeeples = t.validMeeples();

        // putField() current board, current tile, list of players,
        // current player, valid moves, valid meeples
        s.seal();
        s.putField("board", r.getBoard());
        s.putField("curTile", r.drawTile());
        // TODO add more
        break;
      case "place tile":
        // receiving: posn, meeple placement (UP RIGHT DOWN LEFT CENTER)
        // putField: board, next player, list of all players, next tile, valid
        // moves, valid meeples
        break;
      default:
        // something
    }
    // return only what the person requesting should know
    return null;
  }

  @Override
  public BackEnd setServer(Server s) {
    this.s = s;
    return this;
  }

}
