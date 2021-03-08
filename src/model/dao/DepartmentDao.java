/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.util.List;
import model.entities.Departament;

/**
 *
 * @author Edgar
 */
public interface DepartmentDao {
    
    void insert(Departament obj);
    void update(Departament obj);
    void deleteById(Integer id);
    Departament findByIdSeller(Integer id);
    List<Departament> findAll();
}
