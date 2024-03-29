import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.mongodb.MongoException;

import org.bson.Document;

public class ManageActivities extends ServerResource {

    private final MongoDBManager mongoDBManager  = new MongoDBManager("base_prueba_v0", "Actividades");
    
    @Post("json")
    public StringRepresentation agregarNuevaActividad(Representation representation) {
        try {
            // Obtener el cuerpo JSON de la solicitud
            String jsonBody = representation.getText();

            // Parsear el JSON para obtener los datos de la nueva actividad
            Document nuevaActividad = Document.parse(jsonBody);

            // Insertar la nueva actividad en la colección
            mongoDBManager.insertDocument(nuevaActividad);

            return new StringRepresentation ("Nueva actividad agregada con éxito");
        } catch (Exception e) {
            e.printStackTrace();
            return new StringRepresentation ("Error al agregar la nueva actividad");
        }
    }

    @Put("json")
    public StringRepresentation actualizarActividadPorTitulo(Representation representation) {
        try {
            // Obtener el cuerpo JSON de la solicitud
            String jsonBody = representation.getText();

            // Parsear el JSON para obtener los datos actualizados de la actividad
            Document datosActualizados = Document.parse(jsonBody);
            String titulo = datosActualizados.getString("titulo");
            // Actualizar la actividad en la colección
            mongoDBManager.updateDocumentActivities(titulo, datosActualizados);

            return new StringRepresentation ("Actividad actualizada con éxito");
        } catch (Exception e) {
            e.printStackTrace();
            return new StringRepresentation ("Error al actualizar la actividad");
        }
    }

    @Delete("json")
    public StringRepresentation eliminarActividadPorTitulo() {
        // Eliminar la actividad por su título
        try {
            String titulo = getQueryValue("titulo");
            
            mongoDBManager.deleteDocumentActivities(titulo);
            
            return new StringRepresentation ("Actividad eliminada con éxito");
        } catch (MongoException e) {
            return new StringRepresentation ("Error al eliminar la actividad");
        }
    }

}
