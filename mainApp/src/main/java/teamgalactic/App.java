package teamgalactic;

import lunarlandersimulator.LunarLanderWorld;
import controllerclasses.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



/**
 * 
 */
public class App extends Application {

    public Stage mainWindow;
    LunarLanderWorld lunarLander;

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        mainWindow = primaryStage;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainMenu.fxml"));
        
        loader.setController(new MainMenuController(mainWindow));
                
        
        Pane root = loader.load();
        
        Image img = new Image("/images/starryBackground.jpg");
            BackgroundImage bImg = new BackgroundImage(img,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            new BackgroundSize(1.0,1.0, true, true, false, false)
        );
        Background bGround = new Background(bImg);
        root.setBackground(bGround);
        
        Scene scene = new Scene(root);    
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Main Menu");
        primaryStage.sizeToScene();
        primaryStage.show();

        

    }

    public static void main(String[] args) {
        launch(args);

    }
}
