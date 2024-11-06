// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  String logingID;
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
	if (msg.toString().startsWith("SERVER MSG>")) {
		System.out.println(msg.toString());
	}else {
		clientUI.display(msg.toString());
	}
    
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    
      if(message.startsWith("#")) {
    	  handleCommand(message);
      }else {
    	  sendToServer(message);
      }
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommand(String command){
	  try {
	  if(command.equals("#quit")) {
		  quit();
	  }
	  else if(command.equals("#logoff")) {
		  closeConnection();
	  }
	  else if(command.startsWith("#sethost")) {
		  if(!(isConnected())) {
			  String[] parts = command.split(" ");
		        if (parts.length > 1) {
		            String host = parts[1];
		            setHost(host);  // Call setHost with the specified host
		        } else {
		            System.out.println("Error: No host specified.");
		        }
		  }else {
			  System.out.println("You are already connected");
		  }
	  }
	  else if(command.startsWith("#setport")) {
		  if(!(isConnected())) {
			  String[] parts = command.split(" ");
		        if (parts.length > 1) {
		        	try {
		                int port = Integer.parseInt(parts[1]);
		                setPort(port);  // Assuming you have a setPort method
		            } catch (NumberFormatException e) {
		                System.out.println("Error: Invalid port number.");
		            }
		        } else {
		            System.out.println("Error: No port specified.");
		        }
		  }else {
			  System.out.println("You are already connected");
		  }
	  }
	  else if(command.equals("#login")) {
		  if(!(isConnected())) {
			  openConnection();
		  } else {
	            System.out.println("Error: Already connected.");
		  }
	  }

	  else if(command.equals("#gethost")) {
		  System.out.println("Current host: " + getHost());
		  
	  }
	  else if(command.equals("#getport")) {
		  System.out.println("Current port: " + getPort());
	  }
	  else {
		  
		  System.out.println("Command not found");
	  }
} catch (Exception e) {
	 	System.out.println("Error executing command: " + e.getMessage());
}
		 
	  
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  


/**
	 * Implementation the Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	@Override
	protected void connectionException(Exception exception) {
		clientUI.display("The surver is shut down");
		System.exit(0);
		
	}
	
	// METHODS DESIGNED TO BE OVERRIDDEN BY CONCRETE SUBCLASSES ---------

		/**
		 * Hook method called after the connection has been closed. The default
		 * implementation does nothing. The method may be overriden by subclasses to
		 * perform special processing such as cleaning up and terminating, or
		 * attempting to reconnect.
		 */
		@Override
		protected void connectionClosed() {
			clientUI.display("Connection closed");
		}
}
//End of ChatClient class
