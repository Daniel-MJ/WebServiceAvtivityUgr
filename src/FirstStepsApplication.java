import java.util.Arrays;
import java.util.HashSet;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.engine.application.CorsFilter;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.data.Method;
import org.restlet.service.CorsService;
import org.restlet.util.Series;
import org.restlet.routing.Filter;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Header;
//import org.restlet.Request;


public class FirstStepsApplication extends Application {

    private Filter createCorsFilter(Restlet next) {
        Filter filter = new Filter(getContext(), next) {
            @SuppressWarnings("unchecked")
            @Override
            protected int beforeHandle(Request request, Response response) {
                // Initialize response headers

                Series<Header> responseHeaders = (Series<Header>) response
                            .getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
                if (responseHeaders == null) {
                    responseHeaders = new Series<Header>(Header.class);
                }

                // Request headers

                Series<Header> requestHeaders = (Series<Header>) request.getAttributes().get(HeaderConstants.ATTRIBUTE_HEADERS);
                String requestOrigin = requestHeaders.getFirstValue("Origin", false, "*");
                String rh = requestHeaders.getFirstValue("Access-Control-Request-Headers", false, "*");

                // Set CORS headers in response

                responseHeaders.set("Access-Control-Expose-Headers","Authorization, Link");
                responseHeaders.set("Access-Control-Allow-Credentials", "true");
                responseHeaders.set("Access-Control-Allow-Methods","GET,POST,PUT,DELETE");
                responseHeaders.set("Access-Control-Allow-Origin", requestOrigin);
                responseHeaders.set("Access-Control-Allow-Headers", rh);

                // Set response headers

                response.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS,
                    responseHeaders);

                // Handle HTTP methods

                if (org.restlet.data.Method.OPTIONS.equals(request.getMethod())) {
                    return Filter.STOP;
                }
                return super.beforeHandle(request, response);
            }
            
        };
        return filter;
    }
    /**
     * Creates a root Restlet that will receive all incoming calls.
     */

    @Override
    public synchronized Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a new instance of HelloWorldResource.
        Router router = new Router(getContext());
        ChallengeAuthenticator authenticator = new ChallengeAuthenticator(getContext(), ChallengeScheme.HTTP_BASIC, "Área protegida de la aplicación");
        
        // Defines only one route
        router.attach("/mongodb", MongoAllDocument.class);
        router.attach("/searchActivities", SearchForParameters.class);
        router.attach("/createUser", CreateNewUser.class);
        router.attach("/manageActivities", ManageActivities.class);

        //OptionsFilter optionsFilter = new OptionsFilter(getContext(), router);
        
        // // Crear el filtro CorsFilter y adjuntarlo al enrutador
       // NewCorsFilter corsFilter = new NewCorsFilter(getContext(), router);
        // corsFilter.setAllowingAllRequestedHeaders(true);
        // //corsFilter.setAllowedOrigins( new HashSet(Arrays.asList("*")));
        // corsFilter.setAllowedCredentials(true);
        // corsFilter.setDefaultAllowedMethods(new HashSet<>(Arrays.asList(Method.OPTIONS,Method.GET)));
        // corsFilter.setSkippingResourceForCorsOptions(true);
        // corsFilter.setAllowedOrigins(new HashSet(Arrays.asList("http://localhost:4200")));
        
        // Crear el filtro de autenticación ChallengeAuthenticator
        authenticator.setVerifier(new VerificadorUsuarios());
        authenticator.setNext(router);
        
        // return optionsFilter;
        return createCorsFilter(authenticator);

    }


}

        // // Configurar CorsService para permitir solicitudes desde http://localhost:4200
        // CorsService corsService = new CorsService();
        // corsService.setAllowingAllRequestedHeaders(true);
        // corsService.setAllowedOrigins( new HashSet(Arrays.asList("*")));
        // corsService.setAllowedCredentials(true);
        // corsService.setDefaultAllowedMethods(new HashSet<>(Arrays.asList(Method.OPTIONS,Method.GET)));
        // corsService.setSkippingResourceForCorsOptions(true);

        // // Crear el filtro CorsFilter y adjuntarlo al enrutador
        // CorsFilter corsFilter = new CorsFilter(getContext(), router);
        // corsFilter.setAllowingAllRequestedHeaders(true);
        // corsFilter.setAllowedOrigins( new HashSet(Arrays.asList("*")));
        // corsFilter.setAllowedCredentials(true);
        // corsFilter.setDefaultAllowedMethods(new HashSet<>(Arrays.asList(Method.OPTIONS,Method.GET)));
        // corsFilter.setSkippingResourceForCorsOptions(true);
        // corsFilter.setAllowedOrigins(new HashSet(Arrays.asList("http://localhost:4200")));
        
        //return corsFilter;
