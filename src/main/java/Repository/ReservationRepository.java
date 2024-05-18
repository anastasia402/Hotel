package Repository;

import Domain.Hotel;
import Domain.Rezervare;
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

public class ReservationRepository implements IRepository<Rezervare, Integer>{
    private JDBCUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private HotelRepository hotelRepo;
    private UserRepository userRepository;

    public ReservationRepository() {
        this.dbUtils = new JDBCUtils();
    }

    public void setHotelUserRepo(HotelRepository hotelRepo, UserRepository userRepository) {
        this.hotelRepo = hotelRepo;
        this.userRepository = userRepository;
    }

    @Override
    public void add(Rezervare elem) {
        logger.traceEntry("saving task {}", elem);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("insert into Reservations(user, startDate, endDate, hotel, room) values(?, ?, ?, ?, ?)")){
            //ps.setInt(1, elem.getId());
            ps.setInt(1, elem.getUser().getId());
            ps.setString(2, elem.getStartDate());
            ps.setString(3, elem.getEndDate());
            ps.setInt(4, elem.getHotel().getId());
            ps.setInt(5, elem.getRoom().getId());
            int result = ps.executeUpdate();


            logger.trace("Saved {} instaces", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error adding reservation "+e);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Rezervare elem) {
        logger.traceEntry("saving task {}", elem);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("delete from Reservations where id=?")){
            ps.setInt(1, elem.getId());

            int result = ps.executeUpdate();

            logger.trace("Saved {} instaces", result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error deleting reservation "+e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Rezervare elem, Integer id) {
        logger.traceEntry("saving task {}", elem);
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement ps = conn.prepareStatement("update Reservations set room = ? where id = "+id)) {

            ps.setInt(1, elem.getRoom().getId());

            int result = ps.executeUpdate();
            logger.trace("Saved {} instaces", result);

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB 4 "+e);
        }
        logger.traceExit();
    }

    public void leaveFeedback(Rezervare elem, Integer id) {
        logger.traceEntry("saving task {}", elem);
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement ps = conn.prepareStatement("update Reservations set review = ? where id = "+id)) {

            ps.setString(1, elem.getReview());

            int result = ps.executeUpdate();
            logger.trace("Saved {} instaces", result);

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB 4 "+e);
        }
        logger.traceExit();
    }

    public Rezervare findById(int id){
        for (Rezervare rezervare : getAll()){
            if (rezervare.getId().equals(id))
                return rezervare;
        }
        return null;
    }

    @Override
    public Collection<Rezervare> getAll() {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        Collection<Rezervare> reservations = new ArrayList<>();
        String review = "";
        try(PreparedStatement ps = conn.prepareStatement("select * from Reservations")) {
            try (ResultSet result = ps.executeQuery()){
                while (result.next()){
                    int id = result.getInt("id");
                    int userId = result.getInt("user");
                    String start = result.getString("startDate");
                    String end = result.getString("endDate");
                    int hoteIld = result.getInt("hotel");
                    int roomNumber = result.getInt("room");
                    if (result.getString("review") != null)
                        review = result.getString("review");

                    Hotel hotel = hotelRepo.findById(hoteIld);
                    Room room = hotelRepo.findByRoomNumber(roomNumber, hoteIld);
                    User user = userRepository.findById(userId);

                    Rezervare rezervare = new Rezervare(id, user, start, end, hotel, room, review);
                    reservations.add(rezervare);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB 5 "+e);
        }
        logger.traceExit(reservations);
        return reservations;
    }

    public void addReview(int id, String review){
        logger.traceEntry("adding review task {}");
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement ps = conn.prepareStatement("update Reservations set review = ? where id = "+id)) {

            ps.setString(1, review);

            int result = ps.executeUpdate();
            logger.trace("Saved {} instaces", result);

        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB 4 "+e);
        }
        logger.traceExit();
    }
}
