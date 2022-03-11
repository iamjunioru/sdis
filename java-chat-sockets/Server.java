package sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {	// cria a class server

	private int port; // define int port
	private List<PrintStream> clients; // lista de clients
	private ServerSocket server; // socketserver

	public static void main(String[] args) throws IOException {
		new Server(12345).run(); // coloca o servidor pra rodar na porta 12345
	}

	public Server(int port) {
		this.port = port; // this definido port
		this.clients = new ArrayList<PrintStream>();
	}

	public void run() throws IOException {
		server = new ServerSocket(port) {
			protected void finalize() throws IOException {
				this.close();
			}
		};
		System.out.println("porta 12345 está aberta.");	// mensagem de porta aberta aparece
		System.out.println("conectado! agora você está online."); // mensagem de conectado aparece

		while (true) {
			// acc novo usuario
			Socket client = server.accept();
			System.out.println("estabelecendo conexão com o usuario: " + client.getInetAddress().getHostAddress()); // retorna o ip e pega o endereço
			
			// add usuario a lista
			this.clients.add(new PrintStream(client.getOutputStream()));
			
			// cria nova thead
			new Thread(new ClientHandler(this, client.getInputStream())).start();
		}
	}

	void broadcastMessages(String msg) { // mostra msg p todos
		for (PrintStream client : this.clients) {
			client.println(msg);
		}
	}
}

class ClientHandler implements Runnable {

	private Server server;
	private InputStream client;

	public ClientHandler(Server server, InputStream client) {
		this.server = server;
		this.client = client;
	}

	@Override
	public void run() {
		String message;
		
		// quando houver uma nova mensagem, mandar para todos
		Scanner sc = new Scanner(this.client);
		while (sc.hasNextLine()) {
			message = sc.nextLine();
			server.broadcastMessages(message);
		}
		sc.close();
	}
}
