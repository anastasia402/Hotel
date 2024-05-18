package Service;

import Domain.Room;
import Domain.User;
import Repository.HotelRepository;
import Repository.UserRepository;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;

public class UserService implements IService<User, Integer> {

    private UserRepository userRepository;
    private static final String dbLocation = "/Users/anastasiacutulima/Downloads/GeoLite2-City_20240514/GeoLite2-City.mmdb";

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(User elem) {
        this.userRepository.add(elem);
    }

    @Override
    public void delete(User elem) {
        this.userRepository.delete(elem);
    }

    @Override
    public void update(User elem, Integer id) {

    }

    @Override
    public Collection<User> getAll() {
        return this.userRepository.getAll();
    }

    public double getLatitudeOfUser(int userId) {
        File database = new File(dbLocation);
        String ip = userRepository.findById(userId).getIp();
        double latitude = 0;
        try {
            DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);
            latitude = response.getLocation().getLatitude();
            System.out.println(response.getCountry());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GeoIp2Exception e) {
            throw new RuntimeException(e);
        }
        return latitude;
    }

        public double getUserLongitude(int userId) {
        File database = new File(dbLocation);
        String ip = userRepository.findById(userId).getIp();
        double longitude = 0;
        try {
            DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);
            longitude = response.getLocation().getLongitude();
            System.out.println(response.getCity());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GeoIp2Exception e) {
            throw new RuntimeException(e);
        }
            return longitude;
    }

    public User findById(int id){
        return this.userRepository.findById(id);
    }
}
