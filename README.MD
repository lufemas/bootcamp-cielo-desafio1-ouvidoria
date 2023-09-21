# Bootcamp Cielo - Sistema Básico de Ouvidoria

## Credenciais AWS
- É necessário definir as seguintes variáveis de ambiente:
  - AWS_ACCESS_KEY_ID: Sua chave de acesso da AWS.
  - AWS_SECRET_ACCESS_KEY: Sua chave secreta da AWS.
- A chave de acesso deve ser configurada para um usuário com permissões de leitura e escrita para os seguintes serviços da AWS:
  - SNS
  - SQS
- A chave de acesso deve ter sido criada para ser usada por aplicações em execução fora do ambiente AWS.
