package org.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.logic.Product;
import static org.Main.logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonManager {

    private static final String DATA_DIRECTORY = "data";
    private static final String PRODUCTS_FILE = DATA_DIRECTORY + File.separator + "products.json";
    private static final String ALERTS_FILE = DATA_DIRECTORY + File.separator + "alerts.json";
    private final ObjectMapper objectMapper;

    public JsonManager() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        createDirectoryIfNotExists();
    }

    private void createDirectoryIfNotExists() {
        try {
            Path path = Paths.get(DATA_DIRECTORY);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
                logger.info("Directory created successfully");
            }
        } catch (IOException e) {
            logger.severe("Failed to create directory: " + e.getMessage());
        }
    }

    /**
     * Save products to JSON file
     * @param products List of products to save
     * @return true if the products were saved successfully, false otherwise.
     */

    public boolean saveProducts(List<Product> products) {
        try {
            objectMapper.writeValue(new File(PRODUCTS_FILE), products);
            logger.info("Products saved successfully");
            return true;
        } catch (IOException e) {
            logger.severe("Failed to save products: " + e.getMessage());
            return false;
        }
    }

    public static List<Product> loadProducts() {
        File file = new File(PRODUCTS_FILE);
        if (!file.exists()) {
            logger.info("Products file not found");
            return new ArrayList<>();
        }
        try {
            List <Product> products = objectMapper.readValue(file, new TypeReference<List<Product>>() {});
            logger.info("Products loaded successfully. Total: " + products.size());
            return products;
        } catch (MismatchedInputException e) {
            logger.warning("Product file is empty or invalid format");
            return new ArrayList<>();
        } catch (IOException e) {
            logger.severe("Failed to load products: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Add the product to JSON file (load, add, save)
     * @param product Product to add
     * @return true if the product was added successfully, false otherwise.
     */

    public boolean addProduct(Product product) {
        List<Product> products = loadProducts();
        products.add(product);
        return saveProducts(products);
    }

    /**
     * Actualize a existent product to JSON file
     * @param code Code of the product to update
     * param updatedProduct Product with the new data
     * @return true if the product was updated successfully, false otherwise.
     */

    public boolean updateProduct(String code, Product updatedProduct) {
        List<Product> products = loadProducts();
        boolean found = false;

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getCode().equalsIgnoreCase(code)) {
                products.set(i, updatedProduct);
                found = true;
                break;
            }
        }

        if (found) {
            return saveProducts(products);
        } else {
            logger.warning("Product not found for update: " + code);
            return false;
        }
    }

    /**
     * Search a product by code in the JSON file
     * @param code Code of the product to search
     * @return Optional with the product if the product exist, Optional empty otherwise.
     */

    public Optional<Product> findProductByCode(String code) {
        List<Product> products = loadProducts();
        return products.stream()
                .filter(p -> p.getCode()
                        .equalsIgnoreCase(code)).findFirst();
    }

    /**
     * Remove a product from JSON file
     * @param code Code of the product to remove
     * @return true if the product was removed successfully, false otherwise.
     */

    public boolean deleteProduct(String code) {
        List<Product> products = loadProducts();
        boolean removed = products.removeIf(p -> p.getCode()
                .equalsIgnoreCase(code));

        if (removed) {
            saveProducts(products);
            logger.info("Product removed successfully: " + code);
            return true;
        } else {
            logger.warning("Product not found for remove: " + code);
            return false;
        }
    }

    // --- Alerts ---

    /**
     * Save the list in a JSON file
     * @param alerts List of alerts to save
     * @return true if was saved successfully, false otherwise
     */

    public boolean saveAlerts(List<String> alerts) {
        try {
            objectMapper.writeValue(new File(ALERTS_FILE), alerts);
            logger.info("Alerts saved successfully. Total: " + alerts.size());
            return true;
        } catch (IOException e) {
            logger.severe("Error saving alerts: " + e.getMessage());
            return false;
        }
    }

    /**
     * Load the list of alerts from the JSON file
     * @return list of alerts loaded (empty if file not exist or error)
     */

    public static List<String> loadAlerts() {
        File file = new File(ALERTS_FILE);
        if (!file.exists()) {
            logger.info("Alerts file not found. Starting with empty list");
            return new ArrayList<>();
        }

        try {
            List<String> alerts = objectMapper.readValue(file, new TypeReference<List<String>>() {});
            logger.info("Alerts loaded successfully. Total: " + alerts.size());
            return alerts;
        } catch (MismatchedInputException e) {
            logger.warning("⚠Alerts file is empty");
            return new ArrayList<>();
        } catch (IOException e) {
            logger.severe("Error loading alerts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Add an alert to the JSON file
     * @param alert Alert to add
     * @return true if was added successfully, false otherwise
     */

    public boolean addAlert(String alert) {
        List<String> alerts = loadAlerts();
        if (!alerts.contains(alert)) {
            alerts.add(alert);
            return saveAlerts(alerts);
        }
        return true;
    }

    /**
     * Clear all the alerts from the JSON file
     * @return true if were successfully cleared
     */

    public boolean clearAlerts() {
        return saveAlerts(new ArrayList<>());
    }

    // --- Utility methods ---

    /**
     * Save products and also alerts in his respective files
     * @param products List of products
     * @param alerts List of alerts
     * @return true if both were successfully saved
     */

    public boolean saveAll(List<Product> products, List<String> alerts) {
        boolean productsSaved = saveProducts(products);
        boolean alertsSaved = saveAlerts(alerts);
        return productsSaved && alertsSaved;
    }

    /**
     * Verify if exist the file of products
     * @return true if exist
     */

    public boolean existProductsFile() {
        return new File(PRODUCTS_FILE).exists();
    }

    /**
     * Verify if exist the file of alerts
     * @return true if exist
     */

    public String getProductsFilePath() {
        return new File(PRODUCTS_FILE).getAbsolutePath();
    }

    /**
     * Get the absolute path of the alerts file
     * @return file path
     */

    public String getAlertsFilePath() {
        return new File(ALERTS_FILE).getAbsolutePath();
    }

    /**
     * Get the size of the product file in bytes
     * @return Size in bytes, 0 if file not exist
     */

    public long getProductsFileSize() {
        File file = new File(PRODUCTS_FILE);
        return file.exists() ? file.length() : 0;
    }

    public long getAlertsFileSize() {
        File file = new File(ALERTS_FILE);
        return file.exists() ? file.length() : 0;
    }
}

