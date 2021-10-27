package com.example.lab04;

public class Product {

    private int _id;
    private String _productName;
    private double _price;

    public Product(){

    }

    public Product(String productName, double price){
        _productName = productName;
        _price = price;
    }

    public Product(int id, String productName, double price){
        _id = id;
        _productName = productName;
        _price = price;
    }

}
