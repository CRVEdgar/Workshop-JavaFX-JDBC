/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.util;

/**
 *
 * @author Edgar
 */

import  javafx.scene.control.TextField ;

public class Constraints {
    
    public  static  void  setTextFieldInteger ( TextField  txt ) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
	        if(newValue !=  null  &&  !newValue.matches( "\\d*" )) {
                    txt.setText (oldValue);
	        }
                });
	}

	public  static  void  setTextFieldMaxLength ( TextField  txt , int  max ) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue !=  null  && newValue.length() > max) {
                    txt.setText (oldValue);
	        }
                });
	}

	public  static  void  setTextFieldDouble ( TextField  txt ) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
                    if(newValue !=  null  &&  !newValue.matches("\\d*([\\.]\\d*)?")) {
                        txt.setText(oldValue);
                    }
                });
	}
}
