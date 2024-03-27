import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.restlet.security.Verifier;
import io.jsonwebtoken.Jwts;
import java.util.Date;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bson.Document;

public class AuthenticationController extends ServerResource {

    // Generar una clave secreta aleatoria para HMAC SHA-256
    private static SecretKey generateSecretKey() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            byte[] keyBytes = new byte[32]; // Longitud de la clave en bytes para HMAC SHA-256
            secureRandom.nextBytes(keyBytes);
            return new SecretKeySpec(keyBytes, "HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Manejar la excepción adecuadamente (por ejemplo, lanzar una excepción personalizada o retornar null)
            return null;
        }
    }
    // Usar la clave generada
    private static final SecretKey SECRET_KEY = generateSecretKey();
    //clave_secreta_para_firmar_el_token_jwt
    @Post("json")
    public Representation authenticateUser(StringRepresentation representation) {
        // Obtener el cuerpo JSON de la solicitud
        String jsonBody = representation.getText();
        Document userData = Document.parse(jsonBody);
        String username = userData.getString("username");
        String password = userData.getString("password");
        System.out.println("user:" + username);
        System.out.println("pass:" + password);

        // Verificar las credenciales utilizando la clase VerificadorUsuarios
        VerificadorUsuarios verifier = new VerificadorUsuarios();
        int authenticationResult = verifier.verify(username, password.toCharArray());

        // Manejar el resultado de la autenticación
        if (authenticationResult == Verifier.RESULT_VALID) {
            // Generar y retornar un token JWT
            String jwtToken = generateJwtToken(username);
            return new StringRepresentation(jwtToken);
        } else {
            // Retornar un mensaje de error en caso de credenciales inválidas
            return new StringRepresentation ("Credenciales inválidas");
        }
    }

    private String generateJwtToken(String username) {
        // Obtener la fecha actual y sumarle 1 hora
        Date expirationDate = new Date(System.currentTimeMillis() + 3600 * 1000); // 1 hora en milisegundos
        // Generar una clave secreta con la que se firmará el token

        // Construir el token JWT con el nombre de usuario como sujeto
        String jwtToken = Jwts.builder()
                .claim("sub", username)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();

        return jwtToken;
    }
}

