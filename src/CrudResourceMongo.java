import org.bson.Document;
import org.restlet.resource.*;
import org.restlet.representation.Representation;

import java.util.List;

public class CrudResourceMongo extends ServerResource {
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
        Document newDocument = new Document();
        // Parsear el JSON de la entidad y agregar campos al nuevo documento
        // ...

        // Insertar el nuevo documento en la colección
        collection.insertOne(newDocument);
    }

    @Put("json/{id}")
    public void handlePut(@PathVariable("id") String id, Representation entity) {
        // Método para actualizar un documento existente por ID
        Document updatedDocument = new Document();
        // Parsear el JSON de la entidad y actualizar campos en el documento
        // ...

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