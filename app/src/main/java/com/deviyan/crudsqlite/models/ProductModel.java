package com.deviyan.crudsqlite.models;

public class ProductModel {

    String id, name,  unit,  price, dataOfExpiry, availableInventory, availableInventoryCost, imagePath;

    public ProductModel() {

    }

    public ProductModel(String id, String name, String unit, String price, String dataOfExpiry, String availableInventory, String availableInventoryCost, String imagePath) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.dataOfExpiry = dataOfExpiry;
        this.availableInventory = availableInventory;
        this.availableInventoryCost = availableInventoryCost;
        this.imagePath = imagePath;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDataOfExpiry() {
        return dataOfExpiry;
    }

    public void setDataOfExpiry(String dataOfExpiry) {
        this.dataOfExpiry = dataOfExpiry;
    }

    public String getAvailableInventory() {
        return availableInventory;
    }

    public void setAvailableInventory(String availableInventory) {
        this.availableInventory = availableInventory;
    }

    public String getAvailableInventoryCost() {
        return availableInventoryCost;
    }

    public void setAvailableInventoryCost(String availableInventoryCost) {
        this.availableInventoryCost = availableInventoryCost;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
