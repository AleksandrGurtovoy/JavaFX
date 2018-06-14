/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javafxapplication.managers;

import java.io.Serializable;
import java.util.List;
import javafxapplication.JavaFXApplication;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Admin
 */

public class GenericDAO<T extends Serializable> {
    private EntityManagerFactory emf = JavaFXApplication.emf;
    private EntityManager em;
    private Class<T> clazz;
    public EntityTransaction entityTransaction;
    public GenericDAO(Class<T> clazz) {
        this.clazz = clazz;
        em = emf.createEntityManager();
        entityTransaction = em.getTransaction();
        entityTransaction.begin();
    }
    
    @Override
    protected void finalize() throws Throwable{
        entityTransaction.commit();
        em.close();
        super.finalize();
    }
    
    public void create(T t) {     
        em.persist(t);
    }
    
    public T getByField(String fieldname, String value){
        try{
            Query q = em.createQuery("SELECT u FROM "+ clazz.getSimpleName() +" u WHERE u."+ fieldname +" = ?1");
            return (T)q.setParameter(1, value).getSingleResult();
        }catch(Exception ex){
            return null;
        }
    }

    public T read(long id) {
       return em.find(clazz, id);
   }

    public void update(T t) {
        em.merge(t);
        entityTransaction.commit();
        entityTransaction.begin();
    }

    public void delete(T t) {
        t = em.merge(t);
        em.remove(t);
    }
    
    public List getAll() {
        return em.createQuery("SELECT u FROM "+ clazz.getSimpleName() +" u").getResultList();
    }
    
    public void updateList(){
       entityTransaction.commit();
       entityTransaction.begin();
    }
}

