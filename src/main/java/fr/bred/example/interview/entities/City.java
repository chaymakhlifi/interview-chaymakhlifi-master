package fr.bred.example.interview.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {

    private Long id;

    private String name;

    private String zipCode;

    public Point coordinates;

    public City(Long id, String name, String zipCode, Point coordinates) {
        this.id = id;
        this.name = name;
        this.zipCode = zipCode;
        this.coordinates = coordinates;
    }
}