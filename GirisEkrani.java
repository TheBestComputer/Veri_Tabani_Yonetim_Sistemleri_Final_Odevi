import javax.swing.*;
import java.awt.*;
import java.util.List;

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
                JOptionPane.showMessageDialog(frame, "Hatalı kullanıcı adı veya şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
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
        JButton btnGorevEkle = new JButton("Yeni Görev Ekle");
        JButton btnCikis = new JButton("Çıkış");

        panel.add(btnProjeEkle);
        panel.add(btnProjeListele);
        panel.add(btnPersonelEkle);
        panel.add(btnPersonelListele);
        panel.add(btnGorevEkle);
        panel.add(btnCikis);

        frame.add(panel);

        btnProjeEkle.addActionListener(e -> ProjeEkle.main(null));
        btnProjeListele.addActionListener(e -> ProjeListele.main(null));
        btnPersonelEkle.addActionListener(e -> PersonelEkle.main(null));
        btnPersonelListele.addActionListener(e -> PersonelListele.main(null));
        btnGorevEkle.addActionListener(e -> GorevEkle.main(null));

        btnCikis.addActionListener(e -> {
            frame.dispose();
            GirisEkrani.main(null);
        });

        frame.setVisible(true);
    }
}

class GorevEkle {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Görev İşlemleri");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1));

        JButton btnGorevEkle = new JButton("Görev Ekle");
        JButton btnGorevAtama = new JButton("Görev Atama");

        panel.add(btnGorevEkle);
        panel.add(btnGorevAtama);

        frame.add(panel);

        btnGorevEkle.addActionListener(e -> acGorevEklePenceresi());
        btnGorevAtama.addActionListener(e -> acGorevAtamaPenceresi());

        frame.setVisible(true);
    }

    private static void acGorevEklePenceresi() {
        JFrame frame = new JFrame("Yeni Görev Ekle");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        JLabel lblAd = new JLabel("Görev Adı:");
        JTextField txtAd = new JTextField();
        JLabel lblBaslangic = new JLabel("Başlangıç Tarihi (YYYY-MM-DD):");
        JTextField txtBaslangic = new JTextField();
        JLabel lblBitis = new JLabel("Bitiş Tarihi (YYYY-MM-DD):");
        JTextField txtBitis = new JTextField();
        JLabel lblAdamGun = new JLabel("Adam Gün:");
        JTextField txtAdamGun = new JTextField();

        JButton btnKaydet = new JButton("Kaydet");

        panel.add(lblAd);
        panel.add(txtAd);
        panel.add(lblBaslangic);
        panel.add(txtBaslangic);
        panel.add(lblBitis);
        panel.add(txtBitis);
        panel.add(lblAdamGun);
        panel.add(txtAdamGun);
        panel.add(btnKaydet);

        frame.add(panel);

        btnKaydet.addActionListener(e -> {
            int projeId = 0;
            int calisanId = 0;
            String ad = txtAd.getText();
            String durum = null;
            String baslangic = txtBaslangic.getText();
            String bitis = txtBitis.getText();
            int adamGun = Integer.parseInt(txtAdamGun.getText());

            GorevRepository repository = new GorevRepository();
            repository.gorevEkle(projeId, calisanId, ad, durum, baslangic, bitis, adamGun);

            JOptionPane.showMessageDialog(frame, "Görev başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        });

        frame.setVisible(true);
    }

    private static void acGorevAtamaPenceresi() {
        JFrame frame = new JFrame("Görev Atama");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel lblPersonel = new JLabel("Personel Adı:");
        JComboBox<String> cmbPersonel = new JComboBox<>();
        JLabel lblProje = new JLabel("Proje Adı:");
        JComboBox<String> cmbProje = new JComboBox<>();

        JButton btnAtama = new JButton("Ata");

        // Veri tabanından personel ve proje adlarını yükle
        CalisanRepository calisanRepo = new CalisanRepository();
        List<String> personelListesi = calisanRepo.calisanListele();
        for (String personel : personelListesi) {
            cmbPersonel.addItem(personel);
        }

        ProjeRepository projeRepo = new ProjeRepository();
        List<String> projeListesi = projeRepo.projeListele();
        for (String proje : projeListesi) {
            cmbProje.addItem(proje);
        }

        panel.add(lblPersonel);
        panel.add(cmbPersonel);
        panel.add(lblProje);
        panel.add(cmbProje);
        panel.add(btnAtama);

        frame.add(panel);

        btnAtama.addActionListener(e -> {
            String secilenPersonel = (String) cmbPersonel.getSelectedItem();
            String secilenProje = (String) cmbProje.getSelectedItem();

            if (secilenPersonel != null && secilenProje != null) {
                GorevRepository repository = new GorevRepository();
                repository.gorevAta(secilenPersonel, secilenProje);

                JOptionPane.showMessageDialog(frame, "Görev başarıyla atandı.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Lütfen seçim yapınız!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
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
            JOptionPane.showMessageDialog(frame, "Personel başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        });

        frame.setVisible(true);
    }
}

class PersonelListele {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Personelleri Listele");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JTextArea txtArea = new JTextArea(10, 30);
        txtArea.setEditable(false);

        CalisanRepository repository = new CalisanRepository();
        List<String> personeller = repository.calisanListele();

        StringBuilder sb = new StringBuilder();
        for (String personel : personeller) {
            sb.append(personel).append("\n");
        }
        txtArea.setText(sb.toString());

        panel.add(new JScrollPane(txtArea));

        frame.add(panel);
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
            JOptionPane.showMessageDialog(frame, "Proje başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
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
        JTextArea txtArea = new JTextArea(10, 30);
        txtArea.setEditable(false);

        ProjeRepository repository = new ProjeRepository();
        List<String> projeler = repository.projeListele();

        StringBuilder sb = new StringBuilder();
        for (String proje : projeler) {
            sb.append(proje).append("\n");
        }
        txtArea.setText(sb.toString());

        panel.add(new JScrollPane(txtArea));

        frame.add(panel);
        frame.setVisible(true);
    }
}