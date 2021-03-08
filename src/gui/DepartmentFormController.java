/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import gui.util.Constraints;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Edgar
 */
public class DepartmentFormController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNome;
    
    @FXML
    private Label lblErroNome;
    
    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;
    
    @FXML
    public void onBtSaveAction(){
        System.out.println("onBtSaveAction");
    }
    
    @FXML
    public void onBtCancelAction(){
        System.out.println("onBtCancelAction");
    }
    
    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtNome, 30);
    }
}
