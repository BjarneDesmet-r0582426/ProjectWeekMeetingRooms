package db;

import domain.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RoomsDBSql implements RoomsDB {
    private Properties properties = new Properties();
    private String url = "jdbc:postgresql://databanken.ucll.be:51718/hakkaton?currentSchema=groep9";

    public RoomsDBSql() {
        properties.setProperty("user", "hakkaton_09");
        properties.setProperty("password", "hai4Shaishienaig");
        properties.setProperty("ssl", "true");
        properties.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {
        }
    }


    @Override
    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, properties);
             Statement statement = connection.createStatement()) {

            ResultSet result = statement.executeQuery("SELECT * FROM room");

            while (result.next()) {
                Room room = new Room(
                        result.getString("number"),
                        result.getString("name"),
                        result.getInt("capacity"),
                        result.getInt("plugs")
                );
                rooms.add(room);
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

        return rooms;
    }

    @Override
    public Room getRoom(String roomNumber) {
        Room room = null;

        try (Connection connection = DriverManager.getConnection(url, properties);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM room WHERE number = ?")) {

            statement.setString(1, roomNumber);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                room = new Room(
                        result.getString("number"),
                        result.getString("name"),
                        result.getInt("capacity"),
                        result.getInt("plugs")
                );
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

        return room;
    }
}
