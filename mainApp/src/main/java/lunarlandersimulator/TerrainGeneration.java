/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lunarlandersimulator;

import java.util.stream.IntStream;
import java.util.Random;
import static javafx.geometry.Pos.BASELINE_LEFT;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * A class that generates terrain for the Lunar Lander space shuttle to avoid
 * and eventually land on. Generate a random string, called the seed. Parse seed
 * and based on generation, create
 *
 * @author theo
 */
public class TerrainGeneration  extends LunarLanderWorld {

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


    public double computeNormalizedHeight(int lookback, int j, int[] seed, int heightFactor){
        double sum = heightFactor + seed[j] * heightFactor;

        for (int i = 1; i <= lookback; i++) {
            sum += heightFactor + seed[j - i] * heightFactor;
        }

        return sum / (lookback + 1);
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
        double strokeWidth = 3;
        double blockWidth = 60;
        double height;
        double prevHeight = 0;
        double nextHeight = 0;
        int normalizationFactor = 2;

        for (int j = 0; j < seed.length; j++) {
            height = computeNormalizedHeight(Math.min(j, normalizationFactor), j, seed, heightFactor);

            nextHeight = (j < seed.length - 1) ? computeNormalizedHeight(Math.min(j+1, normalizationFactor), j+1, seed, heightFactor) : 0;

            Rectangle terrainBlock = new Rectangle(blockWidth, height, Color.BLACK);

            Line Horizontalline = new Line(strokeWidth/2, 0, blockWidth - strokeWidth/2, 0);
            Horizontalline.setStroke(Color.DARKBLUE);
            Horizontalline.setStrokeWidth(strokeWidth);
            Group cell = new Group();

            cell.getChildren().addAll(terrainBlock, Horizontalline);
            Line verticalLine;


            if(height > nextHeight){

                verticalLine = new Line(blockWidth - strokeWidth/2, 0, blockWidth - strokeWidth/2, height - nextHeight);
                verticalLine.setStroke(Color.DARKBLUE);
                verticalLine.setStrokeWidth(strokeWidth);
                cell.getChildren().add(verticalLine);
            }

            if (height > prevHeight && j != 0){

                verticalLine = new Line(strokeWidth/2, 0, strokeWidth/2,  height - prevHeight);
                verticalLine.setStroke(Color.DARKBLUE);
                verticalLine.setStrokeWidth(strokeWidth);
                cell.getChildren().add(verticalLine);
            }

            terrainRow.getChildren().add(cell);
            prevHeight = height;
        }

        terrainRow.setAlignment(BASELINE_LEFT);

        currentTerrainHbox = terrainRow;
        return terrainRow;
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
