
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

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel lblKullaniciAdi = new JLabel("Kullanıcı Adı:");
        JTextField txtKullaniciAdi = new JTextField();
        JLabel lblSifre = new JLabel("Şifre:");
        JPasswordField txtSifre = new JPasswordField();
        JButton btnGiris = new JButton("Giriş Yap");

        panel.add(lblKullaniciAdi);
        panel.add(txtKullaniciAdi);
        panel.add(lblSifre);
        panel.add(txtSifre);
        panel.add(btnGiris);

        frame.add(panel);

        btnGiris.addActionListener(e -> {
            String kullaniciAdi = txtKullaniciAdi.getText();
            String sifre = new String(txtSifre.getPassword());

            if (kullaniciAdi.equals("admin") && sifre.equals("1234")) {
                frame.dispose();
                AnaMenu.main(null);
            } else {
                JOptionPane.showMessageDialog(frame, "Hatalı kullanıcı adı veya şifre!", "Hata",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
}

class AnaMenu {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ana Menü");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 1));

        JButton btnProjeEkle = new JButton("Yeni Proje Ekle");
        JButton btnProjeListele = new JButton("Projeleri Listele");
        JButton btnPersonelEkle = new JButton("Yeni Personel Ekle");
        JButton btnPersonelListele = new JButton("Personelleri Listele");
        JButton btnGorevYonetimi = new JButton("Görev Yönetimi");
        JButton btnCikis = new JButton("Çıkış");

        panel.add(btnProjeEkle);
        panel.add(btnProjeListele);
        panel.add(btnPersonelEkle);
        panel.add(btnPersonelListele);
        panel.add(btnGorevYonetimi);
        panel.add(btnCikis);

        frame.add(panel);

        btnProjeEkle.addActionListener(e -> ProjeEkle.main(null));

        btnProjeListele.addActionListener(e -> ProjeListele.main(null));

        btnPersonelEkle.addActionListener(e -> PersonelEkle.main(null));

        btnPersonelListele.addActionListener(e -> PersonelListele.main(null));

        btnGorevYonetimi.addActionListener(e -> GorevYonetimi.main(null));

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

        btnKaydet.addActionListener(e -> {
            String ad = txtAd.getText();
            String soyad = txtSoyad.getText();
            String email = txtEmail.getText();

            CalisanRepository repository = new CalisanRepository();
            repository.calisanEkle(ad, soyad, email);
            JOptionPane.showMessageDialog(frame, "Personel başarıyla eklendi.", "Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        });

        frame.setVisible(true);
    }
}

class PersonelListele {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Personelleri Listele");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JTextArea txtArea = new JTextArea(15, 40);
        txtArea.setEditable(false);

        CalisanRepository repository = new CalisanRepository();
        List<String> personeller = repository.calisanListele();

        // Listeleri panelde göster
        JList<String> personelListesi = new JList<>(personeller.toArray(new String[0]));
        personelListesi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        personelListesi.setVisibleRowCount(10);
        JScrollPane listScroller = new JScrollPane(personelListesi);
        listScroller.setPreferredSize(new Dimension(250, 80));

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

        btnKaydet.addActionListener(e -> {
            String ad = txtAd.getText();
            String baslangic = txtBaslangic.getText();
            String bitis = txtBitis.getText();

            ProjeRepository repository = new ProjeRepository();
            repository.projeEkle(ad, baslangic, bitis);
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

        ProjeRepository repository = new ProjeRepository();
        List<String> projeler = repository.projeListele();
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
                GorevEkle.ProjeGorevYonetimi.main(new String[]{String.valueOf(projeId)});
            } else {
                JOptionPane.showMessageDialog(frame, "Lütfen bir proje seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
}

class GorevYonetimi {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Görev Yönetimi");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));

        JButton btnGorevEkle = new JButton("Yeni Görev Ekle");
        JButton btnGorevListele = new JButton("Görevleri Listele");
        JButton btnGorevDurumGuncelle = new JButton("Görev Durumunu Güncelle");

        panel.add(btnGorevEkle);
        panel.add(btnGorevListele);
        panel.add(btnGorevDurumGuncelle);

        frame.add(panel);

        btnGorevEkle.addActionListener(e -> GorevEkle.main(null));

        btnGorevListele.addActionListener(e -> GorevListele.main(null));

        btnGorevDurumGuncelle.addActionListener(e -> {
            String gorevIdStr = JOptionPane.showInputDialog(frame, "Görev ID'sini girin:");
            try {
                int gorevId = Integer.parseInt(gorevIdStr);
                GorevRepository repository = new GorevRepository();
                repository.gorevDurumuGuncelle(gorevId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Geçersiz Görev ID'si.");
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
        
        CalisanRepository calisanRepository = new CalisanRepository();
        Map<Integer, String> calisanMap = calisanRepository.calisanIdListele();

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

            JFrame frame = new JFrame("Görev Yönetimi - Proje ID: " + projeId);
            frame.setSize(500, 400);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(3, 1));

            JButton btnGorevEkle = new JButton("Yeni Görev Ekle");
            JButton btnGorevListele = new JButton("Görevleri Listele");
            JButton btnGeriDon = new JButton("Geri Dön");

            panel.add(btnGorevEkle);
            panel.add(btnGorevListele);
            panel.add(btnGeriDon);

            frame.add(panel);

            btnGorevEkle.addActionListener(e -> {
                GorevEkle.main(new String[]{String.valueOf(projeId)});
            });

            btnGorevListele.addActionListener(e -> {
                GorevListele.main(new String[]{String.valueOf(projeId)});
            });

            btnGeriDon.addActionListener(e -> {
                frame.dispose();
                ProjeListele.main(null);
            });

            frame.setVisible(true);
        }
    }
}
