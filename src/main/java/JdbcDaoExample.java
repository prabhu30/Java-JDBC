import java.sql.*;
import java.util.Scanner;

public class JdbcDaoExample {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Scanner sc = new Scanner(System.in);

        MovieDao movie = new MovieDao();
        movie.connectToDB();

        System.out.println("Select an Operation : " + "\n" +
                "1. Get Movies List" + "\n" +
                "2. Get movie by ID" + "\n" +
                "3. Add a movie to the list" + "\n" +
                "4. Remove a movie from the list" + "\n" +
                "\n\n" + "Enter input number : ");
        int option = sc.nextInt();


        if (option == 1) {
            movie.getMovies();
        } else if (option == 2) {
            // Get a movie by ID
            int numOfMovies = movie.moviesCount();
            System.out.println("Total " + numOfMovies + " movies found. Enter id of the movie you want to fetch (1-" + numOfMovies + ") : ");
            int movieId = sc.nextInt();

            if (movieId < 1 || movieId > numOfMovies) {
                System.out.println("Invalid Option. Please try again.");
            } else {
                Movie m1 = movie.getMovieByID(movieId);
                System.out.println(m1.id + " | " + m1.name + " | " + m1.rating);
            }
        } else if (option == 3) {
            // Insert movie into database
            Movie m2 = new Movie();
            System.out.println("Enter name of the movie");
            String movieName = sc.next();
            m2.name = movieName;

            System.out.println("Rate the movie (1-5) : ");
            Double movieRating = sc.nextDouble();
            m2.rating = movieRating;

            movie.addMovie(m2);
        } else if (option == 4) {
            movie.getMovies();

            System.out.println("\n" + "Enter ID of movie to be removed : ");
            int movieId = sc.nextInt();
            movie.removeMovie(movieId);
        } else {
            System.out.println("Invalid Option. Please try again.");
        }
    }
}

class MovieDao {

    String url = "jdbc:mysql://localhost:3306/prabhudb";
    String uname = "root";
    String password = "Anuprakash123@";
    Connection con = null;

    public void connectToDB() throws SQLException {
        // Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(url, uname, password);
    }

    // READ Operation - Read movies from Database
    public void getMovies() throws SQLException {
        String query = "select * from movies";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            System.out.println(rs.getString("id") + " | " + rs.getString("name") + " | " + rs.getString("rating"));
        }
        this.closeStatement(st);
    }

    // READ Operation - Read movie by ID from Database
    public Movie getMovieByID(int movieId) throws SQLException {
        Statement st = con.createStatement();

        String query = "select * from movies where id=" + movieId;
        ResultSet rs = st.executeQuery(query);
        rs.next();
        String movieName = rs.getString("name");
        Double movieRating = rs.getDouble("rating");

        Movie m = new Movie(movieId, movieName, movieRating);

        this.closeStatement(st);
        return m;
    }

    // Helper Function
    public int moviesCount() throws SQLException {
        String countRecordsQuery = "select count(*) from movies";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(countRecordsQuery);
        rs.next();
        int numOfRecords = rs.getInt(1);

        this.closeStatement(st);
        return numOfRecords;
    }

    // CREATE Operation - Add movie into database
    public void addMovie(Movie m) throws SQLException {
        int numOfRecords = this.moviesCount();

        String query = "insert into movies values (?, ?, ?)";

        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, ++numOfRecords);
        pst.setString(2, m.name);
        pst.setDouble(3, m.rating);

        int rows = pst.executeUpdate();
        System.out.println(rows + " row/s affected" + "\n");

        pst.close();

        this.getMovies();
    }

    // DELETE Operation - Delete movie from database
    public void removeMovie(int movieId) throws SQLException {
        String query = "delete from movies where id=?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, movieId);
        int rows = pst.executeUpdate();
        System.out.println("Delete operation successful" + "\n" + rows + " row/s affected.");
    }

    public void closeStatement(Statement st) throws SQLException {
        st.close();
    }
}

class Movie {
    int id;
    String name;
    Double rating;

    public Movie() {
    }

    public Movie(int id, String name, Double rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }
}