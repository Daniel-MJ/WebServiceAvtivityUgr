import org.restlet.security.MapVerifier;

public class VerificadorUsuarios extends MapVerifier {

    public VerificadorUsuarios() {
        // Agregar usuarios con nombre de usuario y contraseña
        super.getLocalSecrets().put("usuario1", "contrasena1".toCharArray());
        super.getLocalSecrets().put("usuario2", "contrasena2".toCharArray());
        // Agregar más usuarios según sea necesario
    }
}