/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lunarlandersimulator;

import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * CollisionEngine deals with the checking and updating of collision detection.
 *
 * @author 2059611
 */
public class CollisionEngine {

    private SpaceShip spaceShip;
    private Pane spaceSimulationPane;
    private Rectangle exampleTerrain;
    private ImageView spaceShipImageView;
    private Bounds shipBounds;
    private HBox terrainH;
    private double maxSpeedX = 20.0;
    private double maxSpeedY = 26.0;
    private boolean hasShipTouchedDown = false;
    public boolean hasWon = false;
    private Node[] HitBoxes;
    private double maxAngleForLanding = 8;
    private Bounds screenBounds ;

    /**
     * Constructor, receives spaceShip reference and terrainH copy
     *
     * @param ship A reference to the ingame ship object so the program can make
     * accurate coordinate checks.
     *
     */
    public CollisionEngine(SpaceShip ship) {
        this.spaceShip = ship;
        this.HitBoxes = ship.getHitBoxes();
        this.terrainH = getTerrainH();
      //  this.screenBounds = spaceSimulationPane.getBoundsInLocal();
        
    }

    /**
     * Runs every frame and is used to call other methods.
     *
     * Loops through terrainHbox and the checks if the ship is within it's
     * bounds. If so, this implies the ship is "in terrain" and has either
     * crashed or landed, then sends to ShipChecker
     */
    public void collisionChecker() {
        for (int i = 0; i < terrainH.getChildren().size(); i++) {

            for (Node HitBox : HitBoxes) {
                //System.out.println(getBoundsInSimulationPane(HitBoxe));
                if (getBoundsInSimulationPane(terrainH.getChildren().get(i)).intersects(getBoundsInSimulationPane(HitBox))) {
                    landingChecker();
                }
            }
        }
    }
    /**
     * returns the bounds of a node relative to the simulationPane
     * @param node for which we want to find its bounds relative to the simulation Pane.
     * @return the bounds of a node relative to the simulationPane.
     */
    protected Bounds getBoundsInSimulationPane(Node node) {

        Bounds boundsInNode = node.getBoundsInLocal();
        Bounds boundsInScene = node.localToScene(boundsInNode);
        return getSpaceSimulationPane().sceneToLocal(boundsInScene);

    }
    
 
    /**
     * What happens when the ship is TOO far off the screen.
     * int location determines where the ship is.
     * 0 = in-bounds
     * 1 = to the left
     * 2 = to the right
     * 3 = above
     * 4 = below
     * @return returns the number mentioned above.
     */
    public int isShipWithinScreenBounds(){
    
        
        for (int j = 0; j < HitBoxes.length; j++) {
                
            //if less than zero, ship is to the left of the screen.
            //teleport ship to rightside and -100 for a little room
            if (spaceShip.getSpaceShipGroup().getTranslateX() < 0 ){
                 spaceShip.getSpaceShipGroup().setTranslateX(spaceSimulationPane.getLayoutBounds().getWidth()-100);
                 return 1;
                } ;
            //if ship is to the right
            //teleport ship to leftside and +100 for a little room
            if (spaceShip.getSpaceShipGroup().getTranslateX() > spaceSimulationPane.getLayoutBounds().getWidth()){
                spaceShip.getSpaceShipGroup().setTranslateX(100);
                 return 2; 
            }
             //if the ship is in the negative, it is at the top
             //at -100 it is high enough that the player is completly off screen.
            if (spaceShip.getSpaceShipGroup().getTranslateY() < -100 ){

                 return 3;
                } ;
            //this event should almost never trigger due to the terrain at the bottom.
            if (spaceShip.getSpaceShipGroup().getTranslateY() > spaceSimulationPane.getLayoutBounds().getHeight() ){

                 return 4;
                } ;

            }
        // if (spaceSimulationPane.getLayoutBounds().contains(getBoundsInSimulationPane(HitBoxes[j]))) {} 
        //if the ship isn't out of bounds, it's assumed to be in bounds. 
        return 0;
    }


    /**
     * compares the speed and the angle at which the ship lands and determines whether the ship should explode or not.
     */
    public void landingChecker() {

        hasShipTouchedDown = true;

        if (Math.abs(spaceShip.getCurrentXVelocity()) < maxSpeedX
         && Math.abs(spaceShip.getCurrentYVelocity()) < maxSpeedY) {
            
            if (spaceShip.getNormalizedAngleWithVertical() < maxAngleForLanding) {
                
                //Ship landed, run victory function               
                hasWon = true;
            }

        } else {
            
            //Ship crashed, run failure function
            hasWon = false;
        }

    }

    public HBox getTerrainH() {
        return terrainH;
    }

    public void setTerrainH(HBox terrain) {
        this.terrainH = terrain;

        //terrainH.getChildren().add(hitbox);
    }

    public Pane getSpaceSimulationPane() {
        return spaceSimulationPane;
    }

    public void setSpaceSimulationPane(Pane spaceSimulationPane) {
        this.spaceSimulationPane = spaceSimulationPane;
    }

    public void setShipLanded(boolean shipLanded) {
        this.hasShipTouchedDown = shipLanded;
    }

    public boolean getHasShipTouchedDown() {
        return hasShipTouchedDown;
    }

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public void setSpaceShip(SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
    }

    public Rectangle getExampleTerrain() {
        return exampleTerrain;
    }

    public void setExampleTerrain(Rectangle exampleTerrain) {
        this.exampleTerrain = exampleTerrain;
    }

    public ImageView getSpaceShipImageView() {
        return spaceShipImageView;
    }

    public void setSpaceShipImageView(ImageView spaceShipImageView) {
        this.spaceShipImageView = spaceShipImageView;
    }

    public Bounds getShipBounds() {
        return shipBounds;
    }

    public void setShipBounds(Bounds shipBounds) {
        this.shipBounds = shipBounds;
    }

    public double getMaxSpeedX() {
        return maxSpeedX;
    }

    public void setMaxSpeedX(double maxSpeedX) {
        this.maxSpeedX = maxSpeedX;
    }

    public double getMaxSpeedY() {
        return maxSpeedY;
    }

    public void setMaxSpeedY(double maxSpeedY) {
        this.maxSpeedY = maxSpeedY;
    }

    public boolean isHasWon() {
        return hasWon;
    }

    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    public double getMaxAngleForLanding() {
        return maxAngleForLanding;
    }

    public void setMaxAngleForLanding(double maxAngleForLanding) {
        this.maxAngleForLanding = maxAngleForLanding;
    }

    public Bounds getScreenBounds() {
        return screenBounds;
    }

    public void setScreenBounds(Bounds screenBounds) {
        this.screenBounds = screenBounds;
    }

    public void setHasShipTouchedDown(boolean hasShipTouchedDown) {
        this.hasShipTouchedDown = hasShipTouchedDown;
    }
    

}
