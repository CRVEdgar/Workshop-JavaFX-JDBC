/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import aplicacao.FXMain;
import gui.util.Alerts;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Edgar
 */
public class MainViewController implements Initializable {
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private MenuItem menuItemSeller;
    @FXML
    private MenuItem menuItemDepartment;
    @FXML
    private MenuItem menuItemAbout;
    
    @FXML
    public void onMenuItemSellerAction(){
        System.out.println("onMenuItemSellerAction");
    }
    
    @FXML
    public void onMenuItemDepartmentAction(){
        System.out.println("onMenuItemDepartmentAction");
    }
    
    @FXML
    public void onMenuItemAboutAction(){
        loadView("/gui/About.fxml");
    }
    

    private synchronized void loadView(String absoluteName){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVbox = loader.load(); //adicionando um vbox[o vbox loader passado por parametro] à tela MainView
            
            //Inserin uma janela dentro dentro da tela principal (MDI - janela interna)
            Scene mainScene = FXMain.getMainScene(); // capturando a cena principal para colocar o vbox[outra tela - "filha"] 
            VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); //pegando a classe principal(que eh um scrollpane) e feito um casting
            //VER VIDEO 274 com explicacao                          //acessando o conteudo
            Node mainMenu = mainVbox.getChildren().get(0);//salvando o menuBar que eh o filho (0) da cena principal
            mainVbox.getChildren().clear();//limpando a cena
            mainVbox.getChildren().add(mainMenu); //adicioando novamente o menuBar
            mainVbox.getChildren().addAll(newVbox.getChildren()); //adicionando no vbox os filhos do newVbox
            
            
        } catch (IOException ex) {
            Alerts.showAlert("IO Exception", "Erro ao carregar View[About]", ex.getMessage(), AlertType.ERROR);
        }
    }
    
}
