/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllerclasses;

import com.opencsv.bean.CsvBindByPosition;

/**
 * The PlanetPreset class is meant to hold all information about a specific
 * planet. The intended utilization is with the ControllerHelper's
 * putHashElements() method. This class holds multiple variables, all of which
 * can be stored within this class and thereinafter within a single Hashmap.
 *
 * @author theo
 */
public class PlanetPreset {

    @CsvBindByPosition(position = 0)
    int indexPosition;

    @CsvBindByPosition(position = 1)
    String name;

    @CsvBindByPosition(position = 2)
    Double gravity;

    @CsvBindByPosition(position = 3)
    String color;

    public PlanetPreset(int indexPosition, String name, Double gravity, String color) {
        this.indexPosition = indexPosition;
        this.name = name;
        this.gravity = gravity;
        this.color = color;
    }

    public int getIndexPosition() {
        return indexPosition;
    }

    public void setIndexPosition(int indexPosition) {
        this.indexPosition = indexPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getGravity() {
        return gravity;
    }

    public void setGravity(Double indexRefraction) {
        this.gravity = indexRefraction;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
