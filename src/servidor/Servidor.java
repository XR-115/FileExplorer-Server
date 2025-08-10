package servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.modelos.FilesDAO;
import servidor.modelos.Usuario;
import servidor.modelos.UsuarioDAO;

public class Servidor {

    static final int PUERTO = 5000;

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Servidor escuchando en el puerto " + PUERTO);

            while (true) {
                Socket socketCliente = servidor.accept();
                new Thread(() -> {
                    try {
                        manejarCliente(socketCliente);
                    } catch (Exception ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }).start();
            }

        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    private static void manejarCliente(Socket socket) {
        try (
                ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream())) {
            String accion = entrada.readUTF(); // Leer acción del cliente

            Object recibido = entrada.readObject(); // Leer objeto (puede ser cualquier cosa)

            switch (accion) {
                case "REGISTER": {
                    if (recibido instanceof Usuario usuario) {
                        String resultado = UsuarioDAO.crearUsuario(usuario);
                        salida.writeObject(resultado); // Enviar un String de respuesta
                    } else {
                        salida.writeObject("ERROR_TIPO_DATOS");
                    }
                    break;
                }

                case "LOGIN": {
                    System.out.println("Validando usuario" + recibido);
                    if (recibido instanceof Usuario usuario) {
                        Usuario u = UsuarioDAO.validarUsuario(usuario.getUSUARIO(), usuario.getPASSWORD());
                        salida.writeObject(u); // Enviamos el usuario si es válido, o nul}o
                    } else {
                        salida.writeObject(null);
                    }
                    break;
                }

                case "GET_FILES": {
                    List<String> u = FilesDAO.obtenerArchivosEnCarpeta("src/files");
                    System.out.println("[GET_FILES] Los archivos montados en el servidor son: " + u);
                    salida.writeObject(u); // Enviamos el usuario si es válido, o nul}o

                    break;
                }

                case "GET_FILES_DATA": {
                    if (recibido instanceof String nombreArchivo) {
                        File archivo = new File("src/files/" + nombreArchivo);
                        byte[] contenido = Files.readAllBytes(archivo.toPath());
                        salida.writeObject(contenido);
                    } else {
                        salida.writeObject(null);
                    }
                    break;
                }

                case "UPLOAD_FILE":
                    if (recibido instanceof Map<?, ?> datos) {

                        System.out.println("[UPLOAD_FILE] Los archivos montados en el servidor son: " + datos);
                        String nombreDestino = (String) datos.get("nombreArchivo");
                        byte[] archivoRecibido = (byte[]) datos.get("archivoModificado");
                        // Guardar archivo
                        Files.write(Paths.get("src/files/" + nombreDestino), archivoRecibido);
                        salida.writeBoolean(true);
                        salida.flush();
                    }

                    break;

                case "GET_SYSTEM_DATA":
                    try {
                        // Obtener la información del sistema como String
                        String infoSistema = FilesDAO.obtenerInfoSystem();

                        // Guardar temporalmente en un archivo en el servidor
                        File tempFile = File.createTempFile("InfoSistema_", ".txt");
                        try (FileWriter writer = new FileWriter(tempFile)) {
                            writer.write(infoSistema);
                        }

                        // Leer el contenido del archivo como bytes
                        byte[] contenido = Files.readAllBytes(tempFile.toPath());

                        // Enviar el contenido al cliente
                        salida.writeObject(contenido);
                        salida.flush();

                        // (Opcional) enviar mensaje de confirmación
                        System.out.println("Archivo enviado al cliente: " + tempFile.getAbsolutePath());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "Ejecute paint":
                    Runtime.getRuntime().exec("mspaint");
                    break;

                case "Ejecute cmd":
                    Runtime.getRuntime().exec("cmd.exe");
                    break;

                case "Ejecute msconfig":
                    Runtime.getRuntime().exec("cmd.exe /c start msconfig");
                    break;

                default:
                    salida.writeObject("ACCION_DESCONOCIDA");
                    break;
            }

            salida.flush();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
