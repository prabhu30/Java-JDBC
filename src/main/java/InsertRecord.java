import java.sql.*;
import java.util.Scanner;

public class InsertRecord {
    public static void main(String[] args) throws SQLException {

        String url = "jdbc:mysql://localhost:3306/prabhudb";
        String username = "root";
        String password = "Anuprakash123@";
        String getMoviesQuery = "select * from movies";

        Connection con = DriverManager.getConnection(url, username, password);

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Movie ID : ");
        int inputMovieId = sc.nextInt();

        System.out.println("Enter Movie Name : ");
        String inputMovieName = sc.next();

        System.out.println("Enter Movie Rating : ");
        Double inputMovieRating = sc.nextDouble();

        String insertQuery = "insert into movies values (?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(insertQuery);
        pst.setInt(1, inputMovieId);
        pst.setString(2, inputMovieName);
        pst.setDouble(3, inputMovieRating);
        int rowsAffected = pst.executeUpdate();
        System.out.println(rowsAffected + " row/s affected");

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(getMoviesQuery);
        while (rs.next()) {
            String details = rs.getString("id") + " : " + rs.getString("name") + " : " + rs.getString("rating");
            System.out.println(details);
        }
    }
}
