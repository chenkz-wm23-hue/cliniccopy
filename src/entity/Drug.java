package entity;

public class Drug {
    private String id;
    private String name;
    private int quantity;
    private double price;
    private String description;
    private String category;
    private String expiryDate;
    private int minStockLevel;

    
    public Drug() {
        this.id = "";
        this.name = "";
        this.quantity = 0;
        this.price = 0.0;
        this.description = "";
        this.category = "";
        this.expiryDate = "";
        this.minStockLevel = 10;
    }

  
    public Drug(String id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.description = "";
        this.category = "";
        this.expiryDate = "";
        this.minStockLevel = 10;
    }

  
    public Drug(String id, String name, int quantity, double price, String description, 
                String category, String expiryDate, int minStockLevel) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.category = category;
        this.expiryDate = expiryDate;
        this.minStockLevel = minStockLevel;
    }

    
    public String getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getExpiryDate() { return expiryDate; }
    public int getMinStockLevel() { return minStockLevel; }

    
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public void setMinStockLevel(int minStockLevel) { this.minStockLevel = minStockLevel; }

    
    @Override
    public String toString() {
        return String.format("Drug ID: %s | Name: %s | Quantity: %d | Price: $%.2f | Category: %s | Expiry: %s", 
                           id, name, quantity, price, category, expiryDate);
    }

   
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Drug drug = (Drug) obj;
        return id.equals(drug.id);
    }

   
    @Override
    public int hashCode() {
        return id.hashCode();
    }

   
    public boolean isLowStock() {
        return quantity <= minStockLevel;
    }

    public boolean isOutOfStock() {
        return quantity == 0;
    }

    public double getTotalValue() {
        return quantity * price;
    }
}
