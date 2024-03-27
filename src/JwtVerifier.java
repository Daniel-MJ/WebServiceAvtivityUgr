import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;

import javax.crypto.SecretKey;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.routing.Filter;

//import io.jsonwebtoken.Header;
//import io.jsonwebtoken.Jwt;
public class JwtVerifier extends Filter {

    public JwtVerifier() {
        super();
    }

    private static final SecretKey SECRET_KEY = SecretKeyManager.getInstance().getSecretKey();

    @Override
    protected int beforeHandle(Request request, Response response) {
        // Extraer el token del encabezado de autorización
        String token = request.getHeaders().getFirstValue("Authorization");

        // Validar el token JWT
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7); // Eliminar el prefijo "Bearer "

            // Validar el token JWT utilizando tu verificador JWT
            if (verifyToken(jwtToken)) {
                // Si el token es válido, continuar con la solicitud
                return CONTINUE;
            }
        }

        // Si el token no es válido, denegar el acceso
        response.setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
        return STOP;
    }    
    
    public boolean verifyToken(String identifier) {
            System.out.println("token: " + identifier);

            try {
                // Verificar el token JWT utilizando la clave secreta
                Jws<Claims> jws = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(identifier);

                // Si el token es válido, la verificación se completa exitosamente
                return true;
            } catch (ExpiredJwtException e) {
                System.err.println("El token ha expirado: " + e.getMessage());
                e.printStackTrace();
            } catch (MalformedJwtException | io.jsonwebtoken.security.SignatureException e) {
                System.err.println("El token no es válido o la firma es incorrecta: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Error durante la verificación del token: " + e.getMessage());
                e.printStackTrace();
            }

        // Si ocurre algún error durante la verificación, se considera que el token no es válido
        return false;
    }


}