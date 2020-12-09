package tbajfx.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tbajfx.db.entity.Entity;


public final class EntityManager {
    
    private static EntityManager instance;
    private Map<String, EntityStorage> storage;

    private EntityManager()
    {
        storage = new HashMap<>();
    }
    
    public static EntityManager getInstance()
    {
        if (instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }

    private static Map<String, EntityStorage> getStorage()
    {
        return getInstance().storage;
    }

   
    private static EntityStorage getEntityStorage(String tableName)
    {
        // Garde en mémoire le cache
        Map<String, EntityStorage> storage = getStorage();
        // Si le cache ne contient pas encore d'unité de stockage pour l'entité demandée...
        if (storage.get(tableName) == null) {
            // ...crée une nouvelle entité de stockage vide associée à cette entité
            storage.put(tableName, new EntityStorage());
        }
        // Renvoie l'unité de stockage associée à l'entité demandée
        return storage.get(tableName);
    }


    private static void store(String tableName, Entity object)
    {
        // Ajoute un nouvel objet indexé en fonction de son ID dans cette unité de stockage
        getEntityStorage(tableName).put(object);
    }


    private static Entity fetch(String tableName, int id)
    {
        return getEntityStorage(tableName).getById(id);
    }


    public static <T extends Entity> List<T> findAll(Repository<T> repository)
    {
        try {
            String query = "SELECT * FROM `" + repository.getTableName() + "`";
            // Vérifie si l'ensemble de la table concernée a déjà été récupéré en base de données,
            // et renvoie les objets en cache le cas échéant
            List<T> result = (List<T>)getEntityStorage(repository.getTableName()).getQueryResults(query);
            if (result != null) {
                return result;
            }
            // Crée une liste prête à accueillir les objets qui vont être créés à partir des données récupérées
            result = new ArrayList<>();
            // Envoie une requête en base de données et récupère les résultats
            ResultSet set = DatabaseHandler.query(query);
            // Tant qu'il reste des résultats non traités, prend le résultat suivant...
            while (set.next()) {
                // ... et crée un objet à partir des colonnes présentes dans ce résultat
                T object = repository.instantiateFromResultSet(set);
                store(repository.getTableName(), object);
                // Ajoute l'objet à la liste
                result.add(object);
            }
            getEntityStorage(repository.getTableName()).storeQueryResults(query, (ArrayList<Entity>)result);
            // Renvoie la liste
            return result;
        }
        catch (SQLException exception) {
            System.out.println(exception);
            System.exit(1);
            return null;
        }
    }


    public static <T extends Entity> T findById(Repository<T> repository, int id)
    {
        try {
            // Vérifie si l'objet existe déjà dans le cache, et le renvoie le cas échéant
            T object = (T)fetch(repository.getTableName(), id);
            if (object != null) {
                return object;
            }
            // Envoie une requête en base de données
            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
            PreparedStatement statement = dbHandler.getConnection().prepareStatement("SELECT * FROM `" + repository.getTableName() + "` WHERE `id` = ?"
                // Rajouter ces deux lignes si on rencontre une erreur de type "Operation not allowed for a result set of type ResultSet.TYPE_FORWARD_ONLY"
                // ,ResultSet.TYPE_SCROLL_SENSITIVE
                // ,ResultSet.CONCUR_UPDATABLE
            );
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();

            // Comme on sait que la requête peut uniquement renvoyer un seul résultat (s'il existe),
            // ou aucun (s'il n'existe pas), cherche le premier résultat de la requête...
            if (set.first()) {
                // ...et renvoie un nouvel objet à partir de ses données
                object = repository.instantiateFromResultSet(set);
                store(repository.getTableName(), object);
                return object;
            // Si la requête ne renvoie aucun résultat, renvoie null
            } else {
                return null;
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(1);
            return null;
        }
    }


    public static <T extends Entity> List<T> findByCriteria(Repository<T> repository, HashMap<String, String> criteria)
    {
        try {
            // Récupère l'ensemble des noms de colonnes
            String[] columnNames = new String[ criteria.size() ];
            columnNames = criteria.keySet().toArray(columnNames);

            String[] criteriaToInsert = new String[ criteria.size() ];

            for (int i = 0; i < criteria.size(); i++) {
                criteriaToInsert[i] = "`" + columnNames[i] + "` = ?";
            }

            // Construit la requête SQL...
            String sql =
                // ...avec la clause SELECT FROM et le nom de la table...
                "SELECT * FROM `" + repository.getTableName() + "` WHERE "
                // ...la liste des colonnes à insérer, séparées par une virgule...
                + String.join(" AND ", criteriaToInsert);

            // Crée la requête SQL à partir de le chaîne constituée précédemment
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(sql);
            // Remplit les champs variables avec les différentes valeurs des propriétés
            for (int i = 0; i < criteria.size(); i += 1) {
                statement.setString(i + 1, criteria.get( columnNames[i] ) );
            }

            // Vérifie si les résultats de la requête ont déjà été récupérées en base de données,
            // et renvoie les objets en cache le cas échéant
            List<T> result = (List<T>)getEntityStorage(repository.getTableName()).getQueryResults(statement.toString());
            if (result != null) {
                return result;
            }

            // Crée une liste prête à accueillir les objets qui vont être créés à partir des données récupérées
            result = new ArrayList<>();

            ResultSet set = statement.executeQuery();
            // Tant qu'il reste des résultats non traités, prend le résultat suivant...
            while (set.next()) {
                // ... et crée un objet à partir des colonnes présentes dans ce résultat
                T object = repository.instantiateFromResultSet(set);
                store(repository.getTableName(), object);
                // Ajoute l'objet à la liste
                result.add(object);
            }
            getEntityStorage(repository.getTableName()).storeQueryResults(statement.toString(), (ArrayList<Entity>)result);
            // Renvoie la liste
            return result;
        }
        catch (SQLException exception) {
            System.out.println(exception);
            System.exit(1);
            return null;
        }
    }


    /**
     * Create new record in database based on this object's properties
     * @see AbstractModel#save()
     */
    public static <T extends Entity> void insert( Repository<T> repository, T object )
    {
        try {
        // Récupère l'ensemble des propriétés avec leur valeur, tel que défini dans la classe enfant
            HashMap<String, String> properties = object.getProperties();
            // Récupère l'ensemble des noms de colonnes
            String[] columnNames = new String[ properties.size() ];
            columnNames = properties.keySet().toArray(columnNames);
            // Prépare un tableau pour contenir les noms de colonne prêts à être insérés dans la requête SQL
            String[] columnNamesToInsert = new String[ properties.size() ];
            // Ajoute des backticks (`) autour de chaque nom de colonne
            for (int i = 0; i < columnNames.length; i += 1) {
                String columnName = columnNames[i];
                columnNamesToInsert[i] = "`" + columnName + "`";
            }
            // Prépare un tableau rempli de "?" pour faire office de champs variables
            String[] placeholders = new String[ properties.size() ];
            Arrays.fill(placeholders, "?");
            
            // Construit la requête SQL...
            String sql =
                // ...avec la clause INSERT INTO et le nom de la table...
                "INSERT INTO `" + repository.getTableName() + "` ("
                // ...la liste des colonnes à insérer, séparées par une virgule...
                + String.join(", ", columnNamesToInsert) +
                // ...la liste des champs variables, séparées par une virgule
                ") VALUES (" + String.join(", ", placeholders) + ");" ;
            
            // Crée la requête SQL à partir de le chaîne constituée précédemment
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // Remplit les champs variables avec les différentes valeurs des propriétés
            for (int i = 0; i < properties.size(); i += 1) {
                statement.setString(i + 1, properties.get( columnNames[i] ) );
            }
            // Exécute la requête SQL
            statement.executeUpdate();
            getEntityStorage(repository.getTableName()).resetQueryResults();;
            // Récupère l'ID généré par la BDD et l'associe à l'objet
            ResultSet set = statement.getGeneratedKeys();
            if (set.first()) {
                object.setId(set.getInt(1));
                return;
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * Update macthing existing record in database based on this object's properties
     * @see AbstractModel#save()
     */
    public static <T extends Entity> void update( Repository<T> repository, T object )
    {
        try {
            // Récupère l'ensemble des propriétés avec leur valeur, tel que défini dans la classe enfant
            HashMap<String, String> properties = object.getProperties();
            // Récupère l'ensemble des noms de colonnes
            String[] columnNames = new String[ properties.size() ];
            columnNames = properties.keySet().toArray(columnNames);
            // Construit la liste des assignations à effectuer
            String[] assignments = new String[ properties.size() ];
            for (int i = 0; i < properties.size(); i += 1) {
                assignments[i] = "`" + columnNames[i] + "` = ?";
            }

            // Construit la requête SQL...
            String sql =
                // ...avec la clause UPDATE et le nom de la table...
                "UPDATE `" + repository.getTableName() + "` SET " +
                // ...la liste des assignations à effectuer, séparées par une virgule...
                String.join(", ", assignments) +
                // ...la clause WHERE permettant de cibler uniquement l'enregistrement désiré
                " WHERE `id` = ?;";
            
            // Crée la requête SQL à partir de le chaîne constituée précédemment
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // Remplit les champs variables avec les différentes valeurs des propriétés
            for (int i = 0; i < properties.size(); i += 1) {
                statement.setString(i + 1, properties.get( columnNames[i] ) );
            }
            statement.setInt(properties.size() + 1, object.getId());
            // Exécute la requête SQL
            statement.executeUpdate();
            getEntityStorage(repository.getTableName()).resetQueryResults();;
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * Delete matching record from database
     */
    public static <T extends Entity> void delete( Repository<T> repository, T object )
    {
        try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement("DELETE FROM `" + repository.getTableName() + "` WHERE `id` = ?");
            statement.setInt(1, object.getId());
            statement.executeUpdate();
            object.setId(0);
            getEntityStorage(repository.getTableName()).resetQueryResults();;
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

}
