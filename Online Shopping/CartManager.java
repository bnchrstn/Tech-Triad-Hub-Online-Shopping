package shopping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.io.BufferedWriter;

public class CartManager {

    static List<Product> products;

   static void saveCartToFile(User user, ShoppingCart cart) throws IOException {
    if (user != null) {
        String filename = user.getUsername() + "_cart.txt";
        // Create a set to keep track of the unique items
        Set<String> uniqueItems = new HashSet<>();
        // Iterate over the cart items and add unique items to the set
        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            String item = product.getName() + "," + quantity + "," + product.getPrice();
            uniqueItems.add(item);
        }
        // Append the unique items to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            for (String item : uniqueItems) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    static void loadCartFromFile(User user, ShoppingCart cart) {
        if (user != null) {
            String filename = user.getUsername() + "_cart.txt";
            File file = new File(filename);
            if (file.exists()) {
                try (Scanner scanner = new Scanner(file)) {
                    cart.clearCart(); // Clear the cart before loading from file
                    double totalPrice = 0.0;
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] parts = line.split(",");
                        String productName = parts[0];
                        int quantity = Integer.parseInt(parts[1]);
                        double price = Double.parseDouble(parts[2]);
                        double itemTotalPrice = price * quantity;
                        totalPrice += itemTotalPrice; 
                        System.out.println("\nProduct: " + parts[0] + "\nQuantity: " + parts[1] + "\nPrice: PHP " + itemTotalPrice);
                        Product product = new Product(productName, price, quantity);
                        cart.addItem(product, quantity);
                    }
                    System.out.println("\nTOTAL CART PRICE: PHP " + totalPrice);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static Product findProductByName(String productName) {
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        return null; // Return null if the product is not found
    }

    public static List<Product> getProducts() {
        return products;
    }

    public static void setProducts(List<Product> products) {
        CartManager.products = products;
    }
}
