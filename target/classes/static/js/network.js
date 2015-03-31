jQuery(window).bind(
    "beforeunload", 
    function() {
      network.server.disconnect();
      return;
    }
)

var network = {
  ip: undefined,
  id: undefined,
  key: undefined,
  consecutivePingFailures: 0,
  connectedInterval: undefined,
  connected: true,
  pingInterval: undefined,
  fatal: false,

  onLoad: function() {
    network.getIp();
  },

  /**
   * Contains all data the server is sharing with the client. Automatically
   * updated each second by the ping() process.
   */
  fields: { },

  /**
   * Contains methods related to the server.
   */
  server: {
    inPingFunction: false,
    /**
     * Called each second to see if the page needs updating.
     */
    ping: function() {
      if (network.server.inPingFunction || typeof network.key === "undefined") {
        return;
      }
      inPingFunction = true;
      var response = {
        key: network.key,
      }

      $.post("/ping", response, function(responseJSON) {
        var responseObject = JSON.parse(responseJSON);
        if (responseObject.val == "undefined" && !network.fatal) {
          network.fatal = true;
          alert(responseObject.alert);
          clearInterval(network.pingInterval);
        }
        if (responseObject.val == true) {
          network.server.update(responseObject.updates);
        }
        network.consecutivePingFailures = 0;
        if (network.connected == false) {
          network.reconnect();
        }
        inPingFunction = false;
      }).fail(function() {
        network.consecutivePingFailures = network.consecutivePingFailures + 1;
        if (network.connected == true && network.consecutivePingFailures >= 5) {
          network.disconnect();
        }
        inPingFunction = false;
      });
    },

    inConnectFunction: false,
    /**
     * Called when the user first connects to the server (by loading the page).
     */
    connect: function() {
      if (network.server.inConnectFunction || typeof network.ip === "undefined") {
        return;
      }
      network.server.inConnectFunction = true;

      clearInterval(network.connectedInterval);

      var response = {
        ip: network.ip,
      };

      $.post("/connect", response, function(responseJSON) {
        var responseObject = JSON.parse(responseJSON);
        if ((responseObject.key == "undefined"
          || responseObject.player == "undefined") && !network.fatal) {
          network.fatal = true;
          alert(responseObject.alert);
          clearInterval(network.connectedInterval);
          return;
        }
        network.id = responseObject.player;
        network.key = responseObject.key;

        network.reconnect();

        network.pingInterval = setInterval(network.server.ping, 100);
        network.server.ping();
        network.server.inConnectFunction = false;
      });
    },

    disconnect: function() {
      var response = {
        key: network.key,
      }
      $.post("/disconnect", response);
    },

    /**
     * Updates necessary fields.
     */
    update: function(updates) {
      for (var key in updates) {
        network.fields[key] = updates[key];
        try {
          var fn = window["network"]["updators"][key];
          if (typeof fn === "function") {
            fn(network.fields[key]);
          }
        } catch (err) { }
        try {
          var fn = window["updators"][key];
          if (typeof fn === "function") {
            fn(network.fields[key]);
          }
        } catch (err) { }
      }
    },
  },

  updators: {
    connected: function(statuses) {
      var html = "";
      for (var key in statuses) {
        html += "Player " + key + ": "
          + (statuses[key] ? "connected" : "disconnected")
          + ". <br>";
      }
      $("#connected").html(html);
    },
  },

  /**
   * Called when the user reconnects after disconnecting.
   */
  reconnect: function() {
    $("#status").html("Status: connected.");
    network.connected = true;
  },

  /**
   * sets ip to the users current ip.
   */
  getIp: function() {
    $.getJSON("http://jsonip.com?callback=?", function(data) {
      network.ip = data.ip;
      network.connectedInterval = setInterval(network.server.connect, 10);
    }).fail(function() {
      network.consecutivePingFailures++;
      if (network.connected == true && network.consecutivePingFailures >= 5) {
        network.disconnect();
      }
      network.getIp();
    });
  },

  /**
   * Lets the user know they have disconnected.
   */
  disconnect: function() {
    $("#status").html("Status: disconnected.");
    network.fields.connected[network.id] = false;
    network.updators.connected(network.fields.connected);
    network.connected = false;
  },
}
