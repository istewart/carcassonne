package edu.brown.cs.scij.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class DummyBackEnd implements BackEnd {
  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  Server server;
  
  @Override
  public Object answer(int player, String field, String val) {
    if ("server".equals(field)) {
      return handleServer(val);
    }
    return "This is my reponse to " + val;
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
}
