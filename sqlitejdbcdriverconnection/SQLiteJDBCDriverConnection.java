package sqlitejdbcdriverconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLiteJDBCDriverConnection {

    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:chinook.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:tests.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS clase ("
                + "	id integer PRIMARY KEY,"
                + "	name text NOT NULL,"
                + "	secondname text NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static class InsertApp {

        private Connection connecta() {
            // SQLite connection string
            String url = "jdbc:sqlite:tests.db";
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return conn;
        }

        public void insert(int id, String name, String secondname) {
            String sql = "INSERT INTO clase(id,name,secondname) VALUES(?,?,?)";

            try (Connection conn = this.connecta();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, name);
                pstmt.setString(3, secondname);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static class Querying {

        private Connection connect() {
            // SQLite connection string
            String url = "jdbc:sqlite:tests.db";
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return conn;
        }

        /**
         * select all rows in the warehouses table
         */
        public void selectAll() {
            String sql = "SELECT id, name, secondname FROM clase";

            try (Connection conn = this.connect();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {

                // loop through the result set
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + "\t"
                            + rs.getString("name") + "\t"
                            + rs.getString("secondname"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public class UpdateApp {

        /**
         * Connect to the test.db database
         *
         * @return the Connection object
         */
        private Connection connect() {
            // SQLite connection string
            String url = "jdbc:sqlite:tests.db";
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return conn;
        }

        /**
         * Update data of a warehouse specified by the id
         *
         * @param id
         * @param name name of the warehouse
         * @param capacity capacity of the warehouse
         */
        public void update(int id, String name, String secondname) {
            String sql = "UPDATE clase SET name = ? , "
                    + "capacity = ? "
                    + "WHERE id = ?";

            try (Connection conn = this.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // set the corresponding param
                pstmt.setString(1, name);
                pstmt.setString(2, secondname);
                pstmt.setInt(3, id);
                // update 
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static void main(String[] args) {
        connect();
        createNewTable();
        InsertApp app = new InsertApp();
        // insert three new rows
        app.insert(6468, "Iago", "Gonzalez");
        app.insert(6485, "Cesar", "Romero");
        Querying quest = new Querying();

        quest.connect();
        quest.selectAll();

    }
}
