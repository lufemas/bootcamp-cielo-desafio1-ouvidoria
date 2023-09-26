### bootcamp-cielo-desafio1-ouvidoria

OBS: frontend está na pasta frontend com o readme próprio.

### Necessário:

- Docker
  
### 🎲 Criar banco PostgreSQL com o comando abaixo:

docker run --name bootcamp-cielo -e POSTGRES_USER=dev -e POSTGRES_PASSWORD=dev -e POSTGRES_DB=feedback -p 5432:5432 --restart always -d postgres

### 📚 Documentação
http://localhost:8080/swagger-ui/index.html

### ⚙️ Execução:
Abrir o projeto no editor de sua escolha e executar o projeto.
Pode ser realizada através do Swagger ou chamando o endpoint pelo Postman/Insomnia.
