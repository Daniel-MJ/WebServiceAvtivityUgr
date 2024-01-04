import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.util.Series;
import org.restlet.Server;
import org.restlet.data.Parameter;
//import org.restlet.resource.ClientResource;

public class ExampleRestlet {

	public static void main(String[] args) throws Exception {  
	    // Create a new Component.  
	    Component component = new Component();
		
		// Add a new HTTPS server listening on port 8183
        Server server = component.getServers().add(Protocol.HTTPS, 8183);  
        Series <Parameter> parameters = server.getContext().getParameters();
        parameters.add("sslContextFactory",
        "org.restlet.engine.ssl.DefaultSslContextFactory");
        parameters.add("keyStorePath", "certs/localhost.jks");
        parameters.add("keyStorePassword", "NoloVasEncontrar");
        parameters.add("keyPassword", "NoloVasEncontrar");
        parameters.add("keyStoreType", "JKS");

	    // Add a new HTTP server listening on port 8182.  
	    //component.getServers().add(Protocol.HTTP, 8182);  

	    // Attach the sample application.  
	    component.getDefaultHost().attach("/ApiServerWeb", new FirstStepsApplication());
	    // Start the component.  
	    component.start();  
		//new ClientResource("http://restlet.com").get().write(System.out);
	}

}
