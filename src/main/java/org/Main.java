package org;

import org.app.SysInventory;
import java.util.Scanner;
import java.util.logging.Logger;

//Note: This console menu will be replaced by a UI

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

    }

}