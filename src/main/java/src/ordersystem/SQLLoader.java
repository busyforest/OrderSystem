package src.ordersystem;
import com.mysql.cj.jdbc.result.ResultSetImpl;
import com.mysql.jdbc.Driver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
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
        // 根据给定的 url 连接数据库
        connect = driver.connect(url, properties);
        statement = connect.createStatement();
        init();
        insert();
    }
    public  void init(){
        run("src/main/SQLStatements/init.sql");
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
    public  boolean  search(int id, String passwd) throws SQLException {
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

//    public static void main(String[] args) throws SQLException {
//        SQLLoader sqlLoader = new SQLLoader();
//        sqlLoader.connect();
//        sqlLoader.search(1234,"root");
//    }
}
