/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import aplicacao.FXMain;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

/**
 * FXML Controller class
 *
 * @author Edgar
 */
public class DepartmentListController implements Initializable {
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializaNodes();
    }
    
    private void inicializaNodes() {//iniciar o comportamento das colunas de a cordo com os atributos do objeto Department
        tblColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tblColunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        //acompanhar [redimencionar] largura e altura da janela
        Stage stage = (Stage) FXMain.getMainScene().getWindow();
        tblVwDpto.prefHeightProperty().bind(stage.heightProperty());
    }
    
    @FXML
    private TableView<Department> tblVwDpto;
    
    @FXML
    private TableColumn<Department, Integer> tblColunaId;
    
    @FXML
    private TableColumn<Department, String> tblColunaNome;
    
    @FXML
    private Button btNew;
    
    @FXML
    private void onBtNewAction(){
        System.out.println("onbtnewAction");
    }

    
}
