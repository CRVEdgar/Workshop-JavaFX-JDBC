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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    
    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtNome, 70);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEmail, 40);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
    }
    
    public void setSeller(Seller entidade){
        this.entidade = entidade;
    }
    
    public void setSellerService(SellerService service){ //chamado [injetado] na DepatmenteListController]
        this.service = service;
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
        
        obj.setId(Utils.tryParseToInt(txtId.getText()));
        //VIDEO 285 - testando se o label de nome eh vazio, se for uma excessao eh lancada, senao os dados sao inseridos
        if(txtNome.getText()== null || txtNome.getText().trim().equals("")){
            excessao.addError("Nome", "* O campo nao pode ser vazio"); // adicionando um erro
        }
        obj.setName(txtNome.getText());
        
        if(excessao.getErros().size() > 0){ // se houver alguma erro na escessao personalizada, a esxcessao sera lancada, "quebrando o sistema"
            throw excessao;
        }

        return obj;
    }
    //metodo que preenche os erros no label de erros (lblErroNome)
    public void setErrorMessages(Map<String, String> erros){
        Set<String> fields = erros.keySet();
        if(fields.contains("Nome")){
           lblErroNome.setText(erros.get("Nome")); //pegando a mensagem do Nome da excessao e setando no label
        }
    }
    
    @FXML
    public void onBtCancelAction(ActionEvent evento){
        Utils.currentStage(evento).close();//pega a referencia da janela atual e fecha
    }
    
    public void updateFormData(){
        
        if( entidade == null){
            throw new IllegalStateException("Entidade Nula");
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
    }
}
