// sdis - sistemas distribuídos
// chat sockets
// aluno > r. gonçalves de s. junior

package sockets; // importa as lib

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client { // declarando a classe

	private String host; // criei o host em str
	private int port; // criei a porta em int
	private String nickname; // criei o nick em str

	public static void main(String[] args) throws UnknownHostException, IOException { // throws permite dar uma execuçao e essas 2lib vao apresentar os erros
		new Client("127.0.0.1", 12345).run(); // declara o ip, que pode ser 'localhost' tbm e a porta e faz com que rode
	}

	public Client(String host, int port) {
		this.host = host; // this chama a variavel local da classe > host e define como host
		this.port = port; // this chama a variavel local da classe > port e define como port
	}

	public void run() throws UnknownHostException, IOException { // especifica as operaçoes a serem realizadas pela linha de execuçao
		// cconexao do usuario com o servidor
		Socket client = new Socket(host, port); 
		System.out.println("O usuário foi conectado ao chat!"); // indica que o usuario foi conectado ao chat

		// cria um novo thread p mensagens do servidor
		new Thread(new ReceivedMessagesHandler(client.getInputStream())).start();  // manipulador de mensagensd

		// pergunta pelo nick
		Scanner sc = new Scanner(System.in); // scanner = .split() python
		System.out.print("=-=-=-=-=-=-=-=-=\n");
		System.out.print("SEJA BEM-VINDO!\n");
		System.out.print("=-=-=-=-=-=-=-=-=\n");
		System.out.print("Digite seu nome: ");
		nickname = sc.nextLine(); // digitar o nick na prox linha

		// le mensagens digitadas e envia
		System.out.println(">>> Enviar mensagem: "); 
		PrintStream output = new PrintStream(client.getOutputStream());
		while (sc.hasNextLine()) {
			output.println(nickname + " >> " + sc.nextLine()); // nick >> (sua mensagem)
		}
		
		output.close(); //fecha saida
		sc.close();	// fecha scannr
		client.close();	// fecha client
	}
}

class ReceivedMessagesHandler implements Runnable {

	private InputStream server;

	public ReceivedMessagesHandler(InputStream server) {
		this.server = server;
	}

	@Override // sobrepor 
	public void run() {
		// recebe msg e imprime na tela
		Scanner s = new Scanner(server);
		while (s.hasNextLine()) {
			System.out.println(s.nextLine());
		}
		s.close();
	}
}
