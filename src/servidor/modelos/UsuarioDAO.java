package servidor.modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UsuarioDAO {

    // ------------------------ CREAR USUARIO EN LA BASE DE DATOS ------------------------  //
    public static String crearUsuario(Usuario usuario) {
        
        if (existeUsuario(usuario.getUSUARIO())) {
            return "EXISTE";
        }

        String sql = "INSERT INTO usuarios (USUARIO, PASSWORD, TIPOUSUARIO) values (?, ?, ?)";

        try (
            Connection conexion = ConexionDB.obtenerConexion(); 
            PreparedStatement prepararSentencia = conexion.prepareStatement(sql)) {
            prepararSentencia.setString(1, usuario.getUSUARIO());
            prepararSentencia.setString(2, hashearMD5(usuario.getPASSWORD())); // HASHEAR LA CONTRASEÑA DEL USUARIO
            prepararSentencia.setString(3, usuario.getTIPOUSUARIO());

            prepararSentencia.executeUpdate();
            System.out.println("Usuario creado exitosamente: " + usuario.getUSUARIO());
            return "REGISTRADO";

        } catch (SQLException e) {
            System.out.println("Hubo un error al crear el usuario: " + e.getMessage());
            return "ERROR";
        }
    }
    // ------------------------ VALIDAR EXISTENCIA USUARIO EN LA BASE DE DATOS ------------------------  //

    public static boolean existeUsuario(String nombreUsuario) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE USUARIO = ?";

        try (
                Connection conexion = ConexionDB.obtenerConexion(); PreparedStatement prepararSentencia = conexion.prepareStatement(sql)) {
            prepararSentencia.setString(1, nombreUsuario);
            ResultSet resultado = prepararSentencia.executeQuery();

            if (resultado.next()) {
                return resultado.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar si existe el usuario: " + e.getMessage());
        }

        return false;
    }

// ------------------------ VALIDAR USUARIO EN LA BASE DE DATOS ------------------------  //
    public static Usuario validarUsuario(String usuario, String passwordPlana) {
        String sql = "SELECT * FROM usuarios WHERE USUARIO = ?";

        try (
                Connection conexion = ConexionDB.obtenerConexion(); PreparedStatement prepararSentencia = conexion.prepareStatement(sql)) {
            prepararSentencia.setString(1, usuario);
            ResultSet resultado = prepararSentencia.executeQuery();
            System.out.println("El resultado de la consulta fue: " + resultado);
            if (resultado.next()) {
                String hashAlmacenado = resultado.getString("PASSWORD");
                String hashIngresado = hashearMD5(passwordPlana);

                if (hashAlmacenado.equals(hashIngresado)) {
                    Usuario usuarioValido = new Usuario();
                    usuarioValido.setUSUARIO(resultado.getString("USUARIO"));
                    usuarioValido.setTIPOUSUARIO(resultado.getString("TIPOUSUARIO"));
                    return usuarioValido;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar el usuario: " + e.getMessage());
        }

        return null;
    }

    // ------------------------ HASHEAR PASSWORD EN LA BASE DE DATOS ------------------------  //
    private static String hashearMD5(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(texto.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear con MD5", e);
        }
    }
}
