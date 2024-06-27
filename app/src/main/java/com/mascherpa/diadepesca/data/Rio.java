package com.mascherpa.diadepesca.data;

import java.io.Serializable;

public class Rio implements Serializable {
    String nombreRio;
    String puerto;
    String altura;
    String variacion;
    String fechaUltimaActualizacion;

    public Rio(String nombreRio,String puerto, String altura, String variacion, String fechaUltimaActualizacion) {
        this.nombreRio = "RIO "+nombreRio;
        this.puerto = puerto;
        this.altura = altura;
        this.variacion = variacion;
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }

    public String getNombre(){
        return nombreRio;
    }
    public String getPuerto(){
        return puerto;
    }
    public String getAltura(){
        return altura;
    }
    public String getVariacion(){
        return variacion;
    }
    public String getFecha(){
        return fechaUltimaActualizacion;
    }





}
