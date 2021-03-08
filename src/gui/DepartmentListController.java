/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import aplicacao.FXMain;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Departament;
import model.services.DepartmentService;

/**
 * FXML Controller class
 *
 * @author Edgar
 */
public class DepartmentListController implements Initializable {
    private DepartmentService service;
    
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
    private TableView<Departament> tblVwDpto;
    
    @FXML
    private TableColumn<Departament, Integer> tblColunaId;
    
    @FXML
    private TableColumn<Departament, String> tblColunaNome;
    
    @FXML
    private Button btNew;
    
    private ObservableList<Departament> obsList;//responsavel por associar o objeto no TableView (tblVwDpto)
    
    @FXML
    private void onBtNewAction(){
        System.out.println("onbtnewAction");
    }
    
    public void setDepartmentService(DepartmentService service){
        this.service = service;
    }
    
    //VIDEO 277
    public void updateTableView(){ //metodo que acessa o servico, carrega os departamento e insere na ObservableList
        if(service == null){
            throw new IllegalStateException("Servico nulo - o metodo [setDepartmentService] nao foi setado");
        }
        List<Departament> lista = service.findAll();
        obsList = FXCollections.observableArrayList(lista); //carregando a lista dentro do ObservableList responsavel por carregar o TableView
        tblVwDpto.setItems(obsList); // carregando os itens na tableView e mostrar na tela
    }
    
}
