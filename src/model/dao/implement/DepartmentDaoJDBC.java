/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.implement;

import db.DB;
import db.DbException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.dao.DepartmentDao;
import model.entities.Departament;
import model.entities.Seller;



/**
 *
 * @author Edgar
 */
public class DepartmentDaoJDBC implements DepartmentDao{
    
    private Connection conn;
    public DepartmentDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Departament obj) {
        PreparedStatement st = null;
        String comando = "INSERT INTO department"
                       + " (Name)"
                       + " VALUES"
                       + " (?)";
        try{
            st = conn.prepareStatement(comando, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getName());
            int linhasAfetadas = st.executeUpdate();
            
            if(linhasAfetadas>0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }else{
                throw new DbException("ERRO NA INSERCAO, nehuma linha afetada");
            }
            
        }catch(SQLException e){
            throw new DbException(e.getMessage());         
        }finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Departament obj) {
        PreparedStatement st = null;
        String comando = "UPDATE department"
                        + " SET Name = ?"
                        + " WHERE Id = ?";
        
        try{
            st = conn.prepareStatement(comando);
            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());
            st.executeUpdate();
            
        }catch(SQLException e){
            throw new DbException(e.getMessage());         
        }finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        String comando = "DELETE FROM department"
                        + " WHERE Id = ?";
        try{
            st = conn.prepareStatement(comando);
            st.setInt(1, id);
            int linhasAfetadas = st.executeUpdate();
            
            if(linhasAfetadas == 0){
                throw new DbException("ERRO! OBJETO NAO ENCONTRADO NO BANCO!");
            }
            
        }catch(SQLException e){
            throw new DbException(e.getMessage());         
        }finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public Departament findByIdSeller(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String comando = "SELECT department.*,seller.Name as SellerName"
                        + " FROM department INNER JOIN seller"
                        + " ON seller.DepartmentId = department.Id"
                        + " WHERE seller.Id = ?";
        try{
            st = conn.prepareStatement(comando);
            st.setInt(1, id);
            rs = st.executeQuery();
            
            if(rs.next()){
                Departament dep = new Departament(rs.getInt("department.Id"), rs.getString("department.Name"));
                //Seller vendedor = new Seller();
                //vendedor.setName("SellerName");
                return dep;
            }
            return null;
            
        }catch(SQLException e){
            throw new DbException(e.getMessage());         
        }finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Departament> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        String comando = "SELECT * "
                        + " FROM department"
                        + " ORDER BY Id ";
        try{
            st = conn.prepareStatement(comando);
            
            rs = st.executeQuery();
            List<Departament> lista = new ArrayList<>();
            
            while(rs.next()){
                Departament dep = new Departament(rs.getInt("Id"), rs.getString("Name"));
                lista.add(dep);
            }
            return lista;
            
        }catch(SQLException e){
            throw new DbException(e.getMessage());         
        }finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
        
    }
    
}
