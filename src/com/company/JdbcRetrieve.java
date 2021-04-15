package com.company;
import org.json.JSONObject;
import java.sql.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcRetrieve {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/bo1?useSSL=false";
        String user = "root";
        String password = "";
        String query = "SELECT * FROM `product sales`";
        Sender me = new Sender();

        try (Connection con= DriverManager.getConnection(url , user, password);
            PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()){
            while (rs.next()){
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("Date",rs.getString(1));
                data.put("Region",rs.getString(2));
                data.put("Product",rs.getString(3));
                data.put("Qty",rs.getString(4));
                data.put("Cost",rs.getString(5));
                data.put("Amt",rs.getString(6));
                data.put("Taxe",rs.getString(7));
                data.put("Total",rs.getString(8));
                JSONObject obj = new JSONObject(data);
                System.out.println(obj);
                me.send(obj);
            }
        }
        catch (SQLException ex){
            Logger lgr= Logger.getLogger(JdbcRetrieve.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
