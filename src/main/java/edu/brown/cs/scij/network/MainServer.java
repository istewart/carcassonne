package edu.brown.cs.scij.network;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

/**
 * Simple example of the {@link Server} class. This one can handle all of the
 * basic requests a <code>Server</code> needs to handle, while simultaneously
 * testing for connectivity of clients.
 */
public class MainServer implements Server {
  private BackEnd back;
  private Map<String, Key> playerIps = new HashMap<>();
  private Map<Key, NetworkPlayer> players = new HashMap<>();
  private int playerCount = 0;
  private Map<String, Object> fields = new HashMap<>();
  private boolean talkative = false;

  /* tells whether or not this server is active */
  private boolean active;
  private int serverTime = 0;
  private boolean isSealed = false;

  private Thread pingerThread;

  /**
   * The basic constructor for the MainServer.
   */
  public MainServer() {
    Key.setActiveGeneration((int) System.currentTimeMillis());

    if (talkative) {
      System.out.println("Server opened.");
    }
    active = true;
    fields.put("connected", new HashMap<Integer, Boolean>());
    pingerThread = new Thread(new ConnectionThread(), "server pinger");
    pingerThread.setDaemon(true);
    pingerThread.start();
  }

  /**
   * Constructs a MainServer with the given {@link BackEnd}.
   * @param back  The BackEnd to use
   */
  public MainServer(BackEnd back) {
    this();
    this.back = back;
  }

  @Override
  public MainServer setBackEnd(BackEnd back) {
    this.back = back;
    return this;
  }

  /**
   * Makes this server talkative. It will print various status updates to
   * System.out.
   * @return <code>this</code>
   */
  public MainServer talk() {
    talkative = true;
    return this;
  }

  @Override
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
  public boolean ping(Key key) throws NoSuchPlayerException {
    NetworkPlayer p = players.get(key);
    if (p == null) {
      if (talkative) {
        System.out.println(
          "Unknown player attempted to ping with broken key "
            + key.toJSONString() + ".");
      }
      throw new NoSuchPlayerException();
    }
    synchronized (p) {
      p.incrementPingCount();
      p.setLastPing(serverTime);
      if (talkative) {
        //System.out.println("Player " + p.getId() + " pinged.");
      }
      connect(p);
      return !p.isUpToDate();
    }
  }

  @Override
  public int pingCount(Key key) {
    NetworkPlayer p = players.get(key);
    if (p == null) {
      return -1;
    }

    synchronized (p) {
      return p.getPingCount();
    }
  }

  @Override
  public Map<String, Object> connect(String ip) {
    if (playerIps.containsKey(ip)) {
      NetworkPlayer p = players.get(playerIps.get(ip));
      synchronized (p) {
        if (p.isOpenWindow()) {
          if (talkative) {
            System.out.println("Player " + p.getId()
              + " opened a second window.");
          }
          Map<String, Object> m = new HashMap<>();
          m.put("key", "undefined");
          m.put("player", "undefined");
          m.put("alert", "You appear to have a window open already."
            + " If so, please close this window."
            + " If not, refresh this page.");
          return Collections.unmodifiableMap(m);
        }
        p.setOpenWindow(true);

        p.notify(fields.keySet());

        return ImmutableMap.of("key", playerIps.get(ip).toJSONString(),
          "player", p.getId());
      }
    } else {
      if (isSealed) {
        return ImmutableMap.of("key", "undefined",
          "player", "undefined",
          "alert", "The server has closed and the game has begun. It is too"
          + " late to connect to this server.");
      }
      Key key = Key.generate();
      playerCount += 1;
      NetworkPlayer player = new NetworkPlayer(key, playerCount, ip).setOpenWindow(true);
      playerIps.put(ip, key);
      players.put(key, player);
      try {
        ping(key);
      } catch (NoSuchPlayerException ex) {
        return ImmutableMap.of("key", "undefined",
          "player", "undefined",
          "alert", "Error: The server could not be reached.");
      }
      synchronized (player) {
        player.notify(fields.keySet());
      }
      return ImmutableMap.of("key", key.toJSONString(), "player", playerCount);
    }
  }

  /**
   * Notifies all players that a field has changed.
   * @param field The field that has changed
   */
  private void notify(String field) {
    for (NetworkPlayer p : players.values()) {
      synchronized (p) {
        p.notify(field);
      }
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
  public Map<String, Object> updateAll(Key key) {
    NetworkPlayer p = players.get(key);
    if (p == null) {
      return null;
    }
    synchronized (p) {
      p.update();
      return Collections.unmodifiableMap(fields);
    }
  }

  @Override
  public Map<String, Object> getAllFields() {
    return Collections.unmodifiableMap(fields);
  }

  @Override
  public Map<String, Object> update(Key key) {
    NetworkPlayer p = players.get(key);
    if (p == null) {
      return null;
    }
    Map<String, Object> updates = new HashMap<>();
    synchronized (p) {
      Set<String> needsUpdate = p.update();
      if (talkative) {
        System.out.println("Player " + p.getId() + " updated {");
        for (String field : needsUpdate) {
          updates.put(field, fields.get(field));
          System.out.println(field + ", ");
        }
        System.out.println("}.");
      } else {
        for (String field : needsUpdate) {
          updates.put(field, fields.get(field));
        }
      }
    }
    return Collections.unmodifiableMap(updates);
  }

  @Override
  public void seal() {
    isSealed = true;
  }

  @Override
  public void unseal() {
    isSealed = false;
  }

  /**
   * Marks a player as connected and notifies clients who do not already
   * know.
   * @param p The player to mark as connected.
   */
  private void connect(NetworkPlayer p) {
    synchronized (p) {
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
  }

  @Override
  public void disconnect(Key k) {
    NetworkPlayer p = players.get(k);
    if (p == null) {
      return;
    }
    synchronized (p) {
      disconnect(p);
    }
  }

  @Override
  public Object ask(Key key, String field, String val) {
    NetworkPlayer p = players.get(key);
    if (p == null) {
      return null;
    }
    if (talkative) {
      System.out.println("Player " + p.getId() + " asked " + field + ":" + val + ".");
    }
    if (back != null) {
      Object answer = back.answer(p.getId(), field, val);
      if (talkative) {
        System.out.println("BackEnd answered " + answer + ".");
      }
      return answer;
    } else {
      if (talkative) {
        System.out.println("No BackEnd connected.");
      }
      return null;
    }
  }

  /**
   * Marks a player as disconnected and notifies clients who do not already
   * know.
   * @param p The player to mark as disconnected.
   */
  private void disconnect(NetworkPlayer p) {
    synchronized (p) {
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
  }

  /**
   * Checks once per second to see if all players are still connected to the
   * server. If a client has not pinged the server in TIMEOUT_TIME seconds,
   * they are marked as disconnected until they reconnect.
   */
  private class ConnectionThread implements Runnable {
    private static final int TIMEOUT_TIME = 8;
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
          //System.out.println("Clock: " + serverTime + ".");
        }
        checkConnections();
      }
    }

    public void checkConnections() {
      Collection<NetworkPlayer> playerSet = players.values();
      for (NetworkPlayer p : playerSet) {
        synchronized (p) {
          if (p.getLastPing() + TIMEOUT_TIME <= serverTime) {
            disconnect(p);
          }
        }
      }
    }
  }
}
