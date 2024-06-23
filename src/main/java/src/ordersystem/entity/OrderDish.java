package src.ordersystem.entity;

import lombok.Data;

@Data
public class OrderDish {
    private int orderID;
    private int dishID;
    private int quantity;
    private String purChaseMethod;
    private String comment;
    private double mark;
}

