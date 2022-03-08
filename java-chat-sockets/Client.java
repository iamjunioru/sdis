// sdis - sistemas distribuídos
// chat sockets
// aluno > r. gonçalves de s. junior

package sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	private String host;
	private int port;
	private String nickname;

	public static void main(String[] args) throws UnknownHostException, IOException {
		new Client("127.0.0.1", 12345).run();
	}

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void run() throws UnknownHostException, IOException {
		// cconexao do usuario com o servidor
		Socket client = new Socket(host, port);
		System.out.println("O usuário foi conectado ao chat!");

		// cria um novo thread p mensagens do servidor
		new Thread(new ReceivedMessagesHandler(client.getInputStream())).start();

		// pergunta pelo nick
		Scanner sc = new Scanner(System.in);
		System.out.print("=-=-=-=-=-=-=-=-=\n");
		System.out.print("SEJA BEM-VINDO!\n");
		System.out.print("=-=-=-=-=-=-=-=-=\n");
		System.out.print("Digite seu nome: ");
		nickname = sc.nextLine();

		// le mensagens digitadas e envia
		System.out.println(">>> Enviar mensagem: ");
		PrintStream output = new PrintStream(client.getOutputStream());
		while (sc.hasNextLine()) {
			output.println(nickname + " >> " + sc.nextLine());
		}
		
		output.close();
		sc.close();
		client.close();
	}
}

class ReceivedMessagesHandler implements Runnable {

	private InputStream server;

	public ReceivedMessagesHandler(InputStream server) {
		this.server = server;
	}

	@Override
	public void run() {
		// recebe msg e imprime na tela
		Scanner s = new Scanner(server);
		while (s.hasNextLine()) {
			System.out.println(s.nextLine());
		}
		s.close();
	}
}
