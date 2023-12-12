import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class SearchForParameters extends ServerResource {

    @Get("json")
    public String handleGetRequest() {
        String path = getReference().getPath();
        switch (path) {
            case "/forDate":
                String fechaInicioParam = getQueryValue("fechaInicio");
                String fechaFinalParam = getQueryValue("fechaFinal");

                // Validar que los parámetros no sean nulos o vacíos
                if (fechaInicioParam == null || fechaFinalParam == null) {
                    return "Parámetros de fecha no proporcionados";
                }

                // Mostrar las variables de entrada
                System.out.println("Fecha de inicio: " + fechaInicioParam);
                System.out.println("Fecha final: " + fechaFinalParam);

                // Convertir las fechas a objetos Date
                Date fechaInicio, fechaFinal;
                try {
                    fechaInicio = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(fechaInicioParam);
                    fechaFinal = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(fechaFinalParam);
                } catch (ParseException e) {
                    return "Formato de fecha no válido";
                }
            case "/ruta2":
                return metodoRuta2();
            // Agrega más casos según tus necesidades
            default:
                return "Ruta no manejada";
        }
    }

    private String metodoRuta2() {
        // Lógica para manejar la ruta2
        return "Manejando ruta2";
    }
}

