# Proje Yönetim Sistemi

## Geliştiriciler:

- **Furkan Sözkesen** (22060652) - GitHub: [TheBestComputer](https://github.com/TheBestComputer)
- **Alperen Sağut** (22060337) - GitHub: [AlperenSagutdev](https://github.com/AlperenSagutdev)
- **Selahiddin Duman** (22060321) - GitHub: [SelahiddinDuman](https://github.com/SelahiddinDuman)
- **Yusuf Akçakaya** (22060382) - GitHub: [yusuf22060382](https://github.com/yusuf22060382)

---

## Derleme ve Çalıştırma Talimatları:

### Derleme:

Terminalde aşağıdaki komutu yazarak tüm Java dosyalarını derleyin:

```bash
javac *.java
```

### Çalıştırma:

Aşağıdaki komutları sırasıyla çalıştırarak uygulamayı başlatabilirsiniz:

```bash
java -cp ".;JarFile\mysql-connector-j-9.0.0.jar" GirisEkrani
```

---

## MySQL Veri Tabanı Yapılandırması:

### Veritabanı ve Tabloların Oluşturulması

MySQL terminalinde aşağıdaki kodları sırasıyla çalıştırın:

```sql
CREATE DATABASE ProjeYonetim;
USE ProjeYonetim;
CREATE TABLE Kullanicilar (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    Email VARCHAR(100),
    password VARCHAR(255) NOT NULL
);
CREATE TABLE Calisanlar (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    Ad VARCHAR(100),
    Soyad VARCHAR(100),
    Email VARCHAR(100),
    kullaniciId INT,
    FOREIGN KEY (kullaniciId) REFERENCES Kullanicilar(Id)
);

CREATE TABLE Projeler (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    Ad VARCHAR(200),
    BaslangicTarihi DATE,
    BitisTarihi DATE,
    kullaniciId INT,
    FOREIGN KEY (kullaniciId) REFERENCES Kullanicilar(Id)
);

CREATE TABLE Gorevler (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    ProjeId INT,
    CalisanId INT NULL,
    Ad VARCHAR(200),
    Durum VARCHAR(50),
    BaslangicTarihi DATE,
    BitisTarihi DATE,
    AdamGun FLOAT,
    FOREIGN KEY (ProjeId) REFERENCES Projeler(Id),
    FOREIGN KEY (CalisanId) REFERENCES Calisanlar(Id) ON DELETE SET NULL
);  
```

---

## Proje Hakkında:

Bu proje, bir Proje Yönetim Sistemi geliştirilmesini hedeflemektedir.

### Özellikler:

- Çalışan ve proje kayıtlarının tutulması.
- Görevlerin atanması ve durumlarının izlenmesi.
- Proje ve çalışan bilgileri arasında ilişki kurulması.

---

### Gereksinimler:

- **Java JDK 8 veya üstü**
- **MySQL Server**
- **MySQL Connector JAR** (mysql-connector-j-9.0.0.jar dosyasının proje klasöründe olması gerekmektedir.)

