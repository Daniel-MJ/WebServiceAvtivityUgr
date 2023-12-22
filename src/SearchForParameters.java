import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class SearchForParameters extends ServerResource {

    @Get("json")
    public Representation handleGetRequest() {
        //String path = getReference().getPath();
        //System.out.println("Solicitud recibida para la ruta: " + path);
        String metodo = getQueryValue("METODO");
        switch (metodo) {
            case "forDate":
                String fechaInicioParam = getQueryValue("fechaInicio");
                String fechaFinalParam = getQueryValue("fechaFinal");

                // Validar que los parámetros no sean nulos o vacíos
                if (fechaInicioParam == null || fechaFinalParam == null) {
                    return new StringRepresentation("Parámetros de fecha no proporcionados");
                }
                fechaInicioParam = fechaInicioParam + "T00:00:00.000Z";
                fechaFinalParam = fechaFinalParam + "T00:00:00.000Z";
                // Mostrar las variables de entrada
                System.out.println("Fecha de inicio: " + fechaInicioParam);
                System.out.println("Fecha final: " + fechaFinalParam);
                // Convertir las fechas a objetos Date
                Date fechaInicio, fechaFinal;
                try {
                    fechaInicio = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(fechaInicioParam);
                    fechaFinal = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(fechaFinalParam);
                } catch (ParseException e) {
                    return new StringRepresentation("Formato de fecha no válido");
                }

                List<Document> allDocuments = buscarActividadesPorFecha(fechaInicio, fechaFinal);
                
                // Convierte los documentos a cadenas usando document.toString()
                List<String> allDocumentsJson = new ArrayList<>();

                for (Document document : allDocuments) {
                    String json = document.toJson();
                    allDocumentsJson.add(json);
                }

                // Convierte la lista de cadenas JSON a una sola cadena JSON
                String jsonResponse = "[" + String.join(",", allDocumentsJson) + "]";

                // Retorna una representación de la cadena JSON
                return new StringRepresentation(jsonResponse);
                
                //return "Manejando /forDate. FechaInicio:" + fechaInicioParam + "fin:" + fechaFinalParam;

            case "forPlace":
                // Implementación para búsqueda por lugar
                String lugarParam = getQueryValue("lugar");
                if (lugarParam == null) {
                    return new StringRepresentation("Parámetro de lugar no proporcionado");
                }
                List<Document> actividadesPorLugar = buscarActividadesPorLugar(lugarParam);

                // Convierte los documentos a cadenas usando document.toString()
                List<String> actividadesPorLugarJson = new ArrayList<>();

                for (Document document : actividadesPorLugar) {
                    String json = document.toJson();
                    actividadesPorLugarJson.add(json);
                }

                // Convierte la lista de cadenas JSON a una sola cadena JSON
                String jsonResponseAct = "[" + String.join(",", actividadesPorLugarJson) + "]";

                // Retorna una representación de la cadena JSON
                return new StringRepresentation(jsonResponseAct);
                
            case "forCategory":
                // Implementación para búsqueda por lugar
                String CatParam = getQueryValue("categoria");
                if (CatParam == null) {
                    return new StringRepresentation("Parámetro de categoria no proporcionado");
                }
                List<Document> actividadesPorCategoria = buscarActividadesPorCategoria(CatParam);

                // Convierte los documentos a cadenas usando document.toString()
                List<String> actividadesPorCategoriaJson = new ArrayList<>();

                for (Document document : actividadesPorCategoria) {
                    String json = document.toJson();
                    actividadesPorCategoriaJson.add(json);
                }

                // Convierte la lista de cadenas JSON a una sola cadena JSON
                String jsonResponseCat = "[" + String.join(",", actividadesPorCategoriaJson) + "]";

                // Retorna una representación de la cadena JSON
                return new StringRepresentation(jsonResponseCat);

            default:
                return new StringRepresentation("Ruta no manejada" + metodo);
        }
        //return "Hello, World!" + path;
    }

    private List<Document> buscarActividadesPorFecha(Date fechaInicio, Date fechaFinal) {
        // Realizar la búsqueda en la colección de MongoDB
        MongoDBManager connector = new MongoDBManager("base_prueba_v0", "Actividades");
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

    private List<Document> buscarActividadesPorLugar(String lugar) {
        // Realizar la búsqueda en la colección de MongoDB por lugar
        MongoDBManager connector = new MongoDBManager("base_prueba_v0", "Actividades");
        List<Document> todasLasActividades = connector.getAllDocuments();

        // Filtrar las actividades que coincidan con el lugar
        return todasLasActividades.stream()
                .filter(actividad -> lugar.equals(actividad.getString("lugar")))
                .collect(Collectors.toList());
    }

    private List<Document> buscarActividadesPorCategoria(String categoria) {
        // Realizar la búsqueda en la colección de MongoDB por categoría
        MongoDBManager connector = new MongoDBManager("base_prueba_v0", "Actividades");
        List<Document> todasLasActividades = connector.getAllDocuments();
    
        // Filtrar las actividades que contengan la categoría
        return todasLasActividades.stream()
                .filter(actividad -> {
                    List<String> categorias = actividad.getList("categoria", String.class);
                    return categorias != null && categorias.contains(categoria);
                })
                .collect(Collectors.toList());
    }
    
}

