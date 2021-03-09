/*
CLASSE QUE REALIZA AS OPERAÇÕES CRUD NO BANCO
 */
package model.dao.implement;

import db.DB;
import db.DbException;
import java.util.List;
import model.dao.SellerDao;
import model.entities.Seller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.entities.Departament;

/**
 *
 * @author Edgar
 */
public class SellerDaoJDBC implements SellerDao{
    
    private Connection conn;
    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        
        PreparedStatement st = null;
        String comando = "INSERT INTO seller"
                       + " (Name, Email, BirthDate, BaseSalary, DepartmentId)"
                       + " VALUES"
                       + " (?, ?, ?, ?, ?)";
       try{
           st = conn.prepareStatement(comando, Statement.RETURN_GENERATED_KEYS);
           st.setString(1, obj.getName());
           st.setString(2, obj.getEmail());
           st.setDate(3, new java.sql.Date(obj.getBirthdate().getTime())); //instanciando uma data do sql
           st.setDouble(4, obj.getBasesalary());
           st.setInt(5, obj.getDepartment().getId());
           
           int linhasAfetadas = st.executeUpdate();
           
           if(linhasAfetadas > 0){
               ResultSet rs = st.getGeneratedKeys();
               if(rs.next()){
               int id = rs.getInt(1);//posicao 1[primeira coluna das chaves ~getGeneratedKeys~]
               obj.setId(id);//setando no objeto o valor do id que eh gerado automaticamente (auto_increment) no banco
               }
               DB.closeResultSet(rs);
           }else{//se nenhuma linha foi afeitada eh porque houve erro
               throw new DbException("Erro inesperado, nenhuma linha afetada");
           }
           
        }catch(SQLException e){
            throw new DbException(e.getMessage());         
        }finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement st = null;
        String comando =   "UPDATE seller"
                         + " SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?"
                         + " WHERE Id = ?";
       try{
           st = conn.prepareStatement(comando);
           st.setString(1, obj.getName());
           st.setString(2, obj.getEmail());
           st.setDate(3, new java.sql.Date(obj.getBirthdate().getTime())); //instanciando uma data do sql
           st.setDouble(4, obj.getBasesalary());
           st.setInt(5, obj.getDepartment().getId());
           st.setInt(6, obj.getId());
           
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
        String comando = "DELETE FROM seller"
                         + " WHERE id = ?";
        
        try{
            st = conn.prepareStatement(comando);
            st.setInt(1, id);
            //st.executeUpdate();
            
            ///* CASO QUEIRA TESTAR SE O ID EXISTE NO BANCO
            int linhasAfetadas = st.executeUpdate();
            if(linhasAfetadas == 0){
                throw new DbException("ERRO! OBJETO NAO ENCONTRADO NO BANCO!");
            }
           // */
            
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        
        String comando = "SELECT seller.*,department.Name as DepName"
                        + " FROM seller INNER JOIN department"
                        + " ON seller.DepartmentId = department.Id"
                        + " WHERE seller.Id = ?";
        try{
            st = conn.prepareStatement(comando);
            st.setInt(1, id);
            rs = st.executeQuery();
            if(rs.next()){ //testa se rs retornou algum resultado da consulta
                Departament dep = instantiateDepartment(rs); //instancia a partir da funcao descrita no final deste classe
                
                Seller vendedor = instantiateSeller(rs, dep); //instancia a partir da funcao descrita no final deste classe
                
                return vendedor;
            }
            return null;
        }catch(SQLException e){
            throw new DbException(e.getMessage());         
        }finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
        
    }
    
    @Override
    public List<Seller> findByDepartment(Departament department){
        PreparedStatement st = null;
        ResultSet rs = null;
        
        String comando = "SELECT seller.*,department.Name as DepName"
                        + " FROM seller INNER JOIN department"
                        + " ON seller.DepartmentId = department.Id"
                        + " WHERE DepartmentId = ?"
                        + " ORDER BY Name ";
        
        try{
            st = conn.prepareStatement(comando);
            st.setInt(1, department.getId());
            rs = st.executeQuery();
            List<Seller> lista = new ArrayList<>();
            Map<Integer, Departament> map = new HashMap<>();
            
            //a Query pode retorna mais de um resultado por isso usamos a lista e o while
            while(rs.next()){ //enquanto houver resultados retornados da busca
                
                Departament dep = map.get(rs.getInt("DepartmentId")); // realiza uma busca de acordo com DepartmentId retornado do executeQuery
                
                if(dep == null){ // se a busca retornou null, ou seja, nao a variavela ainda nao foi instanciada com o metodo instantiateDepartment, entao ela sera instanciada
                    dep = instantiateDepartment(rs); //soh eh instaciada uma unica vez (ver explicacao na pg do material)
                    map.put(rs.getInt("DepartmentId"), dep); //agora a variavel recebe um valor, entao nao caira mais no IF nos proximos loops
                }
                
                Seller vendedor = instantiateSeller(rs, dep); //instancia a partir da funcao descrita no final deste classe
                lista.add(vendedor);
                
            }
            
            return lista;
        }catch(SQLException e){
            throw new DbException(e.getMessage());         
        }finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
        
        
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        
        String comando = "SELECT seller.*,department.Name as DepName"
                        + " FROM seller INNER JOIN department"
                        + " ON seller.DepartmentId = department.Id"
                        + " ORDER BY Name ";
        
        try{
            st = conn.prepareStatement(comando);
            
            rs = st.executeQuery();
            List<Seller> lista = new ArrayList<>();
            Map<Integer, Departament> map = new HashMap<>();
            
            //a Query pode retorna mais de um resultado por isso usamos a lista e o while
            while(rs.next()){ //enquanto houver resultados retornados da busca
                
                Departament dep = map.get(rs.getInt("DepartmentId")); // realiza uma busca de acordo com DepartmentId retornado do executeQuery
                
            //se o departamento ja exixtir, ele sera reaproveitado, senao sera criada uma nova instancia
                if(dep == null){ // se a busca retornou null, ou seja, nao a variavela ainda nao foi instanciada com o metodo instantiateDepartment, entao ela sera instanciada
                    dep = instantiateDepartment(rs); //UM MESMO DEPARTAMENTO (neste caso) soh eh instaciada uma unica vez (ver explicacao na pg do material)
                    map.put(rs.getInt("DepartmentId"), dep); //agora a variavel recebe um valor, entao nao caira mais no IF nos proximos loops
                }
                
                Seller vendedor = instantiateSeller(rs, dep); //instancia a partir da funcao descrita no final deste classe
                lista.add(vendedor);
                
            }
            
            return lista;
        }catch(SQLException e){
            throw new DbException(e.getMessage());         
        }finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
      
    }

    private Departament instantiateDepartment(ResultSet rs) throws SQLException {
        Departament dep = new Departament();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        
        return dep;  
    }

    private Seller instantiateSeller(ResultSet rs, Departament dep) throws SQLException {
        
        Seller vendedor  = new Seller();
        vendedor.setName(rs.getString("Name"));
        vendedor.setId(rs.getInt("Id"));
        vendedor.setEmail(rs.getString("Email"));
        vendedor.setDepartment(dep);
        vendedor.setBirthdate(new java.util.Date(rs.getTimestamp("BirthDate").getTime()));
        //vendedor.setBirthdate(rs.getDate("BirthDate"));
        vendedor.setBasesalary(rs.getDouble("BaseSalary"));
        
        return vendedor;
    }
    
}
