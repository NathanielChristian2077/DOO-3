# DOO-3: Aplicação de Locadora de Filmes

## Índice
1. [Requisitos](#1-requisitos)
2. [Configuração da Base de Dados](#2-configuração-da-base-de-dados)
3. [Executando a Aplicação](#3-executando-a-aplicação)
4. [Funcionalidades Principais](#4-funcionalidades-principais)
5. [Interface da Aplicação](#5-interface-da-aplicação)
6. [Erros e Exceções](#6-erros-e-exceções)
7. [Considerações de Segurança](#7-considerações-de-segurança)

---

## 1. Requisitos

- **Java 17 ou superior**
- **Maven**
- **PostgreSQL** (versão recomendada: 13+)

> ⚠️ Certifique-se de que sua instalação do Java e Maven está configurada corretamente no PATH do sistema operacional.

---

## 2. Configuração da Base de Dados

### 2.1. Configuração Inicial
1. **Crie um banco de dados no PostgreSQL** (exemplo: `lock_proj`).
2. **Execute o script de configuração** localizado em:
   ```
   lock-proj/lock_proj_database.sql
   ```
   Este script:
    - Cria tabelas, *enums* e relacionamentos.
    - Insere um administrador padrão:
        - **Usuário**: `admin`
        - **Email**: `admin@padrao.com`
        - **Senha**: `*****` *(recomenda-se alterar após o login).*

### 2.2. Configuração da Conexão no Código-Fonte
No arquivo [`ConexaoBD.java`](src/main/java/me/model/persistencia/ConexaoBD.java), ajuste as credenciais:

---

## 3. Executando a Aplicação

### 3.1. Compilação do Projeto
Compile o projeto usando o Maven:

```bash
   mvn clean install
```

### 3.2. Inicializando a Interface Gráfica
Inicie a aplicação executando:

```bash
   mvn exec:java -Dexec.mainClass="view.telas.TelaLogin"
```
---

## 4. Funcionalidades Principais

### 4.1. Para Usuários Comuns
- Acessar catálogo de filmes disponíveis.
- Realizar o aluguel de filmes.
- Listar e devolver filmes já alugados.
- Editar dados de perfil (email, nome de usuário, senha).

### 4.2. Para Administradores
- Gerenciar usuários:
    - Adicionar, editar e excluir usuários.
    - Filtrar e visualizar dados de usuários.
    - Visualizar filmes alugados por usuários.
- Gerenciar filmes:
    - Cadastrar, editar e excluir filmes do catálogo.
    - Filtrar por nome, gênero e qualidade.

---

## 5. Interface da Aplicação

### 5.1. Tela de Login
- Autenticação baseada em email ou nome de usuário.
- Diferenciação de acesso para usuários comuns e administradores.

### 5.2. Painel do Usuário
- Exibição de **catálogo de filmes** com opções para filtrar e ordenar.
- A opção de visualizar **detalhes do filme** ao clicar duas vezes em uma linha na tabela.
- Disponibilidade para **alugar** e **devolver filmes**.

### 5.3. Painel do Administrador
- **Gestão de Usuários** e funcionalidades como:
    - Adição ou remoção de administradores.
    - Visualização de aluguel de filmes por usuários.
- **Gestão de Filmes**, incluindo:
    - Cadastro de novos filmes com informações detalhadas (nome, descrição, duração, preço, qualidade e gêneros).

---

## 6. Erros e Exceções

Lista de exceções tratadas no sistema:
- **DadosInvalidosException**: Utilizada para entradas inválidas do usuário.
- **UsuarioDuplicadoException**: Lançada quando tenta-se cadastrar um email ou nome de usuário já existente.
- **UsuarioNaoEncontradoException**: Lançada no login quando nenhum usuário corresponde às credenciais.
- **PermissaoNegadaException**: Usada para tentativas de ações restritas para administradores.
- **FilmeNaoEncontradoException**: Lançada ao tentar acessar um filme inexistente.
- **FilmeJaAlugadoException**: Indicativo de tentativas redundantes de aluguel.
- **OperacaoBancoException**: Representa erros de conexão ou transações no banco de dados.

---

## 7. Considerações de Segurança

- **Alteração de Senhas Padrão**:
  Alterar a senha padrão do administrador (`admin`) imediatamente após o primeiro acesso.

- **Proteção de Credenciais**:
  Certifique-se de armazenar credenciais do banco (usuário/senha) em ambientes seguros (ex.: arquivos `.env`).

- **Senha do Usuário**:
    - As senhas são armazenadas com a prática recomendada de hashing seguro.

---