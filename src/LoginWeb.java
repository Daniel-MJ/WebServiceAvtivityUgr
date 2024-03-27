
import java.util.HashSet;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.restlet.util.Series;
import org.restlet.routing.Filter;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Header;
import org.restlet.data.Method;



public class LoginWeb extends Application {

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
                String requestOrigin = requestHeaders.getFirstValue("Origin", false, "http://localhost:4200");
                String rh = requestHeaders.getFirstValue("Access-Control-Request-Headers", false, "http://localhost:4200");

                // Set CORS headers in response
                // Set response headers
                HashSet<Method> methodHashSet = new HashSet<>();
                methodHashSet.add(Method.GET);
                methodHashSet.add(Method.POST);
                methodHashSet.add(Method.PUT);
                methodHashSet.add(Method.DELETE);
                
                response.setAccessControlAllowCredentials(true);
                response.setAccessControlAllowMethods(methodHashSet);
                response.setAccessControlAllowOrigin(requestOrigin);
                response.setAccessControlAllowOrigin(rh);

                java.util.Set<String> allowedHeaders = new HashSet<>();
                allowedHeaders.add("Content-Type");
                allowedHeaders.add("Authorization");
                allowedHeaders.add("X-Custom-Header");
                response.setAccessControlAllowHeaders(allowedHeaders);
               
                // Handle HTTP methods

                if (Method.OPTIONS.equals(request.getMethod())) {
                    return Filter.STOP;
                }
                return super.beforeHandle(request, response);
            }
            
        };
        return filter;
    }

    @Override
    public synchronized Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a new instance of HelloWorldResource.
        Router router = new Router(getContext());
    
        // Define routes
        router.attach("/login", AuthenticationController.class);
    
        // Return the CORS filter
        return createCorsFilter(router);
    }
    


}
