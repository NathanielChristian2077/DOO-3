-- Criação do enum de tipo de usuário
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'tipo') THEN
        CREATE TYPE tipo AS ENUM ('COMUM', 'ADMIN');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'genero') THEN
        CREATE TYPE genero AS ENUM (
            'ACAO', 'AVENTURA', 'COMEDIA', 'DRAMA', 'FANTASIA',
            'SCI_FI', 'ROMANCE', 'TERROR', 'SUSPENSE', 'DOCUMENTARIO',
            'MUSICAL', 'GUERRA', 'FAROESTE', 'BIOGRAFIA', 'POLICIAL',
            'MISTERIO', 'HISTORICO', 'ESPORTE', 'FAMILIA',
            'THRILLER_PSICOLOGICO', 'CURTA_METRAGEM'
        );
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'qualidade') THEN
        CREATE TYPE qualidade AS ENUM ('HD', 'FullHD', 'QHD', 'ULTRAHD');
    END IF;
END $$;

-- Tabela de Users
CREATE TABLE IF NOT EXISTS usuario (
    id UUID PRIMARY KEY,
    email TEXT NOT NULL,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    tipo TEXT NOT NULL
);

-- Tabela de Filmes
CREATE TABLE IF NOT EXISTS filme (
    id UUID PRIMARY KEY,
    nome TEXT NOT NULL,
    descricao TEXT NOT NULL,
    duracao INTEGER NOT NULL,
    preco DECIMAL(5, 2) NOT NULL,
    qualidade TEXT NOT NULL,
    genero TEXT NOT NULL
);

-- Tabela relacional n<->n usuario_filme (filmes alugados pelo usuario)
CREATE TABLE IF NOT EXISTS usuario_filme (
    usuario_id UUID REFERENCES usuario(id) ON DELETE CASCADE,
    filme_id UUID REFERENCES filme(id) ON DELETE CASCADE,
    data_aluguel TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (usuario_id, filme_id)
);

-- ADM padronizado para possibilitar a inclusão de outros users como adms
INSERT INTO usuario (id, email, username, password, tipo)
VALUES ('00000000-0000-0000-0000-000000000001', 'admin@padrao.com', 'admin', '*****', 'ADMIN')