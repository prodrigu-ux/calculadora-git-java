import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Servidor HTTP en Java que resuelve exclusivamente operaciones de suma,
 * resta, multiplicación y potencia con enteros de hasta 5 dígitos.
 */
public class Server {

    // Rango permitido para los números enteros de entrada [-99999...99999]
    private static final int MIN_VALOR = -99999;
    private static final int MAX_VALOR = 99999;

    public static void main(String[] args) throws IOException {
        // Inicia el servidor HTTP en el puerto local 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Endpoint raíz: devuelve la interfaz HTML al navegador
        server.createContext("/", exchange -> {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                enviarRespuesta(exchange, 405, "text/plain; charset=UTF-8", "Metodo no permitido");
                return;
            }
            Path htmlPath = Path.of("index.html");
            if (!Files.exists(htmlPath)) {
                enviarRespuesta(exchange, 404, "text/plain; charset=UTF-8", "Archivo index.html no encontrado");
                return;
            }
            byte[] contenido = Files.readAllBytes(htmlPath);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, contenido.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(contenido);
            }
        });

        // Endpoint de cálculo: valida entradas y ejecuta la operación matemática
        server.createContext("/calcular", exchange -> {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                enviarRespuesta(exchange, 405, "application/json; charset=UTF-8", "{\"error\":\"Metodo no permitido\"}");
                return;
            }

            Map<String, String> params = parsearParametros(exchange.getRequestURI().getRawQuery());
            String rawNum1 = params.get("num1");
            String rawNum2 = params.get("num2");
            String operacion = params.get("operacion");

            int num1;
            int num2;

            // Validación de que ambos operandos sean números enteros
            try {
                if (rawNum1 == null || rawNum2 == null || rawNum1.isBlank() || rawNum2.isBlank()) {
                    throw new IllegalArgumentException("Ambos numeros son obligatorios.");
                }
                num1 = Integer.parseInt(rawNum1.trim());
                num2 = Integer.parseInt(rawNum2.trim());
            } catch (NumberFormatException e) {
                enviarRespuesta(exchange, 400, "application/json; charset=UTF-8",
                        "{\"error\":\"Solo se permiten numeros enteros validos.\"}");
                return;
            } catch (IllegalArgumentException e) {
                enviarRespuesta(exchange, 400, "application/json; charset=UTF-8",
                        "{\"error\":\"" + e.getMessage() + "\"}");
                return;
            }

            // Validación del rango permitido de 5 dígitos [-99999...99999]
            if (num1 < MIN_VALOR || num1 > MAX_VALOR || num2 < MIN_VALOR || num2 > MAX_VALOR) {
                enviarRespuesta(exchange, 400, "application/json; charset=UTF-8",
                        "{\"error\":\"Los numeros deben ser enteros de hasta 5 digitos [-99999...99999].\"}");
                return;
            }

            if (operacion == null) {
                enviarRespuesta(exchange, 400, "application/json; charset=UTF-8",
                        "{\"error\":\"Debe indicar una operacion.\"}");
                return;
            }

            // Selección exclusiva de las cuatro operaciones permitidas
            String resultado;
            switch (operacion) {
                case "suma":
                    resultado = String.valueOf((long) num1 + (long) num2);
                    break;
                case "resta":
                    resultado = String.valueOf((long) num1 - (long) num2);
                    break;
                case "multiplicacion":
                    resultado = String.valueOf((long) num1 * (long) num2);
                    break;
                case "potencia":
                    if (num1 == 0 && num2 < 0) {
                        enviarRespuesta(exchange, 400, "application/json; charset=UTF-8",
                                "{\"error\":\"No se puede elevar 0 a un exponente negativo.\"}");
                        return;
                    }
                    if (num2 == 0) {
                        resultado = "1";
                    } else if (num2 > 0) {
                        resultado = BigInteger.valueOf(num1).pow(num2).toString();
                    } else {
                        resultado = String.valueOf(Math.pow(num1, num2));
                    }
                    break;
                default:
                    enviarRespuesta(exchange, 400, "application/json; charset=UTF-8",
                            "{\"error\":\"Operacion no permitida. Solo suma, resta, multiplicacion o potencia.\"}");
                    return;
            }

            enviarRespuesta(exchange, 200, "application/json; charset=UTF-8",
                    "{\"resultado\":\"" + resultado + "\"}");
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Servidor iniciado en http://localhost:8080");
    }

    // Extrae los parámetros de la cadena de consulta (query string) de la URL
    private static Map<String, String> parsearParametros(String query) {
        Map<String, String> mapa = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return mapa;
        }
        String[] pares = query.split("&");
        for (String par : pares) {
            String[] kv = par.split("=", 2);
            String clave = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String valor = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            mapa.put(clave, valor);
        }
        return mapa;
    }

    // Envía la respuesta HTTP con su código de estado, tipo de contenido y cuerpo
    private static void enviarRespuesta(HttpExchange exchange, int codigo, String contentType, String cuerpo) throws IOException {
        byte[] bytes = cuerpo.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(codigo, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}