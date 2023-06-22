package shopping;

class User {
    String email;
    String password;
    String username;
    String address;
    private ShoppingCart cart;

    public User(String email, String password, String username, String address) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.address = address;
        this.cart = new ShoppingCart();
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public ShoppingCart getCart() {
        return cart;
    }
    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }   
}
