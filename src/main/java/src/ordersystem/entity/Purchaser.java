package src.ordersystem.entity;

import lombok.Data;

@Data
public class Purchaser extends User{
    private char gender;
    private int age;
    private int studentIDOrWorkID;
}
