import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;

import org.restlet.security.SecretVerifier;
//import io.jsonwebtoken.Header;
//import io.jsonwebtoken.Jwt;
public class JwtVerifier extends SecretVerifier {

    private static final SecretKey SECRET_KEY = SecretKeyManager.getInstance().getSecretKey();

    @Override
    public int verify(String identifier, char[] secret) {
        throw new UnsupportedOperationException("Not supported");
    }

    public boolean verifyToken(String token) {
        try {
            // Verificar el token JWT utilizando la clave secreta
            Jws<Claims> jws = Jwts.parser()
            .verifyWith(SECRET_KEY)
            .build()
            .parseSignedClaims(token);

            Claims claims = jws.getPayload();
            // Si el token es válido, la verificación se completa exitosamente
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("El token ha expirado: " + e.getMessage());
            e.printStackTrace();
        } catch (MalformedJwtException | SignatureException e) {
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