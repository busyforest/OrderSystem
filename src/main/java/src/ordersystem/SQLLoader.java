package src.ordersystem;
import com.mysql.jdbc.Driver;
import src.ordersystem.entity.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
        properties.setProperty("password", "735568");  // 密码（填入自己用户名对应的密码）
        properties.put("allowMultiQueries", "true");  // 允许多条 SQL 语句执行
        // 根据给定的 url 连接数据库
        connect = driver.connect(url, properties);
        statement = connect.createStatement();
//        init();
//        insert();
    }
    public  void init(){
        run("src/main/SQLStatements/init.sql");
        try {
            statement.executeUpdate("CREATE TRIGGER IF NOT EXISTS SEND_MESSAGE_AFTER_PURCHASE\n" +
                    "AFTER INSERT ON orderOverview\n" +
                    "FOR EACH ROW\n" +
                    "BEGIN\n" +
                    "    INSERT INTO message VALUES (NEW.purchaser_id, 1, '有新的订单需要处理', NOW());\n" +
                    "    INSERT INTO message VALUES (1, NEW.purchaser_id, '您的订单已提交，请耐心等待', NOW());\n"+
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
                    "    UPDATE message set sender_id = (select seller_id from dish where dish_id = NEW.dish_id) where message.message_time=(select order_time from orderOverview where order_id = NEW.order_id) and message.receiver_id = (select purchaser_id from orderOverview where order_id = NEW.order_id);\n"+
                    "END;");
            statement.executeUpdate("CREATE TRIGGER IF NOT EXISTS UPDATE_DISH_AVG_MARK\n" +
                    "AFTER UPDATE ON order_dish\n" +
                    "FOR EACH ROW\n" +
                    "BEGIN\n" +
                    "    UPDATE dish set avg_mark = (select AVG(mark) from order_dish where dish_id = NEW.dish_id) where dish_id = NEW.dish_id;\n" +
                    "END;");
            statement.executeUpdate("CREATE TRIGGER IF NOT EXISTS UPDATE_DISH_SALES_VOLUME\n"+
                    "AFTER INSERT ON order_dish\n" +
                    "FOR EACH ROW\n" +
                    "BEGIN\n" +
                    "    IF NEW.purchase_method = '在线点餐' THEN\n" +
                    "       UPDATE dish set online_sales_volume = online_sales_volume + NEW.quantity where dish_id = NEW.dish_id;\n" +
                    "    ELSEIF NEW.purchase_method = '排队点餐' THEN\n" +
                    "       UPDATE dish set offline_sales_volume = offline_sales_volume + NEW.quantity where dish_id = NEW.dish_id;\n" +
                    "    END IF;\n" +
                    "END;");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void insert() {
        run("src/main/SQLStatements/insert.sql");
        run("src/main/SQLStatements/insert_dish.sql");
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
                    statement.execute(sql);
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
            purchaser.setName(resultSet.getString("name"));
            purchaser.setGender(resultSet.getString("gender").charAt(0));
            purchaser.setStudentIDOrWorkID(resultSet.getInt("studentIDOrWorkID"));
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
            seller.setName(resultSet2.getString("name"));
            seller.setAddress(resultSet2.getString("address"));
            seller.setBriefInfomation(resultSet2.getString("brief_information"));
            seller.setFeaturedDish(resultSet2.getString("featured_dish"));
            seller.setAvg_mark(resultSet2.getFloat("avg_mark"));
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
            administrator.setName(resultSet3.getString("name"));
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
            seller.setName(resultSet.getString("name"));
            seller.setAddress(resultSet.getString("address"));
            seller.setBriefInfomation(resultSet.getString("brief_information"));
            seller.setFeaturedDish(resultSet.getString("featured_dish"));
            seller.setAvg_mark(resultSet.getFloat("avg_mark"));
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
    public void commentDish(int orderId,int dishId,String comment,int mark) throws SQLException {
        statement.executeUpdate("update order_dish set mark="+mark+", comment='"+comment+"' where order_id="+orderId+" and dish_id="+dishId);
    }
    //买家收藏菜品
    public void favoriteDish(int userId,int dishId) throws SQLException {
        String checkIfExist = "select * from interact_dish where purchaser_id="+userId+" and dish_id="+dishId;
        ResultSet resultSet = statement.executeQuery(checkIfExist);
        if(resultSet.next()){
            boolean isFavorite = resultSet.getBoolean("isFavorite");
            statement.executeUpdate("update interact_dish set isFavorite = " +!isFavorite + " where purchaser_id="+userId+" and dish_id="+dishId);
        }else {
            statement.executeUpdate("insert into interact_dish(purchaser_id,dish_id,isFavorite) values(" + userId + "," + dishId + ",true)");
        }
    }
    // 检查是否已经收藏过了
    public boolean checkFavoriteDish(int userId,int dishId) throws SQLException {
        String checkIfExist = "select * from interact_dish where purchaser_id="+userId+" and dish_id="+dishId+ " and isFavorite = true";
        ResultSet resultSet = statement.executeQuery(checkIfExist);
        if(resultSet.next()){
            return true;
        }else {
            return false;
        }
    }
    //买家收藏商家
    public void favoriteSeller(int userId,int sellerId) throws SQLException {
        String checkIfExist = "select * from interact_seller where purchaser_id="+userId+" and seller_id="+sellerId;
        ResultSet resultSet = statement.executeQuery(checkIfExist);
        if(resultSet.next()){
            statement.executeUpdate("update interact_seller set isFavorite='true' where purchaser_id="+userId+" and seller_id="+sellerId);
        }else {
            statement.executeUpdate("insert into interact_seller(purchaser_id,seller_id,comment,isFavorite) values(" + userId + "," + sellerId + ",'','true')");
        }
    }
    //买家评论商家
    public void commentSeller(int userId,int sellerId,String comment) throws SQLException {
        String checkIfExist = "select * from interact_seller where purchaser_id="+userId+" and seller_id="+sellerId;
        ResultSet resultSet = statement.executeQuery(checkIfExist);
        if(resultSet.next()){
            statement.executeUpdate("update interact_seller set comment='"+comment+"' where purchaser_id="+userId+" and seller_id="+sellerId);
        }else {
            statement.executeUpdate("insert into interact_seller(purchaser_id,seller_id,comment,isFavorite) values(" + userId + "," + sellerId + ",'" + comment + "','false')");
        }
    }

    //买家查看自己的收藏
    public ArrayList<Dish> getFavoriteDish(int userId) throws SQLException {
        ArrayList<Integer> dishIds = new ArrayList<>();
        ArrayList<Dish> dishes = new ArrayList<>();
        String selectFavoriteDish = "select dish_id from interact_dish where purchaser_id="+userId+" and isFavorite = true";
        ResultSet resultSet = statement.executeQuery(selectFavoriteDish);
        while (resultSet.next()){
            dishIds.add(resultSet.getInt("dish_id"));
        }
        Statement statement1 = connect.createStatement();
        for(Integer id: dishIds){
            String getDish = "select * from dish where dish_id = " + id;
            ResultSet resultSet1 =  statement1.executeQuery(getDish);
            while (resultSet1.next()) {
                Dish dish = new Dish();
                dish.setDishId(resultSet1.getInt("dish_id"));
                dish.setSellerId(resultSet1.getInt("seller_id"));
                dish.setDishName(resultSet1.getString("name"));
                dish.setDishPrice(resultSet1.getInt("price"));
                dish.setDishPictureUrl(resultSet1.getString("picture"));
                dish.setDishDescription(resultSet1.getString("description"));
                dish.setIngredients(resultSet1.getString("ingredients"));
                dish.setNutritionInfo(resultSet1.getString("nutrition_information"));
                dish.setPossibleAllergens(resultSet1.getString("possible_allergens"));
                dish.setAvg_mark(resultSet1.getFloat("avg_mark"));
                dishes.add(dish);
            }
        }
        return dishes;
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
    public void purchaseDishList(int purchaserId, ArrayList<Dish> dishes, String purchaseMethod) throws SQLException {
        HashMap<Dish, Integer> dishNum = getDishNum(dishes);
        statement.executeUpdate("insert into orderOverview(purchaser_id, order_time, dish_status) values (" + purchaserId + ", now(), \"已支付\");");
        for(HashMap.Entry <Dish, Integer>  entry : dishNum.entrySet()){
            statement.executeUpdate("insert into order_dish values((select max(order_id) from orderOverview where purchaser_id = " + purchaserId + ")," + entry.getKey().getDishId() +","+entry.getValue()+",'"+purchaseMethod+"', null, null);");
        }
    }
    public HashMap getDishNum(ArrayList list){
        HashMap<Dish, Integer> map = new HashMap<>();
        ArrayList newList = new ArrayList();
        Iterator it = list.iterator();
        while(it.hasNext()){
            Object obj = it.next();
            if(!newList.contains(obj)){
                newList.add(obj);
            }
        }
        for(Object obj : newList){
            int count = 0;
            for(Object obj1 : list){
                if(obj.equals(obj1)){
                    count++;
                }
            }
            map.put((Dish)obj, count);
        }
        return map;
    }

    //没搜索的时候显示所有卖家
    public ArrayList<Seller> getAllSellers() throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from seller");
        ResultSet resultSet = statement.executeQuery(sb.toString());
        ArrayList<Seller> sellers = new ArrayList<>();
        while (resultSet.next()){
            Seller seller = new Seller();
            seller.setId(resultSet.getInt("id"));
            seller.setName(resultSet.getString("name"));
            seller.setAddress(resultSet.getString("address"));
            seller.setBriefInfomation(resultSet.getString("brief_information"));
            seller.setFeaturedDish(resultSet.getString("featured_dish"));
            seller.setAvg_mark(resultSet.getFloat("avg_mark"));
            sellers.add(seller);
        }
        return sellers;
    }
    //获得一个卖家的所有商品用于显示
    public ArrayList<Dish> getDishesInSeller(Seller seller) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from dish where seller_id = " + seller.getId());
        ResultSet resultSet = statement.executeQuery(sb.toString());
        ArrayList<Dish> dishes = new ArrayList<>();
        while (resultSet.next()) {
            Dish dish = new Dish();
            dish.setDishId(resultSet.getInt("dish_id"));
            dish.setSellerId(resultSet.getInt("seller_id"));
            dish.setDishName(resultSet.getString("name"));
            dish.setDishPrice(resultSet.getInt("price"));
            dish.setDishPictureUrl(resultSet.getString("picture"));
            dish.setDishDescription(resultSet.getString("description"));
            dish.setIngredients(resultSet.getString("ingredients"));
            dish.setNutritionInfo(resultSet.getString("nutrition_information"));
            dish.setPossibleAllergens(resultSet.getString("possible_allergens"));
            dish.setAvg_mark(resultSet.getFloat("avg_mark"));
            dishes.add(dish);
        }
        return dishes;
    }
    // 买家查看历史订单
    public ArrayList<OrderOverview> getHistory(Purchaser purchaser) throws SQLException {
        StringBuilder sb = new StringBuilder();
        ArrayList<OrderOverview> orderList = new ArrayList<>();
        sb.append("select * from orderoverview where purchaser_id ="+ purchaser.getId());
        ResultSet resultSet = statement.executeQuery(sb.toString());
        while(resultSet.next()){
            OrderOverview orderOverview = new OrderOverview();
            orderOverview.setOrderID(resultSet.getInt("order_id"));
            orderOverview.setPurChaserID(resultSet.getInt("purchaser_id"));
            orderOverview.setOrderTime(resultSet.getString("order_time"));
            orderOverview.setDishStatus(resultSet.getString("dish_status"));
            orderList.add(orderOverview);
        }
        return orderList;
    }
    // 从一个订单中获取所有的菜
    public ArrayList<Dish> getDishesInOrderOverview(OrderOverview orderOverview) throws SQLException {
        StringBuilder sb = new StringBuilder();
        ArrayList<Dish> dishes = new ArrayList<>();
        ArrayList<Integer> dishIds = new ArrayList<>();
        sb.append("select * from order_dish where order_id = " + orderOverview.getOrderID());
        ResultSet resultSet = statement.executeQuery(sb.toString());
        while (resultSet.next()){
            dishIds.add(resultSet.getInt("dish_id"));
        }
        Statement statement1 = connect.createStatement();
        for(Integer id: dishIds){
            String getDish = "select * from dish where dish_id = " + id;
            ResultSet resultSet1 =  statement1.executeQuery(getDish);
            while (resultSet1.next()) {
                Dish dish = new Dish();
                dish.setDishId(resultSet1.getInt("dish_id"));
                dish.setSellerId(resultSet1.getInt("seller_id"));
                dish.setDishName(resultSet1.getString("name"));
                dish.setDishPrice(resultSet1.getInt("price"));
                dish.setDishPictureUrl(resultSet1.getString("picture"));
                dish.setDishDescription(resultSet1.getString("description"));
                dish.setIngredients(resultSet1.getString("ingredients"));
                dish.setNutritionInfo(resultSet1.getString("nutrition_information"));
                dish.setPossibleAllergens(resultSet1.getString("possible_allergens"));
                dish.setAvg_mark(resultSet1.getFloat("avg_mark"));
                dishes.add(dish);
            }
        }
        return dishes;
    }
    // 根据order_id 获得 order_dish
    public ArrayList<OrderDish> getOrderDishByOrderID(int orderID) throws SQLException {
        ArrayList<OrderDish> orderDishes = new ArrayList<>();
        String string = "select * from order_dish where order_id = "+ orderID;
        ResultSet resultSet = statement.executeQuery(string);
        while(resultSet.next()){
            OrderDish orderDish = new OrderDish();
            orderDish.setOrderID(orderID);
            orderDish.setDishID(resultSet.getInt("dish_id"));
            orderDish.setQuantity(resultSet.getInt("quantity"));
            orderDish.setPurChaseMethod(resultSet.getString("purchase_method"));
            orderDish.setComment(resultSet.getString("comment"));
            orderDish.setMark(resultSet.getDouble("mark"));
            orderDishes.add(orderDish);
        }
        return orderDishes;
    }
    // 根据order_id 和 dish_id 获得Order_dish
    public OrderDish getOrderDishByOrderIdAndDishId(int orderID, int dishID) throws SQLException {
        String  s = "select * from order_dish where order_id = " + orderID +" and dish_id = " + dishID;
        ResultSet resultSet  = statement.executeQuery(s);
        if(resultSet.next()){
            OrderDish orderDish = new OrderDish();
            orderDish.setOrderID(orderID);
            orderDish.setDishID(resultSet.getInt("dish_id"));
            orderDish.setQuantity(resultSet.getInt("quantity"));
            orderDish.setPurChaseMethod(resultSet.getString("purchase_method"));
            orderDish.setComment(resultSet.getString("comment"));
            orderDish.setMark(resultSet.getDouble("mark"));
            return orderDish;
        }
        return  null;
    }
    // 根据id获得名字
    // TODO:如果表结构改了这里要相应调整
    public String getName(int id) throws SQLException {
        String name =null;
        StringBuilder sb = new StringBuilder();
        sb.append("select name from user where id =" + id);
        ResultSet resultSet = statement.executeQuery(sb.toString());
        if(resultSet.next()){
            name = resultSet.getString("name");
        }
        return name;
    }
    //在一个商家中搜索菜品
    public ArrayList<Dish> searchDishes(String name, Seller seller) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from dish where name like '%"+name+"%'"+ " and seller_id = "+ seller.getId());
        ResultSet resultSet = statement.executeQuery(sb.toString());
        ArrayList<Dish> dishes = new ArrayList<>();
        while(resultSet.next()){
            Dish dish = new Dish();
            dish.setDishId(resultSet.getInt("dish_id"));
            dish.setSellerId(resultSet.getInt("seller_id"));
            dish.setDishName(resultSet.getString("name"));
            dish.setDishPrice(resultSet.getInt("price"));
            dish.setDishPictureUrl(resultSet.getString("picture"));
            dish.setDishDescription(resultSet.getString("description"));
            dish.setIngredients(resultSet.getString("ingredients"));
            dish.setNutritionInfo(resultSet.getString("nutrition_information"));
            dish.setPossibleAllergens(resultSet.getString("possible_allergens"));
            dish.setAvg_mark(resultSet.getFloat("avg_mark"));
            dishes.add(dish);
        }
        return dishes;
    }
    //商家添加菜品
    public void addDish(int sellerId,String dishName,String dishDescription,double dishPrice,String dishPictureUrl,String ingredients,String nutritionInfo,String possibleAllergens) throws SQLException {
        statement.executeUpdate("insert into dish (seller_id, name, description, price, picture, ingredients, nutrition_information, possible_allergens) values("+sellerId+",'"+dishName+"','"+dishDescription+"',"+dishPrice+",'"+dishPictureUrl+"','"+ingredients+"','"+nutritionInfo+"','"+possibleAllergens+"')");
    }
    //商家删除菜品
    public void deleteDish(int dishId) throws SQLException {
        statement.executeUpdate("delete from dish where dish_id="+dishId);
    }
    //商家修改菜品信息
    public void updateDish(int dishId,String dishName,String dishDescription,double dishPrice,String dishPictureUrl,String ingredients,String nutritionInfo,String possibleAllergens) throws SQLException {
        statement.executeUpdate("update dish set name='"+dishName+"',description='"+dishDescription+"',price="+dishPrice+",picture='"+dishPictureUrl+"',ingredients='"+ingredients+"',nutrition_information='"+nutritionInfo+"',possible_allergens='"+possibleAllergens+"' where dish_id="+dishId);
    }
    //商家更新菜品状态
    public void updateDishStatus(int orderId, String dishStatus) throws SQLException {
        statement.executeUpdate("update orderOverview set dish_status='" + dishStatus + "' where order_id=" + orderId +";");
    }
    //查询某个商户某个菜品的收藏量，显示在菜品的具体信息里面
    public int getFavoriteNum(int dishId) throws SQLException {
        String selectFavoriteNum = "select count(*) from interact_dish where dish_id="+dishId+" and isFavorite='true'";
        ResultSet resultSet = statement.executeQuery(selectFavoriteNum);
        if(resultSet.next()){
            return resultSet.getInt(1);
        }else {
            return 0;
        }
    }



    public static void main(String[] args) throws SQLException {
        SQLLoader sqlLoader = new SQLLoader();
        ArrayList<Seller> sellers = new ArrayList<>();
        sqlLoader.connect();
        sellers = sqlLoader.getAllSellers();
        System.out.println(sqlLoader.getDishesInSeller(sellers.get(2)));
    }
}
