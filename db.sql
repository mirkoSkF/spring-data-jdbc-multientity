-- 1. Nuova Entità Indipendente per i Tipi di Contatto
CREATE TABLE IF NOT EXISTS tipi_contatti (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    denominazione VARCHAR(50) NOT NULL UNIQUE -- Es. 'TELEFONO', 'EMAIL', 'FACEBOOK'
);

-- I ruoli e i titoli rimangono invariati...
CREATE TABLE IF NOT EXISTS ruoli_aziendali (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    denominazione VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS titoli_studio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descrizione VARCHAR(100) NOT NULL
);

-- 2. Entità Principale
CREATE TABLE IF NOT EXISTS dipendenti (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    codice_fiscale VARCHAR(16) NOT NULL UNIQUE,
    genere VARCHAR(10) NOT NULL,
    data_di_nascita DATE NOT NULL,
    luogo_nascita VARCHAR(100) NOT NULL,
    ruolo_id BIGINT,
    FOREIGN KEY (ruolo_id) REFERENCES ruoli_aziendali(id)
);

-- 3. Entità Account (1-1)
CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    dipendente_id BIGINT NOT NULL,
    FOREIGN KEY (dipendente_id) REFERENCES dipendenti(id) ON DELETE CASCADE
);

-- 4. NUOVA Tabella di Giunzione M-M tra Dipendenti e Tipi Contatti
-- Contiene l'ID del dipendente, l'ID del tipo di contatto e il 'valore' effettivo (es. il numero o l'indirizzo)
CREATE TABLE IF NOT EXISTS contatti (
    dipendente_id BIGINT NOT NULL,
    tipo_contatto_id BIGINT NOT NULL,
    valore VARCHAR(100) NOT NULL,
    PRIMARY KEY (dipendente_id, tipo_contatto_id),
    FOREIGN KEY (dipendente_id) REFERENCES dipendenti(id) ON DELETE CASCADE,
    FOREIGN KEY (tipo_contatto_id) REFERENCES tipi_contatti(id)
);

-- 5. Tabella di Giunzione M-M tra Dipendenti e Titoli di Studio
CREATE TABLE IF NOT EXISTS dipendenti_titoli (
    dipendente_id BIGINT NOT NULL,
    titolo_studio_id BIGINT NOT NULL,
    PRIMARY KEY (dipendente_id, titolo_studio_id),
    FOREIGN KEY (dipendente_id) REFERENCES dipendenti(id) ON DELETE CASCADE,
    FOREIGN KEY (titolo_studio_id) REFERENCES titoli_studio(id)
);
