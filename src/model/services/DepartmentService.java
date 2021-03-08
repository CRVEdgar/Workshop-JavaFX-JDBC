/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.services;

import java.util.ArrayList;
import java.util.List;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Departament;


public class DepartmentService {
    private DepartmentDao dao = DaoFactory.createDepartmentDao();
    
    public List<Departament> findAll(){
       return dao.findAll();
    }
}
