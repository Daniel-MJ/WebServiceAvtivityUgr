import org.bson.Document;
import org.restlet.representation.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Delete;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import org.restlet.data.Status;
import org.restlet.resource.PathVariable;
import org.restlet.representation.Representation;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.List;

public class CrudResourceMongo extends ServerResource {

    // Crear la conexión a MongoDB en la inicialización
    private MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private MongoDatabase database = mongoClient.getDatabase("mi_base_de_datos");
    private MongoCollection<Document> collection = database.getCollection("mi_coleccion");

    @Get("json")
    public List<Document> handleGet() {
        // Método para obtener todos los documentos en la colección
        return collection.find().into(List.class);
    }

    @Get("json/{id}")
    public Representation handleGetById(@PathVariable("id") String id) {
        // Método para obtener un documento por ID
        Document document = collection.find(new Document("_id", id)).first();

        if (document != null) {
            return new JsonRepresentation(document.toJson());
        } else {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return null;
        }
    }

    @Post("json")
    public void handlePost(Representation entity) {
        // Método para crear un nuevo documento
        Document newDocument = Document.parse(entity.getText());
        // Insertar el nuevo documento en la colección
        collection.insertOne(newDocument);
    }

    @Put("json/{id}")
    public void handlePut(@PathVariable("id") String id, Representation entity) {
        // Método para actualizar un documento existente por ID
        Document updatedDocument = Document.parse(entity.getText());
        // Realizar la actualización en la colección
        collection.replaceOne(new Document("_id", id), updatedDocument);
    }

    @Delete("json/{id}")
    public void handleDelete(@PathVariable("id") String id) {
        // Método para eliminar un documento por ID
        collection.deleteOne(new Document("_id", id));
    }

    @Override
    protected void doRelease() {
        // Cerrar la conexión de MongoDB cuando se liberen los recursos
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
