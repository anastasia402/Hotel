package Service;

import Domain.Hotel;
import Domain.Room;
import Repository.HotelRepository;

import java.util.Collection;

public class HotelService implements IService<Hotel, Integer>{

    private HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public void add(Hotel elem) {
        this.hotelRepository.add(elem);
    }

    @Override
    public void delete(Hotel elem) {
        this.hotelRepository.delete(elem);
    }

    @Override
    public void update(Hotel elem, Integer id) {
        this.hotelRepository.update(elem, id);
    }

    @Override
    public Collection<Hotel> getAll() {
        return this.hotelRepository.getAll();
    }

    public void addRoom(Room room, int hotelId){
        this.hotelRepository.addRoom(room, hotelId);
    }

    public Collection<Room> getAllRoomsForHotel(int hotelId){
        return this.hotelRepository.getAllRooms(hotelId);
    }

    public Hotel findHotelById(int id){
        return this.hotelRepository.findById(id);
    }

    public Room findRoomByHotelId(int roomId, int hotelId){
        return this.hotelRepository.findByRoomNumber(roomId, hotelId);
    }

    public double getHotelLatitude(int id){
        return findHotelById(id).getLatitude();
    }

    public double getHotelLongitude(int id){
        return findHotelById(id).getLogitude();
    }
}
