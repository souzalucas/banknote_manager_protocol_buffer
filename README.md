# banknote_manager_protocol_buffer
Um serviço de gerenciamento de notas de alunos usando o método de serialização Protocol Buffer. 

## Instalação de dependências:

### Obter compilador protoc e descompactar
```
> mkdir protoc
> wget https://github.com/protocolbuffers/protobuf/releases/download/v3.13.0/protoc-3.13.0-linux-x86_64.zip
> unzip protoc−3.13.0−linux−x86 64.zip
```

### Criar atalho para o protoc
```
> alias protoc=/home/user/banknote_manager_protocol_buffer/protoc/bin/protoc
```

### Gerar a estrutura e o código para Python e Java
```
> cd ..
> protoc --python_out=pythoncode/ banknoteManager.proto
> protoc --java_out=javacode/ banknoteManager.proto
```

### Obter API Python3
```
> pip3 install python3-protobuf protobuf
```

### Obter APIs Java
```
> cd javacode/lib
> wget https://repo1.maven.org/maven2/com/google/protobuf/protobuf-java/3.13.0/protobuf-java-3.13.0.jar
> wget https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.32.3.2/sqlite-jdbc-3.32.3.2.jar
```

## Compilação e execução

### Compilar e executar o servidor em javacode/
```
> javac -cp ":lib/*" *.java -Xlint
> java -cp ":lib/*" ServidorTcpBanknoteManager
```

### Executar o cliente em pythoncode/
```
> python3 client.py
```