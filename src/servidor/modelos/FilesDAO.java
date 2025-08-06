/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor.modelos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Johan
 */
public class FilesDAO {
    
    public static List<String> obtenerArchivosEnCarpeta(String rutaCarpeta) {
    File carpeta = new File(rutaCarpeta);
    List<String> archivos = new ArrayList<>();

    if (carpeta.exists() && carpeta.isDirectory()) {
        for (File archivo : carpeta.listFiles()) {
            if (archivo.isFile()) {
                archivos.add(archivo.getName()); // solo el nombre, o usa archivo.getPath()
            }
        }
    }

    return archivos;
}
}
