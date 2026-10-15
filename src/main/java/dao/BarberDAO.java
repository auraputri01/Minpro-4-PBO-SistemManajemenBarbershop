package dao;

import java.util.ArrayList;
import model.Barber;

/** INTERFACE: kontrak akses data barber. */
public interface BarberDAO {
    ArrayList<Barber> findAll();

    Barber findById(String idBarber);

    void insert(Barber barber);

    void update(Barber barber);

    void delete(String idBarber);

    String generateId();
}