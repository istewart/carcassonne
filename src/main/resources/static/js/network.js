jQuery(window).bind(
    "beforeunload", 
    function() {
      network.server.disconnect();
      return;
    }
)

/**
 * Handles the connections to the network. The following functions are usable:
 *
 *                send(field, val)
 *                ask(field, val)
 *                sendBlock(field, val)
 *                askBlock(field, val)
 *                get(field)
 *                isConnected()
 * 
 *   You can also define specific handlers that handle updates to data. In order
 * to create a handler for a specific field, make an object name handlers and
 * populate it with functions. Each function's name should be the name of the
 * field that it handles. Whenever that field changes, this handler will be
 * called automatically. For example, in order to track changes to a two
 * variables named foo and bar, write the following in the main namespace:
 * 
 *                var handler = {
 *                  foo: function(value) {
 *                    console.log(value);
 *                    // More code here...
 *                  },
 *   
 *                  bar: function(value) {
 *                    console.log(value);
 *                    // More code here....
 *                  },
 *                }
 *     
 *   In addition, you may define functions named connect, disconnect, and
 * reconnect that are called when this client connects, disconnects, or
 * reconnects to the server. These functions will not be passed any arguments.
 */
var network = {

  /**
   * Sends a message to the server. A message must consist of two parts:
   * a field, and a value. These are sent to the server, then control is
   * returned.
   */
  say: function(field, val) {
    var response = {
      key: network.key,
      field: field,
      val: val,
    }
    $.post("/ask", response);
  },

  /**
   * This function is the same as send, except that it will call a handler
   * on the server's responseObject. This object will be parsed and then 
   * send to this handler. If the server is not expected to send a response to
   * this query, use the one-way send function which does not require a handler
   * or pass undefined as the handler to this function.
   * Handler is the handler that should be called when the response is
   * returned. This handler will be passed the return value.
   */
  ask: function(field, val, handler) {
    var response = {
      key: network.key,
      field: field,
      val: val,
    }
    $.post("/ask", response, function(responseJSON) {
      answer = JSON.parse(responseJSON);
      if (handler != undefined) {
        handler(answer);
      }
    });
  },

  /**
   * This function is identical to send except that it blocks until the server
   * confirms that it has read the message.
   *//*
  sendBlock: function(field, val) {
    var response = undefined;
    response = network.send(field, val);
    while (response == undefined)
      ;
    return response;
  },*/

  /**
   * This functio nis identical to ask except that it blocks until the server
   * sends a return message.
   *//*
  askBlock: function(field, val) {
    var response = undefined;
    response = network.ask(field, val);
    while (response == undefined)
      ;
    return response;
  },*/

  /**
   * Gets the value of a field.
   */
  get: function(field) {
    return network.fields[field];
  },

  /**
   * Returns true if this client is connected, and false if it is not.
   */
  isConnected: function() {
    return network.connected;
  },


  /**********************************************************************
   *              DO NOT USE FUNCTIONS BELOW THIS LINE                  *
   **********************************************************************/


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
   * Contains methods related to the server. Do not call these methods.
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
          console.log("reconnected");
          network.server.handle("reconnect", undefined);
        }
        inPingFunction = false;
      }).fail(function() {
        network.consecutivePingFailures = network.consecutivePingFailures + 1;
        if (network.connected == true && network.consecutivePingFailures >= 5) {
          network.server.handle("disconnect", undefined);
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

        network.server.handle("connect", undefined);

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
        network.server.handle(key, updates[key]);
      }
    },

    handle: function(key, value) {
      try {
        var fn = window["network"]["updators"][key];
        if (typeof fn === "function") {
          fn(value);
        }
      } catch (err) { }
      try {
        var fn = window["handler"][key];
        if (typeof fn === "function") {
          fn(value);
        }
      } catch (err) { }
    },
  },

  updators: {
    connect: function() {

    },

    reconnect: function() {
      console.log("hi");
      network.connected = true;
    },

    disconnect: function() {
      network.fields.connected[network.id] = false;
      handle("connected", network.fields.connected);
      network.connected = false;
    },
  },

  /**
   * Sets ip to the users current ip.
   */
  getIp: function() {
    $.getJSON("http://jsonip.com?callback=?", function(data) {
      network.ip = data.ip;
      network.connectedInterval = setInterval(network.server.connect, 100);
    }).fail(function() {
      network.consecutivePingFailures++;
      if (network.connected == true && network.consecutivePingFailures >= 5) {
        network.server.handle("disconnect", undefined);
      }
      network.getIp();
    });
  },
}
