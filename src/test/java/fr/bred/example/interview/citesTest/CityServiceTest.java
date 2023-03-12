package fr.bred.example.interview.citesTest;

import fr.bred.example.interview.entities.City;
import fr.bred.example.interview.entities.Point;
import fr.bred.example.interview.repositories.CityRepositoryService;
import fr.bred.example.interview.services.CityService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CityServiceTest {


    @InjectMocks
    private CityService cityService;
    @InjectMocks
    private CityRepositoryService cityRepositoryService;

    @Test
    public void testSearchCities() throws IOException {
        // Création des villes pour le test
        City city1 = new City(1L, "Paris", "75000", new Point("1", "2"));
        City city2 = new City(2L, "Marseille", "13000", new Point("3", "4"));
        List<City> cities = Arrays.asList(city1, city2);
        Page<City> page = new PageImpl<>(cities);

        // Mock du service de repository pour renvoyer les villes créées
        CityRepositoryService cityRepositoryService = Mockito.mock(CityRepositoryService.class);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Mockito.when(cityRepositoryService.findAll(pageable)).thenReturn(new ArrayList<>(page.getContent()));


        // Appel de la méthode de recherche
        CityService cityService = new CityService(cityRepositoryService);
        List<City> result = cityService.searchCities(null, null, null, null, null, null);

        // Vérification du résultat
        Assert.assertEquals(cities, result);
    }
}