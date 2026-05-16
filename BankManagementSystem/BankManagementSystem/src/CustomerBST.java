// Ağacın temel yapı taşı olan Düğüm sınıfı
class Node {
    Customer customer;
    Node left, right;

    public Node(Customer customer) {
        this.customer = customer;
        left = right = null;
    }
}

// Asıl İkili Arama Ağacı Sınıfımız
public class CustomerBST {
    private Node root; // Ağacın en tepesindeki kök düğüm

    public CustomerBST() {
        root = null;
    }

    // --- 1. Müşteri Ekleme (Insert) ---
    public void insert(Customer customer) {
        root = insertRec(root, customer);
    }

    // Rekürsif (kendi kendini çağıran) ekleme fonksiyonu
    private Node insertRec(Node root, Customer customer) {
        // Eğer ağaç boşsa veya sona ulaştıysak yeni düğümü buraya ekle
        if (root == null) {
            root = new Node(customer);
            return root;
        }

        // ID numaralarını String olarak karşılaştırmak için compareTo kullanıyoruz.
        // Eğer eklenecek ID, mevcut kökün ID'sinden küçükse SOLA git
        if (customer.getId().compareTo(root.customer.getId()) < 0) {
            root.left = insertRec(root.left, customer);
        } 
        // Eğer eklenecek ID, mevcut kökün ID'sinden büyükse SAĞA git
        else if (customer.getId().compareTo(root.customer.getId()) > 0) {
            root.right = insertRec(root.right, customer);
        }

        return root;
    }

    // --- 2. ID'ye Göre Müşteri Arama (Search) ---
    public Customer search(String id) {
        Node result = searchRec(root, id);
        if (result != null) {
            return result.customer; // Müşteri bulunduysa bilgilerini döndür
        }
        return null; // Müşteri yoksa boş (null) döndür
    }

    // Rekürsif arama fonksiyonu
    private Node searchRec(Node root, String id) {
        // Eğer ağacın sonuna geldiysek (null) veya aradığımız ID'yi bulduysak düğümü döndür
        if (root == null || root.customer.getId().equals(id)) {
            return root;
        }

        // Aranan ID, mevcut düğümün ID'sinden küçükse SOL ağaçta aramaya devam et
        if (root.customer.getId().compareTo(id) > 0) {
            return searchRec(root.left, id);
        }

        // Değilse SAĞ ağaçta aramaya devam et
        return searchRec(root.right, id);
    }
}