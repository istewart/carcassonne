package edu.brown.cs.scij.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

class DummyBackEnd implements BackEnd {
  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  Server server;
  
  @Override
  public Object answer(int player, String field, String val) {
    switch (field) {
      case "server": return handleServer(val);
      case "chat": return handleChat(player, val);
      default: return "This is my reponse to " + val;
    }
  }

  @Override
  public DummyBackEnd setServer(Server server) {
    this.server = server;
    return this;
  }

  private Object handleServer(String val) {
    if ("seal".equals(val)) {
      server.seal();
      return null;
    } else {
      return "invalid command";
    }
  }

  private static List<Chat> chatList = new ArrayList<>();
  private Object handleChat(int player, String message) {
    chatList.add(new Chat(player, message));
    server.putField("chat", chatList);
    return chatList;
  }

  private static class Chat {

    int player;
    String message;

    public Chat(int player, String message) {
      this.player = player;
      this.message = message;
    }

    @Override
    public String toString() {
      return player + ": " + message;
    }
  }
}
