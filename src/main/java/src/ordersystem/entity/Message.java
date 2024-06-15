package src.ordersystem.entity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Message {
    private int message_id;
    private int receiver_id;
    private int sender_id;
    private Timestamp time;
    private String message;
}
