import socket
import addressbook_pb2
import sqlite3

# Conectando com o banco de dados
conn = sqlite3.connect('../database/gerenciamento_notas.db')
# Definindo um cursor
cursor = conn.cursor()
# Desconectando com banco de dados
conn.close()

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(("localhost", 7000))

# Instanciar e preencher a estrutura
person = addressbook_pb2.Person()
person.id = 234
person.name = "Lucas_Souza"
person.email = "lsouza.santos98@gmail.com"

# Marshalling
msg = person.SerializeToString()
size = len(msg)

client_socket.send((str(size) + "\n").encode())
client_socket.send(msg)

client_socket.close()