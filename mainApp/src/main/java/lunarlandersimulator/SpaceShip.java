/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lunarlandersimulator;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.*;

/**
 * This class configures all the fields and methods related to the space ship.
 * @author Kirill Vishnyakov
 */
public class SpaceShip {

    /**
     * ImageView object representing the spaceship.
     */
    private ImageView spaceShipImageView;

    /**
     * Node object representing the spaceship.
     */
    private Node spaceShipNode;

    /**
     * Rotate object for rotating the spaceship.
     */
    private Rotate rotateGroup;

    /**
     * Translate object for moving the spaceship.
     */
    private Translate translateGroup;

    /**
     * X-coordinate of the pivot point of the spaceship.
     */
    private double pivotX = 30;

    /**
     * Y-coordinate of the pivot point of the spaceship.
     */
    private double pivotY = 60;

    /**
     * X-coordinate of the pivot point of the thrust.
     */
    private double thrustPivotX;

    /**
     * Y-coordinate of the pivot point of the thrust.
     */
    private double thrustPivotY;

    /**
     * Mass of the spaceship in kg.
     */
    private double mass = 2000;

    /**
     * Thrust force of the spaceship in N.
     */
    private double thrustForce = 34000;

    /**
     * Thrust force in X direction.
     */
    private double thrustForceInX;

    /**
     * Thrust force in Y direction.
     */
    private double thrustForceInY;

    /**
     * Angle between the spaceship and the vertical axis.
     */
    private double angleWithVertical;

    /**
     * Counter for the angle of the spaceship.
     */
    private double angleCounter = 0;

    /**
     * ImageView object representing the thrust.
     */
    private ImageView thrustImageView;

    /**
     * Current state of the engine.
     */
    private boolean currentEngineState = false;

    /**
     * Rectangle objects representing the hitboxes of the spaceship that can be landed on.
     */
    private Rectangle WinHitBoxOne, WinHitBoxTwo, WinHitBoxThree, WinHitBoxFour, WinHitBoxFive;
    /**
     * Current velocity of the spaceship in the X direction.
     */
    private double currentXVelocity;

    /**
     * Current velocity of the spaceship in the Y direction.
     */
    private double currentYVelocity;

    /**
     * Current height of the spaceship.
     */
    private double currentHeight;

    /**
     * Group object containing the spaceship and its hitboxes.
     */
    private Group spaceShipGroup = new Group();

    /**
     * Y-coordinate translation of the hitboxes.
     */
    private double hitBoxTranslateY = 70;

    /**
     * Width of the hitboxes.
     */
    private double hitBoxWidth = 10;

    /**
     * Image object representing the spaceship.
     */
    private Image spaceShipImage;

    /**
     * Image object representing the thrust.
     */
    private Image thrustImage;

    /**
     * Boolean indicating whether the spaceship is alive or not.
     */
    private boolean isAlive = true;
    /**
     * An array list filled with the inverses of every transform applied to the ship by the user.
     */
    private List<Transform> resetTransforms = new ArrayList<>();
    /**
     * The height of the pane in which the space ship group is.
     */
    private double simulationPaneHeight;
    /**
     * The width of the pane in which the space ship group is.
     */
    private double simulationPaneWidth;
    /**
     * the initial height of the ship.
     */
    private double shipInitialHeight;
    /**
     * Position of the thrust imageView inside the space ship group.
     */
    private int thrustImageViewPosInGroup = 5;
    /**
     * The hitbox horizontal offset relative to the ship imageView.
     */
    private int hitBoxPositionOffset = 5;
    /**
     * the thrust vertical imageView offset relative to the ship imageView.
     */
    private double thrustImageViewOffset = 13;
    /**
     * Amount of fuel left in the spaceship.
     */
    private double remainingFuel = 1000; //some units

    private Rectangle fuelTank;

