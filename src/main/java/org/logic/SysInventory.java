package org.logic;
import java.util.List;
import java.util.ArrayList;

public class SysInventory {
    private List<Product> products;
    private List<String> alerts;

    public SysInventory() {
        this.products = new ArrayList<>();
        this.alerts = new ArrayList<>();
    }
}
