
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.routing.Router;
import org.restlet.routing.Template;

public class JwtVerifierAuth extends Restlet {

    private final Router router;

    public JwtVerifierAuth(Context context, Router router) {
        super(context);
        this.router = router;
    }

    @Override
    public void handle(Request request, Response response) {
        // Extraer el token del encabezado de autorización
        String token = request.getHeaders().getFirstValue("Authorization");

        // Validar el token JWT
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7); // Eliminar el prefijo "Bearer "

            // Validar el token JWT utilizando tu verificador JWT
            JwtVerifier verifier = new JwtVerifier();
            if (verifier.verifyToken(jwtToken)) {
                // Si el token es válido, continuar con la solicitud
                router.handle(request, response);
                return;
            }
        }

        // Si el token no es válido o no se proporcionó, denegar el acceso
        response.setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
    }
}