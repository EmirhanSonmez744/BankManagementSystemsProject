
public class Customer {

		    private String id;
		    private String name;
		    private String surname;

		    // Yapıcı Metot (Constructor) - Yeni bir müşteri nesnesi oluştururken bilgileri atamak için
		    public Customer(String id, String name, String surname) {
		        this.id = id;
		        this.name = name;
		        this.surname = surname;
		    }

		    // --- Getters & Setters (Bilgilere ulaşmak ve değiştirmek için) ---
		    
		    public String getId() {
		        return id;
		    }

		    public void setId(String id) {
		        this.id = id;
		    }

		    public String getName() {
		        return name;
		    }

		    public void setName(String name) {
		        this.name = name;
		    }

		    public String getSurname() {
		        return surname;
		    }

		    public void setSurname(String surname) {
		        this.surname = surname;
		    }

		    // Test aşamasında müşteriyi konsola yazdırırken anlamlı bir metin dönmesi için
		    @Override
		    public String toString() {
		        return "ID: " + id + " | " + name + " " + surname;
		    }
		}
	