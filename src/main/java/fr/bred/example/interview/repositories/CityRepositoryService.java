package fr.bred.example.interview.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.bred.example.interview.entities.City;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CityRepositoryService implements CityRepository{

    public static final String DOCUMENTS_MAP_JSON = "cities.json";
    public static List<City> mapCitiesFromJson(String filename) {
        ObjectMapper mapper = new ObjectMapper();

        List<City> cities = null;
        try {
            cities = mapper.readValue(new File(filename), new TypeReference<List<City>>(){});

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public List<City> findByNameContainingIgnoreCaseAndZipCodeContainingIgnoreCase(
            String name, String zipCode, Pageable pageable)  {

        // récupérer la liste des villes mappées à partir du fichier JSON
        List<City> cities = mapCitiesFromJson(DOCUMENTS_MAP_JSON);
        if (cities == null) {
            System.out.println("ERROR: Unable to map cities from JSON");
        }
        // Filtrer les villes qui contiennent le nom spécifié (ignorant la casse) et le code postal spécifié (ignorant la casse)
        Stream<City> cityStream = cities.stream()
                .filter(city -> city.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(city -> city.getZipCode().toLowerCase().contains(zipCode.toLowerCase()));

        // Paginer les résultats
        List<City> result = cityStream
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        return result;
    }
    @Override
    public List<City> findByZipCodeContainingIgnoreCase(String zipCode, Pageable pageable) {

        // récupérer la liste des villes mappées à partir du fichier JSON
        List<City> cities = mapCitiesFromJson(DOCUMENTS_MAP_JSON);
        if (cities == null) {
            System.out.println("ERROR: Unable to map cities from JSON");
        }
        Stream<City> cityStream = cities.stream()
                .filter(city -> city.getName().toLowerCase().contains(zipCode.toLowerCase()));

        // Paginer les résultats
        List<City> result = cityStream
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public List<City> findByNameContainingIgnoreCase(String name, Pageable pageable) {

        // récupérer la liste des villes mappées à partir du fichier JSON
        List<City> cities = mapCitiesFromJson(DOCUMENTS_MAP_JSON);
        if (cities == null) {
            System.out.println("ERROR: Unable to map cities from JSON");
        }
        // Filtrer les villes qui contiennent le nom spécifié
        Stream<City> cityStream = cities.stream()
                .filter(city -> city.getName().toLowerCase().contains(name.toLowerCase()));

        // Paginer les résultats
        List<City> result = cityStream
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        return result;
    }



    @Override
    public List<City> findAll(Pageable pageable)  {

        // récupérer la liste des villes mappées à partir du fichier JSON
        List<City> cities = CityRepositoryService.mapCitiesFromJson(DOCUMENTS_MAP_JSON);
        Stream<City> cityStream = cities.stream();

        // Paginer les résultats
        List<City> result = cityStream
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public List<City> findAll() {
       return CityRepositoryService.mapCitiesFromJson(DOCUMENTS_MAP_JSON);
    }
}
