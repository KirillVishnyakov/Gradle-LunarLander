/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lunarlandersimulator;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The highest level, the GameEngine class runs every frame and handles the animation timing.
 * @author Kirill Vishnyakov
 */
public class GameEngine {
    private static final Log log = LogFactory.getLog(GameEngine.class);
    /**
     * Space ship object.
     */
    private SpaceShip ship = new SpaceShip();
    /**
     * Reference to the Lunar Lander simulation Pane
     */
    private Pane LunarlanderSimulationPane;
    /**
     * CollisionEngine object taking in the space ship object as a parameter.
     */
    public CollisionEngine collisionHandler = new CollisionEngine(ship);
    /**
     * Desired frames per second of the gameLoop.
     */
    private static final int FPS = 60;
    /**
     * The time each frame of the animation lasts.
     */
    private static final long FRAME_DURATION = 1000000000L / FPS;
    /**
     * The time elapsed between each frame.
     */
    private double elapsedTime;
    /**
     * The initial time recorded by the system in nanoseconds.
     */
    public long initialTimeNanoseconds = System.nanoTime();
    /**
     * counts the number of frames elapsed.
     */
    private int frameCounter = 0;
    /**
     * Indicates whether the simulation is running/on/active or not.
     */
    private boolean isRunning = true;
    long pausedTimeStart = 0;
    long pausedTimeEnd = 0;
    double pausedTime = 0;
    boolean wasPaused = false;
    public long lastTime = System.nanoTime();
    private long frameTime = 0;
    public double currentNetTime;
    protected final AnimationTimer timer = new AnimationTimer() {



        /**
         * The handle method that is called on each frame update.
         *
         * @param currentTime The current time in nanoseconds.
         */
        @Override
        public void handle(long currentTime) {

            if(wasPaused){
                pausedTime += pausedTimeEnd - pausedTimeStart;
                wasPaused = false;
            }

            elapsedTime = currentTime - lastTime - pausedTime;
            lastTime = currentTime;
            frameTime += elapsedTime;
            pausedTime = 0;

            // If the frame time has reached the desired frame duration, update and render
            if (frameTime >= FRAME_DURATION) {
                update();
                render();
                frameTime -= FRAME_DURATION;
            }

        }
    };

    /**
     * Starts the animation timer used for rendering and updating the game loop.
     */
    public void start() {
        timer.start();
        pausedTimeEnd = System.nanoTime();
    }

    /**
     * Stops the animation timer used for rendering and updating the game loop.
     */
    public void stop() {
        pausedTimeStart = System.nanoTime();
        wasPaused = true;
        timer.stop();

    }

    protected void changeGravity() {

    }

    /**
    * Updates the game state, including checking for collisions, updating statistics,
    * and managing ship physics.
    */
    protected void update() {
        
        checkCollisions();
        updateStats(elapsedTime);
        //System.out.println(elapsedTime);
         if (!(collisionHandler.getHasShipTouchedDown()))
            { 
            rotateShip();
            }

        // Manage ship physics and stop the timer if the ship has crashed.
        if (updateShipPosAndCheckGroundCollision((FRAME_DURATION) / 1e9)) {
            //stops if the ship has collided with the ground.
            stop();

        }
    }
    /**
     * Checks collisions.
     */
    protected void checkCollisions() {

    }
    /**
     * Updates statistics about the ship.
     * @param elapsedTime time elapsed between frames.
     */
    protected void updateStats(double elapsedTime) {

    }
    /**
     * manage the ship physics.
     * @param dt time elapsed between last time.
     * @return whether the ship has crashed.
     */
    protected boolean updateShipPosAndCheckGroundCollision(double dt) {

        return false;
    }
    /**
     * Manages the rotation of the ship.
     * 
     */
    protected void rotateShip() {

    }

    private void render() {

    }
    
    /**
     * @return The pane used for the lunar lander simulation.
     */
    public Pane getLunarlanderSimulationPane() {
        return LunarlanderSimulationPane;
    }

    /**
     * Sets the pane used for the lunar lander simulation.
     *
     * @param LunarlanderSimulationPane The pane used for the lunar lander simulation.
     */
    public void setLunarlanderSimulationPane(Pane LunarlanderSimulationPane) {
        this.LunarlanderSimulationPane = LunarlanderSimulationPane;

        // Set the space simulation pane for the collision handler
        collisionHandler.setSpaceSimulationPane(LunarlanderSimulationPane);
    }
    public boolean getIsRunning() {
        return isRunning;
    }
    public void setIsRunning(boolean b) {
        isRunning = b;
    }
    public SpaceShip getShip() {
        return ship;
    }

    public void setShip(SpaceShip ship) {
        this.ship = ship;
    }

    public CollisionEngine getCollisionHandler() {
        return collisionHandler;
    }

    public void setCollisionHandler(CollisionEngine collisionHandler) {
        this.collisionHandler = collisionHandler;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public long getInitialTimeNanoseconds() {
        return initialTimeNanoseconds;
    }

    public void setInitialTimeNanoseconds(long initialTimeNanoseconds) {
        this.initialTimeNanoseconds = initialTimeNanoseconds;
    }

    public int getFrameCounter() {
        return frameCounter;
    }

    public void setFrameCounter(int frameCounter) {
        this.frameCounter = frameCounter;
    }
    

}
