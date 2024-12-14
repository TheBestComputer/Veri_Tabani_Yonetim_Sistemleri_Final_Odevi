MySql indirme aşamalarını youtube videosundan indirebilirsiniz:
-https://www.youtube.com/watch?v=dbSdY_Gj4zM



Aşağıdaki kodu derlemek için terminale yazın:
javac *.java
Aşağıdaki üç kodu yazarak çalıştırın:
java -cp ".;JarFile\mysql-connector-j-9.0.0.jar" DatabaseHelper
java -cp ".;JarFile\mysql-connector-j-9.0.0.jar" ProjeRepository
java -cp ".;JarFile\mysql-connector-j-9.0.0.jar" ProjeYonetimUI

MySql kodları aşağıdadır:

CREATE DATABASE ProjeYonetim;
USE ProjeYonetim;
CREATE TABLE Calisanlar (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    Ad VARCHAR(100),
    Soyad VARCHAR(100),
    Email VARCHAR(100)
);

CREATE TABLE Projeler (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    Ad VARCHAR(200),
    BaslangicTarihi DATE,
    BitisTarihi DATE
);

CREATE TABLE Gorevler (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    ProjeId INT,
    CalisanId INT,
    Ad VARCHAR(200),
    Durum VARCHAR(50),
    BaslangicTarihi DATE,
    BitisTarihi DATE,
    AdamGun INT,
    FOREIGN KEY (ProjeId) REFERENCES Projeler(Id),
    FOREIGN KEY (CalisanId) REFERENCES Calisanlar(Id)
);
