/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllerclasses;


import lunarlandersimulator.TerrainGeneration;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import static javafx.geometry.Pos.CENTER;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The LunarLanderController class is meant to parse the associated SceneBuilder
 * file. As well as applying changes to the nodes when needed.
 *
 * @author theo
 */
public class LunarLanderController extends ControllerHelper {

    @FXML
    public MenuItem helpLanding;
    @FXML
    public MenuItem helpUI;
    @FXML
    public MenuItem helpControls;
    @FXML
    public MenuItem helpAbout;
    @FXML
    Pane SimulationPane;
    @FXML
    AnchorPane SimulationAnchorPane;
    @FXML
    Text textAngle;
    @FXML
    Text textVelocity;
    @FXML
    Text textFuel;
    @FXML
    Text textPlanet;
    @FXML
    Text textTimeSeconds;
    @FXML
    Button buttonPause;
    @FXML
    Button buttonReset;
    @FXML
    Button buttonHelp;
    @FXML
    HBox terrainHbox;


    Stage primaryStage;
    public TerrainGeneration terrainGenerator = new TerrainGeneration();


    public double bottomSimulationPane;
    public Stage infoUIScreen = new Stage();
    public Stage victoryScreen = new Stage();
    public Text textOfResult = new Text();
    Button buttonRestartAfterResult = new Button();
    MenuButton menuButtonChangePlanetAfterResult = new MenuButton();



    // The texts can be set here, or in the LunarLanderWorld should variables be included
    String infoHelpLanding = "null, check LunarLanderWorld";
    String infoUI = "The menus at the top : "
            + "\n       Close: allows you to close the simulation "
            + "\n       About : Shows a selection of informational instructions/guides "
            + "\n"
            + "\nThe lower UI : "
            + "\n       Planet Selector : is a drop down menu full of planets, clicking on these celestial bodies sets the simulation to said celestial bodie's gravity. "
            + "\n       Pause : This button pauses and un-pauses the simulation."
            + "\n       Reset : Clicking this restarts the simulation."
            + "\n       The gravity slider: can be moved, however only takes effect upon a reset."
            + "\n       The gravity text input : enter any number between 0 and 30 and upon reset, the gravity of the simulation will change to that number.";
    String infoSimulation = "This is the Lunar Lander Simulation!"
            + "\n As the ship gets closer to the planet, the gravity get's stronger.";
    String infoControls = "\"W\" : thrust the rocket, propelling it in the faced direction "
            + "\n \"A\" & \"D\" : rotates left and right respectively";

    /**
     * Constructor used to bring parent stage into local class.
     * @param stage the stage which this new stage is the child of.
     * @throws IOException throws exception.
     * @throws CsvException throws exception.
     */
    public LunarLanderController(Stage stage) throws IOException, CsvException  {

        this.primaryStage = stage;
    }

  
    
     /**
     * Sets the settings for the window that shows up at victory.
     */
    public void resultScreenSettings(){

    buttonRestartAfterResult.setText("Restart?");
    buttonRestartAfterResult.setOnAction(buttonReset.getOnAction());

    
    VBox root = new VBox();
    root.getChildren().addAll(textOfResult,buttonRestartAfterResult,menuButtonChangePlanetAfterResult);
    root.setSpacing(10d);
    root.setAlignment(CENTER);
    victoryScreen.setScene(new Scene(root, 250, 150));
    victoryScreen.setResizable(false);
//    victoryScreen.initOwner(primaryStage);
//    victoryScreen.initModality(Modality.APPLICATION_MODAL); 
    victoryScreen.setTitle("Result Screen");
    

    }
    
    /**
     * Sets the default settings for the terrain as to maintain consistency and
     * aesthetic integrity.
     *
     * @param terrain   the terrain which is being modified.
     * @param bottomSim the location of the terrain in the scene.
     */
    public void HBoxPrefferedSetting(HBox terrain, double bottomSim) {

        terrain.setBackground(Background.EMPTY);
        terrain.setLayoutX(bottomSim);
    }

