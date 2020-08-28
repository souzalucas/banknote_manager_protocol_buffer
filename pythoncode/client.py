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
req = addressbook_pb2.Req()
req.opCode = "listAlunos"
req.RA = 1858
req.nota = 95
req.discCode = "bcc32c"
req.ano = 2020
req.semestre = 1

# Marshalling
msg = req.SerializeToString()
size = len(msg)

client_socket.send((str(size) + "\n").encode())
client_socket.send(msg)

resposta = client_socket.recv(1024)

res = addressbook_pb2.Res()
res.ParseFromString(resposta)
print(res)

client_socket.close()