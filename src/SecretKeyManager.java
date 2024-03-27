import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SecretKeyManager {
    private static SecretKeyManager instance;
    private SecretKey secretKey;

    private SecretKeyManager() {
        generateSecretKey(); // Genera la clave secreta al inicializar el singleton
        scheduleKeyGeneration(); // Programa la generación de claves cada dos días
    }

    public static SecretKeyManager getInstance() {
        if (instance == null) {
            instance = new SecretKeyManager();
        }
        return instance;
    }

    private void generateSecretKey() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            byte[] keyBytes = new byte[32]; // Longitud de la clave en bytes para HMAC SHA-256
            secureRandom.nextBytes(keyBytes);
            secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Manejar la excepción adecuadamente (lanzar una excepción personalizada, registrar el error, etc.)
        }
    }

    private void scheduleKeyGeneration() {
        Timer timer = new Timer(true); // Timer daemon para que no impida la finalización de la aplicación
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                generateSecretKey();
            }
        }, getNextExecutionTime(), 2 * 24 * 60 * 60 * 1000); // Ejecutar cada dos días
    }

    private Date getNextExecutionTime() {
        // Obtener la fecha y hora actual
        Date now = new Date();
        // Calcular la próxima ejecución en dos días
        long nextExecutionTime = now.getTime() + 2 * 24 * 60 * 60 * 1000;
        return new Date(nextExecutionTime);
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}