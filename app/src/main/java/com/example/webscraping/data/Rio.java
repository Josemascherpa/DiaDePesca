package com.example.webscraping.data;

public class Rio {
    String nombreRio;
    String puerto;
    float altura;
    float variacion;
    String fechaUltimaActualizacion;
    String estado;

    public Rio(String nombreRio,String puerto, float altura, float variacion, String fechaUltimaActualizacion, String estado) {
        this.nombreRio = nombreRio;
        this.puerto = puerto;
        this.altura = altura;
        this.variacion = variacion;
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
        this.estado = estado;
    }

}
