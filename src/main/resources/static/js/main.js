var network = {
  ip: undefined,
  id: undefined,
  consecutivePingFailures: 0,
  connectedInterval: undefined,
  connected: false,

  onLoad: function() {
    network.getIp();
    network.connectedInterval = setInterval(network.connect, 1000);
  },

  /**
   * Called each second to see if the page needs updating.
   */
  ping: function() {
    var response = {
      id: network.id,
    }

    $.post("/ping", response, function(responseJSON) {
      var responseObject = JSON.parse(responseJSON);
      console.log(responseObject);
      network.consecutivePingFailures = 0;
      if (network.connected == false) {
        network.reconnect();
      }
    }).fail(function() {
      network.consecutivePingFailures = network.consecutivePingFailures + 1;
      if (network.connected == true && network.consecutivePingFailures >= 5) {
        network.disconnect();
      }
    });
  },

  /**
   * Called when the user reconnects after disconnecting.
   */
  reconnect: function() {
    $("#status").html("Status: connected.");
    network.connected = true;
  },

  /**
   * Called when the user first connects to the server (by loading the page).
   */
  connect: function() {
    if (typeof network.ip === "undefined") {
      return;
    }

    var response = {
      ip: network.ip,
    };

    $.post("/connect", response, function(responseJSON) {
      var responseObject = JSON.parse(responseJSON);
      network.id = responseObject.player;
    });

    clearInterval(network.connectedInterval);
    setInterval(network.ping, 1000);
  },

  /**
   * sets ip to the users current ip.
   */
  getIp: function() {
    $.getJSON("http://jsonip.com?callback=?", function(data) {
      network.ip = data.ip;
    })
  },

  /**
   * Lets the user know they have disconnected.
   */
  disconnect: function() {
    $("#status").html("Status: disconnected.");
    network.connected = false;
  },
}
