package edu.brown.cs.scij.network;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single player/client. Keeps track of their id, ip, ping count,
 * most recent ping, connected state, and what information they have that is
 * out of date. Used by the {@link MainServer} class.
 *
 * @author  Joseph Bellavia
 */
class NetworkPlayer {
  private String ip;
  private final int id;
  private final Key key;
  private int pingCount;
  private int lastPing;
  private boolean connected;
  private Set<String> outOfDate;
  private boolean openWindow;

  public NetworkPlayer(Key key, int id, String ip) {
    this.key = key;
    this.ip = ip;
    this.id = id;
    pingCount = 0;
    lastPing = 0;
    connected = true;
    outOfDate = new HashSet<>();
  }

  public int getId() {
    return id;
  }

  public String getIp() {
    return ip;
  }

  public Key getKey() {
    return key;
  }

  public int getPingCount() {
    return pingCount;
  }

  public NetworkPlayer setPingCount(int setPingCount) {
    pingCount = setPingCount;
    return this;
  }

  public NetworkPlayer incrementPingCount() {
    pingCount++;
    return this;
  }

  public int getLastPing() {
    return lastPing;
  }

  public NetworkPlayer setLastPing(int setLastPing) {
    lastPing = setLastPing;
    return this;
  }

  public boolean isConnected() {
    return connected;
  }

  public NetworkPlayer setConnected(boolean setConnected) {
    connected = setConnected;
    return this;
  }

  public NetworkPlayer connect() {
    connected = true;
    return this;
  }

  public NetworkPlayer disconnect() {
    connected = false;
    return this;
  }

  public NetworkPlayer setOpenWindow(boolean setOpenWindow) {
    openWindow = setOpenWindow;
    return this;
  }

  public boolean isOpenWindow() {
    return openWindow;
  }

  /**
   * Tells whether this player has the most up-to-date information or not.
   * @return <code>true</code> if the player has up-to-date information about
   * ever notable field, <code>false</code> if the player does not.
   */
  public boolean isUpToDate() {
    return outOfDate.isEmpty();
  }

  /**
   * Gets a <code>Set</code> of the fields this player needs to update. Clears
   * the set of fields that need updating.
   * @return A <code>Set&lt;String&gt;</code> containing the names of the
   * fields that this player needs to update.
   */
  public Set<String> update() {
    Set<String> ret = outOfDate;
    outOfDate = new HashSet<>();
    return ret;
  }

  /**
   * Tells this player that a field needs updating.
   * @param field The field that must be updated
   */
  public void notify(String field) {
    outOfDate.add(field);
  }

  /**
   * Tells this player that all of the given fields need updating.
   * @param fields A <code>Collection&lt;String&gt;</code> containing a list of
   * fields that this player needs to update.
   */
  public void notify(Collection<String> fields) {
    for (String field : fields) {
      notify(field);
    }
  }
}
