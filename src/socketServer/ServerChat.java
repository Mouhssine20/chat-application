package socketServer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerChat extends Thread {
    private int nbrClient;
    private List<Connexion> clients = new ArrayList<>();

    public static void main(String[] args) {
        new ServerChat().start();
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(1000);
            System.out.println("waiting for connection ..");
            while (true) {
                Socket socket = serverSocket.accept();
                ++nbrClient;
                Connexion connexion = new Connexion(socket, nbrClient);
                clients.add(connexion);
                connexion.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class Connexion extends Thread {
        protected Socket socket;
        protected int nbrClient;

        public Connexion(Socket s, int n) {
            this.nbrClient = n;
            this.socket = s;
        }

        // Method to broadcast messages to all other clients
        public void broadcastMessage(String msg, Socket senderSocket, int numClient) {
            try {
                for (Connexion client : clients) {
                    // Send message to all clients except the sender
                    if (client.socket != senderSocket) {
                    	
                    	if(client.nbrClient == numClient || numClient==-1 ) {
                    		PrintWriter printWriter = new PrintWriter(client.socket.getOutputStream(), true);
	                        printWriter.println(msg);
                    	}
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                InputStream in = socket.getInputStream();
                InputStreamReader inr = new InputStreamReader(in);
                BufferedReader bf = new BufferedReader(inr);

                OutputStream out = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(out, true); // Auto-flush enabled

                String IP = socket.getRemoteSocketAddress().toString();
                System.out.println("Connexion client N° : " + nbrClient + " with IP : " + IP);
                pw.println("Welcome client N° " + nbrClient);

                // Continuously read messages from the client
                while (true) {
                	
                    String req = bf.readLine();
                    if (req == null) {
                        break; // Exit if client disconnects
                    }
                    if (req.contains("=>")) {
                    	String[] listmsg = req.split("=>");
                        if(listmsg.length == 2) {
                        	String msg = listmsg[1];
                        	int numClient = Integer.parseInt(listmsg[0]);
                        	System.out.println("Client " + nbrClient + " says: " + req);
                        	broadcastMessage("Client " + nbrClient + ": " + msg, socket, numClient);
                        	
                        }
                    }
                    else {
                    	broadcastMessage("Client " + nbrClient + ": " + req, socket, -1);
                    }
                    
                    
                    
                    

                    // Broadcast the message to all other clients
                    
                }

                
                System.out.println("Client " + nbrClient + " disconnected.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
