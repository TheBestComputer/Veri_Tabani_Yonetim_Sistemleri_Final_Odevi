import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProjeYonetimUI extends JFrame {

    public ProjeYonetimUI() {
        setTitle("Proje Yönetim Sistemi");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel ve bileşenler
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel lblKullanici = new JLabel("Kullanıcı Adı:");
        JTextField txtKullanici = new JTextField();

        JLabel lblSifre = new JLabel("Şifre:");
        JPasswordField txtSifre = new JPasswordField();

        JButton btnGiris = new JButton("Giriş Yap");

        panel.add(lblKullanici);
        panel.add(txtKullanici);
        panel.add(lblSifre);
        panel.add(txtSifre);
        panel.add(new JLabel());
        panel.add(btnGiris);

        add(panel);

        // Giriş butonuna tıklama olayı
        btnGiris.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kullaniciAdi = txtKullanici.getText();
                String sifre = new String(txtSifre.getPassword());

                if (kullaniciAdi.equals("admin") && sifre.equals("1234")) {
                    JOptionPane.showMessageDialog(null, "Giriş Başarılı!");
                    // Ana menüye yönlendir
                    openAnaMenu();
                } else {
                    JOptionPane.showMessageDialog(null, "Kullanıcı adı veya şifre yanlış!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Ana menüyü açan metot
    private void openAnaMenu() {
        // Ana menü penceresini oluştur
        JFrame anaMenuFrame = new JFrame("Ana Menü");
        anaMenuFrame.setSize(500, 400);
        anaMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        anaMenuFrame.setLocationRelativeTo(null);

        // Ana menü paneli
        JPanel anaMenuPanel = new JPanel();
        anaMenuPanel.setLayout(new BoxLayout(anaMenuPanel, BoxLayout.Y_AXIS));

        JButton btnProjeEkle = new JButton("Yeni Proje Ekle");
        JButton btnProjeListele = new JButton("Projeleri Listele");
        JButton btnCikis = new JButton("Çıkış");

        // Ana menüye butonları ekleyin
        anaMenuPanel.add(btnProjeEkle);
        anaMenuPanel.add(btnProjeListele);
        anaMenuPanel.add(Box.createVerticalStrut(20));  // Boşluk
        anaMenuPanel.add(btnCikis);

        // Butonlara aksiyon ekleyin
        btnProjeEkle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Yeni proje ekleme işlemleri
                yeniProjeEkle();
            }
        });

        btnProjeListele.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Proje listeleme işlemleri
                projeListele();
            }
        });

        btnCikis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Çıkış işlemi
                System.exit(0);
            }
        });

        // Ana menüyü göster
        anaMenuFrame.add(anaMenuPanel);
        anaMenuFrame.setVisible(true);

        // Mevcut giriş ekranını kapat
        dispose();
    }

    // Yeni Proje Ekleme İşlemi
    private void yeniProjeEkle() {
        // Yeni proje eklemek için bir form açalım
        JFrame projeEkleFrame = new JFrame("Yeni Proje Ekle");
        projeEkleFrame.setSize(400, 300);
        projeEkleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        projeEkleFrame.setLocationRelativeTo(null);

        // Panel ve bileşenler
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel lblAd = new JLabel("Proje Adı:");
        JTextField txtAd = new JTextField();

        JLabel lblBaslangic = new JLabel("Başlangıç Tarihi:");
        JTextField txtBaslangic = new JTextField();

        JLabel lblBitis = new JLabel("Bitiş Tarihi:");
        JTextField txtBitis = new JTextField();

        JButton btnEkle = new JButton("Proje Ekle");

        panel.add(lblAd);
        panel.add(txtAd);
        panel.add(lblBaslangic);
        panel.add(txtBaslangic);
        panel.add(lblBitis);
        panel.add(txtBitis);
        panel.add(new JLabel());
        panel.add(btnEkle);

        // Ekleme butonuna tıklama olayı
        btnEkle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ad = txtAd.getText();
                String baslangicTarihi = txtBaslangic.getText();
                String bitisTarihi = txtBitis.getText();

                // Veritabanına proje ekleme
                ProjeRepository repository = new ProjeRepository();
                repository.projeEkle(ad, baslangicTarihi, bitisTarihi);
                JOptionPane.showMessageDialog(projeEkleFrame, "Proje başarıyla eklendi.");
                projeEkleFrame.dispose();
            }
        });

        // Pencereyi göster
        projeEkleFrame.add(panel);
        projeEkleFrame.setVisible(true);
    }

    // Proje Listeleme İşlemi
    private void projeListele() {
        // Projeleri listelemek için basit bir pencere açalım
        JFrame projeListeleFrame = new JFrame("Projeleri Listele");
        projeListeleFrame.setSize(400, 300);
        projeListeleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        projeListeleFrame.setLocationRelativeTo(null);

        // Panel ve bileşenler
        JPanel panel = new JPanel();
        JTextArea txtArea = new JTextArea(10, 30);
        txtArea.setEditable(false);

        // Veritabanından projeleri listele
        ProjeRepository repository = new ProjeRepository();
        StringBuilder sb = new StringBuilder();
        // Burada projeleri veritabanından çekip listelemeniz gerekiyor.
        sb.append("Proje 1: 2024-01-01 - 2024-12-31\n");
        sb.append("Proje 2: 2024-03-01 - 2024-11-30\n");
        txtArea.setText(sb.toString());

        // Paneli ekle
        panel.add(new JScrollPane(txtArea));

        // Pencereyi göster
        projeListeleFrame.add(panel);
        projeListeleFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProjeYonetimUI().setVisible(true);
        });
    }
}
