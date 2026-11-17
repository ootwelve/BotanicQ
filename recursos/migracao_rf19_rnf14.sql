-- =====================================================================
-- BotanicQ - migracao para RF19 e RNF14
-- Rode UMA vez no banco botanicq_db que ja existe (pgAdmin > Query Tool,
-- ou: psql -U postgres -d botanicq_db -f migracao_rf19_rnf14.sql)
-- Seus dados atuais sao preservados.
-- =====================================================================

-- ---------------------------------------------------------------------
-- PASSO 0 (opcional, so diagnostico): existe nome repetido hoje?
-- Se alguma dessas consultas retornar linhas, resolva (renomeie ou apague
-- o repetido) ANTES de seguir, senao o PASSO 2 vai falhar.
-- ---------------------------------------------------------------------
-- SELECT LOWER(BTRIM(nome))   AS nome, COUNT(*) FROM panc    GROUP BY 1 HAVING COUNT(*) > 1;
-- SELECT LOWER(BTRIM(titulo)) AS nome, COUNT(*) FROM receita GROUP BY 1 HAVING COUNT(*) > 1;
-- SELECT LOWER(BTRIM(nome))   AS nome, COUNT(*) FROM sintoma GROUP BY 1 HAVING COUNT(*) > 1;

BEGIN;

-- ---------------------------------------------------------------------
-- PASSO 1 - RF19: quem foi o ultimo administrador a alterar o cadastro
-- ---------------------------------------------------------------------
ALTER TABLE panc    ADD COLUMN IF NOT EXISTS ultimo_admin_id INTEGER REFERENCES usuario(id) ON DELETE SET NULL;
ALTER TABLE panc    ADD COLUMN IF NOT EXISTS ultima_alteracao TIMESTAMP;

ALTER TABLE receita ADD COLUMN IF NOT EXISTS ultimo_admin_id INTEGER REFERENCES usuario(id) ON DELETE SET NULL;
ALTER TABLE receita ADD COLUMN IF NOT EXISTS ultima_alteracao TIMESTAMP;

ALTER TABLE sintoma ADD COLUMN IF NOT EXISTS ultimo_admin_id INTEGER REFERENCES usuario(id) ON DELETE SET NULL;
ALTER TABLE sintoma ADD COLUMN IF NOT EXISTS ultima_alteracao TIMESTAMP;

-- ---------------------------------------------------------------------
-- PASSO 2 - RNF14: nao permitir nome duplicado
-- (ignora maiusculas/minusculas e espacos nas pontas:
--  'Ora-pro-nobis' e ' ora-pro-nobis ' contam como o mesmo nome)
-- ---------------------------------------------------------------------
CREATE UNIQUE INDEX IF NOT EXISTS ux_panc_nome    ON panc    (LOWER(BTRIM(nome)));
CREATE UNIQUE INDEX IF NOT EXISTS ux_receita_nome ON receita (LOWER(BTRIM(titulo)));
CREATE UNIQUE INDEX IF NOT EXISTS ux_sintoma_nome ON sintoma (LOWER(BTRIM(nome)));

COMMIT;
