package edu.brown.cs.scij.network;

import java.util.Map;

/**
 * A BackEnd is a class that can communicate with a Server.
 * A Server can run withotu a back end, but it will only serve up a single,
 * static page and track who is connected. In order to allow dynamic
 * functionality, a Server needs a BackEnd. Although only one method is
 * required, a BackEnd should usually contain a reference back to the Server
 * it is associated with, so that it can call methods on the server to set
 * the values of objects, etc. Since multiple threads may attempt to access
 * this BackEnd simultaneously, it is best to synchronize the answer method
 * to the object itself to limit access to one thread at a time.
 * 
 * @author  jbellavi
 */
public interface BackEnd {
  /**
   * Responds to an ask query from the front end or Server.
   * @param player  The player sending the query
   * @param field  The field the query should be applied to
   * @param val  The value of the field
   * @return  A response object or <code>null</code> if the query could
   * not be processed. A Map of field to value is a good example of a response
   * object.
   */
  Object answer(int player, String field, String val);

  /**
   * Optional operation that tells the Back end what server it is
   * associated with.
   * @param s  The Server
   * @return  <code>this</code>
   * @throws UnsupportedOperationException  if this method is not supported
   */
  BackEnd setServer(Server s);
}