    /**
     * SpaceShip's constructor, initializes the initial conditions of the spaceShip group.
     */
    public SpaceShip() {
        this.spaceShipImage = new Image("images/spaceShip.png");
        this.thrustImage = new Image("images/Thrust.png");


        spaceShipImageView = new ImageView();
        spaceShipImageView.setImage(spaceShipImage);
        WinHitBoxOne = new Rectangle(5, 5, Color.PINK);
        WinHitBoxTwo = new Rectangle(5, 5, Color.PINK);
        WinHitBoxThree = new Rectangle(5, 5, Color.PINK);
        WinHitBoxFour = new Rectangle(5, 5, Color.PINK);
        WinHitBoxFive = new Rectangle(5, 5, Color.PINK);

        thrustImageView = new ImageView();
        thrustImageView.setImage(thrustImage);

        //TODO: Make it look better than a Rectangle
        fuelTank = new Rectangle(15, remainingFuel/10, Color.RED);
        initFuelTankCoord();

        thrustImageView.setTranslateY(spaceShipImageView.getBoundsInParent().getMaxY() - thrustImageViewOffset);


        WinHitBoxOne.setTranslateY(hitBoxTranslateY);
        WinHitBoxTwo.setTranslateY(hitBoxTranslateY);
        WinHitBoxThree.setTranslateY(hitBoxTranslateY);
        WinHitBoxFour.setTranslateY(hitBoxTranslateY);
        WinHitBoxFive.setTranslateY(hitBoxTranslateY);

        WinHitBoxOne.setWidth(hitBoxWidth);
        WinHitBoxTwo.setWidth(hitBoxWidth);
        WinHitBoxThree.setWidth(hitBoxWidth);
        WinHitBoxFour.setWidth(hitBoxWidth);
        WinHitBoxFive.setWidth(hitBoxWidth);

        WinHitBoxTwo.setTranslateX(WinHitBoxOne.getWidth() + hitBoxPositionOffset);
        WinHitBoxThree.setTranslateX(WinHitBoxTwo.getWidth() + WinHitBoxTwo.getTranslateX() + hitBoxPositionOffset);
        WinHitBoxFour.setTranslateX(WinHitBoxThree.getWidth() + WinHitBoxThree.getTranslateX() + hitBoxPositionOffset);
        WinHitBoxFive.setTranslateX(WinHitBoxFour.getWidth() + WinHitBoxFour.getTranslateX() + hitBoxPositionOffset);
       
        
        
        setSpaceShipNode(spaceShipImageView);

        modifyEngineState(currentEngineState);
        // this group contains all the visible components that make the ship visible and ready for the simulation.
        spaceShipGroup.getChildren().addAll(WinHitBoxOne, WinHitBoxTwo, WinHitBoxThree, WinHitBoxFour, WinHitBoxFive, thrustImageView, spaceShipImageView, fuelTank);

    }

    public void resizeShipTransform(){
        Scale resize = new Scale(0.7, 0.7);
        getSpaceShipGroup().getTransforms().addAll(resize);
    }
    //TODO: Normalize X,Y relative to ship and not just numbers
    public void initFuelTankCoord(){
        fuelTank.setY(-24);
        fuelTank.setX(-10); // Position at top (full)
        fuelTank.setHeight(remainingFuel/10);
        resizeShipTransform();

    }
    //manages the visual loss of fuel
    public void loseFuel(){
        fuelTank.setHeight(remainingFuel/10);
        fuelTank.setY(fuelTank.getY() + 0.1); // Moves down as fuel depletes
    }
    
    /**
     * initialize the height and width of the pane in which the ship is.
     */
    public void initPaneHeightAndWidth(){
    
        Parent simulationPane = this.getSpaceShipGroup().getParent();
        Parent anchorPane = simulationPane.getParent();
        Bounds simPaneBounds = anchorPane.getBoundsInParent();
        
        simulationPaneHeight = simPaneBounds.getMaxY() - simPaneBounds.getMinY();
  
        simulationPaneWidth = simPaneBounds.getMaxX() - simPaneBounds.getMinX();
    
    }

