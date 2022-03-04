import socket, threading

connections = []

def handle_user_connection(connection: socket.socket, address: str) -> None:
    '''
        obtem a conexão do usuário para continuar recebendo suas mensagens e
        enviados para outros usuários/conexoes.
    '''
    while True:
        try:
            # pega a msg do cliente
            msg = connection.recv(1024)

            # se nenhuma mensagem for recebida é pq a conexao falhou
            # então nesse caso, precisamos fechar a conexão ou remover da lista de conexões.
            if msg:
                # mensagem log de erro pro usuario
                print(f'{address[0]}:{address[1]} - {msg.decode()}')
                
                # cria o formato da mensagem e transmita para os usuarios conectados no server
                msg_to_send = f'de {address[0]}:{address[1]} - {msg.decode()}'
                broadcast(msg_to_send, connection)

            # fecha a conexao se nenhuma mensagem foi enviada
            else:
                remove_connection(connection)
                break

        except Exception as e:
            print(f'erro na conexão do usuário: {e}')
            remove_connection(connection)
            break


def broadcast(message: str, connection: socket.socket) -> None:
    '''
        transmitir mensagem para todos os usuários conectados ao servidor
    '''

    # enviar mensagens para todos os clientes conectados
    for client_conn in connections:
        # verifique se não é a conexão de quem está enviando
        if client_conn != connection:
            try:
                # envia msg pra conexao do cliente
                client_conn.send(message.encode())

            # se der erro, tem chance de ter problema com o socket 
            except Exception as e:
                print('erro ao transmitir a mensagem: {e}')
                remove_connection(client_conn)


def remove_connection(conn: socket.socket) -> None:
    '''
        remove a conexao especificada da lista de conexoes
    '''

    # conferir se existe a conexao na lista
    if conn in connections:
        # fecha a conexao do socket e remove a conexao da lista de conexoes
        conn.close()
        connections.remove(conn)


def server() -> None:
    '''
        processo principal que recebe as conexões do cliente e inicia uma nova thread
        para mandar as mensagens
    '''

    LISTENING_PORT = 12000
    
    try:
        # cria o servidor e especifica que ele so pode lidar com 4 conexoes ao mesmo tempo
        socket_instance = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        socket_instance.bind(('', LISTENING_PORT))
        socket_instance.listen(4)

        print('servidor funcionando!')
        
        while True:

            # aceita a conexao do cliente
            socket_connection, address = socket_instance.accept()
            # adicionar conexão de cliente à lista de conexões
            connections.append(socket_connection)
            # iniciar um novo thread p a conexão do cliente e receber suas mensagens
            # para enviar a outras conexões
            threading.Thread(target=handle_user_connection, args=[socket_connection, address]).start()

    except Exception as e:
        print(f'ocorreu um erro ao instanciar o socket: {e}')
    finally:
        # em caso de algum problema, limpa todas as conexões e fecha a conexão do server
        if len(connections) > 0:
            for conn in connections:
                remove_connection(conn)

        socket_instance.close()


if __name__ == "__main__":
    server()