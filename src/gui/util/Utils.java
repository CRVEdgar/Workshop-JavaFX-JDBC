/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 *
 * @author Edgar
 */
public class Utils {
    
    //metodo que retorna um Stage onde oncorre um evento atual(retorna um Stage onde o evento esta acontecendo)
    public static Stage currentStage(ActionEvent evento){ 
        return (Stage) ((Node) evento.getSource()).getScene().getWindow();
    }

}
