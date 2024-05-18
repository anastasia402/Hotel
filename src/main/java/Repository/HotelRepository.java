package Repository;

import Domain.Entity;
import Domain.Hotel;
import Domain.Room;
import com.fasterxml.jackson.core.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class HotelRepository implements IRepository<Hotel, Integer>{
    private String fileName;
    private JSONParser parser;
    private JDBCUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public HotelRepository(String fileName) {
        this.fileName = fileName;
        this.parser = new JSONParser();
        logger.info("Initializing HotelRepository with properties: {} ",fileName);
        dbUtils=new JDBCUtils();
    }

    @Override
    public void add(Hotel elem) {
        logger.traceEntry("saving task {}", elem);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("insert into Hotels(id, name, latitude, longitude) values(?, ?, ?, ?)")){
            ps.setInt(1, elem.getId());
            ps.setString(2, elem.getName());
            ps.setDouble(3, elem.getLatitude());
            ps.setDouble(4, elem.getLogitude());
            int result = ps.executeUpdate();

            for (Room room : elem.getRooms()){
                addRoom(room, elem.getId());
            }

            logger.trace("Saved {} instaces", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error adding hotel "+e);
        }
        logger.traceExit();
    }

    public void addRoom(Room room, int idHotel) {
        logger.traceEntry("saving task {}", room);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("insert into Rooms(roomNumber, type, price, isAvailable, hotel) values(?, ?, ?, ?, ?)")){
            ps.setInt(1, room.getId());
            ps.setInt(2, room.getType());
            ps.setInt(3, room.getPrice());
            ps.setBoolean(4, room.isAvailable());
            ps.setInt(5, idHotel);
            int result = ps.executeUpdate();

            logger.trace("Saved {} instaces", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error adding room "+e);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Hotel elem) {

    }

    public void populate(Collection<Hotel> hotels){
        for (Hotel hotel : getAllJSON()){
            add(hotel);
        }
    }

    @Override
    public void update(Hotel elem, Integer id) {

    }


    @Override
    public Collection<Hotel> getAll() {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        Collection<Hotel> hotels = new ArrayList<>();
        try(PreparedStatement ps = conn.prepareStatement("select * from Hotels")) {
            try (ResultSet result = ps.executeQuery()){
                while (result.next()){
                    int id = result.getInt("id");
                    String  nume = result.getString("name");
                    double lat = result.getDouble("latitude");
                    double longit = result.getDouble("longitude");
                    Hotel hotel = new Hotel(id, nume, lat, longit, getAllRooms(id));

                    hotels.add(hotel);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB 5 "+e);
        }
        logger.traceExit(hotels);
        return hotels;
    }

    public Collection<Room> getAllRooms(int id) {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        Collection<Room> rooms = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Rooms WHERE hotel = ?")) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int roomNumber = rs.getInt("roomNumber");
                    int type = rs.getInt("type");
                    int price = rs.getInt("price");
                    boolean isAvailable = rs.getBoolean("isAvailable");

                    Room room = new Room(roomNumber, type, price, isAvailable);
                    rooms.add(room);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rooms;
    }

    public Collection<Hotel> getAllJSON() {
        try {
            Collection<Hotel> hotelsCollection = new ArrayList<>();
            Object object = this.parser.parse(new FileReader(this.fileName));
            JSONArray hotelsArray = (JSONArray) object;
            for (Object obj : hotelsArray) {
                JSONObject hotelJson = (JSONObject) obj;
                hotelsCollection.add(parseHotel(hotelJson));
            }
            return hotelsCollection;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Hotel parseHotel(JSONObject jsonObject) {
        int id = ((Long) jsonObject.get("id")).intValue();
        String name = (String) jsonObject.get("name");
        double latitude = (Double) jsonObject.get("latitude");
        double longitude = (Double) jsonObject.get("longitude");

        JSONArray roomsArray = (JSONArray) jsonObject.get("rooms");
        Collection<Room> hotelsRooms = new ArrayList<>();
        for (Object obj : roomsArray) {
            JSONObject roomJson = (JSONObject) obj;
            Room room = parseRoom(roomJson);
            hotelsRooms.add(room);
        }

        return new Hotel(id, name, latitude, longitude, hotelsRooms);
    }

    private Room parseRoom(JSONObject jsonObject) {
        int id = ((Long) jsonObject.get("roomNumber")).intValue();
        int type = ((Long) jsonObject.get("type")).intValue();
        int price = ((Long) jsonObject.get("price")).intValue();
        boolean available = (boolean) jsonObject.get("isAvailable");

        return new Room(id, type, price, available);
    }

    public Hotel findById(int hoteIld) {
        for (Hotel hotel : getAll()){
            if (hotel.getId().equals(hoteIld))
                return hotel;
        }
        return null;
    }

    public Room findByRoomNumber(int roomNumber, int hotelId) {
        for (Room room : getAllRooms(hotelId)){
            if (room.getId().equals(roomNumber))
                return room;
        }
        return null;
    }
}
