package shopping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static List<User> users;
    static List<Product> products;
    static User currentUser;
    static ShoppingCart cart;
    public static void main(String[] args) {
        users = new ArrayList<>();
        products = new ArrayList<>();
        cart = new ShoppingCart();
        Scanner scanner = new Scanner(System.in);

        loadUsersFromFile();
        loadProducts();

        boolean exit = false;
        while (!exit) {
            System.out.println("============================");
            System.out.println("WELCOME TO TECH TRIAD HUB!");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Admin Menu");
            System.out.println("4. Exit");
            System.out.println("============================");
            System.out.print("Please enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    break;
                case 3:
                    displayAdminMenu(scanner);
                    break;
                case 4:
                    System.out.println("Exiting the program. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        scanner.close();
    }

    static void registerUser(Scanner scanner) {
        System.out.println("\nREGISTRATION:");
        System.out.print("Email: ");
        String email = scanner.nextLine();

        String emailRegex = "^(.+)@(gmail|yahoo)\\.com$"; // Validate email using regex
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            System.out.println("Invalid email format. Please use either @gmail.com or @yahoo.com.\n");
            return;
        }
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        User newUser = new User(email, password, username, address);
        users.add(newUser);
        saveUsersToFile();
        System.out.println("Registration successful. Please login.");
        System.out.println();
    }

    static void loginUser(Scanner scanner) {
        System.out.println("\nLOGIN:");
        System.out.print("Email or Username: ");
        String emailOrUsername = scanner.nextLine();

        currentUser = findUserByEmailOrUsername(emailOrUsername);
        if (currentUser != null) {
            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (password.equals(currentUser.getPassword())) {
                System.out.println("Login successful. Welcome, " + currentUser.getUsername() + "!");
                System.out.println();

            // Create a new ShoppingCart instance for the logged-in user
            ShoppingCart cart = new ShoppingCart();
            currentUser.setCart(cart);

                boolean loggedIn = true;
                while (loggedIn) {
                    if (currentUser.getUsername().equals("admin")) {
                        displayAdminMenu(scanner);
                    } else {

                        try {
                            displayCustomerMenu(scanner);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                    }
                    loggedIn = false;
                }

            } else {
                System.out.println("Incorrect password. Please try again.");
                System.out.println();
            }
        } else {
            System.out.println("User not found. Please register an account.");
            System.out.println();
        }
    }

    static User findUserByEmailOrUsername(String emailOrUsername) {
        for (User user : users) {
            if (user.getEmail().equals(emailOrUsername) || user.getUsername().equals(emailOrUsername)) {
                return user;
            }
        }
        return null;
    }

    static void displayAdminMenu(Scanner scanner) {
        System.out.print("\nENTER ADMIN PASSWORD: ");
        String password = scanner.nextLine();

        if (password.equals("@admin")) {
        System.out.println("Welcome, Admin!");
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=========================");
            System.out.println("ADMIN MENU:");
            System.out.println("1. View Products");
            System.out.println("2. Add Product");
            System.out.println("3. Remove Product");
            System.out.println("4. Update Product Stock");
            System.out.println("5. View Users");
            System.out.println("6. Logout");
            System.out.println("=========================");
            System.out.print("Please enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    displayProducts();
                    break;
                case 2:
                    addProduct(scanner);
                    break;
                case 3:
                    removeProduct(scanner);
                    break;
                case 4:
                    updateProductStock(scanner);
                    break;
                case 5:
                    viewUsers();
                    break;
                case 6:
                    exit = true;
                    currentUser = null;
                    System.out.println("Logged out successfully.");
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        } else {
            System.out.println("Invalid password. Access denied.");
            System.out.println();
        }
    }

    static void addProduct(Scanner scanner) {
            System.out.println("\nADD A PRODUCT:");
            System.out.print("Enter the product name: ");
            String name = scanner.nextLine();

            System.out.print("Enter the product price: ");
            double price = scanner.nextDouble();
            scanner.nextLine(); // Consume newline character

            System.out.print("Enter the product quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            Product newProduct = new Product(name, price, quantity);
            products.add(newProduct);
            saveProductsToFile();
            System.out.println("Product added successfully.");
}   

static void removeProduct(Scanner scanner) {
    System.out.println("\nAvailable Products:");
    for (Product product : products) {
        System.out.println(product.getName());
    }
    System.out.print("Enter the product name to remove: ");
    String productName = scanner.nextLine();

    boolean productRemoved = false;
    Iterator<Product> iterator = products.iterator();
    while (iterator.hasNext()) {
        Product product = iterator.next();
        if (product.getName().equalsIgnoreCase(productName)) {
            iterator.remove();
            productRemoved = true;
            break;
        }
    }
    if (productRemoved) {
        saveProductsToFile();
        System.out.println("Product removed successfully.");
    } else {
        System.out.println("Product not found with the given name.");
    }
}

    static void updateProductStock(Scanner scanner) {
        System.out.println("\nPRODUCT STOCK UPDATE:");

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println((i + 1) + ". " + product.getName() + " - Quantity: " + product.getQuantity());
        }
        System.out.print("Select a product to update (enter the number): ");
        int productIndex = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        if (productIndex >= 1 && productIndex <= products.size()) {
            Product selectedProduct = products.get(productIndex - 1);
            System.out.print("Enter the new quantity: ");
            int newQuantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            selectedProduct.setQuantity(newQuantity);
            System.out.println("Product stock updated successfully.");
            System.out.println();
            // Save changes to the .txt file
            saveProductsToFile("products.txt");
        } else {
            System.out.println("Invalid product selection. Please try again.");
        }
    }

    static void saveProductsToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Product product : products) {
                writer.println(product.getName() + "," + product.getPrice() + "," + product.getQuantity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void viewUsers() {
        System.out.println("\nREGISTERED USERS:");

        for (User user : users) {
            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Address: " + user.getAddress());
            System.out.println();
        }
    }

    static void displayCustomerMenu(Scanner scanner) throws IOException{
        ShoppingCart cart = currentUser.getCart();
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n==================================");
            System.out.println("CUSTOMER MENU:");
            System.out.println("1. View Products");
            System.out.println("2. Add Product to Cart");
            System.out.println("3. Remove Product from Cart");
            System.out.println("4. Update Product Quantity in Cart");
            System.out.println("5. View Cart");
            System.out.println("6. Checkout");
            System.out.println("7. Logout");
            System.out.println("==================================");
            System.out.print("Please enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    displayProducts();
                    break;
                case 2:
                    displayProducts();
                    addProductToCart(scanner);
                    break;
                case 3:
                    removeProductFromCart(scanner, currentUser, cart);
                    break;
                case 4:
                    updateProductQuantityInCart(scanner, currentUser, cart);;
                    break;
                case 5:
                    System.out.print("\nCART CONTENTS:");
                    CartManager.loadCartFromFile(currentUser, cart);; // Save the cart to file after updating quantity
                    break;
                case 6:
                    checkout(scanner);
                    exit = true;
                    deleteCartFile(currentUser);
                    System.out.println("Logged out successfully.");
                    System.out.println();
                    break;
                case 7:
                    exit = true;
                    currentUser = null;
                   CartManager.saveCartToFile(currentUser, cart);
                    logout(currentUser, cart);
                    System.out.println("Logged out successfully.");
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

static void logout(User user, ShoppingCart cart) throws IOException{
    CartManager.saveCartToFile(user, cart); // Save the cart to file before logout
    currentUser = null; // Set the currentUser to null
}

    static void displayProducts() {
        System.out.println("\nAVAILABLE PRODUCTS:");

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println((i + 1) + ". " + product.getName() + " - Price: PHP " + product.getPrice() + " - " + product.getQuantity() + " left!");
        }
    }

    static void addProductToCart(Scanner scanner) throws IOException {
        // System.out.println("\nAVAILABLE PRODUCTS:");
        // displayProducts();

        System.out.print("Select a product to add to cart (enter the number): ");
        int productIndex = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        if (productIndex >= 1 && productIndex <= products.size()) {
            Product selectedProduct = products.get(productIndex - 1);
            System.out.print("Enter the quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            if (selectedProduct.getQuantity() >= quantity) {
                cart.addItem(selectedProduct, quantity);
                selectedProduct.setQuantity(selectedProduct.getQuantity() - quantity); // Update the quantity of the remaining product

                // Save the updated product information to the file
                saveProductsToFile("products.txt");

                // Update the cart in the text file
                String filename = currentUser.getUsername() + "_cart.txt";
                File file = new File(filename);
                if (!file.exists()) {
                    file.createNewFile();
                }
                
                List<String> lines = new ArrayList<>();
                try (Scanner fileScanner = new Scanner(file)) {
                    while (fileScanner.hasNextLine()) {
                        String line = fileScanner.nextLine();
                        String[] parts = line.split(",");
                        String productName = parts[0].trim();
                        int existingQuantity = Integer.parseInt(parts[1].trim());
                        double price = Double.parseDouble(parts[2].trim());

                        if (!productName.equalsIgnoreCase(selectedProduct.getName())) {
                            lines.add(line);
                        } else {
                            // Update the quantity by adding the new quantity
                            int newQuantity = existingQuantity + quantity;
                            lines.add(productName + "," + newQuantity + "," + price);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Add the selected product as a new line if it doesn't exist in the cart
                boolean productExistsInCart = false;
                for (String line : lines) {
                    String[] parts = line.split(",");
                    String productName = parts[0].trim();
                    if (productName.equalsIgnoreCase(selectedProduct.getName())) {
                        productExistsInCart = true;
                        break;
                    }
                }
                if (!productExistsInCart) {
                    lines.add(selectedProduct.getName() + "," + quantity + "," + selectedProduct.getPrice());
                }

                // Write the updated lines back to the cart file
                try (PrintWriter writer = new PrintWriter(file)) {
                    for (String line : lines) {
                        writer.println(line);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                System.out.println("Cart file updated successfully.");
                System.out.println();
            } else {
                System.out.println("Insufficient stock. Please try again.");
                System.out.println();
            }
        } else {
            System.out.println("Invalid product selection. Please try again.");
            System.out.println();
        }
    }
    
    static void removeProductFromCart(Scanner scanner, User user, ShoppingCart cart) throws IOException {
        System.out.println("\nCART ITEMS:");
        CartManager.loadCartFromFile(user, cart);

        System.out.print("Enter the name of the product to remove from cart: ");
        String productName = scanner.nextLine();

        // Remove the selected product from the cart
        Product selectedProduct = null;
        for (Product product : cart.getItems().keySet()) {
            if (product.getName().equalsIgnoreCase(productName)) {
                selectedProduct = product;
                break;
            }
        }
        if (selectedProduct != null) {
            int selectedProductQuantity = cart.getItems().get(selectedProduct);
            cart.removeItem(selectedProduct);
            System.out.println("Product removed from cart successfully.");
            System.out.println();

            // Update the cart in the text file
            String filename = user.getUsername() + "_cart.txt";
            File file = new File(filename);
            if (file.exists()) {
                List<String> lines = new ArrayList<>();
                try (Scanner fileScanner = new Scanner(file)) {
                    while (fileScanner.hasNextLine()) {
                        String line = fileScanner.nextLine();
                        String[] parts = line.split(",");
                        String productToRemove = parts[0];
                        int quantity = Integer.parseInt(parts[1]);
                        double price = Double.parseDouble(parts[2]);
                        if (!productToRemove.equalsIgnoreCase(productName)) {
                            lines.add(line);
                        } else {
                            // Reduce the quantity by the removed quantity
                            int newQuantity = quantity - selectedProductQuantity;
                            if (newQuantity > 0) {
                                lines.add(productToRemove + "," + newQuantity + "," + price);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Write the updated lines back to the cart file
                try (PrintWriter writer = new PrintWriter(file)) {
                    for (String line : lines) {
                        writer.println(line);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Cart file not found.");
            }
        } else {
            System.out.println("Product not found in cart. Please try again.");
            System.out.println();
        }
    }

    static void displayCartItems(ShoppingCart cart) {
        Map<Product, Integer> items = cart.getItems();
        if (items.isEmpty()) {
            System.out.println("Cart is empty.\n");
        } else {
            for (Map.Entry<Product, Integer> entry : items.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                System.out.println("Product: " + product.getName() + "\nQuantity: " + quantity + "\nPrice: PHP " + product.getPrice());
                System.out.println();
            }
        }
    }
    
static void updateProductQuantityInCart(Scanner scanner, User user, ShoppingCart cart) throws IOException {
    System.out.println("\nCART ITEMS:");
    CartManager.loadCartFromFile(user, cart);

    System.out.print("Enter the name of the product to update quantity: ");
    String productName = scanner.nextLine();

    // Find the selected product in the cart
    Product selectedProduct = null;
    for (Product product : cart.getItems().keySet()) {
        if (product.getName().equalsIgnoreCase(productName)) {
            selectedProduct = product;
            break;
        }
    }
    if (selectedProduct != null) {
        System.out.print("Enter the new quantity: ");
        int newQuantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        if (newQuantity >= 0) {
            int currentQuantity = cart.getItems().get(selectedProduct);
            int quantityDifference = newQuantity - currentQuantity;
            if (quantityDifference > 0) {
                // Check if the product has sufficient stock
                if (selectedProduct.getQuantity() >= quantityDifference) {
                    cart.addItem(selectedProduct, quantityDifference);
                    selectedProduct.setQuantity(selectedProduct.getQuantity() - quantityDifference);
                    System.out.println("Quantity updated successfully.");
                    System.out.println();
                } else {
                    System.out.println("Insufficient stock. Please try again.");
                    System.out.println();
                }
            } else if (quantityDifference < 0) {
                cart.removeItemQuantity(selectedProduct, -quantityDifference);
                selectedProduct.setQuantity(selectedProduct.getQuantity() + quantityDifference);
                System.out.println("Quantity updated successfully.");
                System.out.println();
            } else {
                System.out.println("No change in quantity.");
                System.out.println();
            }
            // Update the cart in the cart file
            String filename = user.getUsername() + "_cart.txt";
            File file = new File(filename);
            if (file.exists()) {
                List<String> lines = new ArrayList<>();
                try (Scanner fileScanner = new Scanner(file)) {
                    while (fileScanner.hasNextLine()) {
                        String line = fileScanner.nextLine();
                        String[] parts = line.split(",");
                        String productToUpdate = parts[0];
                        double price = Double.parseDouble(parts[2]);
                        if (!productToUpdate.equalsIgnoreCase(productName)) {
                            lines.add(line);
                        } else {
                            lines.add(productToUpdate + "," + newQuantity + "," + price);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Write the updated lines back to the cart file
                try (PrintWriter writer = new PrintWriter(file)) {
                    for (String line : lines) {
                        writer.println(line);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Cart file not found.");
            }
        } else {
            System.out.println("Invalid quantity. Please enter a non-negative value.");
            System.out.println();
        }
    } else {
        System.out.println("Product not found in cart. Please try again.");
        System.out.println();
    }
}

    static void saveProductsToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"));
            for (Product product : products) {
                writer.write(product.getName() + "," + product.getPrice() + "," + product.getQuantity());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

    static void checkout(Scanner scanner) {
        System.out.println("\nCHECKOUT:");
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy 'at' hh:mm:ss a");
        SimpleDateFormat timeFormat = new SimpleDateFormat("MM.dd.yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        Date estimatedDeliveryDate = calendar.getTime();

        System.out.print("Select payment option (1. Credit Card, 2. Debit Card, 3. Cash): ");
        int paymentOption = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        String paymentMethod;
        if (paymentOption == 1) {
            paymentMethod = "Credit Card";
        } else if (paymentOption == 2) {
            paymentMethod = "Debit Card";
        } else {
            paymentMethod = "Cash";
        }
        System.out.println("\n===================================================RECIEPT=====================================================");
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Delivery Address: " + currentUser.getAddress());
        System.out.println("\nCART CONTENTS:");
        CartManager.loadCartFromFile(currentUser, cart);
    
        System.out.println("\nCheckout Date: " + dateFormat.format(currentDate));
        System.out.println("Estimated Delivery Date: " + timeFormat.format(estimatedDeliveryDate));
        System.out.println("Thank you for your purchase! The products will be delivered to your address within the estimated delivery date.");
        System.out.println("================================================================================================================");
        System.out.println();
        cart.clearCart();
    }

    public static void deleteCartFile (User user) {
        try {
            File myFile = new File("C:\\Users\\bench\\Downloads\\onlayn shap\\" + user.getUsername() + "_cart.txt");
            if (myFile.delete()) {
                System.out.println(myFile.getName() + " was emptied successfully!");
            } else {
                System.out.println("Failed to delete " + myFile.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void loadUsersFromFile() {
        try {
            File file = new File("users.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        String email = parts[0];
                        String password = parts[1];
                        String username = parts[2];
                        String address = parts[3];

                        User user = new User(email, password, username, address);
                        users.add(user);
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void saveUsersToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"));
            for (User user : users) {
                writer.write(user.getEmail() + "," + user.getPassword() + "," + user.getUsername() + "," + user.getAddress());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void loadProducts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("products.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Product product = new Product(data[0], Double.parseDouble(data[1]), Integer.parseInt(data[2]));
                products.add(product);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading products from file.");
        }
    }
}
