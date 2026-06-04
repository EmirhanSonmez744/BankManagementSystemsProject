import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BankGUI {
    private JFrame frame;
    private JTextField idField;
    private JTextArea displayArea;
    
    // Arka plan sistemlerimiz
    private CustomerBST bst;
    private CustomerQueue queue;

    public BankGUI() {
        bst = new CustomerBST();
        queue = new CustomerQueue();

        // Varsayılan Müşteriler
        bst.insert(new Customer("2203016", "Berat", "Karataş"));
        bst.insert(new Customer("2364656", "Emirhan", "Sönmez"));
        bst.insert(new Customer("1905885", "Melisa", "Okay"));

        initialize();
    }

    private void initialize() {
        frame = new JFrame("Banka Kuyruk Yönetim Sistemi");
        frame.setBounds(100, 100, 600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(10, 10));

        // --- ÜST PANEL: ID Girişi ve Butonlar ---
        JPanel topPanel = new JPanel();
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        JLabel lblId = new JLabel("Müşteri ID:");
        topPanel.add(lblId);

        idField = new JTextField();
        idField.setColumns(12);
        topPanel.add(idField);

        JButton btnSiraAl = new JButton("Sıra Al");
        topPanel.add(btnSiraAl);

        // YENİ: Kayıt Ol Butonu eklendi
        JButton btnKayitOl = new JButton("Yeni Kayıt Ol");
        topPanel.add(btnKayitOl);

        // --- ORTA PANEL: Bilgi ve Durum Ekranı ---
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(displayArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // --- ALT PANEL: Vezne İşlemleri ---
        JPanel bottomPanel = new JPanel();
        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        JButton btnCagir = new JButton("Sıradakini Çağır");
        bottomPanel.add(btnCagir);

        // ==========================================
        // BUTON İŞLEMLERİ
        // ==========================================

        // 1. "Sıra Al" Butonu
        btnSiraAl.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText().trim();
                
                if (id.isEmpty()) {
                    displayArea.append("Hata: Lütfen işlem için bir ID girin!\n");
                    return;
                }

                Customer c = bst.search(id);
                
                if (c != null) {
                    queue.enqueue(c);
                    displayArea.append("Sıraya Eklendi -> " + c.getName() + " " + c.getSurname() + "\n");
                    idField.setText(""); // İşlem başarılıysa kutuyu temizle
                } else {
                    displayArea.append("Güvenlik Uyarısı: " + id + " numaralı müşteri kayıtlı değil. Lütfen kayıt olun!\n");
                }
            }
        });

        // 2. YENİ: "Yeni Kayıt Ol" Butonu
        btnKayitOl.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText().trim();
                
                if (id.isEmpty()) {
                    displayArea.append("Hata: Kayıt olmak için önce bir ID numarası yazın!\n");
                    return;
                }

                // ID sistemde var mı diye kontrol et
                Customer mevcutMusteri = bst.search(id);
                if (mevcutMusteri != null) {
                    displayArea.append("Sistem: Bu ID (" + id + ") zaten " + mevcutMusteri.getName() + " adına kayıtlı!\n");
                    return;
                }

                // Kullanıcıdan Ad ve Soyad istemek için küçük bir form paneli oluşturuyoruz
                JTextField nameField = new JTextField(10);
                JTextField surnameField = new JTextField(10);
                
                JPanel myPanel = new JPanel();
                myPanel.add(new JLabel("Ad:"));
                myPanel.add(nameField);
                myPanel.add(Box.createHorizontalStrut(15)); // Boşluk ekler
                myPanel.add(new JLabel("Soyad:"));
                myPanel.add(surnameField);

                // Ekrana pop-up (açılır pencere) çıkartıyoruz
                int result = JOptionPane.showConfirmDialog(null, myPanel, 
                         "Yeni Müşteri Kaydı", JOptionPane.OK_CANCEL_OPTION);
                
                // Eğer kullanıcı pencerede "OK" tuşuna basarsa
                if (result == JOptionPane.OK_OPTION) {
                    String name = nameField.getText().trim();
                    String surname = surnameField.getText().trim();
                    
                    // İsim ve soyisim boş bırakılmadıysa ağaca ekle
                    if (!name.isEmpty() && !surname.isEmpty()) {
                        Customer yeniMusteri = new Customer(id, name, surname);
                        bst.insert(yeniMusteri); // Ağaca kayıt yapıldı!
                        displayArea.append("BAŞARILI: Yeni müşteri eklendi -> " + name + " " + surname + " (" + id + ")\n");
                    } else {
                        displayArea.append("Hata: Ad veya soyad alanı boş bırakılamaz. Kayıt iptal edildi!\n");
                    }
                }
            }
        });

        // 3. "Sıradakini Çağır" Butonu
        btnCagir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!queue.isEmpty()) {
                    Customer c = queue.dequeue();
                    displayArea.append("--------------------------------------------------\n");
                    displayArea.append(">>> VEZNEYE ÇAĞRILAN MÜŞTERİ: " + c.getName() + " " + c.getSurname() + "\n");
                    displayArea.append("--------------------------------------------------\n");
                } else {
                    displayArea.append("Sistem: Kuyrukta bekleyen müşteri bulunmuyor.\n");
                }
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    BankGUI window = new BankGUI();
                    window.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
