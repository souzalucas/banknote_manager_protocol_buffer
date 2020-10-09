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

    # Preenche a estrutura de acordo com o opCode
    if (opCode == "addNota" or opCode == "rmNota" or opCode == "listAlunos"):
      discCode = input("Codigo da Disciplina > ")
      ano = input("Ano > ")
      semestre = input("Semestre > ")

      # Tratando campos vazios
      if(discCode == '' or ano == '' or semestre == ''):
        print("ERRO: CAMPOS VAZIOS!")
        continue

      req.discCode = str(discCode)
      req.ano = int(ano)
      req.semestre = int(semestre)

    else:
      print("ERRO: OPERACAO INVALIDA!")
      continue

    if (opCode == "addNota" or opCode == "rmNota"):
      ra = input("RA do aluno > ")

      # Tratando campos vazios
      if(ra == ''):
        print("ERRO: CAMPO VAZIO!")
        continue
      
      req.RA = int(ra)
      
    if (opCode == "addNota"):
      nota = input("Nota > ")

      # Tratando campos vazios
      if(nota == ''):
        print("ERRO: CAMPO VAZIO!")
        continue

      req.nota = float(nota)
    
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
          print("--------------------")
      
      else: 
        print("Operação realizada com sucesso")

    else:
      print(res.retorno)

main()
client_socket.close()
