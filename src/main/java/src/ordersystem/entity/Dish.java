package src.ordersystem.entity;

import lombok.Data;

@Data
public class Dish {
    private int dishId;
    private int sellerId;
    private String dishName;
    private String dishDescription;
    private double dishPrice;
    private String dishPictureUrl;
    private String ingredients;
    private String nutritionInfo;
    private String possibleAllergens;
    private float avg_mark;
}
