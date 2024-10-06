package socketServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(1000);
		System.out.println("waiting connection for user ...");
		Socket socket = serverSocket.accept();
		System.out.println("connected");
		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();
		int number = in.read();
		int res = number*3;
		out.write(res);
		}

}
