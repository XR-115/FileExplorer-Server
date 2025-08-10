/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor.modelos;

import com.sun.management.OperatingSystemMXBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
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

    public static String obtenerInfoSystem() {
        StringBuilder info = new StringBuilder();
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            String arquitectura = System.getProperty("os.arch");
            String sistemaOperativo = System.getProperty("os.name");
            String osVersion = System.getProperty("os.version");
            int numeroNucleos = Runtime.getRuntime().availableProcessors();
            String nombreProcesador = obtenerNombreProcesador();

            OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            long ramTotalMB = osBean.getTotalPhysicalMemorySize() / (1024 * 1024);
            long ramLibreMB = osBean.getFreePhysicalMemorySize() / (1024 * 1024);
            long virtualMB = osBean.getTotalSwapSpaceSize() / (1024 * 1024);

            info.append("La IP de esta máquina es: ").append(ip).append("\n")
                    .append("Arquitectura: ").append(arquitectura).append("\n")
                    .append("Sistema Operativo: ").append(sistemaOperativo).append("\n")
                    .append("Versión Sistema Operativo: ").append(osVersion).append("\n")
                    .append("Memoria RAM Total: ").append(ramTotalMB).append(" MB\n")
                    .append("Memoria RAM Libre: ").append(ramLibreMB).append(" MB\n")
                    .append("Memoria Virtual (Swap): ").append(virtualMB).append(" MB\n")
                    .append("Procesador: ").append(nombreProcesador).append("\n")
                    .append("Número de núcleos: ").append(numeroNucleos).append("\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return info.toString();
    }

    private static String obtenerNombreProcesador() {
        String nombre = "Desconocido";
        try {
            // Ejecutar comando PowerShell para obtener el nombre del CPU
            Process process = Runtime.getRuntime().exec(
                    "powershell.exe Get-CimInstance Win32_Processor | Select-Object -ExpandProperty Name"
            );
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String linea;
            StringBuilder resultado = new StringBuilder();
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    resultado.append(linea.trim());
                }
            }
            reader.close();
            nombre = resultado.toString();

            if (nombre.isEmpty()) {
                nombre = "No detectado";
            }

        } catch (Exception e) {
            System.out.println("Error obteniendo nombre del procesador: " + e.getMessage());
        }
        return nombre;
    }
}
