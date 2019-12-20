package com.carlosmestas.projectac;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Actividad donde se desarrollara el respectivo tres en raya
 */
public class MainActivity2 extends AppCompatActivity{

    private static final int REC_CODE_INPUT = 100;

    String numPlayer = "";
    EditText editTextTest;
    ImageButton imageButtonMic;

    public String ipPCv4 = "";

    String np;
    int img;

    ImageButton buttonA0, buttonA1, buttonA2;
    ImageButton buttonB0, buttonB1, buttonB2;
    ImageButton buttonC0, buttonC1, buttonC2;

    Jugador jugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Thread myThread = new Thread(new MainActivity2.MyServerThread());
        myThread.start();

        // Intent de donde llegamos a esta actividad
        Intent intent = getIntent();
        // Datos que son enviamos
        // Almacenamos la direccion IP de la computadora
        String ip = intent.getStringExtra("ipPCv4");
        ipPCv4 = ip;
        // Almacenamos el numero de jugador
        np = intent.getStringExtra("jugador");

        // Mensaje donde mostramos la direccion IP de la computadora, asi como el numero de jugador respectivo
      //  Toast.makeText(getApplicationContext(),ip + " - " + np, Toast.LENGTH_LONG).show();

        img = 0;
        // Seleccionaremos la respectiva imagen de acuerdo al numero de jugador que es
        if(np.equals("Jugador 1")){
            img = R.drawable.x;
            Toast.makeText(MainActivity2.this,img, Toast.LENGTH_LONG).show();
        }
        else if(np.equals("Jugador 2")){
            img = R.drawable.o;
            Toast.makeText(MainActivity2.this,img, Toast.LENGTH_LONG).show();
        }
        // Creacion de nuestro objeto jugador y le asignamos su respectiva imagen para marcar
        jugador = new Jugador(np,img);

        // Se asignan los respectivos botones del layout
        imageButtonMic = findViewById(R.id.imageButton2);
        editTextTest = findViewById(R.id.editTextTest2);

