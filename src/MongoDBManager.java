import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoDBManager {

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public MongoDBManager(String databaseName, String collectionName) {

        //*********************************
        //Usuarios para leer Documnetos de las colecciones de base_prueba_v0
        String username = "insertUser";
        String password = "Polo67";

        
        //*********************************
        //Usuarios para leer Documnetos de las colecciones de base_prueba_v0
        //String username = "ReadActivities";
        //String password = "pepito34";

         // Crear las credenciales
        MongoCredential credential = MongoCredential.createCredential(username, databaseName, password.toCharArray());

          // Configurar la dirección del servidor
        ServerAddress serverAddress = new ServerAddress("localhost", 27017);

        // Configurar el cliente MongoDB con las credenciales
        MongoClientSettings settings = MongoClientSettings.builder()
        .credential(credential)
        .applyToClusterSettings(builder ->
                builder.hosts(Arrays.asList(serverAddress)))
        .build();

        // Crear la conexión a MongoDB
        this.mongoClient = MongoClients.create(settings);

        // Seleccionar la base de datos y la colección
        this.database = mongoClient.getDatabase(databaseName);
        this.collection = database.getCollection(collectionName);
    }

    public List<Document> getAllDocuments() {
        // Obtener todos los documentos en la colección
        //return collection.find().pretty().into(new ArrayList<>());
        return collection.find().projection(Projections.excludeId()).into(new ArrayList<>());
    }

    public Document getDocumentById(String id) {
        // Obtener un documento por ID
        return collection.find(Filters.eq("_id", id)).first();
    }

    public void insertDocument(Document document) {
        // Insertar un nuevo documento en la colección
        collection.insertOne(document);
    }

    public void updateDocument(String id, Document updatedDocument) {
        // Actualizar un documento existente por ID
        collection.replaceOne(Filters.eq("_id", id), updatedDocument);
    }

    public void deleteDocument(String id) {
        // Eliminar un documento por ID
        collection.deleteOne(Filters.eq("_id", id));
    }

    public void closeConnection() {
        // Cerrar la conexión de MongoDB
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
