package com.carlosmestas.projectac;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageSender3 extends AsyncTask<String,Void,Void> {

    String ipPCv4;
    String numPlayer;

    Socket s;
    DataOutputStream dos;
    PrintWriter pw;


    public MessageSender3(String ipPCv4, String numPlayer){

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
}