        buttonA0 = findViewById(R.id.imageButton3);
        buttonA0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity2.this,R.drawable.o+" - "+jugador.getImagen()+" - "+np, Toast.LENGTH_LONG).show();

                buttonA0.setImageResource(jugador.getImagen());
                MessageSender2 messageSender = new MessageSender2(ipPCv4,numPlayer);
                messageSender.execute(np + " marco " + "A0");
                buttonA0.setEnabled(false);
        //        buttonA0.setBackgroundResource(jugador.getImagen());

            }
        });

        buttonA1 = findViewById(R.id.imageButton4);
        buttonA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonA1.setImageResource(jugador.getImagen());
                MessageSender2 messageSender = new MessageSender2(ipPCv4,numPlayer);
                messageSender.execute(np + " marco " + "A1");
                buttonA1.setEnabled(false);

            }
        });

        buttonA2 = findViewById(R.id.imageButton5);
        buttonA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonA2.setImageResource(jugador.getImagen());
                MessageSender2 messageSender = new MessageSender2(ipPCv4,numPlayer);
                messageSender.execute(np + " marco " + "A2");
                buttonA2.setEnabled(false);


            }
        });

        buttonB0 = findViewById(R.id.imageButton6);
        buttonB0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonB0.setImageResource(jugador.getImagen());
                MessageSender2 messageSender = new MessageSender2(ipPCv4,numPlayer);
                messageSender.execute(np + " marco " + "B0");
                buttonB0.setEnabled(false);

            }
        });

        buttonB1 = findViewById(R.id.imageButton7);
        buttonB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonB1.setImageResource(jugador.getImagen());

                MessageSender2 messageSender = new MessageSender2(ipPCv4,numPlayer);
                messageSender.execute(np + " marco " + "B1");
                buttonB1.setEnabled(false);

            }
        });

        buttonB2 = findViewById(R.id.imageButton8);
        buttonB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonB2.setImageResource(jugador.getImagen());

                MessageSender2 messageSender = new MessageSender2(ipPCv4,numPlayer);
                messageSender.execute(np + " marco " + "B2");
                buttonB2.setEnabled(false);

            }
        });


        buttonC0 = findViewById(R.id.imageButton9);
        buttonC0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonC0.setImageResource(jugador.getImagen());

                MessageSender2 messageSender = new MessageSender2(ipPCv4,numPlayer);
                messageSender.execute(np + " marco " + "C0");
                buttonC0.setEnabled(false);

            }
        });

        buttonC1 = findViewById(R.id.imageButton10);
        buttonC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonC1.setImageResource(jugador.getImagen());

                MessageSender2 messageSender = new MessageSender2(ipPCv4,numPlayer);
                messageSender.execute(np + " marco " + "C1");
                buttonC1.setEnabled(false);

            }
        });

        buttonC2 = findViewById(R.id.imageButton11);
        buttonC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonC2.setImageResource(jugador.getImagen());


                MessageSender2 messageSender = new MessageSender2(ipPCv4,numPlayer);
                messageSender.execute(np + " marco " + "C2");
                buttonC2.setEnabled(false);

            }
        });


        // Metodo donde se grabara el audio
        imageButtonMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startListening();
            }
        });





    }

    class MyServerThread implements Runnable{

        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader br;
        String message;

        Handler h = new Handler();

        @Override
        public void run() {
            try{
                ss = new ServerSocket(7801);
                while(true){
                    s = ss.accept();
                    isr = new InputStreamReader(s.getInputStream());
                    br = new BufferedReader(isr);
                    message = br.readLine();

                    h.post(new Runnable() {
                        @Override
                        public void run() {

                            if(message.equals("Jugador 1 marco A0")){// || message.equals("Jugador 2 marco A0")){
                             /*   if(jugador.getImagen() == R.drawable.o)
                                    buttonA0.setImageResource(R.drawable.x);
                                else
                                    buttonA0.setImageResource(R.drawable.o);
                             */ Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

                                buttonA0.setEnabled(false);
                            }
                            if(message.equals("Jugador 2 marco A0")){ //|| message.equals("Jugador 2 marco A0")){
                                if(jugador.getImagen() == R.drawable.o)
                                    buttonA0.setImageResource(R.drawable.x);
                                else
                                    buttonA0.setImageResource(R.drawable.o);
                                buttonA0.setEnabled(false);
                            }
                            else if(message.equals("Jugador 1 marco A1") || message.equals("Jugador 2 marco A1")){
                                if(img == R.drawable.o)
                                    buttonA1.setImageResource(R.drawable.x);
                                else
                                    buttonA1.setImageResource(R.drawable.o);
                                buttonA1.setEnabled(false);
                            }
                            else if(message.equals("Jugador 1 marco A2") || message.equals("Jugador 2 marco A2")){
                                if(img == R.drawable.o)
                                    buttonA2.setImageResource(R.drawable.x);
                                else
                                    buttonA2.setImageResource(R.drawable.o);
                                buttonA2.setEnabled(false);
                            }
                            else if(message.equals("Jugador 1 marco B0") || message.equals("Jugador 2 marco B0")){
                                if(img == R.drawable.o)
                                    buttonB0.setImageResource(R.drawable.x);
                                else
                                    buttonB0.setImageResource(R.drawable.o);
                                buttonB0.setEnabled(false);
                            }
                            else if(message.equals("Jugador 1 marco B1") || message.equals("Jugador 2 marco B1")){
                                if(img == R.drawable.o)
                                    buttonB1.setImageResource(R.drawable.x);
                                else
                                    buttonB1.setImageResource(R.drawable.o);
                                buttonB1.setEnabled(false);
                            }
                            else if(message.equals("Jugador 1 marco B2") || message.equals("Jugador 2 marco B2")){
                                if(img == R.drawable.o)
                                    buttonB2.setImageResource(R.drawable.x);
                                else
                                    buttonB2.setImageResource(R.drawable.o);
                                buttonB2.setEnabled(false);
                            }
                            else if(message.equals("Jugador 1 marco C0") || message.equals("Jugador 2 marco C0")){
                                if(img == R.drawable.o)
                                    buttonC0.setImageResource(R.drawable.x);
                                else
                                    buttonC0.setImageResource(R.drawable.o);
                                buttonC0.setEnabled(false);
                            }
                            else if(message.equals("Jugador 1 marco C1") || message.equals("Jugador 2 marco C1")){
                                if(img == R.drawable.o)
                                    buttonC1.setImageResource(R.drawable.x);
                                else
                                    buttonC1.setImageResource(R.drawable.o);
                                buttonC1.setEnabled(false);
                            }
                            else if(message.equals("Jugador 1 marco C2") || message.equals("Jugador 2 marco C2")){
                                if(img == R.drawable.o)
                                    buttonC2.setImageResource(R.drawable.x);
                                else
                                    buttonC2.setImageResource(R.drawable.o);
                                buttonC2.setEnabled(false);
                            }

                        }
                    });

                }
            }
            catch (IOException e){

            }
        }
    }

    /*
    public void send(View v){
        MessageSender2 messageSender = new MessageSender2(ipPCv4,numPlayer);
        messageSender.execute(numPlayer + " movio " + editTextTest.getText().toString());
    }
*/
    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga la posición para marcar");
        try{
            startActivityForResult(intent,REC_CODE_INPUT);

        }
        catch(ActivityNotFoundException e){

        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode){
            case REC_CODE_INPUT:{
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String s = "";
                    for(int i = 0 ; i < result.size() ; i++){
                        s += result.get(i);
                    }
                    editTextTest.setText(result.get(0));

                }
            }
        }

    }

    public void send(View v){
        // La primera parte del mensaje es la direccion IP de la computadora
        // La segunda parte del mensaje es el numero de jugador, donde se envia el movimiento respectivo
        // de cada jugador
        MessageSender2 messageSender = new MessageSender2(ipPCv4,numPlayer);
        messageSender.execute(np + " marco " + editTextTest.getText().toString());
    }


}
