package edu.seg2105.edu.server.backend;

import java.util.Scanner;

import edu.seg2105.client.common.ChatIF;

public class ServerConsole implements ChatIF {
	 private EchoServer server;
	 private Scanner fromConsole;

	 public ServerConsole(EchoServer server) {
	        this.server = server;  // Use the provided server instance
	        fromConsole = new Scanner(System.in);
	    }

	 public void accept() {
		    try {
		        String message;
		        while (true) {
		            message = fromConsole.nextLine();
		            if (message.startsWith("#")) {
		                handleCommand(message);
		            } else {
		            	String m = "SERVER MSG> " + message;
		                server.sendToAllClients(m);
		                display("SERVER MSG> " + message);
		            
		                
		            }
		        }
		    } catch (Exception ex) {
		        System.out.println("Unexpected error while reading from console!");
		    }
		}

	 private void handleCommand(String command) {
		    try {
		        if (command.equals("#quit")) {
		            System.out.println("Shutting down the server ");
		            server.close();
		            System.exit(0);

		        } else if (command.equals("#stop")) {
		            if (server.isListening()) {
		                server.stopListening();
		                System.out.println("Server stopped listening for new clients.");
		            } else {
		                System.out.println("Server is already stopped.");
		            }

		        } else if (command.equals("#close")) {
		            server.close();
		            System.out.println("Server closed and all clients disconnected.");

		        } else if (command.startsWith("#setport")) {
		            if (!server.isListening()) {
		                String[] parts = command.split(" ");
		                if (parts.length > 1) {
		                    int port = Integer.parseInt(parts[1]);
		                    server.setPort(port);
		                    System.out.println("Port set to: " + port);
		                } else {
		                    System.out.println("Error: No port specified.");
		                }
		            } else {
		                System.out.println("Error: Server must be closed to set the port.");
		            }

		        } else if (command.equals("#start")) {
		            if (!server.isListening()) {
		                server.listen();
		                System.out.println("Server started listening for new clients.");
		            } else {
		                System.out.println("Server is already listening.");
		            }

		        } else if (command.equals("#getport")) {
		            System.out.println("Current port: " + server.getPort());

		        } else {
		            System.out.println("Unknown command.");
		        }
		    } catch (Exception e) {
		        System.out.println("Error executing command: " + e.getMessage());
		    }
		}
	 

	    @Override
	    public void display(String message) {
	        System.out.println(message);
	    }
	    

	
	
		

}
