package com.carlosmestas.projectac;

/**
 * Clase jugador
 */
public class Jugador {
    String jugador;
    int imagen;

    public Jugador(String jugador, int imagen){
        this.jugador = jugador;
        this.imagen = imagen;
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
