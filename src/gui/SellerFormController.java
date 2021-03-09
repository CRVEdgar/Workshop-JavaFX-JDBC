/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Departament;
import model.entities.Seller;
import model.exception.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

/**
 *
 * @author Edgar
 */
public class SellerFormController implements Initializable {
        @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }
    
    private Seller entidade;
    private SellerService service;
    private DepartmentService departmentService;
    
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>(); //VIDEO 284

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker dpBirthDate;
    @FXML
    private TextField txtBaseSalary;
    
    @FXML
    private ComboBox<Departament> comboBoxDepartament;
    
    @FXML
    private Label lblErroNome;
    @FXML
    private Label lblErroBirthDate;
    @FXML
    private Label lblErroEmail;
    @FXML
    private Label lblErroBaseSalary;
    
    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;
    
    private ObservableList<Departament> obsList; // eh carregado a partir do metodo: [loadAssociatedObjects]
    
    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtNome, 70);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail, 40);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
        initializeComboBoxDepartment();
    }
    
    public void setSeller(Seller entidade){
        this.entidade = entidade;
    }
    
    public void setServices(SellerService service, DepartmentService departmentService){ //chamado [injetado] os servicos na SellerListController]
        this.service = service;
        this.departmentService = departmentService;
    }
    
    public void inscreverDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }
    
    private void notifyDataChangeListeners() { //VIDEO 284
        for(DataChangeListener listener : dataChangeListeners){
            listener.onDataChanger(); // chamando[notificando com] o metodo -> DepartmentListController -> onDataChanger
        }
    }
    
    //Salvando no banco de dados apos click[VIDEO 283]
    @FXML
    public void onBtSaveAction(ActionEvent evento){
        if(entidade == null){
            throw new IllegalStateException("Entidade Nula - Devera ser setada na classe [SellerListControlle]");
        }
        if(service == null){
            throw new IllegalStateException("Service Nulo - Devera ser setado na classe [SellerListControlle]");
        }
        try{
            entidade = getFormData();
            service.saveOrUpdate(entidade);
            notifyDataChangeListeners(); //VIDEO 284 - notifica quando ocorrer uma alteracao
            Utils.currentStage(evento).close();//pega a referencia da janela atual e fecha
            Alerts.showAlert("Adicionando Vendedor", null, "Vendedor Adicionado com sucesso", Alert.AlertType.INFORMATION);
        }catch(ValidationException e){
            setErrorMessages(e.getErros());
        }
        catch(DbException e){
            Alerts.showAlert("Erro ao salvar no Banco de dados", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    private Seller getFormData(){ //metodo que captura o que eh digitado nos campos do formulario da view[departmentForm]
        Seller obj = new Seller();
        
        ValidationException excessao = new ValidationException("Erro de Validacao");
        
        obj.setId(Utils.tryParseToInt(txtId.getText()));//converte o que foi digitado pata int [gui.Utilis->tryParseToInt]
        //VIDEO 285 - testando se o label de nome eh vazio, se for uma excessao eh lancada, senao os dados sao inseridos
        if(txtNome.getText()== null || txtNome.getText().trim().equals("")){
            excessao.addError("Nome Excessao", "* O campo nao pode ser vazio"); // adicionando um erro
        }
        obj.setName(txtNome.getText()); //adiciona no objeto o que foi digitado no label
        
        if(txtEmail.getText()== null || txtEmail.getText().trim().equals("")){
            excessao.addError("Email Excessao", "* O campo nao pode ser vazio"); // adicionando um erro
        }
        obj.setEmail(txtEmail.getText());//adiciona no objeto o que foi digitado no label
        
        if(dpBirthDate.getValue() == null){
            excessao.addError("BirthDate Excessao", "* O campo nao pode ser vazio"); // adicionando um erro
        }else{
            Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setBirthdate(Date.from(instant));  
        }
            
        if(txtBaseSalary.getText()== null || txtBaseSalary.getText().trim().equals("")){
            excessao.addError("Salario Base Excessao", "* O campo nao pode ser vazio"); // adicionando um erro
        }
        obj.setBasesalary(Utils.tryParseToDouble(txtBaseSalary.getText())); //converte o que foi digitado pata double [gui.Utilis->tryParseToInt]
        //adiciona no objeto o que foi digitado no label
        
        obj.setDepartment(comboBoxDepartament.getValue()); //pega o que foi selecionado no comboBox e insere na entidade vendedor
        
        if(excessao.getErros().size() > 0){ // se houver alguma erro na escessao personalizada, a esxcessao sera lancada, "quebrando o sistema"
            throw excessao;
        }
        //se nao houver execesao, o objeto eh retonado;
        return obj;
    }
    //metodo que preenche os erros no label de erros (lblErros) com a mesnagem da excecao contida em getFormData -> excessao.addError
    public void setErrorMessages(Map<String, String> erros){
        Set<String> fields = erros.keySet();
        //testando se houve erro com os nomes nos parametros dos blocos IF (nome, emial, salario base)
        if(fields.contains("Nome Excessao")){ //o nome do parametro deve ser igual ao da excessao criada em [getFormData] (excessao.addError)
           lblErroNome.setText(erros.get("Nome Excessao")); //pegando a mensagem da excessao com titulo "Nome Excessao" e setando no label
        }else{
           lblErroNome.setText("");
        }
        
        // SUBSTITUIDO PELO BLOCO IF ELSE ACIMA -> lblErroNome.setText(fields.contains("Nome Excessao") ? erros.get("Nome Excessao") : "");
        lblErroEmail.setText(fields.contains("Email Excessao") ? erros.get("Email Excessao") : "");
        lblErroBaseSalary.setText(fields.contains("Salario Base Excessao") ? erros.get("Salario Base Excessao") : "");
        lblErroBirthDate.setText(fields.contains("BirthDate Excessao") ? erros.get("BirthDate Excessao") : "");
        
        /* os itens abaixos foram substituidos pela declaracao ternario acima
        if(fields.contains("Email Excessao")){//o nome do parametro deve ser igual ao da excessao criada em [getFormData] (excessao.addError)
           lblErroEmail.setText(erros.get("Email Excessao")); //pegando a mensagem da excessao com titulo "Email Excessao" e setando no label
        }
        
        if(fields.contains("Salario Base Excessao")){//o nome do parametro deve ser igual ao da excessao criada em [getFormData] (excessao.addError)
           lblErroBaseSalary.setText(erros.get("Salario Base Excessao")); //pegando a mensagem da excessao com titulo "Salario Base Excessao" e setando no label
        }
        
        if(fields.contains("BirthDate Excessao")){//o nome do parametro deve ser igual ao da excessao criada em [getFormData] (excessao.addError)
           lblErroBirthDate.setText(erros.get("BirthDate Excessao")); //pegando a mensagem da excessao com titulo "BirthDate Excessao" e setando no label
        }
        */
    }
    
    @FXML
    public void onBtCancelAction(ActionEvent evento){
        Utils.currentStage(evento).close();//pega a referencia da janela atual e fecha
    }
    
    public void updateFormData(){//metodo que faz as atualizacoes em tempo de execucao - automaticamente
        
        if( entidade == null){
            throw new IllegalStateException("Entidade Nula - a classe Vendedor deve ser setado");
            //Alerts.showAlert("Entidade NULL", null, "Entidade Nula - A entidade Departamento devera ser Carregada", AlertType.ERROR);
        }
        txtId.setText(String.valueOf(entidade.getId()));
        txtNome.setText(entidade.getName());
        txtEmail.setText(entidade.getEmail());
        Locale.setDefault(Locale.US);
        txtBaseSalary.setText(String.format("%.2f",entidade.getBasesalary()));
        if(entidade.getBirthdate()!=null){//so converte se a data for diferente de nulo, ou seja quando clicar no botao "CADASTRAR NOVO VENDEDOR" a conversao nao sera feita // se clicar em editar(campo ao lado da tupla) a conversao sera feita
            dpBirthDate.setValue(LocalDate.ofInstant(entidade.getBirthdate().toInstant(), ZoneId.systemDefault()));
        }                                                                            //pega o fusorario do computador da pessoa que estiver utilizando o sistema
        if(entidade.getDepartment() == null){ //se estiver casdastrando um novo vendedor, o departamento sera nulo, e o comboBox setara na primeira ocorrencia da lista
            //OPCIONAL SETAR NO PRIMEIRO
            comboBoxDepartament.getSelectionModel().selectFirst(); //associa o first primeiro depto
        }else{ // se nao for nulo, ou seja, se estiver fazendo uma atualizacao, o comboBox associara com o depto do vendedor
            comboBoxDepartament.setValue(entidade.getDepartment()); // o departamento que estiver associado ao vendedor, ira pro comboBox
        }    
    }
    
    //metodo responsavel por carregar no obsList a lista do BD
    public void loadAssociatedObjects(){
        if(departmentService == null){
            throw new IllegalStateException("DepartamentService estava nulo - o atributo deve ser setado");
        }
        List<Departament> lista = departmentService.findAll();
        obsList = FXCollections.observableArrayList(lista); // convertenso e inseridno a lista no obsList
        comboBoxDepartament.setItems(obsList); // inserindo no combobox a lista de departamentos
    }
    
    //metodo para inicializar o comboBox
    private void initializeComboBoxDepartment() {
        Callback<ListView<Departament>, ListCell<Departament>> factory = lv -> new ListCell<Departament>() {
            @Override
            protected void updateItem(Departament item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartament.setCellFactory(factory);
        comboBoxDepartament.setButtonCell(factory.call(null));
    }
}
