import socket, threading

def handle_messages(connection: socket.socket):
    '''
        receber mensagens enviadas pelo servidor e exibir pro usuário
    '''

    while True:
        try:
            msg = connection.recv(1024)

            # se não tiver mensagem, é pq ocorreu alguoim erro na conexão
            # se a conexão acabar, aparece um erro
            # caso contrário, vai tentar decodificar a mensagem para mostrar ao usuário.
            if msg:
                print(msg.decode())
            else:
                connection.close()
                break

        except Exception as e:
            print(f'erro ao lidar com mensagem do servidor: {e}')
            connection.close()
            break

def client() -> None:
    '''
        processo principal que inicia a conexão do cliente com o server
        e lida com suas mensagens de entrada
    '''

    SERVER_ADDRESS = '127.0.0.1'
    SERVER_PORT = 12000

    try:
        # Instancia o socket e inicia a conexão com o server
        socket_instance = socket.socket()
        socket_instance.connect((SERVER_ADDRESS, SERVER_PORT))
        # cria um thread para lidar com mensagens enviadas pelo server
        threading.Thread(target=handle_messages, args=[socket_instance]).start()

        print('conectado ao chat!')
        print('digite sua mensagem:')

        # le a entrada do usuário até que ele saia do bate-papo e feche a conexão
        while True:
            msg = input()

            if msg == 'quit':
                break

            # msg p utf-8
            socket_instance.send(msg.encode())

        # fecha a conexao com o server
        socket_instance.close()

    except Exception as e:
        print(f'erro ao conectar ao socket do servidor {e}')
        socket_instance.close()


if __name__ == "__main__":
    client()