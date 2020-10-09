# banknote_manager_protocol_buffer
Um serviço de gerenciamento de notas de alunos usando o método de serialização Protocol Buffer. 

## Bibliotecas utilizadas

### Java
- java.net
- java.io
- java.sql

### Python
- socket
- sqlite3

## Instalação de dependências

### Clonar e acessar repositório
```
git clone https://github.com/souzalucas/banknote_manager_protocol_buffer.git
cd banknote_manager_protocol_buffer
```

### Obter e descompactar o compilador protoc
```
mkdir protoc
cd protoc
wget https://github.com/protocolbuffers/protobuf/releases/download/v3.13.0/protoc-3.13.0-linux-x86_64.zip
unzip protoc-3.13.0-linux-x86_64.zip
```

### Criar atalho para o protoc
```
alias protoc=/home/user/banknote_manager_protocol_buffer/protoc/bin/protoc
```

### Gerar a estrutura e o código para Python e Java
```
cd ..
protoc --python_out=pythoncode/ banknoteManager.proto
protoc --java_out=javacode/ banknoteManager.proto
```

### Obter API Python3
```
pip3 install python3-protobuf protobuf
```

### Obter APIs Java
```
mkdir javacode/lib
cd javacode/lib
wget https://repo1.maven.org/maven2/com/google/protobuf/protobuf-java/3.13.0/protobuf-java-3.13.0.jar
wget https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.32.3.2/sqlite-jdbc-3.32.3.2.jar
```

## Compilação e execução

### Compilar e executar o servidor no diretório javacode
```
javac -cp ":lib/*" *.java -Xlint
java -cp ":lib/*" ServerTcpBanknoteManager
```

### Executar o cliente no diretório pythoncode
```
python3 clientTcpBanknoteManager.py
```

## Exemplo de uso (Listar alunos em uma disciplina)

### No cliente, digite o codigo da operação
```
listAlunos
```

### E por fim, insira as informações da disciplina. O resultado sairá como:
```
RA: 111111
Nome: Fulano
Periodo: 6
--------------------

RA: 222222
Nome: Ciclano
Periodo: 7
--------------------
```
