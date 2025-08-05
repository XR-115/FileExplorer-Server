package servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        // Usar SOLO streams de objetos
        ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream()))
    {
        // Leer datos en MISMO ORDEN que cliente envía
        String accion = entrada.readUTF();        // Leer acción
        Usuario usuario = (Usuario) entrada.readObject();  // Leer objeto

        String resultado;
        switch (accion) {
            case "REGISTER":
                resultado = UsuarioDAO.crearUsuario(usuario);
                break;
            case "LOGIN":
                resultado = UsuarioDAO.validarUsuario(usuario.getUSUARIO(), usuario.getPASSWORD());
                break;
            default:
                resultado = "ACCION_DESCONOCIDA_CLIENTE";
        }

        // Enviar respuesta
        salida.writeUTF(resultado);
        salida.flush();

    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    } finally {
        try { socket.close(); } catch (IOException ex) { /* manejo error */ }
    }
}
}
