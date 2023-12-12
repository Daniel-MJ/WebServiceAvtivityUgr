import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.bson.Document;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class para_despues extends ServerResource {

    @Get("json")
    public String handleGetRequest() {
        String path = getReference().getPath();
        switch (path) {
            case "/ruta1":
                String fechaInicioParam = getQueryValue("fechaInicio");
                String fechaFinalParam = getQueryValue("fechaFinal");

                // Validar que los parámetros no sean nulos o vacíos
                if (fechaInicioParam == null || fechaFinalParam == null) {
                    return "Parámetros de fecha no proporcionados";
                }

                // Convertir las fechas a objetos Date
                Date fechaInicio, fechaFinal;
                try {
                    fechaInicio = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(fechaInicioParam);
                    fechaFinal = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(fechaFinalParam);
                } catch (ParseException e) {
                    return "Formato de fecha no válido";
                }

                // Realizar la búsqueda en la colección
                List<Document> actividadesEnRango = buscarActividadesPorFecha(fechaInicio, fechaFinal);

                // Convertir los resultados a JSON y devolverlos
                return convertirActividadesAJson(actividadesEnRango);

            // Agrega más casos según tus necesidades
            default:
                return "Ruta no manejada";
        }
    }

    private List<Document> buscarActividadesPorFecha(Date fechaInicio, Date fechaFinal) {
        // Realizar la búsqueda en la colección de MongoDB
        MongoDBManager connector = new MongoDBManager("base_prueba_v0", "mi_coleccion");
        List<Document> todasLasActividades = connector.getAllDocuments();

        // Filtrar las actividades que caen dentro del rango de fechas
        return todasLasActividades.stream()
                .filter(actividad -> {
                    Date fechaActividadInicio = actividad.getDate("fechaInicio");
                    Date fechaActividadFinal = actividad.getDate("fechaFinal");
                    return fechaActividadInicio != null && fechaActividadFinal != null &&
                            fechaActividadInicio.after(fechaInicio) && fechaActividadFinal.before(fechaFinal);
                })
                .collect(Collectors.toList());
    }

    private String convertirActividadesAJson(List<Document> actividades) {
        // Convertir los documentos a cadenas JSON
        List<String> actividadesJson = actividades.stream()
                .map(Document::toJson)
                .collect(Collectors.toList());

        // Devolver la lista como un JSON array
        return "[" + String.join(",", actividadesJson) + "]";
    }
}

