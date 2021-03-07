/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacao;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Edgar
 */
public class FXMain extends Application {
    
    private static Scene mainScene;
    
    public static Scene getMainScene(){
        return mainScene;
    }
    
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml")); //instanciando uma tela
            ScrollPane scrollpane = loader.load();
            
            scrollpane.setFitToHeight(true);//comando para ajustar[REDIMENSINAR] altura do menu ao tamanho da tela(scrollpane)
            scrollpane.setFitToWidth(true);//comando para ajustar[REDIMENSINAR] largura do menu ao tamanho da tela(scrollpane)
            
            mainScene = new Scene(scrollpane);
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Sample JavaFX application");
            primaryStage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    
}
