/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lunarlandersimulator;

import controllerclasses.LunarLanderController;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import static javafx.scene.paint.Color.valueOf;
import javafx.scene.paint.Paint;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Modality;
import javafx.util.Duration;
import static lunarlandersimulator.PhysicsHelper.gravitationalFieldBeforeCorrection;

/**
 * 
 * @author theo
 */
public final class LunarLanderWorld extends GameEngine {

    /**
     * The scene nodes for the lunar lander simulation.
     */
    private Pane sceneNodes;
    /**
     * The scene for the lunar lander simulation.
     */
    private Scene scene;
    /**
     * Reference to the LunarLanderController class.
     */
    private LunarLanderController lunarLanderController;
    /**
     * Indicates whether the ship is rotating left or not.
     */
    private boolean rotatingLeft = false;
    /**
     * Indicates whether the ship is rotating right or not.
     */
    private boolean rotatingRight = false;
    /**
     * Indicates whether the ship is using fuel or not.
     */
    private boolean usingFuel = false;
    /**
     * Max angle of rotation of the ship.
     */
    private static final double MAX_ANGLE_DEGREES = 90; //The ship starts at 0 degrees, so 90 is completely horizontal to the x axis.
    /**
     * The angle the ship rotates every frame of the animation.
     */
    private double rotationAnglePerFrame = 0;
    /**
     * Indicates whether the simulation is running/on/active or not.
     */
    private boolean isRunning = true;
    /**
     * Introduction alert.
     */
    private Alert introAlert = new Alert(INFORMATION);
    
    private double gravityMultiplier =1;
    /**
     * main stage of the simulation.
     */
    private Stage mainStage;
    /**
     * the acceleration due to gravitational force, initially 0.
     */
    private double accelerationDuetoGravity = 0;
    /**
     * the current height of the ship, but in km.
     */
    private double shipCurrentHeightinKm; //in km.
    /**
     * the smallest angle increment for the rotation of the ship.
     */
    private final double rotationIncrement;
    /**
     * the ratio of each pixel of the screen with the simulation's world's dimensions.
     */
    private final double ratioScreenToSimulationMultiplier;
    /**
     * the initial fuel of the ship.
     */
    private final double initialFuel;
    /**
     * the thrust sound effect of the ship.
     */
    private final MediaPlayer shipThrustSound;
    /**
     * LunarLanderWorld's constructor method.
     * 
     */
    public LunarLanderWorld() {
        initialFuel = 1000;
        shipThrustSound = new MediaPlayer(new Media(getClass().getClassLoader().getResource("sounds/spaceShipLaunch.mp3").toString()));
        shipThrustSound.setCycleCount(MediaPlayer.INDEFINITE);
        shipThrustSound.setVolume(0.4);//make the audioClip silent.
        shipThrustSound.play();
        ratioScreenToSimulationMultiplier = 1000;
        rotationIncrement = 2;
    }

