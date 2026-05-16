import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*; // Dosya okuma/yazma işlemleri için gerekli kütüphane eklendi

public class BankGUI {
    private JFrame frame;
    private JTextField idField;
    private JTextArea displayArea;
    
    // Arka plan sistemlerimiz
    private CustomerBST bst;
    private CustomerQueue queue;
    
    // Kayıtların tutulacağı dosyanın adı
    private final String DOSYA_ADI = "Musteriler.txt";

    public BankGUI() {
        bst = new CustomerBST();
        queue = new CustomerQueue();

        // 1. Program başladığında verileri dosyadan çek
        verileriYukle();

        // 2. Arayüzü oluştur
        initialize();
    }

    // --- DOSYADAN VERİ OKUMA METODU ---
    private void verileriYukle() {
        File dosya = new File(DOSYA_ADI);
        
        // Eğer dosya daha önce oluşturulmamışsa (ilk çalıştırma)
        if (!dosya.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(dosya))) {
                // Proje tanımındaki varsayılan müşteriler
                String[] varsayilanMusteriler = {
                    "2203016,Berat,Karataş",
                    "2364656,Emirhan,Sönmez",
                    "1905885,Melisa,Okay"
                };
                
                for (String satir : varsayilanMusteriler) {
                    bw.write(satir);
                    bw.newLine();
                    
                    // Virgüllerden bölüp ağaca da (BST) ekliyoruz
                    String[] parcalar = satir.split(",");
                    bst.insert(new Customer(parcalar[0], parcalar[1], parcalar[2]));
                }
            } catch (IOException e) {
                System.out.println("Dosya oluşturma hatası: " + e.getMessage());
            }
        } 
        // Eğer dosya zaten varsa (önceki kayıtlardan)
        else {
            try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
                String satir;
                while ((satir = br.readLine()) != null) {
                    String[] parcalar = satir.split(",");
                    if (parcalar.length == 3) {
                        bst.insert(new Customer(parcalar[0], parcalar[1], parcalar[2]));
                    }
                }
            } catch (IOException e) {
                System.out.println("Dosya okuma hatası: " + e.getMessage());
            }
        }
    }

    // --- DOSYAYA YENİ VERİ YAZMA METODU ---
    private void dosyayaKaydet(Customer c) {
        // FileWriter'daki "true" parametresi, dosyanın içindekileri silmeden EN SONUNA ekleme yapmasını sağlar
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DOSYA_ADI, true))) {
            bw.write(c.getId() + "," + c.getName() + "," + c.getSurname());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Dosyaya yazma hatası: " + e.getMessage());
        }
    }

    private void initialize() {
        frame = new JFrame("Banka Kuyruk Yönetim Sistemi");
        frame.setBounds(100, 100, 600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(10, 10));

        // --- ÜST PANEL ---
        JPanel topPanel = new JPanel();
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        topPanel.add(new JLabel("Müşteri ID:"));
        idField = new JTextField();
        idField.setColumns(12);
        topPanel.add(idField);

        JButton btnSiraAl = new JButton("Sıra Al");
        topPanel.add(btnSiraAl);

        JButton btnKayitOl = new JButton("Yeni Kayıt Ol");
        topPanel.add(btnKayitOl);

        // --- ORTA PANEL ---
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        displayArea.append("Sistem başarıyla başlatıldı. Kayıtlı veriler dosyadan yüklendi.\n\n");
        
        frame.getContentPane().add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // --- ALT PANEL ---
        JPanel bottomPanel = new JPanel();
        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        JButton btnCagir = new JButton("Sıradakini Çağır");
        bottomPanel.add(btnCagir);

        // ==========================================
        // BUTON İŞLEMLERİ
        // ==========================================

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
                    idField.setText("");
                } else {
                    displayArea.append("Uyarı: " + id + " numaralı müşteri kayıtlı değil.\n");
                }
            }
        });

        btnKayitOl.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText().trim();
                if (id.isEmpty()) {
                    displayArea.append("Hata: Kayıt olmak için önce bir ID yazın!\n");
                    return;
                }

                if (bst.search(id) != null) {
                    displayArea.append("Sistem: Bu ID zaten kayıtlı!\n");
                    return;
                }

                JTextField nameField = new JTextField(10);
                JTextField surnameField = new JTextField(10);
                
                JPanel myPanel = new JPanel();
                myPanel.add(new JLabel("Ad:"));
                myPanel.add(nameField);
                myPanel.add(Box.createHorizontalStrut(15));
                myPanel.add(new JLabel("Soyad:"));
                myPanel.add(surnameField);

                int result = JOptionPane.showConfirmDialog(null, myPanel, 
                         "Yeni Müşteri Kaydı", JOptionPane.OK_CANCEL_OPTION);
                
                if (result == JOptionPane.OK_OPTION) {
                    String name = nameField.getText().trim();
                    String surname = surnameField.getText().trim();
                    
                    if (!name.isEmpty() && !surname.isEmpty()) {
                        Customer yeniMusteri = new Customer(id, name, surname);
                        
                        // 1. Ağaca kaydet (O anki kullanım için)
                        bst.insert(yeniMusteri); 
                        
                        // 2. DOSYAYA KAYDET (Kalıcılık için)
                        dosyayaKaydet(yeniMusteri);
                        
                        displayArea.append("BAŞARILI: Yeni müşteri kalıcı olarak kaydedildi -> " + name + " " + surname + "\n");
                    } else {
                        displayArea.append("Hata: Ad veya soyad boş bırakılamaz!\n");
                    }
                }
            }
        });

        btnCagir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!queue.isEmpty()) {
                    Customer c = queue.dequeue();
                    displayArea.append("--------------------------------------------------\n");
                    displayArea.append(">>> VEZNEYE ÇAĞRILAN: " + c.getName() + " " + c.getSurname() + "\n");
                    displayArea.append("--------------------------------------------------\n");
                } else {
                    displayArea.append("Sistem: Kuyrukta bekleyen kimse yok.\n");
                }
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                BankGUI window = new BankGUI();
                window.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}