    /**
     * The initialize function sets all the nodes for the LunarLander scene.
     */
    @FXML
    void initialize() {

        double bottomSimulationPane = (getSimulationPane().getLayoutX() + getSimulationPane().getHeight());
        System.out.println("war "+ terrainHbox);
        terrainHbox = terrainGenerator.generateWorld(terrainHbox);
        HBoxPrefferedSetting(terrainHbox, bottomSimulationPane);
        aboutWindow(helpLanding, infoHelpLanding);
        aboutWindow(helpControls, infoControls);
        aboutWindow(helpAbout, infoSimulation);

        Image iconLunarLander = new Image("/images/spaceShip.png");
        primaryStage.getIcons().add(iconLunarLander);


        menuButtonChangePlanetAfterResult.setText("Change planet?");


        helpUI.setOnAction((event) -> {
            infoUIScreen.showAndWait();
        });
        Image uiInfoImage = new Image("images/uiInformationImage.png");
        ImageView uiInfoImageView = new ImageView(uiInfoImage);
        Text uiInfoText = new Text(infoUI);
        VBox rootUI = new VBox();
        rootUI.getChildren().addAll(uiInfoImageView, uiInfoText);
        rootUI.setSpacing(10d);
        infoUIScreen.setScene(new Scene(rootUI));
        infoUIScreen.setResizable(false);
        infoUIScreen.setTitle("UI info Screen");

        setTextOfTextPlanet("Set Planet : Earth"); //initial setting

        setBackgroundImage(new ImageView(new Image("images/space.png")));


    }

    /**
     * Sets the background image for the simulation.
     *
     * @param background The background image for the scene.
     */
    public void setBackgroundImage(ImageView background) {

        getSimulationPane().getChildren().addFirst(background);
        getSimulationPane().toBack();
        background.fitWidthProperty().bind(getSimulationPane().widthProperty());
        background.fitHeightProperty().bind(getSimulationPane().heightProperty());

    }





    public Button getButtonPause() {
        return buttonPause;
    }

    public Button getButtonReset() {
        return buttonReset;
    }

    public Button getButtonHelp() {
        return buttonHelp;
    }

    public void setInfoHelpLanding(String infoHelpLanding) {
        this.infoHelpLanding = infoHelpLanding;
    }

    public void setInfoUI(String infoUI) {
        this.infoUI = infoUI;
    }

    public void setInfoControls(String infoControls) {
        this.infoControls = infoControls;
    }

    public void setInfoSimulation(String infoSimulation) {
        this.infoSimulation = infoSimulation;
    }

    public Pane getSimulationPane() {
        return SimulationPane;
    }

    public void setSimulationPane(Pane SimulationPane) {
        this.SimulationPane = SimulationPane;
    }

    public HBox getTerrainHbox() {
        return terrainHbox;
    }

    public void setTerrainHbox(HBox terrainHbox) {
        this.terrainHbox = terrainHbox;
    }

    public TerrainGeneration getTerrainGenerator() {
        return terrainGenerator;
    }

    public void setTerrainGenerator(TerrainGeneration terrainGenerator) {
        this.terrainGenerator = terrainGenerator;
    }

    public void setTextOfTextAngle(String textAngle) {
        this.textAngle.setText(textAngle);
    }

    public void setTextOfTextVelocity(String textVelocity) {
        this.textVelocity.setText(textVelocity);
    }

    public void setTextOfTextFuel(String textFuel) {
        this.textFuel.setText(textFuel);
    }

    public void setTextOfTextPlanet(String textPlanet) {
        this.textPlanet.setText(textPlanet);
    }
    
    public String getTextPlanet(){
    
        return this.textPlanet.getText();
    }
    public void setTextOfTextTimeSeconds(String textTimeSeconds) {
        this.textTimeSeconds.setText(textTimeSeconds);
    }
    
    

}
