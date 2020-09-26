package com.sample.model;

public class OrderNotFoundException extends Exception {

    private int id;

    public OrderNotFoundException(int id) {
        super("Order not found with id " + id);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
