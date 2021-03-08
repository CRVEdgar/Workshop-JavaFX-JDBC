/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.services;

import java.util.ArrayList;
import java.util.List;
import model.entities.Department;


public class DepartmentService {
    
   public List<Department> findAll(){
       List<Department> lista = new ArrayList<>();
       lista.add(new Department(1, "Livros"));
       lista.add(new Department(2, "Eletronicos"));
       lista.add(new Department(3, "Computador"));
       return lista;
   }
}
