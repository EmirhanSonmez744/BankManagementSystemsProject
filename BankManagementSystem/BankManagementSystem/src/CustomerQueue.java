// Kuyruktaki her bir elemanı temsil edecek Düğüm sınıfı
class QueueNode {
    Customer customer;
    QueueNode next; // Kuyruktaki bir sonraki kişiyi işaret eder

    public QueueNode(Customer customer) {
        this.customer = customer;
        this.next = null;
    }
}

public class CustomerQueue {
    private QueueNode front; // Sıranın başı (vezneye ilk gidecek kişi)
    private QueueNode rear;  // Sıranın sonu (kuyruğa en son katılan kişi)

    public CustomerQueue() {
        this.front = this.rear = null;
    }

    // --- 1. Kuyruğa Müşteri Ekleme (Sıraya Girme - Enqueue) ---
    public void enqueue(Customer customer) {
        QueueNode newNode = new QueueNode(customer);

        // Eğer kuyruk boşsa, gelen kişi hem sıranın başı hem de sonu olur
        if (this.rear == null) {
            this.front = this.rear = newNode;
            System.out.println("-> " + customer.getName() + " kuyruğa katıldı. (Sıradaki ilk kişi)");
            return;
        }

        // Kuyruk boş değilse, yeni kişiyi sıranın sonuna ekle ve 'rear' işaretçisini güncelle
        this.rear.next = newNode;
        this.rear = newNode;
        System.out.println("-> " + customer.getName() + " kuyruğa katıldı.");
    }

    // --- 2. Kuyruktan Müşteri Çıkarma (Vezneye Çağırma - Dequeue) ---
    public Customer dequeue() {
        // Kuyruk boşsa çağrılacak kimse yoktur
        if (this.front == null) {
            System.out.println("Uyarı: Kuyrukta bekleyen müşteri yok.");
            return null;
        }

        // Sıranın başındaki müşterinin bilgilerini geçici bir değişkene al
        QueueNode temp = this.front;
        
        // Sıranın başını bir arkadaki kişiye kaydır
        this.front = this.front.next;

        // Eğer sıradaki kişiyi aldıktan sonra kuyruk tamamen boşaldıysa, 'rear'ı da null yap
        if (this.front == null) {
            this.rear = null;
        }

        return temp.customer; // Vezneye çağrılan müşteriyi döndür
    }

    // --- 3. Kuyruk Boş mu Kontrolü (isEmpty) ---
    public boolean isEmpty() {
        return front == null;
    }
}