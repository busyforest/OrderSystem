package src.ordersystem.entity;

import lombok.Data;

@Data
public class OrderOverview {
    private int purChaserID;
    private int orderID;
    private String orderTime;
    private String dishStatus;
}