    /**
     * Initializes the initial x and y position of the space ship group.
     * @param x x position of the spaceship before translation.
     * @param y y position of the spaceship before translation.
     */
    public void initInitialSpaceShipPos(double x, double y) {

        getSpaceShipGroup().setTranslateX(x);
        getSpaceShipGroup().setTranslateY(y);
        shipInitialHeight = simulationPaneHeight - y;
    }
    /**
     * Normalizes the current angle of the ship.
     * @return the normalized angle.
     */
    public double getNormalizedAngleWithVertical(){
        
        double degreeFromThevertical = angleWithVertical % 360;
        //int quadrant = 0;
        if(degreeFromThevertical < 0)
            degreeFromThevertical += 360;
        //quadrant = (int) ((degreeFromThevertical/90) % 4 + 1);
        if(degreeFromThevertical > 180)
            degreeFromThevertical = 360 - degreeFromThevertical;
        return degreeFromThevertical;
    }
    /**
     * Resets the initial position of the ship and the rotate variables as well as the forces acting on it.
     */
    public void resetShip() {

        initInitialSpaceShipPos(simulationPaneHeight/9, simulationPaneHeight/11);
        thrustImageView.setTranslateY(spaceShipImageView.getBoundsInParent().getMaxY() - thrustImageViewOffset);
        spaceShipImageView.setImage(spaceShipImage);
        initFuelTankCoord();
        
        // applies every inverted transform to the ship to bring it back to its initial angle.
        spaceShipGroup.getTransforms().addAll(resetTransforms);
        // emptying the list for the next call of this method.
        resetTransforms.clear();
        
        getSpaceShipGroup().getTransforms().clear();
        angleWithVertical = 0;
        angleCounter = 0;
        initFuelTankCoord();

    }
    /**
     * @param isAlive whether the ship is alive or not.
     */
    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
    /**
     * @return whether the ship is alive or not.
     */
    public boolean getIsAlive() {
        return isAlive;
    }
    /** 
     * @return the list containing the hitboxes.
     */
    public Node[] getHitBoxes() {
        Node[] hitBoxArray = new Node[5];
        for (int i = 0; i < hitBoxArray.length; i++) {
            hitBoxArray[i] = spaceShipGroup.getChildren().get(i);
        }
        return hitBoxArray;
    }
    /**
     * Modifies the state of the engine by taking in the newEngineState boolean.
     * @param newEngineState represents whether the ship is using fuel or not.
     */
    public void modifyEngineState(boolean newEngineState) {
        thrustImageView.setVisible(newEngineState);

    }
    /**
     * This method is called every frame of the simulation and applies
     * the rotate Transform with the passed in parameters every frame.
     * An inverse of each of these transforms is then added inside resetTransforms.
     * @param angle the angle of rotation.
     * @throws NonInvertibleTransformException 
     */
    public void initTransforms(double angle) throws NonInvertibleTransformException {
        angleCounter += angle;

        angleWithVertical = angleCounter;
        rotateGroup = new Rotate(angle, pivotX, pivotY);

        //here adds the transform
        getSpaceShipGroup().getTransforms().addAll(rotateGroup);
        //adds the transforms inverse, this list is the used in the resetShip() method.
        resetTransforms.add(rotateGroup.createInverse());

    }

    /**
     * @return The horizontal component of the thrust force.
     */
    public double getThrustForceInX() {

        double angle = Math.toRadians(Math.abs(angleCounter));
        if (angleCounter < 0) {

            return thrustForce * (Math.cos(angle + (Math.PI / 2)));
        } else if (angleCounter == 0) {
            return 0;
        }

        return thrustForce * (Math.sin(angle));
    }
    /**
     * @return The vertical component of the thrust force.
     */
    public double getThrustForceInY() {
        double angle = Math.toRadians(Math.abs(angleCounter));
        if (angleCounter < 0) {
            return thrustForce * (Math.sin(angle + (Math.PI / 2)));
        } else if (angleCounter == 0) {
            return thrustForce;
        }
        return thrustForce * (Math.cos(angle));
    }
    public double getThrustForce() {
        return thrustForce;
    }
    /**
     * @param thrustForce magnitude of the thrust force applied to the spaceship. 
     */
    public void setThrustForce(double thrustForce) {
        this.thrustForce = thrustForce;
    }
    /**
     * @param thrustForceInX The horizontal component of the thrust force.
     */
    public void setThrustForceInX(double thrustForceInX) {
        this.thrustForceInX = thrustForceInX;
    }
    /**
     * @param thrustForceInY The vertical component of the thrust force.
     */
    public void setThrustForceInY(double thrustForceInY) {
        this.thrustForceInY = thrustForceInY;
    }
    /**
     * @return The ImageView representing the spaceship's thrust.
     */
    public ImageView getThrustImageView() {
        return thrustImageView;
    }
    /**
     * @param thrustImageView The ImageView representing the spaceship's thrust.
     */
    public void setThrustImageView(ImageView thrustImageView) {
        this.thrustImageView = thrustImageView;
    }
    /**
     * @return The current horizontal velocity of the spaceship.
     */    
    public double getCurrentXVelocity() {
        return currentXVelocity;
    }
    /**
     * @return The current vertical velocity of the spaceship.
     */
    public double getCurrentYVelocity() {
        return currentYVelocity;
    }
    /**
     * @return The current height of the spaceship.
     */
    public double getCurrentHeight() {
        return currentHeight;
    }
    /**
    * @param currentXVelocity The current horizontal velocity of the spaceship.
    */
    public void setCurrentXVelocity(double currentXVelocity) {
        this.currentXVelocity = currentXVelocity;
    }
    /**
     * @param currentYVelocity The current vertical velocity of the spaceship.
     */
    public void setCurrentYVelocity(double currentYVelocity) {
        this.currentYVelocity = currentYVelocity;
    }
    /**
     * @param currentHeight The current height of the spaceship.
     */
    public void setCurrentHeight(double currentHeight) {
        this.currentHeight = currentHeight;
    }
    /**
     * @return the X-coordinate of the pivot point of the space ship's thruster
     */
    public double getThrustPivotX() {
        return thrustPivotX;
    }

