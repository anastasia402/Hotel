package Service;

import Domain.Hotel;
import Domain.Rezervare;
import Domain.Room;
import Repository.ReservationRepository;

import java.time.LocalDate;
import java.util.*;

public class ReservationService implements IService<Rezervare, Integer>{
    private ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;

    }

    @Override
    public void add(Rezervare elem) {
        this.reservationRepository.add(elem);
    }



    @Override
    public void delete(Rezervare elem) {
        this.reservationRepository.delete(elem);
    }

    @Override
    public void update(Rezervare elem, Integer id) {
        this.reservationRepository.update(elem, id);
    }

    @Override
    public Collection<Rezervare> getAll() {
        return this.reservationRepository.getAll();
    }

    public void addReview(int id, String review){
        this.reservationRepository.addReview(id, review);
    }

    public Rezervare findById(int id){
        return this.reservationRepository.findById(id);
    }

    public void leaveFeedback(Rezervare rezervare, int rezervareId){
        this.reservationRepository.leaveFeedback(rezervare, rezervareId);
    }
}
