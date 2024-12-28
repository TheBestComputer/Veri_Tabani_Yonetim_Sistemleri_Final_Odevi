import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class GirisEkrani {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GirisEkrani::girisEkrani);
    }

    private static void girisEkrani() {
        JFrame frame = new JFrame("Giriş Ekranı");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel lblKullaniciAdi = new JLabel("Kullanıcı Adı:");
        JTextField txtKullaniciAdi = new JTextField();
        JLabel lblSifre = new JLabel("Şifre:");
        JPasswordField txtSifre = new JPasswordField();
        JButton btnGiris = new JButton("Giriş Yap");
        JButton btnKayitOl = new JButton("Kayıt Ol");

        panel.add(lblKullaniciAdi);
        panel.add(txtKullaniciAdi);
        panel.add(lblSifre);
        panel.add(txtSifre);
        panel.add(btnGiris);
        panel.add(btnKayitOl);

        frame.add(panel);

        btnGiris.addActionListener(e -> {
            String kullaniciAdi = txtKullaniciAdi.getText();
            String sifre = new String(txtSifre.getPassword());
            
            KullaniciRepository repository = new KullaniciRepository();

            if (repository.authenticateUser(kullaniciAdi, sifre)) {
                frame.dispose();
                int kullaniciId = repository.kullaniciId(kullaniciAdi, sifre);
                AnaMenu.main(new String[]{String.valueOf(kullaniciId)});
            } else {
                JOptionPane.showMessageDialog(frame, "Hatalı kullanıcı adı veya şifre!", "Hata",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnKayitOl.addActionListener(e -> openKayitEkrani());

        frame.setVisible(true);
    }

    public static void openKayitEkrani() {
        JFrame kayitFrame = new JFrame("Kayıt Ol");
        kayitFrame.setSize(350, 300);
        kayitFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        kayitFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField();
        JLabel lblPassword = new JLabel("Şifre:");
        JPasswordField txtPassword = new JPasswordField();
        JLabel lblName = new JLabel("Ad:");
        JTextField txtName = new JTextField();
        JLabel lblSurname = new JLabel("Soyad:");
        JTextField txtSurname = new JTextField();
        JButton btnKayit = new JButton("Kayıt Ol");

        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblSurname);
        panel.add(txtSurname);
        panel.add(btnKayit);

        kayitFrame.add(panel);

        btnKayit.addActionListener(e -> {
            String email = txtEmail.getText();
            String password = new String(txtPassword.getPassword());

            KullaniciRepository repository = new KullaniciRepository();

            if (repository.registerUser(email, password)) {
                JOptionPane.showMessageDialog(kayitFrame, "Kayıt başarılı!", "Başarılı",
                        JOptionPane.INFORMATION_MESSAGE);
                kayitFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(kayitFrame, "Kayıt başarısız!", "Hata",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        kayitFrame.setVisible(true);
    }
}

class AnaMenu {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ana Menü");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 1));

        JButton btnProjeEkle = new JButton("Yeni Proje Ekle");
        JButton btnProjeListele = new JButton("Projeleri Listele");
        JButton btnPersonelEkle = new JButton("Yeni Personel Ekle");
        JButton btnPersonelListele = new JButton("Personelleri Listele");
        JButton btnPersonelSil = new JButton("Personel Sil");
        JButton btnPersonelGuncelle = new JButton("Personel Güncelle");
        JButton btnCikis = new JButton("Çıkış");

        panel.add(btnProjeEkle);
        panel.add(btnProjeListele);
        panel.add(btnPersonelEkle);
        panel.add(btnPersonelListele);
        panel.add(btnPersonelSil);
        panel.add(btnPersonelGuncelle);
        panel.add(btnCikis);

        frame.add(panel);

        String kullaniciId = args[0];

        btnProjeEkle.addActionListener(e -> ProjeEkle.main(new String[]{String.valueOf(kullaniciId)}));

        btnProjeListele.addActionListener(e -> ProjeListele.main(new String[]{String.valueOf(kullaniciId)}));

        btnPersonelEkle.addActionListener(e -> PersonelEkle.main(new String[]{String.valueOf(kullaniciId)}));

        btnPersonelListele.addActionListener(e -> PersonelListele.main(new String[]{String.valueOf(kullaniciId)}));

        btnPersonelSil.addActionListener(e -> PersonelSil.main(new String[]{String.valueOf(kullaniciId)}));

        btnPersonelGuncelle.addActionListener(e -> PersonelGuncelle.main(new String[]{String.valueOf(kullaniciId)}));

        btnCikis.addActionListener(e -> {
            frame.dispose();
            GirisEkrani.main(null);
        });

        frame.setVisible(true);
    }
}

class PersonelEkle {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Yeni Personel Ekle");
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel lblAd = new JLabel("Ad:");
        JTextField txtAd = new JTextField();
        JLabel lblSoyad = new JLabel("Soyad:");
        JTextField txtSoyad = new JTextField();
        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField();

        JButton btnKaydet = new JButton("Kaydet");

        panel.add(lblAd);
        panel.add(txtAd);
        panel.add(lblSoyad);
        panel.add(txtSoyad);
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(btnKaydet);

        frame.add(panel);

        int kullaniciId = Integer.parseInt(args[0]);

        btnKaydet.addActionListener(e -> {
            String ad = txtAd.getText();
            String soyad = txtSoyad.getText();
            String email = txtEmail.getText();

            CalisanRepository repository = new CalisanRepository();
            repository.calisanEkle(ad, soyad, email, kullaniciId);
            JOptionPane.showMessageDialog(frame, "Personel başarıyla eklendi.", "Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        });

        frame.setVisible(true);
    }
}

class PersonelSil {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Personel Sil");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));

        JLabel lblPersonelId = new JLabel("Silinecek Personeli Seçin:");
        JComboBox<String> cmbPersonel = new JComboBox<>();
        JButton btnSil = new JButton("Sil");

        panel.add(lblPersonelId);
        panel.add(cmbPersonel);
        panel.add(btnSil);

        frame.add(panel);

        int kullaniciId = Integer.parseInt(args[0]);

        // Personel ID ve Ad Soyad bilgilerini yükle
        CalisanRepository repository = new CalisanRepository();
        Map<Integer, String> calisanlar = repository.calisanIdListele(kullaniciId);

        // ComboBox'a personel ekleme
        for (Map.Entry<Integer, String> entry : calisanlar.entrySet()) {
            cmbPersonel.addItem(entry.getKey() + " - " + entry.getValue());
        }

        btnSil.addActionListener(e -> {
            try {
                // Seçilen item'den ID'yi alma
                String selectedItem = (String) cmbPersonel.getSelectedItem();
                if (selectedItem != null) {
                    int personelId = Integer.parseInt(selectedItem.split(" - ")[0]); // ID kısmını al

                    boolean basarili = repository.calisanSil(personelId);

                    if (basarili) {
                        JOptionPane.showMessageDialog(frame, "Personel başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                        cmbPersonel.removeItem(selectedItem); // Silinen personeli combobox'tan çıkar
                    } else {
                        JOptionPane.showMessageDialog(frame, "Personel silinemedi. Lütfen ID'yi kontrol edin.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Bir hata oluştu: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
}

class PersonelGuncelle {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Personel Güncelle");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        JLabel lblPersonelId = new JLabel("Güncellenecek Personel:");
        JComboBox<String> cmbPersonel = new JComboBox<>();
        JLabel lblYeniAd = new JLabel("Yeni Ad:");
        JTextField txtYeniAd = new JTextField();
        JLabel lblYeniSoyad = new JLabel("Yeni Soyad:");
        JTextField txtYeniSoyad = new JTextField();
        JLabel lblYeniEmail = new JLabel("Yeni Email:");
        JTextField txtYeniEmail = new JTextField();
        JButton btnGuncelle = new JButton("Güncelle");

        panel.add(lblPersonelId);
        panel.add(cmbPersonel);
        panel.add(lblYeniAd);
        panel.add(txtYeniAd);
        panel.add(lblYeniSoyad);
        panel.add(txtYeniSoyad);
        panel.add(lblYeniEmail);
        panel.add(txtYeniEmail);
        panel.add(btnGuncelle);

        frame.add(panel);

        int kullaniciId = Integer.parseInt(args[0]);

        // Personel ID ve isim bilgilerini ComboBox'a yükleme
        CalisanRepository repository = new CalisanRepository();
        Map<Integer, String> calisanlar = repository.calisanIdListele(kullaniciId);

        for (Map.Entry<Integer, String> entry : calisanlar.entrySet()) {
            cmbPersonel.addItem(entry.getKey() + " - " + entry.getValue());
        }

        btnGuncelle.addActionListener(e -> {
            try {
                // Seçilen personelin ID'sini ComboBox'tan alma
                String selectedItem = (String) cmbPersonel.getSelectedItem();
                if (selectedItem != null) {
                    int personelId = Integer.parseInt(selectedItem.split(" - ")[0]); // ID kısmını al
                    String yeniAd = txtYeniAd.getText();
                    String yeniSoyad = txtYeniSoyad.getText();
                    String yeniEmail = txtYeniEmail.getText();

                    if (yeniAd.isEmpty() || yeniSoyad.isEmpty() || yeniEmail.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Lütfen tüm alanları doldurun!", "Hata", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean basarili = repository.calisanGuncelle(personelId, yeniAd, yeniSoyad, yeniEmail);

                    if (basarili) {
                        JOptionPane.showMessageDialog(frame, "Personel başarıyla güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Personel güncellenemedi. Lütfen ID'yi kontrol edin.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Bir hata oluştu: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
}

class PersonelListele {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Personelleri Listele");
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JTextArea txtArea = new JTextArea(400, 700);
        txtArea.setEditable(false);

        int kullaniciId = Integer.parseInt(args[0]);

        CalisanRepository repository = new CalisanRepository();
        List<String> personeller = repository.calisanListele(kullaniciId);

        // Listeleri panelde göster
        JList<String> personelListesi = new JList<>(personeller.toArray(new String[0]));
        personelListesi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        personelListesi.setVisibleRowCount(20);
        JScrollPane listScroller = new JScrollPane(personelListesi);
        listScroller.setPreferredSize(new Dimension(380, 650));

        panel.add(listScroller);

        // Personel seçildiğinde görev durumu ve tamamlanma durumu gösterilsin
        personelListesi.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedPersonel = personelListesi.getSelectedValue();
                if (selectedPersonel != null) {
                    int calisanId = Integer.parseInt(selectedPersonel.split(" - ")[0]); // ID'yi ayıklıyoruz
                    CalisanRepository rapor = new CalisanRepository();
                    StringBuilder sb = new StringBuilder();

                    // Çalışanın görev durumları
                    sb.append("Çalışanın Görev Durumları:\n");
                    rapor.calisanGorevDurumlariniListele(calisanId, sb);
                    sb.append("\nÇalışanın Görev Tamamlama Durumu:\n");
                    rapor.calisanGorevTamamlamaDurumu(calisanId, sb);

                    // Ekrana yazdır
                    txtArea.setText(sb.toString());
                }
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(new JScrollPane(txtArea), BorderLayout.CENTER);

        frame.add(panel, BorderLayout.WEST);
        frame.add(bottomPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}

class ProjeEkle {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Yeni Proje Ekle");
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel lblAd = new JLabel("Proje Adı:");
        JTextField txtAd = new JTextField();
        JLabel lblBaslangic = new JLabel("Başlangıç Tarihi (YYYY-MM-DD):");
        JTextField txtBaslangic = new JTextField();
        JLabel lblBitis = new JLabel("Bitiş Tarihi (YYYY-MM-DD):");
        JTextField txtBitis = new JTextField();

        JButton btnKaydet = new JButton("Kaydet");

        panel.add(lblAd);
        panel.add(txtAd);
        panel.add(lblBaslangic);
        panel.add(txtBaslangic);
        panel.add(lblBitis);
        panel.add(txtBitis);
        panel.add(btnKaydet);

        frame.add(panel);

        String kullaniciId = args[0];

        btnKaydet.addActionListener(e -> {
            String ad = txtAd.getText();
            String baslangic = txtBaslangic.getText();
            String bitis = txtBitis.getText();

            ProjeRepository repository = new ProjeRepository();
            repository.projeEkle(ad, baslangic, bitis, kullaniciId);
            JOptionPane.showMessageDialog(frame, "Proje başarıyla eklendi.", "Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        });

        frame.setVisible(true);
    }
}

class ProjeListele {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Projeleri Listele");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JList<String> projeListesi = new JList<>();
        JScrollPane scrollPane = new JScrollPane(projeListesi);

        int kullaniciId = Integer.parseInt(args[0]);

        ProjeRepository repository = new ProjeRepository();
        List<String> projeler = repository.projeListele(kullaniciId);
        DefaultListModel<String> model = new DefaultListModel<>();

        for (String proje : projeler) {
            model.addElement(proje);
        }
        projeListesi.setModel(model);

        JButton btnSec = new JButton("Projeyi Seç");

        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnSec, BorderLayout.SOUTH);

        frame.add(panel);

        btnSec.addActionListener(e -> {
            String secilenProje = projeListesi.getSelectedValue();
            if (secilenProje != null) {
                frame.dispose();
                int projeId = Integer.parseInt(secilenProje.split(" - ")[0]); // Proje ID'sini ayıkla
                GorevEkle.ProjeGorevYonetimi.main(new String[]{String.valueOf(projeId), String.valueOf(kullaniciId)});
            } else {
                JOptionPane.showMessageDialog(frame, "Lütfen bir proje seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
}

class GorevListele {

    public static void main(String[] args) {
        if (args.length == 0) {
            JOptionPane.showMessageDialog(null, "Proje ID eksik!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int projeId;
        try {
            projeId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Geçersiz Proje ID!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFrame frame = new JFrame("Görevleri Listele - Proje ID: " + projeId);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JTextArea txtArea = new JTextArea(10, 30);
        txtArea.setEditable(false);

        GorevRepository repository = new GorevRepository();
        List<String> gorevler = repository.gorevListeleByProje(projeId);

        StringBuilder sb = new StringBuilder();
        if (gorevler.isEmpty()) {
            sb.append("Bu projeye ait görev bulunmamaktadır.");
        } else {
            for (String gorev : gorevler) {
                sb.append(gorev).append("\n");
            }
        }
        txtArea.setText(sb.toString());

        panel.add(new JScrollPane(txtArea));
        frame.add(panel);

        frame.setVisible(true);
    }
}

class GorevEkle {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Yeni Görev Ekle");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2));

        JLabel lblPersonelId = new JLabel("Personel ID:");
        JComboBox<String> comboPersonelId = new JComboBox<>();
        JLabel lblGorevAdi = new JLabel("Görev Adı:");
        JTextField txtGorevAdi = new JTextField();
        JLabel lblDurum = new JLabel("Durum:");
        JComboBox<String> comboDurum = new JComboBox<>(new String[]{"Tamamlanacak", "Devam Ediyor", "Tamamlandı"});
        JLabel lblBaslangicTarihi = new JLabel("Başlangıç Tarihi (YYYY-MM-DD):");
        JTextField txtBaslangicTarihi = new JTextField();
        JLabel lblBitisTarihi = new JLabel("Bitiş Tarihi (YYYY-MM-DD):");
        JTextField txtBitisTarihi = new JTextField();
        JLabel lblAdamGun = new JLabel("Adam Gün:");
        JTextField txtAdamGun = new JTextField();

        JButton btnKaydet = new JButton("Kaydet");

        panel.add(lblPersonelId);
        panel.add(comboPersonelId);
        panel.add(lblGorevAdi);
        panel.add(txtGorevAdi);
        panel.add(lblDurum);
        panel.add(comboDurum);
        panel.add(lblBaslangicTarihi);
        panel.add(txtBaslangicTarihi);
        panel.add(lblBitisTarihi);
        panel.add(txtBitisTarihi);
        panel.add(lblAdamGun);
        panel.add(txtAdamGun);
        panel.add(btnKaydet);

        frame.add(panel);

        int kullaniciId = Integer.parseInt(args[1]);

        CalisanRepository calisanRepository = new CalisanRepository();
        Map<Integer, String> calisanMap = calisanRepository.calisanIdListele(kullaniciId);

        for (String calisanAd : calisanMap.values()) {
            comboPersonelId.addItem(calisanAd);
        }

        btnKaydet.addActionListener(e -> {
            try {
                String selectedProjeId = args[0];
                String selectedCalisanAd = (String) comboPersonelId.getSelectedItem();
                String gorevAdi = txtGorevAdi.getText();
                String durum = (String) comboDurum.getSelectedItem();
                String adamGunStr = txtAdamGun.getText();

                int calisanId = -1;

                for (Map.Entry<Integer, String> entry : calisanMap.entrySet()) {
                    if (entry.getValue().equals(selectedCalisanAd)) {
                        calisanId = entry.getKey();
                        break;
                    }
                }

                int projeId = Integer.parseInt(selectedProjeId);

                // Validate that the fields are not empty
                if (selectedProjeId == "" || selectedProjeId.isEmpty() || calisanId == -1 || adamGunStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Lütfen tüm alanları doldurun!", "Hata",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int adamGun = Integer.parseInt(adamGunStr); // Can throw NumberFormatException if input is not a valid
                // number

                // Parse the dates
                Date baslangicTarihiUtil = new SimpleDateFormat("yyyy-MM-dd").parse(txtBaslangicTarihi.getText());
                Date bitisTarihiUtil = new SimpleDateFormat("yyyy-MM-dd").parse(txtBitisTarihi.getText());

                // Convert to java.sql.Date
                java.sql.Date baslangicTarihi = new java.sql.Date(baslangicTarihiUtil.getTime());
                java.sql.Date bitisTarihi = new java.sql.Date(bitisTarihiUtil.getTime());

                // Create the task and add it to the repository
                GorevRepository repository = new GorevRepository();
                repository.gorevEkle(projeId, calisanId, gorevAdi, durum, baslangicTarihi, bitisTarihi, adamGun);

                JOptionPane.showMessageDialog(frame, "Görev başarıyla eklendi.", "Başarılı",
                        JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Lütfen geçerli bir sayı girin!", "Hata",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, "Tarih formatı hatalı! YYYY-MM-DD formatını kullanın.", "Hata",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Beklenmedik bir hata oluştu: " + ex.getMessage(), "Hata",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    public class ProjeGorevYonetimi {

        public static void main(String[] args) {
            int projeId = Integer.parseInt(args[0]);

            int kullaniciId = Integer.parseInt(args[1]);

            JFrame frame = new JFrame("Görev Yönetimi - Proje ID: " + projeId);
            frame.setSize(500, 400);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(4, 1));

            JButton btnGorevEkle = new JButton("Yeni Görev Ekle");
            JButton btnGorevListele = new JButton("Görevleri Listele");
            JButton btnGorevDurumGuncelle = new JButton("Görev Durum Güncelle");
            JButton btnGeriDon = new JButton("Geri Dön");

            panel.add(btnGorevEkle);
            panel.add(btnGorevListele);
            panel.add(btnGorevDurumGuncelle);
            panel.add(btnGeriDon);

            frame.add(panel);

            btnGorevEkle.addActionListener(e -> {
                GorevEkle.main(new String[]{String.valueOf(projeId), String.valueOf(kullaniciId)});
            });

            btnGorevListele.addActionListener(e -> {
                GorevListele.main(new String[]{String.valueOf(projeId)});
            });

            btnGorevDurumGuncelle.addActionListener(e -> {
                GorevDurumuGuncelle.main(new String[]{String.valueOf(projeId)});
            });

            btnGeriDon.addActionListener(e -> {
                frame.dispose();
                ProjeListele.main(null);
            });

            frame.setVisible(true);
        }
    }
}

class GorevDurumuGuncelle {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Görev Durumu Güncelle");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));

        // Görevler ComboBox
        JLabel lblGorevSec = new JLabel("Görev Seç:");
        JComboBox<String> cbGorevler = new JComboBox<>();

        // Güncelle Butonu
        JButton btnGuncelle = new JButton("Durumu Güncelle");

        panel.add(lblGorevSec);
        panel.add(cbGorevler);
        panel.add(btnGuncelle);

        frame.add(panel);

        // Proje ID'yi argümanlardan al ve görevleri yükle
        try {
            int projeId = Integer.parseInt(args[0]); // Proje ID komut satırından alınır
            GorevRepository repository = new GorevRepository();
            List<String> gorevler = repository.gorevListeleByProje(projeId);

            // ComboBox'a görevleri ekle
            for (String gorev : gorevler) {
                cbGorevler.addItem(gorev);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Görevler yüklenirken bir hata oluştu: " + ex.getMessage(),
                                          "Hata", JOptionPane.ERROR_MESSAGE);
        }

        // Görev durumu güncelleme işlemi
        btnGuncelle.addActionListener(e -> {
            try {
                String gorevAdi = (String) cbGorevler.getSelectedItem();
                if (gorevAdi == null || gorevAdi.isEmpty()) {
                    throw new Exception("Görev seçilmedi.");
                }
                int startIndex = gorevAdi.indexOf(':') + 2; // '[' sonrası
                int endIndex = gorevAdi.indexOf(',');      // ']' öncesi

                String sonuc = gorevAdi.substring(startIndex, endIndex);
                int gorevId = Integer.parseInt(sonuc); // Görev ID'yi görev adından ayır
                GorevRepository repository = new GorevRepository();
                repository.gorevDurumuGuncelle(gorevId);

                JOptionPane.showMessageDialog(frame, "Görev durumu başarıyla güncellendi.",
                                              "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Bir hata oluştu: " + ex.getMessage(),
                                              "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
}