package edu.brown.cs.scij.network;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.google.common.collect.ImmutableMap;

import com.google.gson.Gson;

import freemarker.template.Configuration;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * Opens a new server and runs it, then sets up a Spark Server and attempts to
 * open a browser window for the host player. If it cannot open this window, it
 * directs the player to the correct address.
 */
public final class Network {
  private String ip;

  private static final Gson GSON = new Gson();
  private static final int DEFAULT_PORT = 3141;
  private int port = DEFAULT_PORT;
  private String url;

  private Server server = new MainServer().talk();
  private BackEnd back;

  /**
   * The basic constructor for this class. The default port 4567 will be used.
   */
  private Network() {
  }

  /**
   * Creates a new Network that has yet to be set in motion. To launch it, call
   * {@link #go()} on it.
   * @param args The arguments to the program
   * @return The Network.
   */
  public static Network getNetwork(String[] args) {
    return new Network(args);
  }

  /**
   * Creates a new Network with the given arguments.
   * @param args The command-line arguments
   */
  private Network(String[] args) {
    if (args.length >= 1) {
      try {
        port = Integer.parseInt(args[0]);
      } catch (NumberFormatException ex) {
        // If it cannot be parsed, it is simply ignored and the default
        // port is used.
        port = DEFAULT_PORT;
      }
    }
  }

  /**
   * Sets the <code>BackEnd</code> for this Server. If used, this must be called
   * before {@link #go()}.
   * @param setBack The BackEnd
   * @return <code>this</code>
   */
  public Network setBackEnd(BackEnd setBack) {
    this.back = setBack;
    return this;
  }

  /**
   * Sets the <code>Server</code> for this Network. If used, this must be called
   * before {@link #go()}.
   * @param setServer The Server
   * @return <code>this</code>
   */
  public Network setServer(Server setServer) {
    this.server = setServer;
    return this;
  }

  /**
   * Starts the network running.
   */
  public void go() {
    try {
      ip = InetAddress.getLocalHost().getHostAddress().toString();
      url = "http://" + ip + ":" + Integer.toString(port) + "/carcassonne";
      System.out.println("The network is loading...");
      System.out.println(
          "If a window does not open automatically, please visit " + url);
      server.putField("url", url);
    } catch (Exception ex) {
      System.out.println("Failed to launch server.");
      throw new RuntimeException(ex);
    }

    if (back != null) {
      server.setBackEnd(back);
      back.setServer(server);
    }

    runSparkServer();

    try {
      Desktop.getDesktop().browse(URI.create(url));
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(null,
          "Failed to open default browser. Please visit " + url + " instead.");
    }
  }

  private void runSparkServer() {
    Spark.setPort(port);
    FreeMarkerEngine freeMarker = createEngine();

    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.get("/carcassonne", new HomeHandler(), freeMarker);
    Spark.post("/ping", new PingHandler(server));
    Spark.post("/connect", new ConnectHandler(server));
    Spark.post("/disconnect", new DisconnectHandler(server));
    Spark.post("/ask", new AskHandler(server));
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable to use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * Handles the home page.
   */
  private class HomeHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(final Request req, final Response res) {
      Map<String, Object> variables =
          ImmutableMap.of("title", "Network",
              "body", "A simple website",
              "url", url);
      return new ModelAndView(variables, "main.ftl");
    }
  }

  /**
   * Handles the pings to the server that are sent each second by each client to
   * see if the client needs to update the game in any way.
   */
  private static class PingHandler implements Route {
    private Server server;

    public PingHandler(Server server) {
      this.server = server;
    }

    @Override
    public Object handle(final Request req, final Response res) {
      QueryParamsMap qm = req.queryMap();
      Key key = Key.fromJSONString(qm.get("key").value());

      boolean result;
      try {
        result = server.ping(key);
      } catch (NoSuchPlayerException ex) {
        Map ret = new HashMap<String, Object>();
        ret.put("val", "undefined");
        ret.put("alert",
            "Your server connection has expired. Please refresh to reconnect.");
        return GSON.toJson(Collections.unmodifiableMap(ret));
      }
      Map<String, Object> updates;
      if (result) {
        updates = server.update(key);
      } else {
        updates = ImmutableMap.of();
      }

      return GSON.toJson(ImmutableMap.of("val", result,
          "updates", updates));
    }
  }

  /**
   * Handles requests to /connect, which is used when a player connects to the
   * server (i.e. upon reopening page or opening it for the first time).
   */
  private static class ConnectHandler implements Route {
    private Server server;

    public ConnectHandler(Server server) {
      this.server = server;
    }

    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String playerIp = filterIp(qm.value("ip"));
      return GSON.toJson(server.connect(playerIp));
    }

    /**
     * Some devices connect with complex IP addresses or two IP addresses. This
     * method converts those into simple IP addresses to prevent a single device
     * from having multiple open connections.
     * @param ip The ip to filter
     * @return The filtered ip.
     */
    private static String filterIp(String ip) {
      return ip.split("[^\\d.]")[0];
    }
  }

  /**
   * Handles requests to /disconnect, which is called when a player manually
   * disconnects from the server by unloading the page, refreshing, etc.
   */
  private static class DisconnectHandler implements Route {
    private Server server;

    public DisconnectHandler(Server server) {
      this.server = server;
    }

    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String keyString = qm.get("key").value();
      if (!(keyString == null)) {
        Key key = Key.fromJSONString(keyString);
        server.disconnect(key);
      }
      return GSON.toJson(ImmutableMap.of());
    }
  }

  /**
   * Handles /ask requests, called when a user directly messages the backend.
   */
  private static class AskHandler implements Route {
    private Server server;

    public AskHandler(Server server) {
      this.server = server;
    }

    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      Key key = Key.fromJSONString(qm.get("key").value());
      String field = qm.get("field").value();
      String val = qm.get("val").value();
      return GSON.toJson(server.ask(key, field, (Map<String, String>)
          GSON.fromJson(val, HashMap.class)));
    }
  }
}
