package org.logic;

public class Product {

    private String code;
    private String name;
    private double price;
    private int quantity;

    //Constructor
    public Product(String code, String name, double price, int quantity) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    //Getters and Setters
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
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

    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("Code: %s | Name: %s | Quantity: %d | Price: $%.2f", code, name, quantity, price);
    }
}
