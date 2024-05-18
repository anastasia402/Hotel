package Repository;

import Domain.Hotel;
import Domain.Room;
import Domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.JSONParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class UserRepository implements IRepository<User, Integer>{
    private JDBCUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public UserRepository() {
        logger.info("Initializing UserRepository with properties: {} ");
        dbUtils=new JDBCUtils();
    }
    @Override
    public void add(User elem) {
        logger.traceEntry("saving task {}", elem);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("insert into Users(id, name, ip) values(?, ?, ?)")){
            ps.setInt(1, elem.getId());
            ps.setString(2, elem.getNume());
            ps.setString(3, elem.getIp());
            int result = ps.executeUpdate();


            logger.trace("Saved {} instaces", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error adding hotel "+e);
        }
        logger.traceExit();
    }

    @Override
    public void delete(User elem) {

    }

    @Override
    public void update(User elem, Integer id) {

    }

    @Override
    public Collection<User> getAll() {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        Collection<User> users = new ArrayList<>();
        try(PreparedStatement ps = conn.prepareStatement("select * from Users")) {
            try (ResultSet result = ps.executeQuery()){
                while (result.next()){
                    int id = result.getInt("id");
                    String  nume = result.getString("name");
                    String ip = result.getString("ip");
                    User user = new User(id, nume, ip);

                    users.add(user);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB 5 "+e);
        }
        logger.traceExit(users);
        return users;
    }

    public User findById(int userId) {
        for (User user : getAll()){
            if (user.getId().equals(userId))
                return user;
        }
        return null;
    }
}