    /**
     * Initializes the main widgets of this stage. Sets up the scene nodes and the FXML Loader.
     * Starts the timer and initializes the pause and reset events.
     * @param primaryStage holds the lunar lander scene.
     * @throws IOException the lunarLanderController throws this error due to try/catches
     * @throws CsvException the lunarLanderController throws this error due to try/catches
     */
    public void initialize(Stage primaryStage) throws IOException, CsvException {

        PhysicsHelper.fillThePlanetMap();
        this.mainStage = primaryStage;
        FXMLLoader loaderLL = new FXMLLoader(getClass().getResource("/fxml/lunarLander.fxml"));
        lunarLanderController = new LunarLanderController(primaryStage);

        loaderLL.setController(lunarLanderController);
        AnchorPane loader = loaderLL.load();
        setSceneNodes(loader);
        setScene(new Scene(loader));

        
        collisionHandler.setTerrainH(lunarLanderController.getTerrainHbox());
        setLunarlanderSimulationPane(lunarLanderController.getSimulationPane());
        //lunarLanderController.getMenuButtonPlanetSelector().setId("3"); //Value of Earth in hashmap
        

      
        ChangeTerrainColor();
        addShipToLunarLanderPane(getShip());
        getShip().initPaneHeightAndWidth();
        getShip().initInitialSpaceShipPos(getShip().getSimulationPaneHeight()/9, getShip().getSimulationPaneHeight()/11);
        shipCurrentHeightinKm = getShip().getShipInitialHeight();

        initShipRotateEvents();

        primaryStage.setScene(getScene());

        this.timer.start();

        primaryStage.setOnCloseRequest(event -> {
            System.out.println("close event request");
            this.timer.stop();
        });

        lunarLanderController.aboutWindow(lunarLanderController.helpLanding, "To land you must: "
                + "\n -Decrease Horizontal speed to be less than : " + collisionHandler.getMaxSpeedX()
                + "\n -Decrease Verticle speed to be less than : " + collisionHandler.getMaxSpeedY()
                + "\n -Make sure current angle is 0, or perpendicular with the surface.");

        lunarLanderController.getButtonPause().setOnAction((event) -> {
            if (isRunning) {
                stop();
                lunarLanderController.getButtonPause().setText("Un-Pause");
                isRunning = false;
            } else {
                start();
                lunarLanderController.getButtonPause().setText("Pause");
                isRunning = true;
            }
        });


        lunarLanderController.getButtonReset().setOnAction((event) -> {
            resetSimulation();
            lunarLanderController.victoryScreen.close();
            if(lunarLanderController.getButtonPause().getText().equals("Un-Pause"))
                lunarLanderController.getButtonPause().setText("Pause");
            isRunning = true;    
        });
        
        lunarLanderController.resultScreenSettings();

    }

