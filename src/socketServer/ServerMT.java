package socketServer;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServerMT extends Thread{
	private int nbrClient;
	private int secretNum;
	public static void main(String[] args) {
		new ServerMT().start();
		}
	public void run(){
		try {
			ServerSocket serverSocket = new ServerSocket(1000);
			secretNum = new Random().nextInt(100);
			System.out.println("waiting for connexion ..");
			
			while(true) {
				Socket socket = serverSocket.accept();
				++nbrClient;
				new Connexion(socket , nbrClient).start();
				/*System.out.println("Client N° : "+nbrClient);*/
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public class Connexion extends Thread{
		private Socket socket;
		private int nbrClient;
		private boolean fin = false;
		public Connexion(Socket s , int n) {
			// TODO Auto-generated constructor stub
			this.nbrClient=n;
			this.socket=s;
}
		public void run() {
			try {
				InputStream in = socket.getInputStream();
				InputStreamReader inr = new InputStreamReader(in);
				BufferedReader bf = new BufferedReader(inr);
				
				OutputStream out = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(out);
				String IP = socket.getRemoteSocketAddress().toString();
				System.out.println("Connexion client N° : "+nbrClient+" with IP : "+IP);
				System.out.println("SecretNum is : "+secretNum);
				pw.println("Bienvenu au Jeu, vous etes le client N° : "+nbrClient+'\n'+"vous devez deviner le nombre secret");
				pw.flush();
				while(true) {
					String req = bf.readLine();
					int nombre = 0;
					boolean typeErr = true;
					try {
						nombre = Integer.parseInt(req);
					}catch(Exception e) {
						e.printStackTrace();
						typeErr = false;
					}
					if (typeErr) {
						if (fin == false) {
							if (nombre>secretNum) {
								pw.println("votre nombre est sup au nombre secret, essayer encore ..");
								pw.flush();
							}
							else if (nombre<secretNum) {
								pw.println("votre nombre est inf au nombre secret, essayer encore ..");
								pw.flush();
							}
							else{
								pw.println("BRAVO !");
								pw.flush();
								System.out.println("le gagnant est : "+IP);
								fin=true;
								}
						}else {
							pw.println("Jeu terminé ! le gagnant est : "+IP);
							pw.flush();
						}
						}
					else {
						pw.println("Format incorrect");
						pw.flush();
					}
						/*pw.print(req.length());
						pw.flush();*/
					
					
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	

}
