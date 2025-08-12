/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lunarlandersimulator;

import java.util.stream.IntStream;
import java.util.Random;
import static javafx.geometry.Pos.BASELINE_LEFT;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

/**
 * A class that generates terrain for the Lunar Lander space shuttle to avoid
 * and eventually land on. Generate a random string, called the seed. Parse seed
 * and based on generation, create
 *
 * @author theo
 */
public class TerrainGeneration {

    Random randomNumberGenerator = new Random();
    int streamSize = 100;
    int amountToShiftBy = 20;
    IntStream seedStream = randomNumberGenerator.ints(streamSize, 0, 17);
    HBox currentTerrainHbox;

    public TerrainGeneration() {

    }

    /**
     * Creates NEW seedStream for new terrain generations
     */
    public void refreshSeedStream() {

        seedStream = randomNumberGenerator.ints(streamSize, 0, 17);

    }

    /**
     * Receives an HBox which is used as baseline. Fills this HBox with randomly
     * sized rectangles based on the @seed Returns the now filled HBox
     *
     * @param terrainRow the HBox which will be filled with the terrain segments
     * @return returns a filled HBox full of terrain segments
     */
    public HBox generateWorld(HBox terrainRow) {
        terrainRow.getChildren().clear();
        refreshSeedStream();
        int heightFactor = 12;
        int[] seed = createSeedList();

        for (int i = 0; i < seed.length; i++) {

            Rectangle terrainBlock = new Rectangle();

            terrainBlock.setWidth(60);
            double baseHeight = heightFactor + seed[i] * heightFactor;
            int lookback = Math.min(i, 3);
            double sum = baseHeight;

            for (int j = 1; j <= lookback; j++) {
                sum += heightFactor + seed[i - j] * heightFactor;
            }
            terrainBlock.setHeight(sum / (lookback + 1));


            terrainBlock.toFront();
            terrainBlock.setId("TerrainBlgit ock");
            terrainRow.getChildren().add(terrainBlock);
        }

        terrainRow.setAlignment(BASELINE_LEFT);
        currentTerrainHbox = terrainRow;
        return terrainRow;
    }

    /**
     * Shifts the terrain to the direction of the ship. This takes the terrain
     * terrain from the HBox which holds all the terrain. First creates a copy 
     * , then removes the original, adds the terrain to the other side
     * @param whereTheShip the location of the ship, see CollisionEngine for more info.
     */
    public void shiftTerrain(int whereTheShip){
    Node tempNode;
    switch(whereTheShip){
    
        //remove 1st pos. add it to end
        case 1 -> { 
            for(int i = 0; i < amountToShiftBy ; i++ ){
  
                tempNode = currentTerrainHbox.getChildren().get(1);
                currentTerrainHbox.getChildren().remove(1);
                currentTerrainHbox.getChildren().add(tempNode);
            }
        }
        //remove last pos. add it to front
        case 2 -> {
            
            for(int i = 0; i < amountToShiftBy ; i++ ){
                tempNode = currentTerrainHbox.getChildren().get(currentTerrainHbox.getChildren().size()-1);
                currentTerrainHbox.getChildren().remove(currentTerrainHbox.getChildren().size()-1);
                currentTerrainHbox.getChildren().add(1,tempNode);
            }
            
            
        }
    
    }
    
    }

    /*
    Turns the Intstream into an array
     */
    public int[] createSeedList() {
       
        int[] seedList = seedStream.toArray();

        return seedList;
    }

    public long getStreamSize() {
        return streamSize;
    }

    public void setStreamSize(int streamSize) {
        this.streamSize = streamSize;
    }

    public IntStream getSeedStream() {
        return seedStream;
    }

    public void setSeedStream(IntStream seedStream) {
        this.seedStream = seedStream;
    }

}
