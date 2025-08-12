/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllerclasses;

import javafx.scene.image.ImageView;
import lunarlandersimulator.LunarLanderWorld;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import static javafx.stage.Modality.APPLICATION_MODAL;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * The MainMenu class is meant to parse the associated SceneBuilder file. As
 * well as applying changes to the nodes when needed.
 *
 * @author theo, NightKnightRen
 */
public class MainMenuController extends ControllerHelper {

    // ControllerHelper controlHelp;
    @FXML
    MenuItem menuItemHelp;

    @FXML
    AnchorPane anchorPaneMainBack;
    @FXML
    ImageView spaceShipInMainMenu;

    Stage primaryStage;

    String aboutTheWindow = "This is the main menu. "
            + "\n Click on one of the icons to enter a simulation!";

    public MainMenuController(Stage stage) throws IOException, CsvException {
        this.primaryStage = stage;

    }

    /**
     * The initialize function sets all the nodes for the MainMenu scene.
     */
    @FXML
    void initialize() {
        aboutWindow(menuItemHelp, aboutTheWindow);

        Image imageLL = new Image("/images/spaceShip.png");
        Image iconMainMenu = new Image("/images/icon.png");
        primaryStage.getIcons().add(iconMainMenu);




        spaceShipInMainMenu.setOnMousePressed((e) -> {

            Stage LunarLanderStage = new Stage();
            try {
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                LunarLanderWorld lunarLander = new LunarLanderWorld();
                LunarLanderStage.initModality(APPLICATION_MODAL);
                lunarLander.initialize(LunarLanderStage);
                LunarLanderStage.setWidth(screenBounds.getWidth());
                LunarLanderStage.setHeight(screenBounds.getHeight());
                LunarLanderStage.setResizable(false);
                LunarLanderStage.setTitle("Lunar Lander Simulation");
                LunarLanderStage.show();

            } catch (IOException | CsvException ex) {
                Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }


}
