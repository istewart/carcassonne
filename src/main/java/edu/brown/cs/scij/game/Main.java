package edu.brown.cs.scij.game;

import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;


public class Main {
	

	public static final int PORT = 4567;
	public static Referee r = null;

	/**
	   * This main method simply begins our program and makes the necessary objects
	   * our program will utilize later.
	   *
	   * @param args
	   *          no args
	   */
	  public static void main(String[] args) {
		  r = new Referee();
		  runSparkServer();
	  }
	  
	  /**
	   * Handles all get and post communications between our backend and frontend.
	   *
	   * @param main
	   *         
	   */
	  private static void runSparkServer() {
	    // We need to serve some simple static files containing CSS and JavaScript.
	    // This tells Spark where to look for urls of the form "/static/*".
	    Spark.setPort(PORT);

	    Spark.externalStaticFileLocation("src/main/resources/static");
	    Spark.post("/right", new RightHandler());
	    Spark.post("/left", new LeftHandler());
	    Spark.post("/place", new PlaceHandler());
	    Spark.post("/meeple", new MeepleHandler());
	  }
	  
	  /**
	   * Spark handling right requests. handler rotates tile right.
	   */
	  private static class RightHandler implements Route {

	    /**
	     * Constructor for Right handler.
	     */
	    public RightHandler() {
	    	
	    }

	    
	    // based on referees current state, updates everything and responds
	    @Override
	    public String handle(Request req, Response res) {
	      Gson gson = new Gson();
	      QueryParamsMap qm = req.queryMap();
	      
	      Map<String, Object> variables = null;
	      return gson.toJson(variables);
	    }
	  }
	  
	  /**
	   * Spark handling left requests. handler rotates tile left.
	   */
	  private static class LeftHandler implements Route {

	    /**
	     * Constructor for Right handler.
	     */
	    public LeftHandler() {
	    	
	    }

	    // based on referees current state, updates everything and responds
	    @Override
	    public String handle(Request req, Response res) {
	      Gson gson = new Gson();
	      QueryParamsMap qm = req.queryMap();
	      
	      Map<String, Object> variables = null;
	      return gson.toJson(variables);
	    }
	  }
	  
	  /**
	   * Spark handling place requests. handler places tile.
	   */
	  private static class PlaceHandler implements Route {

	    /**
	     * Constructor for Place handler.
	     */
	    public PlaceHandler() {
	    	
	    }

	    //takes in where tile was placed, updates everything and responds
	    @Override
	    public String handle(Request req, Response res) {
	      Gson gson = new Gson();
	      QueryParamsMap qm = req.queryMap();
	      
	      Map<String, Object> variables = null;
	      return gson.toJson(variables);
	    }
	  }
	  
	  /**
	   * Spark handling meeple requests. handler places meeple.
	   */
	  private static class MeepleHandler implements Route {

	    /**
	     * Constructor for Meeple handler.
	     */
	    public MeepleHandler() {
	    	
	    }

	    //takes in where the meeple was placed, updates everything and responds
	    @Override
	    public String handle(Request req, Response res) {
	      Gson gson = new Gson();
	      QueryParamsMap qm = req.queryMap();
	      
	      Map<String, Object> variables = null;
	      return gson.toJson(variables);
	    }
	  }
}
