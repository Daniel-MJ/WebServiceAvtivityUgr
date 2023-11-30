import java.util.Arrays;
import java.util.HashSet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.service.CorsService;

public class FirstStepsApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a new instance of HelloWorldResource.
        Router router = new Router(getContext());

        // Defines only one route
        router.attach("/mongodb", MongoAllDocument.class);

        // Configurar CorsService para permitir solicitudes desde http://localhost:4200
        CorsService corsService = new CorsService();
        corsService.setAllowingAllRequestedHeaders(true);
        corsService.setAllowedOrigins(new HashSet<>(Arrays.asList("http://localhost:4200")));
        corsService.setAllowedCredentials(true);
        getServices().add(corsService);

        

        return router;
    }

}