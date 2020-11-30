# processamentoArquivo

# Solução de micro serviços para processamento de arquivos
OBS.: Nesse primeiro momento, não está sendo utilizado o eureka e o zuul. Estes seriam para uma futura interface que pudessem disponibilizar para o usuario em forma de relatórios ou qualquer outro formato que o mesmo desejasse. Pensando numa solução mais completa, escalável e robusta.

# Tecnologias
- Java 11
- Spring Boot 2.4.0
    - spring-boot-starter
    - spring-boot-starter-activemq    
    - spring-boot-starter-data-jpa
    - spring-boot-starter-web
    - spring-boot-starter-jdbc
- H2 Database
- RabbitMQ

# Configuração de ambiente

- Pasta de leitura dos arquivos
  -  Criar pasta para leitura dos arquivos
     - %HOMEPATH%/data/in.
  -  Criar pasta para os arquivos processados
     - %HOMEPATH%/data/process.
  -  Criar pasta para saída dos arquivos
     - %HOMEPATH%/data/out.
  -  Criar pasta error para os casos de erros durante o processamento dos arquivos
     - %HOMEPATH%/data/error.

- Docker compose
  - executar o comando 'docker-compose up -d' para baixar a imagem do RabbitMQ e inicializá-lo
  - Acesso visual ao admin do RabbitMQ: http://localhost:15672/
     - usuario: admin
     - senha: admin
  - Configuração(Queue)  
     - Criar exchange:
       -  processor.exchange
     - Criar fila:
       - processor.rabbitmq.vendas.queue
     - Realizar o bind em processor.exchange
       -  to: processor.rabbitmq.vendas.queue
       - routingKey: processor.rabbitmq.routingkey 	
      
# Execução

A execução das aplicações são feitas através do de um comando Maven que envoca a inicialização do Spring Boot.

- reader
    -  cd /processamentoArquivo/reader
    - ```mvn spring-boot:run```
 - processador
    -  cd /processamentoArquivo/processador
    - ```mvn spring-boot:run```
  
 # Utilização
 
- Reader: Apenas é executado em background, lendo os arquivos do diretório configurado e enviando para fila no RabbitMQ.
- Processador: Apenas é executado em background, recebendo arquivos da fila, processando-os e persistindo no Banco de Dados. Nessa versão, também gera os arquivos de saída.
