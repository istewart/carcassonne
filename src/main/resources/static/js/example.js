var n = network;

var handler = {
  connect: function() {
    handler.reconnect();
  },

  connected: function() {
    var statuses = n.get("connected");
    var html = "";
    for (var key in statuses) {
      html += "Player " + key + ": "
        + (statuses[key] ? "connected" : "disconnected")
        + ". <br>";
    }
    $("#connected").html(html);
  },

  /**
   * Called when the user reconnects after disconnecting.
   */
  reconnect: function() {
    $("#status").html("Status: connected.");
  },

  /**
   * Lets the user know they have disconnected.
   */
  disconnect: function() {
    $("#status").html("Status: disconnected.");
  },

  chat: function(chatLog) {
    var html = "<br> CHAT: <br>";
    for (var key in chatLog) {
      html += chatLog[key].player + ": "
       + chatLog[key].message + "<br>";
    }
    $("#chat").html(html);
  }
}

function simpleHandler(val) {
  console.log(val);
}
