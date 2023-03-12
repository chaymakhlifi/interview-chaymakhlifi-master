package fr.bred.example.interview.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Point {

    private String x;
    private String y;

    public Point(String x, String y) {
        this.x = x;
        this.y = y;
    }
}