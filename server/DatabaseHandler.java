package server;

import java.sql.*;

public class DatabaseHandler {
    private final String URL;
    private final String username;
    private final String password;
    private Connection connection;
    private static final String ADD_USER_REQUEST = "INSERT INTO USERS (login, password) VALUES (?, ?)";
    private static final String UPDATE_FLAT_REQUEST = "UPDATE FLATS SET name = ?, coordX = ?, coordY = ?, creationDate = ?, area = ?, numberOfRooms = ?, livingSpace = ?, view = ?, transport = ?, house_name = ?, house_year = ?, house_numberOfFlatsOnFloor = ? WHERE id = ?";
    private static final String ADD_NEW_FLAT_REQUEST = "INSERT INTO FLATS (name, coordX, coordY, creationDate, area, numberOfRooms, livingSpace, view, transport, house_name, house_year, house_numberOfFlatsOnFloor, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String CHECK_USER_REQUEST = "SELECT * FROM USERS WHERE login = ?";
    private static final String FLATS_REQUEST = "SELECT * FROM FLATS";
    private static final String USER_BY_ID_REQUEST = "SELECT * FROM FLATS WHERE id = ?";
    private static final String LOGIN_USER_REQUEST = "SELECT * FROM USERS WHERE login = ? AND password = ?";


    public DatabaseHandler(String URL, String username, String password) {
        this.URL = URL;
        this.username = username;
        this.password = password;
    }
    
    public void connect() {
        try {
            connection = DriverManager.getConnection(URL, username, password);
            System.out.println("Установлено соединение с БД.");
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Не удалось выполнить подключение к БД.");
            System.exit(889);
        }
    }

    public boolean register(String username, String password) throws SQLException {
        if (userExists(username)) {
            return false;
        }
        PreparedStatement register = connection.prepareStatement(ADD_USER_REQUEST);
        register.setString(1, username);
        register.setString(2, password);
        register.executeUpdate();
        register.close();
        return true;
    }


    public boolean login(String username, String password) throws SQLException {
        PreparedStatement login = connection.prepareStatement(LOGIN_USER_REQUEST);
        login.setString(1, username);
        login.setString(2, password);
        ResultSet result = login.executeQuery();
        if (result.next()) {
            login.close();
            return true;
        }
        login.close();
        return false;
    }

    public boolean userExists(String username) throws SQLException {
        PreparedStatement check = connection.prepareStatement(CHECK_USER_REQUEST);
        check.setString(1,username);
        ResultSet result = check.executeQuery();
        if (result.next()) {
            check.close();
            return true;
        }
        check.close();
        return false;
    }


}
