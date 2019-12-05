package com.carlosmestas.projectac;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageSender2 extends AsyncTask<String,Void,Void> {

    String ipPCv4;
    String numPlayer;

    Socket s;
    DataOutputStream dos;
    PrintWriter pw;


    public MessageSender2(String ipPCv4,String numPlayer){

        this.ipPCv4 = ipPCv4;
        this.numPlayer = numPlayer;
    }

    protected Void doInBackground(String... voids) {

        String message = voids[0];


        try{
            Log.d("ga",ipPCv4);
            String localIP = ipPCv4;
            s = new Socket(localIP,7800);
            pw = new PrintWriter(s.getOutputStream());
            pw.write(message);
            pw.flush();
            pw.close();
            s.close();
        }
        catch(IOException e){

        }

        return null;
    }

    public String obtenerNombreDeDispositivo() {
        String fabricante = Build.MANUFACTURER;
        String modelo = Build.MODEL;
        if (modelo.startsWith(fabricante)) {
            return primeraLetraMayuscula(modelo);
        } else {
            return primeraLetraMayuscula(fabricante) + " " + modelo;
        }
    }


    private String primeraLetraMayuscula(String cadena) {
        if (cadena == null || cadena.length() == 0) {
            return "";
        }
        char primeraLetra = cadena.charAt(0);
        if (Character.isUpperCase(primeraLetra)) {
            return cadena;
        } else {
            return Character.toUpperCase(primeraLetra) + cadena.substring(1);
        }
    }
}
