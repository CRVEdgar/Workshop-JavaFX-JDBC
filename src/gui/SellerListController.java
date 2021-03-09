/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import aplicacao.FXMain;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
//import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
//import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.Pane;
//import javafx.stage.Modality;
import javafx.stage.Stage;
//import model.entities.Departament;
import model.entities.Seller;
//import model.services.DepartmentService;
import model.services.SellerService;

/**
 *
 * @author Edgar
 */
public class SellerListController implements Initializable, DataChangeListener{
    private SellerService service;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializaNodes();
    }
    
    @Override
    public void onDataChanger() { //metodo responsavel por atualizar a tabela quando o mesmo for chamado em outra classe; : DepartmentFormController -> inscreverDataChangeListener -> notifyDataChangeListeners(e aqui ele chama com listener.onDataChanger();
        updateTableView();
    }
    
    private void inicializaNodes() {//iniciar o comportamento das colunas de a cordo com os atributos do objeto Department
        tblColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tblColunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        //acompanhar [redimencionar] largura e altura da janela
        Stage stage = (Stage) FXMain.getMainScene().getWindow();
        tblVwSeller.prefHeightProperty().bind(stage.heightProperty());
    }
    
    @FXML
    private TableView<Seller> tblVwSeller;
    
    @FXML
    private TableColumn<Seller, Integer> tblColunaId;
    
    @FXML
    private TableColumn<Seller, String> tblColunaNome;
    
    @FXML
    TableColumn<Seller, Seller> tableColumnEDIT;
    
    @FXML
    TableColumn<Seller, Seller> tableColumnREMOVE;
    
    @FXML
    private Button btNew;
    
    private ObservableList<Seller> obsList;//responsavel por associar o objeto no TableView (tblVwDpto)
    
    @FXML
    private void onBtNewAction(ActionEvent evento){
        Stage parentStage = Utils.currentStage(evento);//chama o metod da classe Utils que eh responsavel por devolver o Stage onde ocorre o evento
        Seller obj = new Seller();
        
        createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
    }
    
    public void setSellerService(SellerService service){ //chamado [injetado] na MainViewController
        this.service = service;
    }
    
    //VIDEO 277
    public void updateTableView(){ //metodo que acessa o servico, carrega os departamento e insere na ObservableList
        if(service == null){
            throw new IllegalStateException("Servico nulo - o metodo [setSellerService] nao foi setado");
        }
        List<Seller> lista = service.findAll();
        obsList = FXCollections.observableArrayList(lista); //carregando a lista dentro do ObservableList responsavel por carregar o TableView
        tblVwSeller.setItems(obsList); // carregando os itens na tableView e mostrar na tela
        initEditButtons(); //VIDEO 286 - acrescentar um novo botao em cada linha da tabela para EDITAR
        initRemoveButtons();  //VIDEO 287 - acrescentar um novo botao em cada linha da tabela para APAGAR 
    }
    
    //metodo para carregar a janela do frmulario para preencher um novo departamento
    public void createDialogForm(Seller obj, String absoluteName, Stage parentStage){ //parametro faz referencia ao nome da View a janela que criou a janela de dialogo
       /* try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load(); //painel para carregar a view
            
            //injentando o departamento [passado no parametro] no controlador
            DepartmentFormController controller = loader.getController(); //capturando a View
            controller.setDepartment(obj);//setando o Departamento no controlador
            controller.setDepartmentService(new DepartmentService()); //injetando um servico
            controller.inscreverDataChangeListener(this); //VIDEO 284 - se increvendo na fila de eventos, quando o vento for disparado sera executado o metodo de atualizacao da tabela 
            controller.updateFormData();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Entrada de Dados do Departamento");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);// Stage pai da janela de dialogo [ a que eh recebida como parametro]
            dialogStage.initModality(Modality.WINDOW_MODAL); //janela modal, trava as demais janelas, enquanto a janela de dialogo[dialogStage] nao for fechada, as demais janelas nao vao poder ser utilizadas[bloqueio na janela de dialogo]
            dialogStage.showAndWait();
            
        }catch(IOException e){
            Alerts.showAlert("IO Exception", "Erro ao carregar View", e.getMessage(), Alert.AlertType.ERROR);
        }*/
    }

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("editar");
            
            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
            setGraphic(button);
            button.setOnAction( //quando o botao for clicado, abrira o formulario de edicao para alterar os dados do departamento
                event -> createDialogForm(obj, "/gui/DepartmentForm.fxml",Utils.currentStage(event)));
            }
        });
    }
    
    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("remover");

            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button); //chama o metodo para remover a tupla caso o botao seja cliclado
                button.setOnAction(event -> removeEntity(obj)); //chama o metodo que cria a caixa de dialogo pra confirmacar DEL
            }
        });
    }
    //metodo para confirmar o delete
    private void removeEntity(Seller obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Deletar Registro", "Deseja realmente apagar o registro?");
        
        if(result.get() == ButtonType.OK){
            if (service == null){
                throw new IllegalStateException("Servico Nulo - o metodo [setSellerService] nao foi setado");
            }
            try{
                service.remove(obj); //remove
                updateTableView(); // atualiza tabela
            }catch(DbIntegrityException e){
                Alerts.showAlert("Erro de Remocao", null, e.getMessage(), Alert.AlertType.ERROR);
            }
            
        }
    }
    
}
