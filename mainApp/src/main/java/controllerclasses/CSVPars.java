/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllerclasses;

import com.opencsv.bean.CsvToBeanBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author bidia
 *
 * The CSVPars class is an object which is meant to be instantiated into a
 * situation requiring the parsing of a CSV file.
 */
public class CSVPars {

    public static HashMap<Integer, PlanetPreset> parse(String file) throws FileNotFoundException {
        //make list using bean builder
        List<PlanetPreset> beanBuilderList = new CsvToBeanBuilder(new FileReader(file))
                .withType(PlanetPreset.class).build().parse();

        //turn the list into a HashMap<Integer, PlanetPreset>
        HashMap<Integer, PlanetPreset> presetsMap = new HashMap<>();
        for (PlanetPreset preset : beanBuilderList) {
            presetsMap.put(preset.getIndexPosition(), preset);
        }

        return presetsMap;
    }
}
