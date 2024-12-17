# Usar uma imagem base do OpenJDK
FROM openjdk:17-jdk-slim

# Configurar o diretório de trabalho no container
WORKDIR /app

# Copiar o JAR gerado para o container
COPY target/calculator-1.0.0.jar app.jar

# Comando para rodar a aplicação
CMD ["java", "-Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager", "-jar", "app.jar"]

