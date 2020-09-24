# clientTcpBanknoteManager.py
# Cliente TCP de um serviço de gerenciamento de notas de alunos 
# usando o método de serialização Protocol Buffer.
# Autores: Lucas Souza Santos & Alan Rodrigo Patriarca 
# Data de Criação: 26/08/2020
# Ultima atualização: 30/08/2020

import socket
import banknoteManager_pb2
import sqlite3

client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(("localhost", 7000))

def main():
  print("[addNota] Adiciona nota a um aluno")
  print("[rmNota] Remove a nota de um aluno")
  print("[listAlunos] Lista os alunos de uma disciplina em um ano/semestre\n")
  while(True):

    opCode = input("Que operação deseja fazer? > ")

    # Instanciando a estrutura
    req = banknoteManager_pb2.Req()
    req.opCode = str(opCode)

    if (opCode == "addNota"):
      ra = input("RA do aluno > ")
      discCode = input("Codigo da Disciplina > ")
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
      discCode = input("Codigo da Disciplina > ")
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

    elif (opCode == "exit"):
      break
    
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
    res = banknoteManager_pb2.Res()
    res.ParseFromString(resposta)

    if (res.retorno == "1"):
      if (opCode == "listAlunos"):
        for aluno in res.alunos:
          print("\nRA:", aluno.RA)
          print("Nome:", aluno.nome)
          print("Periodo:", aluno.periodo)
          print("Nota:", aluno.nota)
          print("Faltas:", aluno.faltas)
          print("--------------------")
      
      else:
        print("Operação realizada com sucesso")

    else:
      print(res.retorno)

main()
client_socket.close()
