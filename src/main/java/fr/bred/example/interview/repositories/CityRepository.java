package fr.bred.example.interview.repositories;

import fr.bred.example.interview.entities.City;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CityRepository  {

    List<City> findByNameContainingIgnoreCaseAndZipCodeContainingIgnoreCase(String name, String zipCode, Pageable pageable);

    List<City> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<City> findByZipCodeContainingIgnoreCase(String zipCode, Pageable pageable);

    List<City> findAll(Pageable pageable);

    List<City> findAll() ;
}

