CREATE TABLE receita (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    src_imagem TEXT,
    descricao TEXT,
    outros_ingredientes TEXT[],
    preparo TEXT,
    uso TEXT
);

CREATE TABLE panc (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    src_imagem TEXT,
    nome VARCHAR(150) NOT NULL,
    descricao TEXT,
    origem_nativa VARCHAR(150)
);

CREATE TABLE sintoma (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    descricao TEXT
);

CREATE TABLE usuario (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    senha TEXT NOT NULL,
    acesso INTEGER,
    data DATE,
    obs TEXT
);

CREATE TABLE receita_panc (
    receita_id INTEGER,
    panc_id INTEGER,

    PRIMARY KEY (receita_id, panc_id),

    FOREIGN KEY (receita_id)
        REFERENCES receita(id)
        ON DELETE CASCADE,

    FOREIGN KEY (panc_id)
        REFERENCES panc(id)
        ON DELETE CASCADE
);

CREATE TABLE receita_sintoma (
    receita_id INTEGER,
    sintoma_id INTEGER,

    PRIMARY KEY (receita_id, sintoma_id),

    FOREIGN KEY (receita_id)
        REFERENCES receita(id)
        ON DELETE CASCADE,

    FOREIGN KEY (sintoma_id)
        REFERENCES sintoma(id)
        ON DELETE CASCADE
);

CREATE TABLE panc_sintoma (
    panc_id INTEGER,
    sintoma_id INTEGER,

    PRIMARY KEY (panc_id, sintoma_id),

    FOREIGN KEY (panc_id)
        REFERENCES panc(id)
        ON DELETE CASCADE,

    FOREIGN KEY (sintoma_id)
        REFERENCES sintoma(id)
        ON DELETE CASCADE
);