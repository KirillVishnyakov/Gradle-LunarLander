/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllerclasses;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This is a class that gets extended by the controller classes, it's purpose is
 * to create functions that are going to be re-used by each controller.
 *
 * @author theo
 */
public class ControllerHelper {

    private final HashMap<Integer, PlanetPreset> planetPresetHash;
    private final String csvFilePath = getClass().getResource("/CSV/planetGravities.csv").getPath().replace("%20", " ");

    public ControllerHelper() throws IOException, CsvException {

        this.planetPresetHash = putHashElements(); //fill the hashmap
    }

    /**
     * Parses a CSV file, converts the items found into PlanetPreset objects,
     * and puts them on a Hashmap. This works by parsing each row of the CSV file
     * and taking the corresponding column as the paramenters for the PlanetPreset
     * objects. Then simply adds those objects into the HashMaps.
     *
     * @return returns a hashmap full of filled Planet Presets
     * @throws FileNotFoundException the CSV file may be missing
     */
    public HashMap<Integer, PlanetPreset> putHashElements() throws FileNotFoundException {

        HashMap<Integer, PlanetPreset> gravityHashMap = new HashMap<>();
        CSVReader reader = new CSVReaderBuilder(new FileReader(csvFilePath)).build();

        List<String[]> r = null;
        try {
            r = reader.readAll();
        
        } catch (IOException | CsvException ex) {
            Logger.getLogger(ControllerHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        String color;

        for (String[] a : r) {
            if (a[3].equals("")) {
                color = "AFAFAF";
            } else {
                color = a[3];
            }

            PlanetPreset preset = new PlanetPreset(Integer.valueOf(a[0]), a[1], Double.valueOf(a[2]), color);
            gravityHashMap.put(Integer.valueOf(a[0]), preset);

        }

        return gravityHashMap;
    }

    /**
     * Receives a menuItem and sets it's action to creating an alert
     * corresponding to the "message"
     *
     * @param aboutMenuItem the menuItem which is clicked to display the alert.
     * @param message information the alert displays 
     */
    public void aboutWindow(MenuItem aboutMenuItem, String message) {
        Alert alertInformation = new Alert(Alert.AlertType.INFORMATION);
        alertInformation.setContentText(message);
        alertInformation.setTitle("Information: ");

    }


}
