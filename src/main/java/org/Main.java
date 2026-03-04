package org;

import org.app.SysInventory;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    public static final Logger logger = Logger.getLogger(Main.class.getName());
    private static SysInventory inventory;
    private static Scanner sc;
    public static final String PRODUCT_CODE = "product_code";

    public static void main(String[] args) {

        sc = new Scanner(System.in);

        //Initializing the system
        logger.info("=== SHOP SIMULATOR - INVENTORY SYSTEM ===");
        logger.info("Initializing with JSON persistence");

        //Ask the user if he wants auto-save
        logger.info("Enable auto-save? (Y/n): ");
        System.out.print("");
        String autoSaveInput = sc.nextLine().trim().toLowerCase();
        boolean autoSave = !autoSaveInput.equals("n");

        inventory = new SysInventory(autoSave);

        int option;

        do {
            showMenu();option = readOption();

            try {
                switch (option) {

                    case 1 -> registerProduct();
                    case 2 -> augmentStock();
                    case 3 -> reduceStock();
                    case 4 -> validateInventory();
                    case 5 -> inventory.showAllProducts();
                    case 6 -> inventory.showAlerts();
                    case 7 -> showLowStockProducts();
                    case 8 -> inventory.clearAlerts();
                    case 9 -> inventory.saveData();
                    case 10 -> inventory.reloadData();
                    case 11 -> toggleAutoSave();
                    case 0 -> exit();
                    default -> logger.warning("Invalid option. Please try again.");
                }

            } catch (NumberFormatException e) {
                logger.warning("Error: please enter a valid number");
            } catch (Exception e) {
                logger.severe("Unexpected error: " + e.getMessage());
            }

            if (option != 0) {
                pause();
            }

        } while (option != 0);

        sc.close();
    }

    private static void showMenu() {

        logger.info("\n === SHOP SIMULATOR - MAIN MENU ===");
        logger.info("1. Register product");
        logger.info("2. Augment stock");
        logger.info("3. Reduce stock");
        logger.info("4. Validate inventory");
        logger.info("5. Show all products");
        logger.info("6. Show alerts");
        logger.info("7. Show low stock products");
        logger.info("8. Clear alerts");
        logger.info("9. Save data manually");
        logger.info("10. Reload data from JSON");
        logger.info("11. Toggle auto-save (current: " +
                (inventory.isAutoSave() ? "ON" : "OFF") + ")");
        logger.info("0. Exit");
        logger.info("Select an option: ");
    }

    private static int readOption() {
        try {
            return Integer.parseInt(sc.nextLine());

        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void registerProduct() {
        logger.info("\n📝 === REGISTER PRODUCT ===");

        String code = prompt("Code: ").toUpperCase();
        String name = prompt("Name: ");
        double price = promptDouble("Price: ");
        int quantity = promptInt("Initial quantity: ");

        inventory.registerProduct(code, name, price, quantity);
    }

    private static void augmentStock() {

        logger.info("\n === AUGMENT STOCK ===");

        logger.info(PRODUCT_CODE);
        logger.info("");
        String code = sc.nextLine().toUpperCase();

        logger.info("Quantity to add: ");
        logger.info("");
        int quantity = Integer.parseInt(sc.nextLine());

        inventory.augmentStock(code, quantity);
    }

    private static void reduceStock() {
        logger.info("\n === REDUCE STOCK ===");

        logger.info(PRODUCT_CODE);
        logger.info("");
        String code = sc.nextLine().toUpperCase();

        logger.info("Quantity to remove: ");
        logger.info("");
        int quantity = Integer.parseInt(sc.nextLine());

        inventory.reduceStock(code, quantity);
    }

    private static void validateInventory() {
        logger.info("\n === VALIDATE INVENTORY ===");

        logger.info(PRODUCT_CODE);
        logger.info("");
        String code = sc.nextLine().toUpperCase();

        logger.info("Required quantity: ");
        logger.info("");
        int quantity = Integer.parseInt(sc.nextLine());

        inventory.validateInventory(code, quantity);
    }

    private static void showLowStockProducts() {
        var lowStockProducts = inventory.getProductsLowStock();

        if (lowStockProducts.isEmpty()) {
            logger.info("No products with low stock.");
        } else {
            logger.info("\n === LOW STOCK PRODUCTS ===");
            lowStockProducts.forEach(p -> logger.info(p.toString()));
        }
    }

    private static void toggleAutoSave() {
        boolean current = inventory.isAutoSave();
        inventory.setAutoSave(!current);
        logger.info("Auto-save %s".formatted(!current ? "activated" : "deactivated"));
    }

    private static void exit() {
        logger.info("\n Saving data before exit...");
        if (inventory.saveData()) {
            logger.info("Data saved successfully");
        }

        logger.info("\n === SESSION STATISTICS ===");
        logger.info(" Total products: " + inventory.getTotalProducts());
        logger.info(" Total alerts: " + inventory.getTotalAlerts());

        logger.info("\n Thank you for using Shop Simulator!");
        logger.info("Goodbye!");
    }

    private static void pause() {
        logger.info("\n Press Enter to continue...");
        sc.nextLine();
    }

    // Aux method to show prompt and read inputs
    private static String prompt(String message) {
        logger.info(message);
        System.out.print("");
        return sc.nextLine();
    }

    private static int promptInt(String message) {
        logger.info(message);
        System.out.print("");
        return Integer.parseInt(sc.nextLine());
    }

    private static double promptDouble(String message) {
        logger.info(message);
        System.out.print("");
        return Double.parseDouble(sc.nextLine());
    }

}