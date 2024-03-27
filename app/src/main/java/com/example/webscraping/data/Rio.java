package com.example.webscraping.data;

import java.io.Serializable;

public class Rio implements Serializable {
    String nombreRio;
    String puerto;
    String altura;
    String variacion;
    String fechaUltimaActualizacion;
    String estado;
    String linkDatesGraphs;

    public Rio(String nombreRio,String puerto, String altura, String variacion, String fechaUltimaActualizacion, String estado,String linkDatesGraphs) {
        this.nombreRio = "RIO "+nombreRio;
        this.puerto = puerto;
        this.altura = altura;
        this.variacion = variacion;
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
        this.estado = estado;
        this.linkDatesGraphs = linkDatesGraphs;
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
    public String GetLinkDatesGraphs(){
        return linkDatesGraphs;
    }

}
