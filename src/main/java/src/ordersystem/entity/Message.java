package src.ordersystem.entity;
import lombok.Data;

@Data
public class Message {
    private int message_id;
    private int user_id;
    private int sender_id;
    private String time;
    private String message;
}