    /**
     * Resets the program to the state it was initially.
     *
     */
    public void resetSimulation() {
        shipThrustSound.setVolume(0);
        setFrameCounter(0);
        getShip().setRemainingFuel(initialFuel);
        getShip().getSpaceShipGroup().getChildren().get(getShip().getThrustImageViewPosInGroup()).setVisible(true);
        shipCurrentHeightinKm = getShip().getShipInitialHeight();
        gravityMultiplier = lunarLanderController.getSliderGravity().getValue();
        getShip().resetShip();
        initialXVelocity = 0;
        initialYVelocity = 0;
        finalXVelocity = 0;
        finalYVelocity = 0;
        yAcceleration = 0;
        xAcceleration = 0;
        initialTimeNanoseconds = System.nanoTime();
        rotatingLeft = false;
        rotatingRight = false;
        usingFuel = false;
        netVerticalForce = 0;
        netHorizontalForce = 0;
        rotationAnglePerFrame = 0;
        collisionHandler.setHasShipTouchedDown(false);
        collisionHandler.setHasWon(false);
        if (!getShip().getIsAlive()) {
            getShip().setIsAlive(true);

        }
        for (Node node : collisionHandler.getSpaceShip().getSpaceShipGroup().getChildren()) {
            node.setVisible(true);
        }
        lunarLanderController.setTerrainHbox(lunarLanderController.terrainGenerator.generateWorld(lunarLanderController.getTerrainHbox())); //create new Terrain
        lunarLanderController.HBoxPrefferedSetting(lunarLanderController.getTerrainHbox(), lunarLanderController.bottomSimulationPane);
        ChangeTerrainColor();
        start();
    }
    /**
     * An initial alert to let the user know some basic information and stop them from losing
     * before they understand whats going on.
     */
    public void startingAlert(){
        introAlert.setTitle("Introduction screen.");
        introAlert.setContentText("Whelcome! "
                + " \n This is a lunar lander simulation, please refer to the Help buttons at the top left of your screen."
                + " \n Have fun! ");
        introAlert.initModality(Modality.APPLICATION_MODAL);
        introAlert.initOwner(mainStage);
        introAlert.setOnShown(event -> {stop();});
        
        // introAlert.setOnCloseRequest(event -> { start(); });
        // introAlert.show();
        
        introAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                    start(); 
            }
            else {
                introAlert.close();
            }
        });
            
    }
    /**
     * Initial x position of the ship.
     */
    private double initialXPos;

    /**
     * Initial y position of the ship.
     */
    private double initialYPos;

    /**
     * Initial x velocity of the ship.
     */
    private double initialXVelocity;

    /**
     * Initial y velocity of the ship.
     */
    private double initialYVelocity;

    /**
     * Final x velocity of the ship.
     */
    private double finalXVelocity;

    /**
     * Final y velocity of the ship.
     */
    private double finalYVelocity;

    /**
     * Acceleration in the y direction.
     */
    private double yAcceleration;

    /**
     * Acceleration in the x direction.
     */
    private double xAcceleration;

    /**
     * Net vertical force acting on the ship.
     */
    private double netVerticalForce;

    /**
     * Net horizontal force acting on the ship.
     */
    private double netHorizontalForce;


    /**
     * Get the terrain, get the currently selected planet, set the terrain to
     * that planet's color
     */
    public void ChangeTerrainColor() {

        lunarLanderController.getTerrainHbox();

        // System.out.println("selected name : " + lunarLanderController.getPlanetPresetHash().get(lunarLanderController.getMenuButtonPlanetSelector().getId()).getName());
        for (int i = 0; i < lunarLanderController.getTerrainHbox().getChildren().size(); i++) {

            Rectangle rect;
            rect = (Rectangle) lunarLanderController.getTerrainHbox().getChildren().get(i);
            Paint color = valueOf(lunarLanderController.getPlanetPresetHash().get(3).getColor()); //3 is for now just for Earth
            rect.setFill(color);
            lunarLanderController.getTerrainHbox().getChildren().set(i, rect);

        }

    }
    /**
     * Rotates the ship to the right or left by 2 increments.
     * The "2" is arbitrary and just works well for the simulation.
     * 
     */
    @Override
    public void rotateShip() {
        rotationAnglePerFrame = 0;
        
        if (rotatingLeft) {

            rotationAnglePerFrame -= rotationIncrement;

            if (rotationAnglePerFrame < -MAX_ANGLE_DEGREES) {
                rotationAnglePerFrame += rotationIncrement;

            }
        } else if (rotatingRight) {

            rotationAnglePerFrame += rotationIncrement;

            if (rotationAnglePerFrame > MAX_ANGLE_DEGREES) {
                rotationAnglePerFrame -= rotationIncrement;

            }
        }

        try {
            getShip().initTransforms(rotationAnglePerFrame);
        } catch (NonInvertibleTransformException ex) {
            Logger.getLogger(LunarLanderWorld.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    /**
     * A function that runs every frame with the intention of checking collisions using the collisionHandler.
     */
    @Override
    protected void checkCollisions() {
        collisionHandler.collisionChecker();
        lunarLanderController.terrainGenerator.shiftTerrain(collisionHandler.isShipWithinScreenBounds());
    }

    float nanoToUnit = 1000000000;
    double displayXVelocity;
    double displayYVelocity;

    /**
     * Rounds the number to one decimal
     * @param number
     * @return 
     */
    private double roundToOneDecimal(double number){

        double temp = Math.round(number * 10);
        temp = temp / 10;
    
        return temp;
    }
    
    /**
     * A method called every frame, used to update the stat trackers for the
     * player's visual pleasure.
     *
     * @param elapsedTime The time between the previous and current frame.
     */
    @Override
    public void updateStats(double elapsedTime) {

        
        double currentTimeSeconds = (System.nanoTime() - initialTimeNanoseconds) / nanoToUnit;
        currentTimeSeconds = roundToOneDecimal(currentTimeSeconds);
        displayXVelocity = roundToOneDecimal(getShip().getCurrentXVelocity());
        displayYVelocity = roundToOneDecimal(getShip().getCurrentYVelocity());

        lunarLanderController.setTextOfTextTimeSeconds("Time (s): " + currentTimeSeconds);
        lunarLanderController.setTextOfTextVelocity("Velocity (m/s) : " + displayXVelocity + "x ," + displayYVelocity + " y");
        lunarLanderController.setTextOfTextFuel("Fuel (L) : " + getShip().getRemainingFuel());
        lunarLanderController.setTextOfTextAngle("Current Angle (degrees): " + getShip().getNormalizedAngleWithVertical());
        lunarLanderController.setGravityText("Gravity : " + String.valueOf(Math.ceil(accelerationDuetoGravity*10000)/10000));
        //System.out.println("Gravity : " + String.valueOf(accelerationDuetoGravity));

    }
    
    /**
     * Manages the Horizontal and Vertical force being applied on the ship. Also
     * manages whether the ship has touched the ground or not.
     *
     * @param dt delta time, which is the time elapsed between frames.
     * @return whether the ship has collided.
     */
    @Override
    public boolean updateShipPosAndCheckGroundCollision(double dt) {

        getShip().modifyEngineState(usingFuel); //change the state of the shipEngine when the fuel has been converted to thrust force for the next frame.
        accelerationDuetoGravity = gravitationalFieldBeforeCorrection(lunarLanderController.getTextPlanet(),shipCurrentHeightinKm*ratioScreenToSimulationMultiplier ) * gravityMultiplier ;
        double gravitationalForce = getShip().getMass() * accelerationDuetoGravity; // Fg = mg
        double thrustForceX = 0;
        double thrustForceY = 0;
        //sets an initial X velocity to the right as if the ship has already been moving to the right before the start of the simulation.
        if(getFrameCounter()==0){
            thrustForceX += 5000000;
        }
        //its no longer the first frame.
        setFrameCounter(getFrameCounter()+1);
        if (usingFuel) {

            if(getShip().getRemainingFuel() >= 1){
                shipThrustSound.setVolume(0.4); //play the thrust sound effect.
                thrustForceX = getShip().getThrustForceInX();
                thrustForceY = getShip().getThrustForceInY();
                getShip().setRemainingFuel(getShip().getRemainingFuel() - 1); //remove 1 unit of fuel.
            }
            else{
                shipThrustSound.setVolume(0); // when theres no more fuel.
                // set the thrust imageView to be invisible
                getShip().getSpaceShipGroup().getChildren().get(getShip().getThrustImageViewPosInGroup()).setVisible(false); 
                
                //TODO: notify user hes doomed.
            }

        }
        else {
            shipThrustSound.setVolume(0);
        }
        //if the ship has collided with the terrain, check whether the ship has won.
        if (collisionHandler.getHasShipTouchedDown()) {

            victoryCondition(collisionHandler.isHasWon());

            return true;
        }
        else{
            // this code is just an application of 2d kinematics.
            netVerticalForce = thrustForceY - gravitationalForce;
            netHorizontalForce = thrustForceX;
            yAcceleration = netVerticalForce / getShip().getMass();
            finalYVelocity = initialYVelocity + (yAcceleration * dt);
            initialYPos = ((initialYVelocity + finalYVelocity) / 2) * dt;
            initialYVelocity = finalYVelocity;
            xAcceleration = netHorizontalForce / getShip().getMass();
            finalXVelocity = initialXVelocity + (xAcceleration * dt);
            initialXPos = ((initialXVelocity + finalXVelocity) / 2) * dt;
            initialXVelocity = finalXVelocity;
            getShip().getSpaceShipGroup().setTranslateY(-initialYPos + getShip().getSpaceShipGroup().getTranslateY());
            shipCurrentHeightinKm += initialYPos;
            getShip().getSpaceShipGroup().setTranslateX(initialXPos + getShip().getSpaceShipGroup().getTranslateX());
            getShip().setCurrentXVelocity(initialXVelocity);
            getShip().setCurrentYVelocity(initialYVelocity);
        }
        
        return false;
    }

    /**
     * Explodes the ship when a crash occurs. 
     * Makes every node inside the spaceShip group invisible and adds the explosion imageview, then adds a fade transition to the group
     */
    public void explodeTheShip() {
        ImageView explosion = new ImageView(new Image("images/Explosion.png"));
        for (Node node : collisionHandler.getSpaceShip().getSpaceShipGroup().getChildren()) {
            node.setVisible(false);
        }
        //System.out.println("exploding");
        getShip().getSpaceShipGroup().getChildren().add(explosion);
        //System.out.println(ship.getSpaceShipGroup().getChildren());
        FadeTransition ft = new FadeTransition(Duration.millis(400), explosion);
        ft.setCycleCount(1);
        ft.setToValue(0);
        ft.setOnFinished((ActionEvent event) -> {
            getShip().getSpaceShipGroup().getChildren().remove(explosion);
            ft.stop();
        });
        ft.play();
        
    }

    /**
     * Handles what happens to the ship depending on whether the player won or loss the game.
     * @param hasWon 
     */
    public void victoryCondition(boolean hasWon) {
        shipThrustSound.setVolume(0);
        //Check if ship should explode.
        // and Show victory/defeat screen.
        if (hasWon) {
            collisionHandler.getSpaceShip().setIsAlive(true);
            lunarLanderController.textOfResult.setText("Victory! You landed sucessfully");
            lunarLanderController.resultScreenSettings();
            lunarLanderController.victoryScreen.show();
        } else {

            explodeTheShip();
            collisionHandler.getSpaceShip().setIsAlive(false);
            lunarLanderController.textOfResult.setText("Failure, The ship crashed");
            lunarLanderController.resultScreenSettings();
            lunarLanderController.victoryScreen.show();
        }
    /*
    if (collisionHandler.spaceShip.isAlive) {
        victoryAlert.show();
    } else if (!collisionHandler.spaceShip.isAlive) {
        defeatAlert.show();
    }*/
    }
    /**
     * Initializes the key events that the players needs to press to play the game.
     */
    public void initShipRotateEvents() {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (null != code) {
                switch (code) {
                    case A ->
                        rotatingLeft = true;
                    case D ->
                        rotatingRight = true;
                    case W ->{
                        usingFuel = true;
                    }
                    default -> {
                    }
                }
            }
        });

        scene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if (null != code) {
                switch (code) {
                    case A ->
                        rotatingLeft = false;
                    case D ->
                        rotatingRight = false;
                    case W ->{
                        usingFuel = false;
                        
                    }
                    default -> {
                    }
                }
            }
        });

    }

    /**
     * Puts the ship in the desired Lunar Lander Pane.
     * @param ship ship to be places in the pane.
     */
    public void addShipToLunarLanderPane(SpaceShip ship) {
        AnchorPane tempSimAPane = (AnchorPane) getSceneNodes().lookup("#SimulationAnchorPane");
        int firstIndex = tempSimAPane.getParent().getChildrenUnmodifiable().indexOf(tempSimAPane);

        Pane tempSimPane = (Pane) getSceneNodes().getChildren().get(firstIndex).lookup("#SimulationPane");
        int PaneInVboxIndex = tempSimPane.getParent().getChildrenUnmodifiable().indexOf(tempSimPane);

        AnchorPane simulationAPPane = (AnchorPane) getSceneNodes().getChildren().get(firstIndex);
        Pane simulationPane = (Pane) simulationAPPane.getChildren().get(PaneInVboxIndex);
        
        simulationPane.getChildren().add(ship.getSpaceShipGroup());
        
    }

    /**
     * @return The pane containing the scene nodes.
     */
    public Pane getSceneNodes() {
        return sceneNodes;
    }

    /**
     * @param sceneNodes The pane containing the scene nodes.
     */
    public void setSceneNodes(Pane sceneNodes) {
        this.sceneNodes = sceneNodes;
    }

    /**
     * @return The scene for the simulation.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * @param scene The scene for the simulation.
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * @return The net vertical force applied to the lunar lander.
     */
    public double getNetVerticalForce() {
        return netVerticalForce;
    }

    /**
     * @param netVerticalForce The net vertical force applied to the lunar lander.
     */
    public void setNetVerticalForce(double netVerticalForce) {
        this.netVerticalForce = netVerticalForce;
    }

    /**
     * @return The net horizontal force applied to the lunar lander.
     */
    public double getNetHorizontalForce() {
        return netHorizontalForce;
    }

    /**
     * @param netHorizontalForce The net horizontal force applied to the lunar lander.
     */
    public void setNetHorizontalForce(double netHorizontalForce) {
        this.netHorizontalForce = netHorizontalForce;
    }
}
