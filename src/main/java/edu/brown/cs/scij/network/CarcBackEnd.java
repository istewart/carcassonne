package edu.brown.cs.scij.network;

import edu.brown.cs.scij.game.Referee;

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
      case "add player":
      case "game start":
        // putField() current board, current tile, list of players,
        // current player, valid moves, valid meeples
        s.seal();
        s.putField("board", r.getBoard());
        s.putField("curTile", r.drawTile());
        // TODO add more

      case "place tile":
        // receiving: posn, meeple placement (UP RIGHT DOWN LEFT CENTER)
        // putField: board, next player, list of all players, next tile, valid
        // moves, valid meeples
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
