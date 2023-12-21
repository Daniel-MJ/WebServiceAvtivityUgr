import java.util.Arrays;
import java.util.HashSet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.engine.application.CorsFilter;
import org.restlet.routing.Router;
//import org.restlet.service.CorsService;
import org.restlet.security.ChallengeAuthenticator;


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
        router.attach("/searchActivities", SearchForParameters.class);

        
        // Crear el filtro de autenticación ChallengeAuthenticator
        ChallengeAuthenticator authenticator = new ChallengeAuthenticator(getContext(), ChallengeScheme.HTTP_BASIC, "Área protegida de la aplicación");
        authenticator.setVerifier(new VerificadorUsuarios());
        authenticator.setNext(router);

        return authenticator;

        // Configurar CorsService para permitir solicitudes desde http://localhost:4200
        //CorsService corsService = new CorsService();
        //corsService.setAllowingAllRequestedHeaders(true);

        //corsService.setAllowedCredentials(true);

        // Crear el filtro CorsFilter y adjuntarlo al enrutador
        /*CorsFilter corsFilter = new CorsFilter(getContext(), router);
        corsFilter.setNext(router);
        corsFilter.setAllowingAllRequestedHeaders(true);
        corsFilter.setAllowedOrigins(new HashSet(Arrays.asList("http://localhost:4200")));
        corsFilter.setAllowedCredentials(true);*/

        
        //return corsFilter;
        

        //return router;
    }

}