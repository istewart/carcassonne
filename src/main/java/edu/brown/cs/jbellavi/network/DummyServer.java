package edu.brown.cs.jbellavi.network;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

/**
 * Simple example of the {@link Server} class. This one can handle all of the
 * basic requests a <code>Server</code> needs to handle, while simultaneously
 * testing for connectivity of clients.
 */
class DummyServer implements Server {
  private Map<String, Key> playerIps = new HashMap<>();
  private Map<Key, Player> players = new HashMap<>();
  private int playerCount = 0;
  private Map<String, Object> fields = new HashMap<>();
  private boolean talkative = false;

  /* tells whether or not this server is active */
  private boolean active;
  private int serverTime = 0;

  private Thread pingerThread;

  /**
   * The basic constructor for the DummyServer.
   */
  public DummyServer() {
    Key.setActiveGeneration((int) System.currentTimeMillis());

    if (talkative) {
      System.out.println("Server opened.");
    }
    active = true;
    fields.put("connected", new HashMap<Integer, Boolean>());
    pingerThread = new Thread(new connectionThread(), "server pinger");
    pingerThread.start();
  }

  /**
   * Makes this server talkative. It will print various status updates to
   * System.out.
   * @return <code>this</code>
   */
  public DummyServer talk() {
    talkative = true;
    return this;
  }

  public void close() {
    Key.deprecate();
    active = false;
    try {
      pingerThread.join();
    } catch (InterruptedException ex) {
      System.out.println("Failed to close server pinger.");
    }
    if (talkative) {
      System.out.println("Server closed.");
    }
  }

  @Override
  public Boolean ping(Key key) {
    Player p = players.get(key);
    if (p == null) {
      if (talkative) {
        System.out.println(
          "Unknown player attempted to ping with broken key "
            + key.toJSONString() + ".");
      }
      return null;
    }
    p.incrementPingCount();
    p.setLastPing(serverTime);
    if (talkative) {
      System.out.println("Player " + p.getId() + " pinged.");
    }
    connect(p);
    return !p.isUpToDate();
  }

  @Override
  public int pingCount(Key key) {
    Player p = players.get(key);

    if (p == null) {
      return -1;
    }
    return p.getPingCount();
  }

  @Override
  public Map<String, Object> connect(String ip) {
    if (playerIps.containsKey(ip)) {
      Player p = players.get(playerIps.get(ip));
      if (p.isOpenWindow()) {
        if (talkative) {
          System.out.println("Player " + p.getId() + " opened a second window.");
        }
        return ImmutableMap.of("key", "undefined",
          "player", "undefined",
          "alert", "You appear to have a window open already."
          + " If so, please close this window."
          + " If not, refresh this page.");
      }
      p.setOpenWindow(true);

      return ImmutableMap.of("key", playerIps.get(ip).toJSONString(),
        "player", p.getId());
    } else {
      Key key = Key.generate();
      playerCount += 1;
      Player player = new Player(key, playerCount, ip).setOpenWindow(true);
      playerIps.put(ip, key);
      players.put(key, player);
      ping(key);
      return ImmutableMap.of("key", key.toJSONString(), "player", playerCount);
    }
  }

  /**
   * Notifies all players that a field has changed.
   * @param field The field that has changed
   */
  private void notify(String field) {
    for (Player p : players.values()) {
      p.notify(field);
    }
  }

  @Override
  public void putField(String field, Object value) {
    fields.put(field, value);
    notify(field);
  }

  @Override
  public Object getField(String field) {
    return fields.get(field);
  }

  @Override
  public Map<String, Object> getAllFields() {
    return ImmutableMap.copyOf(fields);
  }

  @Override
  public Map<String, Object> update(Key key) {
    Player p = players.get(key);
    if (p == null) {
      return null;
    }
    Set<String> needsUpdate = p.update();
    Map<String, Object> updates = new HashMap<>();
    for (String field : needsUpdate) {
      updates.put(field, fields.get(field));
    }
    if (talkative) {
      System.out.println("Player " + p.getId() + " updated.");
    }
    return ImmutableMap.copyOf(updates);
  }

  /**
   * Marks a player as connected and notifies clients who do not already
   * know.
   * @param p The player to mark as connected.
   */
  private void connect(Player p) {
    p.connect();
    Map<Integer, Boolean> connected =
      (Map<Integer, Boolean>) fields.get("connected");
    Boolean status = connected.get(p.getId());
    if (status == null || !status) {
      if (talkative) {
        if (status == null) {
          System.out.println("Player " + p.getId() + " connected at "
            + p.getIp() + ".");
        } else {
          System.out.println("Player " + p.getId() + " reconnected.");
        }
      }
      connected.put(p.getId(), true);
      fields.put("connected", connected);
      notify("connected");
    }
  }

  @Override
  public void disconnect(Key k) {
    Player p = players.get(k);
    if (p == null) {
      return;
    }
    disconnect(p);
  }

  /**
   * Marks a player as disconnected and notifies clients who do not already
   * know.
   * @param p The player to mark as disconnected.
   */
  private void disconnect(Player p) {
    p.disconnect();
    p.setOpenWindow(false);
    Map<Integer, Boolean> connected =
      (Map<Integer, Boolean>) fields.get("connected");
    assert connected != null;
    if (connected.get(p.getId())) {
      connected.put(p.getId(), false);
      fields.put("connected", connected);
      notify("connected");
      if (talkative) {
        System.out.println("Player " + p.getId() + " disconnected.");
      }
    }
  }

  /**
   * Checks once per second to see if all players are still connected to the
   * server. If a client has not pinged the server in TIMEOUT_TIME seconds,
   * they are marked as disconnected until they reconnect.
   */
  private class connectionThread implements Runnable {
    private static final int TIMEOUT_TIME = 5;
    private static final int SLEEP_TIME = 250;

    @Override
    public void run() {
      while (active) {
        try {
          Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException ex) {
          System.out.println("Clock jumped.");
        }
        serverTime++;
        if (talkative) {
          System.out.println("Clock: " + serverTime + ".");
        }
        checkconnections();
      }
    }

    public void checkconnections() {
      Collection<Player> playerSet = players.values();
      for (Player p : playerSet) {
        if (p.getLastPing() + TIMEOUT_TIME <= serverTime) {
          disconnect(p);
        }
      }
    }
  }
}
