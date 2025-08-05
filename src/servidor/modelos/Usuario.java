/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor.modelos;

import java.io.Serializable;

/**
 *
 * @author Johan
 */
public class Usuario implements Serializable {
    private String USUARIO;
    private String PASSWORD;
    private String TIPOUSUARIO;
    
    public Usuario(){
        
    }

    public Usuario(String USUARIO, String PASSWORD, String TIPOUSUARIO) {
        this.USUARIO = USUARIO;
        this.PASSWORD = PASSWORD;
        this.TIPOUSUARIO = TIPOUSUARIO;
    }
    
    // ----------------------- MÉTODOS ACCESORES GETTERS ------------------------ //


    public String getUSUARIO() {
        return USUARIO;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public String getTIPOUSUARIO() {
        return TIPOUSUARIO;
    }
    
    // ----------------------- MÉTODOS ACCESORES SETTERS ------------------------ //


    public void setUSUARIO(String USUARIO) {
        this.USUARIO = USUARIO;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public void setTIPOUSUARIO(String TIPOUSUARIO) {
        this.TIPOUSUARIO = TIPOUSUARIO;
    }
    
    
}
