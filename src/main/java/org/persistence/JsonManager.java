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

    public List<Product> loadProducts() {
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

    public List<String> loadAlerts() {
        File file = new File(ALERTS_FILE);
        if (!file.exists()) {
            logger.info("Alerts file not found. Starting with empty list");
            return new ArrayList<>();
        }

        try {
            List<String> alerts = objectMapper.readValue(file, new TypeReference<>() {});
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

    // --- Utility methods ---

    /**
     * Verify if exist the file of alerts
     * @return file path
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
}

