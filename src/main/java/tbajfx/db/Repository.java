package tbajfx.db;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

import tbajfx.db.entity.Entity;

abstract public class Repository<T extends Entity> {
    
    public List<T> findAll()
    {
        return EntityManager.<T>findAll( this );
    }

    public T findById(int id)
    {
        return EntityManager.<T>findById( this, id );
    }

    public List<T> findByCriteria( HashMap<String, String> criteria )
    {
        return EntityManager.<T>findByCriteria( this, criteria );
    }

    /**
     * Update matching database record based on this object's properties
     */
    public void save(T object)
    {
        // Si l'objet n'existe pas encore en BDD
        if (object.getId() == 0) {
            // Crée un enregistrement à partir des propriétés de l'objet
            EntityManager.<T>insert( this, object );
        // Sinon
        } else {
            // Modifie l'enregistrement déjà existant à partir des propriétés de l'objet
            EntityManager.<T>update( this, object );
        }
    }

    public void delete(T object)
    {
        EntityManager.<T>delete( this, object );
    }

    /**
     * Specify table name associated with this entity
     */
    abstract public String getTableName();

    abstract public T instantiateFromResultSet(ResultSet set) throws SQLException;

}
