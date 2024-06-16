package src.ordersystem;
import com.mysql.jdbc.Driver;
import src.ordersystem.entity.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class SQLLoader {
    Driver driver;
    Properties properties;
    Connection connect;
    Statement statement;
    public void connect() throws SQLException {
        // 1. 注册驱动
        driver = new Driver();  // 创建driver对象

        // 2. 得到连接
        // jdbc:mysql 表示协议，通过jdbc的方式连接mysql
        // localhost 主机，也可以是ip地址
        // 3306 表示 mysql 监听的端口
        String url = "jdbc:mysql://localhost:3306/ordersql";
        // 将 用户名和密码放入 Properties 对象中
        properties = new Properties();
        properties.setProperty("user", "root");  // 用户
        properties.setProperty("password", "654321");  // 密码（填入自己用户名对应的密码）
        properties.put("allowMultiQueries", "true");  // 允许多条 SQL 语句执行
        // 根据给定的 url 连接数据库
        connect = driver.connect(url, properties);
        statement = connect.createStatement();
        init();
        insert();
    }
    public  void init(){
        run("src/main/SQLStatements/init.sql");
        try {
            statement.executeUpdate("CREATE TRIGGER IF NOT EXISTS SEND_MESSAGE_AFTER_PURCHASE\n" +
                    "AFTER INSERT ON orderOverview\n" +
                    "FOR EACH ROW\n" +
                    "BEGIN\n" +
                    "    INSERT INTO message VALUES (NEW.purchaser_id, 1, '您已成功下单，请耐心等待卖家发货', NOW());\n" +
                    "END;");
            statement.executeUpdate("CREATE TRIGGER IF NOT EXISTS SEND_MESSAGE_AFTER_UPDATE_STATUS\n" +
                    "AFTER UPDATE ON orderOverview\n" +
                    "FOR EACH ROW\n" +
                    "BEGIN\n" +
                    "    IF NEW.dish_status = '已发货' THEN\n" +
                    "        INSERT INTO message VALUES ((select distinct seller_id from dish where dish_id = (select dish_id from order_dish where order_id = NEW.order_id limit 1)), NEW.purchaser_id, '您的订单已发货，请注意查收', NOW());\n" +
                    "    END IF;\n" +
                    "END;");
            statement.executeUpdate("CREATE TRIGGER IF NOT EXISTS UPDATE_SELLER_ID\n" +
                    "AFTER INSERT ON order_dish\n"+
                    "FOR EACH ROW\n" +
                    "BEGIN\n" +
                    "    UPDATE message set receiver_id = (select seller_id from dish where dish_id = NEW.dish_id) where message.message_time=(select order_time from orderOverview where order_id = NEW.order_id) and message.sender_id = (select purchaser_id from orderOverview where order_id = NEW.order_id);\n" +
                    "END;");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void insert() {
        run("src/main/SQLStatements/insert.sql");
    }
    public void run(String sqlFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(sqlFilePath))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                // 每行不为空时，将其添加到 StringBuilder 中
                sb.append(line);
                // 如果遇到分号，则认为一个 SQL 语句结束
                if (line.trim().endsWith(";")) {
                    String sql = sb.toString();
                    // 执行 SQL 语句
                    statement.executeUpdate(sql);
                    // 清空 StringBuilder 以准备下一个 SQL 语句
                    sb.setLength(0);
                }
            }
        } catch (SQLException | java.io.IOException e) {
            e.printStackTrace();
        }
    }
    public boolean login(int id, String passwd) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("select passwd from user where id=");
        sb.append(id);
        ResultSet resultSet = null;
        resultSet = statement.executeQuery(sb.toString());
        while (resultSet.next()){
            String password = resultSet.getString("passwd");
            return password.equals(passwd);
        }
        return false;
    }
    public User searchUser(int id) throws SQLException {
        StringBuilder sb = new StringBuilder();

        //首先在 purchase 里面找
        sb.append("select * from purchaser where id =");
        sb.append(id);
        ResultSet resultSet = null;
        resultSet = statement.executeQuery(sb.toString());
        if(resultSet.next()){
            Purchaser purchaser = new Purchaser();
            purchaser.setId(resultSet.getInt("id"));
            purchaser.setGender(resultSet.getString("gender").charAt(0));
            purchaser.setStudentIDOrWorkID(resultSet.getInt("studentIDOrWorkID"));
            StringBuilder sb1 = new StringBuilder();
            sb1.append("select name from user where id =");
            sb1.append(id);
            ResultSet resultSet1 = null;
            resultSet1 = statement.executeQuery(sb1.toString());
            if(resultSet1.next()){
                purchaser.setName(resultSet1.getString("name"));
            }
            return purchaser;
        }

        //然后在商家里面找
        sb.setLength(0);
        sb.append("select * from seller where id =");
        sb.append(id);
        ResultSet resultSet2 = null;
        resultSet2 = statement.executeQuery(sb.toString());
        if(resultSet2.next()){
            Seller seller = new Seller();
            seller.setId(resultSet2.getInt("id"));
            seller.setAddress(resultSet2.getString("address"));
            seller.setBriefInfomation(resultSet2.getString("brief_information"));
            seller.setFeaturedDish(resultSet2.getString("featured_dish"));
            StringBuilder sb1 = new StringBuilder();
            sb1.append("select name from user where id =");
            sb1.append(id);
            ResultSet resultSet1 = null;
            resultSet1 = statement.executeQuery(sb1.toString());
            if(resultSet1.next()){
                seller.setName(resultSet1.getString("name"));
            }
            return seller;
        }
        //最后在管理员里面找
        sb.setLength(0);
        sb.append("select * from administrator where id =");
        sb.append(id);
        ResultSet resultSet3 = null;
        resultSet3 = statement.executeQuery(sb.toString());
        if(resultSet3.next()){
            Administrator administrator = new Administrator();
            administrator.setId(resultSet3.getInt("id"));
            StringBuilder sb1 = new StringBuilder();
            sb1.append("select name from user where id =");
            sb1.append(id);
            ResultSet resultSet1 = null;
            resultSet1 = statement.executeQuery(sb1.toString());
            if(resultSet1.next()){
                administrator.setName(resultSet1.getString("name"));
            }
            return administrator;
        }
        return null;
    }
    public ArrayList<Seller> searchSeller(String name) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from seller where name like '%"+name+"%'");
        ResultSet resultSet = statement.executeQuery(sb.toString());
        ArrayList<Seller> sellers = new ArrayList<>();
        while (resultSet.next()){
            Seller seller = new Seller();
            seller.setId(resultSet.getInt("id"));
            seller.setAddress(resultSet.getString("address"));
            seller.setBriefInfomation(resultSet.getString("brief_information"));
            seller.setFeaturedDish(resultSet.getString("featured_dish"));
            StringBuilder sb1 = new StringBuilder();
            sb1.append("select name from user where id ="+seller.getId());
            ResultSet resultSet1 = statement.executeQuery(sb1.toString());
            if(resultSet1.next()){
                seller.setName(resultSet1.getString("name"));
            }
            sellers.add(seller);
        }
        return sellers;
        //System.out.println(sellers);
    }

    //管理员的功能
    //管理商家
    public void addSeller(Seller seller) throws SQLException {
        statement.executeUpdate("insert into seller(id,address,brief_information,featured_dish) values("+seller.getId()+",'"+seller.getAddress()+"','"+seller.getBriefInfomation()+"','"+seller.getFeaturedDish()+"')");
    }
    public void deleteSeller(int id) throws SQLException {
        statement.executeUpdate("delete from seller where id="+id);
    }
    //TODO:修改商家信息
    //管理顾客
    public void addPurchaser(Purchaser purchaser) throws SQLException {
        statement.executeUpdate("insert into purchaser(id,gender,studentIDOrWorkID) values("+purchaser.getId()+",'"+purchaser.getGender()+"',"+purchaser.getStudentIDOrWorkID()+")");
    }
    public void deletePurchaser(int id) throws SQLException {
        statement.executeUpdate("delete from purchaser where id="+id);
    }
    //TODO:修改顾客信息

    //买家评论菜品
    public void commentDish(int userId,int dishId,String comment) throws SQLException {
        String checkIfExist = "select * from interacted_dish where purchaser_id="+userId+" and dish_id="+dishId;
        ResultSet resultSet = statement.executeQuery(checkIfExist);
        if(resultSet.next()){
            statement.executeUpdate("update interacted_dish set comment='"+comment+"' where purchaser_id="+userId+" and dish_id="+dishId);
        }else {
            statement.executeUpdate("insert into interacted_dish(purchaser_id,dish_id,comment, isFavorite) values(" + userId + "," + dishId + ",'" + comment + "','false')");
        }
    }
    //买家收藏菜品
    public void favoriteDish(int userId,int dishId) throws SQLException {
        String checkIfExist = "select * from interacted_dish where purchaser_id="+userId+" and dish_id="+dishId;
        ResultSet resultSet = statement.executeQuery(checkIfExist);
        if(resultSet.next()){
            statement.executeUpdate("update interacted_dish set isFavorite='true' where purchaser_id="+userId+" and dish_id="+dishId);
        }else {
            statement.executeUpdate("insert into interacted_dish(purchaser_id,dish_id,comment,isFavorite) values(" + userId + "," + dishId + ",'','true')");
        }
    }
    //买家收藏商家
    public void favoriteSeller(int userId,int sellerId) throws SQLException {
        String checkIfExist = "select * from interacted_seller where purchaser_id="+userId+" and seller_id="+sellerId;
        ResultSet resultSet = statement.executeQuery(checkIfExist);
        if(resultSet.next()){
            statement.executeUpdate("update interacted_seller set isFavorite='true' where purchaser_id="+userId+" and seller_id="+sellerId);
        }else {
            statement.executeUpdate("insert into interacted_seller(purchaser_id,seller_id,comment,isFavorite) values(" + userId + "," + sellerId + ",'','true')");
        }
    }
    //买家评论商家
    public void commentSeller(int userId,int sellerId,String comment) throws SQLException {
        String checkIfExist = "select * from interacted_seller where purchaser_id="+userId+" and seller_id="+sellerId;
        ResultSet resultSet = statement.executeQuery(checkIfExist);
        if(resultSet.next()){
            statement.executeUpdate("update interacted_seller set comment='"+comment+"' where purchaser_id="+userId+" and seller_id="+sellerId);
        }else {
            statement.executeUpdate("insert into interacted_seller(purchaser_id,seller_id,comment,isFavorite) values(" + userId + "," + sellerId + ",'" + comment + "','false')");
        }
    }

    //买家查看自己的收藏
    public ArrayList<Integer> getFavoriteDish(int userId) throws SQLException {
        String selectFavoriteDish = "select dish_id from interacted_dish where purchaser_id="+userId+" and isFavorite='true'";
        ResultSet resultSet = statement.executeQuery(selectFavoriteDish);
        ArrayList<Integer> dishIds = new ArrayList<>();
        while (resultSet.next()){
            dishIds.add(resultSet.getInt("dish_id"));
        }
        return dishIds;
    }

    //买家或者商家查看消息
    public ArrayList<Message> getMessages(int userId) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from message where receiver_id="+userId);
        ResultSet resultSet = statement.executeQuery(sb.toString());
        ArrayList<Message> messages = new ArrayList<>();
        while (resultSet.next()){
            Message message = new Message();
            message.setSender_id(resultSet.getInt("sender_id"));
            message.setReceiver_id(resultSet.getInt("receiver_id"));
            message.setMessage(resultSet.getString("message"));
            message.setTime(resultSet.getTimestamp("time"));
            messages.add(message);
        }
        return messages;
    }

    //买家购买菜品
    public void purchaseDish(int purchaserId, int dishId) throws SQLException {
        statement.executeUpdate("insert into orderOverview(purchaser_id, order_time, dish_status) values (" + purchaserId + ", now(), \"已支付\");");
        statement.executeUpdate("insert into order_dish values((select max(order_id) from orderOverview where purchaser_id = " + purchaserId + ")," + dishId + ", null, null);");
    }
    //卖家更新菜品状态
    public void updateDishStatus(int orderId, String dishStatus) throws SQLException {
        statement.executeUpdate("update orderOverview set dish_status='" + dishStatus + "' where order_id=" + orderId +";");
    }



//    public static void main(String[] args) throws SQLException {
//        SQLLoader sqlLoader = new SQLLoader();
//        sqlLoader.connect();
//        sqlLoader.search(1234,"root");
//    }
}
