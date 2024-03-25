//import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.restlet.security.Verifier;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
//import javax.crypto.SecretKey;

import org.bson.Document;
import java.security.Key;
//import org.json.JSONObject;

public class AuthenticationController extends ServerResource {

    // Clave secreta para firmar el token JWT
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
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
        // Generar una clave secreta con la que se firmará el token

        // Construir el token JWT con el nombre de usuario como sujeto
        String jwtToken = Jwts.builder()
                .claim("sub", username)
                .signWith(SECRET_KEY)
                .compact();

        return jwtToken;
    }
}

