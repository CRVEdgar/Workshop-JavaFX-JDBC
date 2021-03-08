/*
CLASSE RESPONSAVEL POR FAZER A CONEXAO COM O BANCO
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
    
    public void saveOrUpdate(Departament obj){
        //se o departamento nao existir, um novo departamento sera add ao Banco. Senao sera realizado um update 
        if(obj.getId() == null){
            dao.insert(obj);
        }else{
            dao.update(obj);
        }
    }
}
