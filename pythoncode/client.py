import socket
import addressbook_pb2
import sqlite3

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(("localhost", 7000))

def main():
  while(True):
    opCode = input("Que operação deseja fazer? > ")

    # Instanciando a estrutura
    req = addressbook_pb2.Req()
    req.opCode = str(opCode)

    if (opCode == "addNota"):
      ra = input("RA do aluno > ")
      discCode = input("Disciplina > ")
      ano = input("Ano > ")
      semestre = input("Semestre > ")
      nota = input("Nota > ")

      # Preencher a estrutura
      req.RA = int(ra)
      req.discCode = str(discCode)
      req.ano = int(ano)
      req.semestre = int(semestre)
      req.nota = float(nota)

    elif (opCode == "rmNota"):
      ra = input("RA do aluno > ")
      discCode = input("Disciplina > ")
      ano = input("Ano > ")
      semestre = input("Semestre > ")

      # Peencher a estrutura
      req.RA = int(ra)
      req.discCode = str(discCode)
      req.ano = int(ano)
      req.semestre = int(semestre)

    elif (opCode == "listAlunos"):
      discCode = input("Codigo da disciplina > ")
      ano = input("Ano > ")
      semestre = input("semestre > ")

      # Preencher a estrutura
      req.discCode = str(discCode)
      req.ano = int(ano)
      req.semestre = int(semestre)
    
    else:
      continue

    # Marshalling
    msg = req.SerializeToString()
    size = len(msg)

    # Enviando
    client_socket.send((str(size) + "\n").encode())
    client_socket.send(msg)

    # Recebendo tamanho da resposta
    receive = client_socket.recv(1024)
    bufferSize = int((receive.decode()).split(" ")[0])

    # Recebendo resposta
    resposta = client_socket.recv(2014)
    res = addressbook_pb2.Res()
    res.ParseFromString(resposta)

    if (res.retorno == "1"):
      if (opCode == "listAlunos"):
        for aluno in res.alunos:
          print("RA: ", aluno.RA)
          print("Nome: ", aluno.nome)
          print("Periodo: ", aluno.periodo)
          print("Nota: ", aluno.nota)
          print("Faltas: ", aluno.faltas)
          print("-------------------- \n")
      else:
        print("Operação realizada com sucesso")

    else:
      print(res.retorno)

main()
client_socket.close()
