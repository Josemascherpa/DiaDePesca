package com.example.webscraping.data;

import java.io.Serializable;

public class Rio implements Serializable {
    String nombreRio;
    String puerto;
    String altura;
    String variacion;
    String fechaUltimaActualizacion;
    String estado;

    public Rio(String nombreRio,String puerto, String altura, String variacion, String fechaUltimaActualizacion, String estado) {
        this.nombreRio = "RIO "+nombreRio;
        this.puerto = puerto;
        this.altura = altura;
        this.variacion = variacion;
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
        this.estado = estado;
    }

    public String GetNombre(){
        return nombreRio;
    }
    public String GetPuerto(){
        return puerto;
    }
    public String GetAltura(){
        return altura;
    }
    public String GetVariacion(){
        return variacion;
    }
    public String GetFecha(){
        return fechaUltimaActualizacion;
    }
    public String GetEstado(){
        return estado;
    }

}
