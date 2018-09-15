package loginfx.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author BURAK
 */
//

public class Database {

    public static final String admin_username = "admin";
    private static final String admin_password = "openit";
    private static final String name = "data.db";
    private Connection connection;
    private Statement stmt;
    
    public boolean isAdmin(String username) throws SQLException {
        String cmd = String.format("SELECT * FROM users "
                + "WHERE username = '%s' AND status = 'admin'", username);
        ResultSet rs = execQ(cmd);
        return !rs.isClosed();
    } 
    
    public ResultSet getAll() throws SQLException {
        return execQ("SELECT * FROM users");
    }
    
    public void sortTable() throws SQLException {
        exec("CREATE TABLE IF NOT EXISTS sorted_users ("
                + "username TEXT PRIMARY KEY,"
                + "password TEXT,"
                + "status TEXT"
                + ")");
        exec("INSERT INTO sorted_users (username, password, status) "
                + "SELECT username, password, status FROM users "
                + "ORDER BY status");
        exec("DROP TABLE users");
        exec("ALTER TABLE sorted_users RENAME TO users");
    }

    public boolean usernameExists(String username) throws SQLException {
        String cmd = String.format("SELECT * FROM users "
                + "WHERE username = '%s'", username);
        ResultSet rs = execQ(cmd);
        return !rs.isClosed();
    }

    //change a users password
    public void changePassword(String username, String newpassword)
            throws SQLException {
        exec(String.format("UPDATE users "
                + "SET password = '%s'"
                + "WHERE username = '%s'", newpassword, username));
    }

    public boolean adminLogin(String username, String password) throws SQLException {
        String cmd = String.format("SELECT * FROM users "
                + "WHERE username = '%s' AND password = '%s' "
                + "AND status = 'admin'", username, password);
        ResultSet rs = execQ(cmd);
        return !rs.isClosed();
    }

    //given username and password are valid
    public boolean userLogin(String username, String password) throws SQLException {
        String cmd = String.format("SELECT * FROM users "
                + "WHERE username = '%s' AND password = '%s' "
                + "AND status = 'user'", username, password);
        ResultSet rs = execQ(cmd);
        return !rs.isClosed();
    }

    //delete a user
    public void deleteUser(String username) throws SQLException {
        if (username.equals(admin_username)) {
            throw new IllegalArgumentException("Main admin account can't be deleted");
        }
        exec(String.format("DELETE FROM users WHERE username = '%s'", username));
    }

    //add new user
    public void addUser(String username, String password) throws SQLException {
        if (usernameExists(username)) {
            return;
        }
        exec(String.format("INSERT INTO users VALUES ('%s', '%s', 'user')",
                username, password));
    }
    
    //make someone admin
    public void giveAdminStatus(String username) throws SQLException {
        exec(String.format("UPDATE users "
                + "SET status = 'admin' "
                + "WHERE username = '%s'", username));
    }
    
    public void giveUserStatus(String username) throws SQLException {
        exec(String.format("UPDATE users "
                + "SET status = 'user' "
                + "WHERE username = '%s'", username));
    }

    //create users table
    //add admin
    private void init() throws SQLException {
        exec("CREATE TABLE IF NOT EXISTS users ("
                + "username TEXT PRIMARY KEY,"
                + "password TEXT,"
                + "status TEXT"
                + ")");
        addUser(admin_username, admin_password);
        //in case addUser() method doesnt update the password since 
        //username is the same
        changePassword(admin_username, admin_password);
        giveAdminStatus(admin_username);
    }

    //execute query
    private ResultSet execQ(String cmd) throws SQLException {
        return stmt.executeQuery(cmd);
    }

    //execute
    private void exec(String cmd) throws SQLException {
        stmt.execute(cmd);
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    //establish connection and create Statement object
    //create users table
    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + name);
            stmt = connection.createStatement();
            init();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
