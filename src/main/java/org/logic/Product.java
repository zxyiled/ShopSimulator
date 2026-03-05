package org.logic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

public class Product {

    private final String code;
    private final String name;
    private double price;
    private int quantity;

    //Constructor
    @JsonCreator
    public Product(
            @JsonProperty("code") String code,
            @JsonProperty("name") String name,
            @JsonProperty("price") double price,
            @JsonProperty("quantity") int quantity) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    //Getters and Setters
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("Code: %s | Name: %s | Quantity: %d | Price: $%.2f", code, name, quantity, price);
    }
}
