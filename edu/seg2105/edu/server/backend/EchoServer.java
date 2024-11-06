package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;
import java.util.Enumeration;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  private String loginKey = "LoginId";
 
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
	String mesage = (String)msg;
	if(mesage.startsWith("#login")) {
		if (client.getInfo("loginID") != null) {
            // The login ID is already set, so we handle this as a duplicate login attempt
            try {
                client.sendToClient("Error: You are already logged in.");
                client.close(); // Close the connection as instructed
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
			System.out.println(mesage);
			String[] parts = mesage.split(" ");
			client.setInfo(loginKey, parts[1]);
			this.sendToAllClients(parts[1] + " has logged on.");
		
        }
	}else {
    System.out.println("Message received: " + msg + " from " + client.getInfo(loginKey));
    this.sendToAllClients(client.getInfo(loginKey) +":"+ msg);
	}
  }
  
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
    sendToAllClients("SERVER MSG> Server started");
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  /**
	 * Hook method called each time a new client connection is
	 * accepted. The default implementation does nothing.
	 * @param client the connection connected to the client.
	 */
  
  	protected void clientConnected(ConnectionToClient client) {
	    System.out.println("Client connected");
	    
	}
	
	/**
	 * Hook method called each time a client disconnects.
	 * The default implementation does nothing. The method
	 * may be overridden by subclasses but should remains synchronized.
	 *
	 * @param client the connection with the client.
	 */
	@Override
	protected void clientDisconnected(ConnectionToClient client) {
	    System.out.println("Client disconnected");
	}
	/**
	 * Hook method called each time an exception is thrown in a
	 * ConnectionToClient thread.
	 * The method may be overridden by subclasses but should remains
	 * synchronized.
	 *
	 * @param client the client that raised the exception.
	 * @param Throwable the exception thrown.
	 */
	@Override
	protected void clientException(ConnectionToClient client, Throwable exception) {
	    System.out.println("Client disconnected unexpectedly");
	}
	
  public static void main(String[] args)  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
      
    }
    ServerConsole serverConsole = new ServerConsole(sv);  // Pass the existing server instance
    serverConsole.accept();
  }
}
//End of EchoServer class
