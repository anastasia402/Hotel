package Repository;

import Domain.Room;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.JSONParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class RoomRepository implements IRepository<Room, Integer> {
    private JDBCUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public RoomRepository() {
        logger.info("Initializing HotelRepository with properties");
        dbUtils=new JDBCUtils();
    }
    @Override
    public void add(Room room) {
    }

    @Override
    public void delete(Room elem) {

    }

    @Override
    public void update(Room elem, Integer id) {

    }

    @Override
    public Collection<Room> getAll() {
        return null;
    }
}
