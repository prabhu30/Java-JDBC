// Java Database Connectivity

import java.sql.*;

/**
 * 1. Import package --> java.sql
 * 2. Load and register the driver --> com.mysql.jdbc.Driver
 * 3. Create connection
 * 4. Create a statement
 * 5. Execute the query
 * 6. Process the results
 * 7. Close connection
 */

public class GetTableRecords {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        String url = "jdbc:mysql://localhost:3306/prabhudb";
        String username = "root";
        String password = "";
        String query = "select * from imdb";

        // Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String details = rs.getString("id") + " : " + rs.getString("hero_name") + " : " + rs.getString("movie") + " : " + rs.getString("age");
            System.out.println(details);
        }

        st.close();
        con.close();
    }
}
