package edu.brown.cs.jbellavi.network;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
 * Opens a new server and runs it, then sets up a Spark Server and attempts
 * to open a browser window for the host player. If it cannot open this window,
 * it directs the player to the correct address.
 */
public final class Network {
  private String ip;

  private static final Gson GSON = new Gson();
  private static final int DEFAULT_PORT = 4567;
  private int port = DEFAULT_PORT;
  private String url;

  private DummyServer server = new DummyServer().talk();

  /**
   * The only constructor for this class.
   */
  public Network() { }

  /**
   * Creates a new Network that has yet to be set in motion. To launch it,
   * call {@link #go()} on it.
   * @param args The arguments to the program
   * @return The Network.
   */
  public static Network getNetwork(String[] args) {
    return new Network();
  }

  /**
   * Starts the network running.
   */
  public void go() {
    try {
      ip = InetAddress.getLocalHost().getHostAddress().toString();
      url = "http://" + ip + ":" + Integer.toString(port) + "/home";
      System.out.println(url);
    } catch (Exception ex) {
      System.out.println("Failed to launch server.");
      throw new RuntimeException(ex);
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
    Spark.get("/home", new HomeHandler(), freeMarker);
    Spark.post("/ping", new PingHandler(server));
    Spark.post("/connect", new ConnectHandler(server));
    Spark.post("/disconnect", new DisconnectHandler(server));
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
      return new ModelAndView(variables, "networkhome.ftl");
    }
  }

  /**
   * Handles the pings to the server that are sent each second by each client
   * to see if the client needs to update the game in any way.
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

      Boolean result = server.ping(key);
      if (result == null) {
        return GSON.toJson(ImmutableMap.of("val", "undefined", "alert",
          "Your server connection has expired. Please refresh to reconnect."));
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
   * Handles requests to /connect, which is used when a player connects to
   * the server (i.e. upon reopening page or opening it for the first time).
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

    private String filterIp(String ip) {
      return ip.split("[^\\d.]")[0];
    }
  }

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
}
