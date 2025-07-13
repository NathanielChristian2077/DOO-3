# DOO-3

## Manual de Uso

### 1. Requisitos

- **Java 17 ou superior**
- **Maven**
- **PostgreSQL** (versão recomendada: 13+)

### 2. Configuração da Base de Dados

1. **Crie um banco de dados PostgreSQL** (ex: `lock_proj`).
2. **Execute o script de criação** localizado em:
   ```
   lock-proj/lock_proj_database.sql
   ```
   Este script cria as tabelas, enums e insere um usuário administrador padrão:
   - Usuário: `admin`
   - Email: `admin@padrao.com`
   - Senha: `*****` (altere após o primeiro acesso)

3. **Configure a conexão no projeto**:
   - Edite o arquivo de configuração de conexão (exemplo: `ConexaoBD.java`) para informar:
     - URL do banco (ex: `jdbc:postgresql://localhost:5432/lock_proj`)
     - Usuário e senha do banco

### 3. Executando a Aplicação Gráfica

1. **Compile o projeto**:
   ```powershell
   mvn clean install
   ```

2. **Execute a interface gráfica**:
   ```powershell
   mvn exec:java -Dexec.mainClass="me.view.App"
   ```
   - A aplicação será iniciada com a tela de login baseada em **AWT/Swing**.
   - Use o usuário administrador padrão para acessar funcionalidades administrativas.

### 4. Funcionalidades Principais

- **Cadastro e login de usuários**
- **Administração de usuários e filmes**
- **Aluguel e devolução de filmes**
- **Filtragem e busca de filmes**
- **Permissões diferenciadas para usuários comuns e administradores**

### 5. Observações

- Recomenda-se alterar a senha do administrador padrão após o primeiro acesso.
- Para zerar as tabelas, utilize o script de truncamento disponível ou comandos SQL apropriados.
- Todas as exceções e erros são tratadas e exibidas na interface.

### 6. Suporte

Em caso de dúvidas, consulte os arquivos de código-fonte.