    /**
     * @param thrustPivotX the new X-coordinate of the pivot point of the space ship's thruster
     */
    public void setThrustPivotX(double thrustPivotX) {
        this.thrustPivotX = thrustPivotX;
    }

    /**
     * @return the Y-coordinate of the pivot point of the space ship's thruster
     */
    public double getThrustPivotY() {
        return thrustPivotY;
    }

    /**
     * @param thrustPivotY the new Y-coordinate of the pivot point of the space ship's thruster
     */
    public void setThrustPivotY(double thrustPivotY) {
        this.thrustPivotY = thrustPivotY;
    }

    /**
     * @return the mass of the space ship
     */
    public double getMass() {
        return mass;
    }

    /**
     * @return the image view of the space ship
     */
    public ImageView getSpaceShipImageView() {
        return spaceShipImageView;
    }

    /**
     * @param spaceShipImageView the new image view of the space ship
     */
    public void setSpaceShipImageView(ImageView spaceShipImageView) {
        this.spaceShipImageView = spaceShipImageView;
    }

    /**
     * @return the node representing the space ship
     */
    public Node getSpaceShipNode() {
        return spaceShipNode;
    }

    /**
     * @param spaceShipNode the new node representing the space ship
     */
    public void setSpaceShipNode(Node spaceShipNode) {
        this.spaceShipNode = spaceShipNode;
    }

    public double getAngleCounter() {
        return angleCounter;
    }

    public void setAngleCounter(double angleCounter) {
        this.angleCounter = angleCounter;
    }
    /**
     * @return the height of the simulation pane.
     */
    public double getSimulationPaneHeight() {
        return simulationPaneHeight;
    }
    /**
     * @param simulationPaneHeight the height of the simulation pane.
     */
    public void setSimulationPaneHeight(double simulationPaneHeight) {
        this.simulationPaneHeight = simulationPaneHeight;
    }
    /**
     * @return the initial height of the ship.
     */
    public double getShipInitialHeight() {
        return shipInitialHeight;
    }
    /**
     * @param shipInitialHeight the initial height of the ship.
     */
    public void setShipInitialHeight(double shipInitialHeight) {
        this.shipInitialHeight = shipInitialHeight;
    }
    /**
     * @return the position of the thrustImageView inside the children of the spaceShipGroup.
     */
    public int getThrustImageViewPosInGroup() {
        return thrustImageViewPosInGroup;
    }
    /**
     * @param thrustImageViewPosInGroup position of the thrustImageView inside the children of the spaceShipGroup.
     */
    public void setThrustImageViewPosInGroup(int thrustImageViewPosInGroup) {
        this.thrustImageViewPosInGroup = thrustImageViewPosInGroup;
    }
    /**
     * @return the remaining fuel.
     */
    public double getRemainingFuel() {
        return remainingFuel;
    }
    /**
     * @param remainingFuel remaining fuel.
     */
    public void setRemainingFuel(double remainingFuel) {
        this.remainingFuel = remainingFuel;
    }
    /**
     * @return the width of the spaceShip imageView.
     */
    public double getSpaceShipWidth() {

        return getSpaceShipImageView().getBoundsInLocal().getWidth();
    }
    /**
     * @return the space ship group.
     */
    public Group getSpaceShipGroup() {
        return spaceShipGroup;
    }

    /**
     * @param spaceShipGroup the space Ship group.
     */
    public void setSpaceShipGroup(Group spaceShipGroup) {
        this.spaceShipGroup = spaceShipGroup;
    }
    
}
