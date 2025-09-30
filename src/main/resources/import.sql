-- Remova ou comente a linha: ALTER SEQUENCE hibernate_sequence RESTART WITH 100;

-- =================================================================================
-- 1. DADOS DOS ÔNIBUS (ID é gerado automaticamente a partir do valor atual da sequence)
-- =================================================================================

INSERT INTO Onibus (id, placa, anoFabricacao, capacidadePassageiros)
VALUES (nextval('hibernate_sequence'), 'ABC1234', 2022, 45);

INSERT INTO Onibus (id, placa, anoFabricacao, capacidadePassageiros)
VALUES (nextval('hibernate_sequence'), 'XYZ5678', 2018, 50);

INSERT INTO Onibus (id, placa, anoFabricacao, capacidadePassageiros)
VALUES (nextval('hibernate_sequence'), 'QWE9012', 2024, 40);


-- =================================================================================
-- 2. DADOS DOS MOTORISTAS
-- =================================================================================

INSERT INTO Motorista (id, cpf, nomeCompleto, numeroCnh, categoriaCnh)
VALUES (nextval('hibernate_sequence'), '11122233344', 'João da Silva', '00112233445', 'D');

INSERT INTO Motorista (id, cpf, nomeCompleto, numeroCnh, categoriaCnh)
VALUES (nextval('hibernate_sequence'), '55566677788', 'Maria de Souza', '00556677889', 'E');

INSERT INTO Motorista (id, cpf, nomeCompleto, numeroCnh, categoriaCnh)
VALUES (nextval('hibernate_sequence'), '99900011122', 'Pedro Alvares', '00990011223', 'D');


-- =================================================================================
-- 3. DADOS DAS VIAGENS
-- Os IDs 1, 2, 3... dos Motoristas e Ônibus serão determinados pela ordem de inserção
-- Certifique-se de que os IDs referenciados aqui (que não estão mais fixos)
-- correspondem aos IDs gerados pela sequência. Se for um banco de dados novo,
-- use 1, 2 e 3 para o Motorista e 1, 2, 3 para o Ônibus.
-- Vamos supor que a sequência começou em 1 e gerou IDs sequenciais:
-- Onibus: 1, 2, 3
-- Motorista: 4, 5, 6
-- =================================================================================

-- Viagem 1: Motorista João (ID 4) com Ônibus ABC1234 (ID 1)
INSERT INTO Viagem (id, motorista_id, onibus_id, origem, destino, dataPartida)
VALUES (
    nextval('hibernate_sequence'),
    4, -- João da Silva (4º item inserido)
    1, -- Ônibus 1 (1º item inserido)
    'São Paulo',
    'Rio de Janeiro',
    '2025-10-15 22:30:00'
);

-- Viagem 2: Motorista Maria (ID 5) com Ônibus XYZ5678 (ID 2)
INSERT INTO Viagem (id, motorista_id, onibus_id, origem, destino, dataPartida)
VALUES (
    nextval('hibernate_sequence'),
    5, -- Maria de Souza (5º item inserido)
    2, -- Ônibus 2 (2º item inserido)
    'Belo Horizonte',
    'Vitória',
    '2025-10-16 08:00:00'
);

-- Viagem 3: Motorista Pedro (ID 6) com Ônibus QWE9012 (ID 3)
INSERT INTO Viagem (id, motorista_id, onibus_id, origem, destino, dataPartida)
VALUES (
    nextval('hibernate_sequence'),
    6, -- Pedro Alvares (6º item inserido)
    3, -- Ônibus 3 (3º item inserido)
    'Curitiba',
    'Florianópolis',
    '2025-10-16 14:00:00'
);