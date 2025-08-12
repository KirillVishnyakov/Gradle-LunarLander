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
     * Sets up a bunch of basic slider settings.
     *
     * @param slider the slider which is being set
     * @param max the max distance of the slider. Should be 30 as none of the presets go higher
     */
    //This function may be too specified and non applicable outside of the original intention
    public void sliderSetup(Slider slider, Double max) {

        slider.setValue(1);
        slider.setMin(0);
        slider.setMax(max);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(5);
        slider.setMinorTickCount(99);
        slider.setSnapToTicks(true);
        slider.setBlockIncrement(0.1);

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

        aboutMenuItem.setOnAction((event) -> {
            alertInformation.show();
        });
    }

    /**
     * Receives a textfield and checks if the inputted value has numbers. Used
     * to avoid running number only functions on letters that the user may have
     * input.
     *
     * @param textfield the textfield's value is tested for Errors
     * @return returns whether the TextField is safe to use in number functions
     */
    public boolean testTextFieldForNumbers(TextField textfield) {

        Double value = 0.0;

        if (textfield.getText().equals("")) {
            System.out.println("Text field is empty");
            textfield.setText("Text field is empty");
            return false;
        } else 
        {

            try {

                value = Double.valueOf(textfield.getText());
                return true;

            } catch (Exception e) {
                System.out.println("Please enter a number.");
                textfield.setText("Please enter a number.");
                return false;
            }

        }

    }

    /**
     * When user presses button, this functions checks textfield's value, and
     * updates the slider accordingly.
     *
     * @param button the button which gets pressed to update the slider.
     * @param slider the slider which gets updated and tells the current gravity.
     * @param textfield the textfield which holds the value that the slider gets set to.
     */
    public void customGravityInput(Button button, Slider slider, TextField textfield) {

        button.setOnAction((event) -> {
            Double value = 0.0;

            if (testTextFieldForNumbers(textfield) == false) {

                value = 0.0;

            } else {
                try {
                    value = Double.valueOf(textfield.getText());
                    System.out.println("Yes, value entered : " + value + " .");
                    
                    slider.setValue(value);

                } catch (Exception e) {
                    textfield.setText("");
                }
            }
        });

    }
    int currentIndexOfMenuBar;

    /**
     * Fills a menuButton with menuItems. These menuItems have their action set
     * to updating various texts according to their corresponding planetPreset
     *
     * @param slider updates slider
     * @param textForGravity updates textForDisplay
     * @param textForPlanetName the Text node which holds the planet's name in the stats
     */
    public void putItemsInMenuBar(Slider slider, Text textForGravity, Text textForPlanetName) { //, Rectangle medium

        for (int i = 0; i < getPlanetPresetHash().size(); i++) {

            MenuItem presetMenuItem = new MenuItem(getPlanetPresetHash().get(i).getName());
            presetMenuItem.setId(String.valueOf(getPlanetPresetHash().get(i).indexPosition));
            presetMenuItem.setOnAction((event) -> {

                slider.setValue(getPlanetPresetHash().get(Integer.parseInt(presetMenuItem.getId())).getGravity()); //set the slider's value to the preset's defined gravity
                // medium.setFill(giveColorToMedium(sliderValue));
                //  updatesIndexOfRefractionText(textForDisplay, slider);
                textForGravity.setText("Current Gravity (G) : " + getPlanetPresetHash().get(Integer.parseInt(presetMenuItem.getId())).getGravity());
                textForPlanetName.setText("Current Planet : " + getPlanetPresetHash().get(Integer.parseInt(presetMenuItem.getId())).getName());
            });
        }

    }

    
    public HashMap<Integer, PlanetPreset> getPlanetPresetHash() {
        return planetPresetHash;

    }

    public String getCsvFilePath() {
        return csvFilePath;
    }

}
