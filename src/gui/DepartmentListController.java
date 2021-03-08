/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import aplicacao.FXMain;
import gui.util.Alerts;
import gui.util.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
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
    private void onBtNewAction(ActionEvent evento){
        Stage parentStage = Utils.currentStage(evento);//chama o metod da classe Utils que eh responsavel por devolver o Stage onde ocorre o evento
        Departament obj = new Departament();
        
        createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
    }
    
    public void setDepartmentService(DepartmentService service){ //chamado [injetado] na MainViewController
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
    
    //metodo para carregar a janela do frmulario para preencher um novo departamento
    public void createDialogForm(Departament obj, String absoluteName, Stage parentStage){ //parametro faz referencia ao nome da View a janela que criou a janela de dialogo
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load(); //painel para carregar a view
            
            //injentando o departamento [passado no parametro] no controlador
            DepartmentFormController controller = loader.getController(); //capturando a View
            controller.setDepartment(obj);//setando o Departamento no controlador
            controller.setDepartmentService(new DepartmentService()); //injetando um servico 
            controller.updateFormData();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Entrada de Dados do Departamento");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);// Stage pai da janela de dialogo [ a que eh recebida como parametro]
            dialogStage.initModality(Modality.WINDOW_MODAL); //janela modal, trava as demais janelas, enquanto a janela de dialogo[dialogStage] nao for fechada, as demais janelas nao vao poder ser utilizadas[bloqueio na janela de dialogo]
            dialogStage.showAndWait();
            
        }catch(IOException e){
            Alerts.showAlert("IO Exception", "Erro ao carregar View", e.getMessage(), AlertType.ERROR);
        }
    }
    
}
