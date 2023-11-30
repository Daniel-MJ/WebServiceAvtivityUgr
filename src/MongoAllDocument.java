import java.util.ArrayList;
import java.util.List;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.bson.Document;

/**
 * Resource which has only one representation.
 */
public class MongoAllDocument extends ServerResource {

    @Get("json")
    public List<String> getAllDocumentsAsJson() {
        // Obtener el parámetro "miBasedeDatos" de la solicitud
        String databaseName = getQueryValue("miBasedeDatos");
        
        // Obtener el parámetro "miColeccion" de la solicitud
        String collectionName = getQueryValue("miColeccion");

        // Validar que los parámetros no sean nulos o vacíos antes de usarlos

        // Obtener todos los documentos usando MongoDBConnector
        MongoDBManager connector = new MongoDBManager(databaseName, collectionName);
        List<Document> allDocuments = connector.getAllDocuments();

        // Convierte los documentos a cadenas usando document.toString()
        List<String> allDocumentsJson = new ArrayList<>();

        for (Document document : allDocuments) {
            String json = document.toString();
            allDocumentsJson.add(json);
        }

        return allDocumentsJson;
    }
}
