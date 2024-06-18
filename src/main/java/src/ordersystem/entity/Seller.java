package src.ordersystem.entity;

import lombok.Data;

@Data
public class Seller extends User{
    private String briefInfomation;
    private String address;
    private String featuredDish;
    private float avg_mark;
}
