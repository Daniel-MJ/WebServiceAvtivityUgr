import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.bson.Document;
//import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Resource which has only one representation.
 */
public class MongoAllDocument_old extends ServerResource {


    @Get("json")
    public List<Document> getAllDocumentsAsJson() {
        try {
            // Obtener el parámetro "miBasedeDatos" de la solicitud
            String databaseName = getQueryValue("miBasedeDatos");
            
            // Obtener el parámetro "miColeccion" de la solicitud
            String collectionName = getQueryValue("miColeccion");
    
            // Validar que los parámetros no sean nulos o vacíos antes de usarlos
    
            // Obtener todos los documentos usando MongoDBConnector
            MongoDBManager connector = new MongoDBManager(databaseName, collectionName);
            List<Document> allDocuments = connector.getAllDocuments();
    
            // Devolver la lista de documentos directamente
            return allDocuments;
        } catch (Exception e) {
            // Manejar la excepción y devolver una respuesta apropiada o registrarla para su análisis
            e.printStackTrace();
            // Devolver una lista vacía o un mensaje de error, según sea necesario
            return Collections.emptyList();
        }
    }

}
