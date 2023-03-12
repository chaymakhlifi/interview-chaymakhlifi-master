package fr.bred.example.interview.services;

import fr.bred.example.interview.entities.City;
import fr.bred.example.interview.repositories.CityRepositoryService;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CityService {
    public final CityRepositoryService cityRepositoryService;
    public CityService(CityRepositoryService cityRepositoryService) {
        this.cityRepositoryService = cityRepositoryService;

    }

    public List<City> searchCities(final String namePattern, final String zipCodePattern,
                                   final Integer _limit, final Integer _start, final String _sort, final String _order) throws IOException {
        // Définition des constantes de pagination
        final int DEFAULT_PAGE_START = 0;
        final int DEFAULT_PAGE_LIMIT = Integer.MAX_VALUE;

        // Validation des paramètres d'entrée
        String validatedNamePattern = StringUtils.isNotBlank(namePattern) ? namePattern : "*";
        String validatedZipCodePattern = StringUtils.isNotBlank(zipCodePattern) ? zipCodePattern : "*";
        int validatedStart = (_start != null && _start >= 0) ? _start : DEFAULT_PAGE_START;
        int validatedLimit = (_limit != null && _limit > 0) ? _limit : DEFAULT_PAGE_LIMIT;
        String validatedSort = StringUtils.isNotBlank(_sort) ? _sort : "name";
        Sort.Direction validatedDirection = Sort.Direction.DESC;
        if (StringUtils.isNotBlank(_order) && _order.equalsIgnoreCase("asc")) {
            validatedDirection = Sort.Direction.ASC;
        }

        // Création de l'objet Pageable
        Sort citySort = Sort.by(validatedDirection, validatedSort);
        Pageable pageable = PageRequest.of(validatedStart, validatedLimit, citySort);

        // Appel de la méthode de recherche appropriée en fonction des paramètres d'entrée validés
        if (!validatedNamePattern.equals("*") && !validatedZipCodePattern.equals("*")) {
            return cityRepositoryService.findByNameContainingIgnoreCaseAndZipCodeContainingIgnoreCase(
                    validatedNamePattern, validatedZipCodePattern, pageable);
        } else if (!validatedNamePattern.equals("*")) {
            return cityRepositoryService.findByNameContainingIgnoreCase(validatedNamePattern, pageable);
        } else if (!validatedZipCodePattern.equals("*")) {
            return cityRepositoryService.findByZipCodeContainingIgnoreCase(validatedZipCodePattern, pageable);
        } else {
            return cityRepositoryService.findAll(pageable);
        }
    }


    public City getNearestCity(String x, String y) throws IllegalArgumentException, IOException {

        // récupération de toutes les villes
        List<City> cities = cityRepositoryService.findAll();
        if (cities == null || cities.isEmpty()) {
            throw new IllegalStateException("La liste des villes est vide ou nulle");
        }

        // initialisation de la ville la plus proche avec une distance infinie
        City nearestCity = null;
        Double nearestDistance = Double.MAX_VALUE;

        // itération à travers toutes les villes et calcul de la distance entre chaque ville et le point (x, y)
        for (City city : cities) {
            Double distance = calculateDistance(x, y, city.getCoordinates().getX(), city.getCoordinates().getY());

            // si la distance est plus petite que la distance actuelle, mettre à jour la ville la plus proche
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestCity = city;
            }
        }

        if (nearestCity == null) {
            throw new IllegalArgumentException("Aucune ville trouvée à proximité");
        }

        return nearestCity;
    }

    private Double calculateDistance(String x1, String y1, String x2, String y2) {
        return Math.sqrt(Math.pow(Double.parseDouble(x1) - Double.parseDouble(x2), 2)
                + Math.pow(Double.parseDouble(y1) - Double.parseDouble(y2), 2));
    }

}
