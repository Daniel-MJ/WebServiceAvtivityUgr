import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.bson.Document;
import org.restlet.representation.StringRepresentation;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public class CreateNewUser extends ServerResource {

    @Post("json")
    public Representation createUsers(StringRepresentation representation) {
        try {
            // Obtener el cuerpo JSON de la solicitud
            String jsonBody = representation.getText();

            // Parsear el JSON para obtener los datos del nuevo usuario
            // (Asegúrate de manejar excepciones y validar la entrada correctamente)
            Document userData = Document.parse(jsonBody);

            // Extraer datos del usuario del documento
            String username = userData.getString("username");
            String password = userData.getString("password");

            // Validar y procesar los datos según sea necesario
            
            // Generar salteado aleatorio
            byte[] salt = generateSalt();

            // Calcular hash con salteado y almacenar el resultado
            String hashedPassword = hashPassword(password.toCharArray(), salt);

            System.out.println("Usuario: " + username);
            System.out.println("Contraseña: " + hashedPassword);
 
            // Crear documento para almacenar en MongoDB
            Document userDocument = new Document("username", username)
                    .append("hashedPassword", hashedPassword)
                    .append("salt", Base64.getEncoder().encodeToString(salt));

            // Insertar el documento en la colección de usuarios
            MongoDBManager connector = new MongoDBManager("base_prueba_v0", "UsersApi");
            connector.insertDocument(userDocument);

            return new StringRepresentation("Nuevo usuario creado con éxito");
        } catch (Exception e) {
            e.printStackTrace();
            return new StringRepresentation("Error al crear el nuevo usuario");
        }
    }

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private static String hashPassword(char[] password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(new String(password).getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar el hash de la contraseña.", e);
        }
    }
}
