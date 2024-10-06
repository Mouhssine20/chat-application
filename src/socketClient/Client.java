package socketClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("127.0.0.1",1000);
		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();
		Scanner scanner = new Scanner(System.in);
		System.out.println("enter a number");
		int number = scanner.nextInt();
		out.write(number);
		int res = in.read();
		System.out.println("result comming from server : "+res);
		}

}
