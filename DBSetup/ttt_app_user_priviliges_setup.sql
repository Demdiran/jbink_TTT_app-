use ttt_db;
CREATE USER 'ttspeler'@'%' IDENTIFIED BY 'ttwachtwoord';
GRANT SELECT, INSERT, UPDATE ON ttt_db.* TO 'ttspeler'@'%';