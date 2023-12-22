import org.restlet.security.MapVerifier;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
public class VerificadorUsuarios extends MapVerifier {

    private Map<String, String> saltMap = new HashMap<>();

    public VerificadorUsuarios() {

        // Obtener las credenciales desde la base de datos y almacenarlas en getLocalSecrets
        cargarCredencialesDesdeMongo();

        // Agregar usuarios con nombre de usuario y contraseña
        //super.getLocalSecrets().put("nuevoUsuario", "i/PljmlmWFF7NYZUNud6om0iQvMPg0MH+4Zv8NyvihQ=".toCharArray());
    }

    private void cargarCredencialesDesdeMongo() {
        try {
            // Conectar a la base de datos MongoDB (asegúrate de tener un servidor MongoDB en ejecución)
            MongoDBManager connector = new MongoDBManager("base_prueba_v0", "UsersApi");

            // Obtener todos los documentos de la colección
            List<Document> documentos = connector.getAllDocuments();

            // Iterar sobre los documentos y agregar las credenciales a getLocalSecrets
            for (Document userDocument : documentos) {
                String username = userDocument.getString("username");
                String hashedPassword = userDocument.getString("hashedPassword");
                String salt = userDocument.getString("salt");
                // Almacenar las credenciales en getLocalSecrets
                super.getLocalSecrets().put(username, hashedPassword.toCharArray());
                // Almacenar la sal en el mapa
                saltMap.put(username, salt);

            }

        } catch (Exception e) {
            e.printStackTrace();
            // Manejar cualquier error al cargar las credenciales desde MongoDB
        }
    }

    // Verificar la contraseña considerando la sal
    @Override
    public int verify(String identifier, char[] secret) {
        // Obtener la sal correspondiente al usuario
        String salt = saltMap.get(identifier);
        if (salt != null) {
            // Hash de la contraseña entrante con la sal
            String hashedPassword = hashPasswordWithSalt(new String(secret), salt);

            // Comparar con el hash almacenado en MongoDB
            if (getLocalSecret(identifier) != null && hashedPassword.equals(new String(getLocalSecret(identifier)))) {
                return RESULT_VALID;
            }
        }

        return RESULT_INVALID;
    }

    private String hashPasswordWithSalt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt)); // Decodificar la sal almacenada
            byte[] hashedPassword = md.digest(password.getBytes());

            // Convertir el hash a una representación en Base64 para almacenamiento
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar el hash de la contraseña con sal.", e);
        }
    }

}