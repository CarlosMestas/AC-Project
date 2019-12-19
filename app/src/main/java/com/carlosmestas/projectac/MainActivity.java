package com.carlosmestas.projectac;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.zxing.Result;

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
 * Actividad principal donde se iniciara la aplicacion
 * Realizaremos las conexiones
 */
public class MainActivity extends AppCompatActivity{

    // Diferentes tipos de codigos para realizar las diferentes conexiones
    // De acerdo a cada tipo de conexion que haremos
    // Codigo para grabar audio
    private static final int REC_CODE_INPUT = 100;
    // Codigo para escanear el codigo QR
    private static final int REQUEST_CODE_QR_SCAN = 101;
    // Codigo para solicitar permiso al usuario de que la aplicacion va a utilizar la camara
    // Esto es importante ya que en algunos celulares al no solicitar el uso de camara
    // n=va a fallar el lector de codigo qr, ya que la camara no va a funcionar
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 102;

    // Almacenaremos en un string el numero de jugador
    String numPlayer = "";
    // Edit text de prueba donde mandamos diferentes tipos de datos
    EditText editTextTest;
    // Boton con imagen
    ImageButton imageButtonMic;
    // Boton donde realizaremos la primera parte de la sincronizacion
    Button buttonSync;
    // Boton donde realizaremos la segunda parte de la sincronizacion
    Button buttonSync2;

    // Almacenaremos la direccion IP que enviamos desde la computadora, para que de esta manera
    // podamos enviar los mensajes a la computadora
    public String ipPCv4 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.ca);
        mp.pause();
        // Se realiza la respectiva solicitud al usuario de que la aplicacion utilizara la camara
        // Luego de que el usuario acepta este permiso, no se le vuelve a solicitar, salvo que la
        // aplicacion se vuelva a instalar
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {

            // Permission has already been granted
        }

        // Realizamos la respectiva seleccion de nuestros objetivos de los respectivos layouts
        editTextTest = findViewById(R.id.editTextTest);
        imageButtonMic = findViewById(R.id.imageButton);
        buttonSync = findViewById(R.id.buttonSync);
        buttonSync2 = findViewById(R.id.buttonSync2);

        // Programacion del boton de el boton de grabacion
        imageButtonMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia el metodo para comenzar la grabacion
                startListening();
            }

        });

        Thread myThread = new Thread(new MyServerThread());
        myThread.start();

        // Primera parte de la conexion
        // En este paso procederemos a leer el codigo QR ya generado por la computadora
        buttonSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Inicia la actividad de leer el codigo QR
                Intent i = new Intent(MainActivity.this, QrCodeActivity.class);
                startActivityForResult(i, REQUEST_CODE_QR_SCAN);

            }
        });

        // Ya teniendo la direccion IP, podemos nosotros enviar la direccion IP de cada celular, asi como el modelo
        buttonSync2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),getLocalIpAddress() + obtenerNombreDeDispositivo(),Toast.LENGTH_SHORT).show();
                MessageSender messageSender = new MessageSender(ipPCv4);
                messageSender.execute(getLocalIpAddress() + " " +obtenerNombreDeDispositivo());
            }
        });
    }

    /**
     * Metodo donde vamos a recibir los mensajes de la compputadora
     */
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
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            // La computadora envia un distinto mensaje de acuerdo a las dos direcciones que se almacenaron
                            // En los diferentes casos se almacenan en cada celular el numero de jugador
                            if(message.equals("Jugador 1 Conectado")){
                                numPlayer = "Jugador 1";
                            }
                            if(message.equals("Jugador 2 Conectado")){
                                numPlayer = "Jugador 2";
                            }
                            // Se recibe el mensaje cuando en la computadora se selcciona que vamos a jugar el tres en raya
                            if(message.equals("3eR")){
                                Intent i = new Intent(MainActivity.this , MainActivity2.class);
                                i.putExtra("ipPCv4",ipPCv4);
                                i.putExtra("jugador",numPlayer);
                                startActivity(i);
                            }
                        }
                    });

                }
            }
            catch (IOException e){

            }
        }
    }

    /**
     * Metodo donde vamos a enviar al informacion de cada movimiento
     * @param v Vista donde estamos trabajando
     */
    public void send(View v){
        // La primera parte del mensaje es la direccion IP de la computadora
        // La segunda parte del mensaje es el numero de jugador, donde se envia el movimiento respectivo
        // de cada jugador
        MessageSender2 messageSender = new MessageSender2(ipPCv4,numPlayer);
        messageSender.execute(numPlayer + " movio " + editTextTest.getText().toString());
    }

    /**
     * Metodo donde comienza la grabacion de audio, para la posicion de ataque
     */
    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        // Cambiamos un pequeño mensaje
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Dime la posición de ataque");
        try{
            startActivityForResult(intent,REC_CODE_INPUT);
        }
        catch(ActivityNotFoundException e){

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Lectura del codigo QR
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getApplicationContext(), "No se pudo obtener una respuesta", Toast.LENGTH_SHORT).show();
            String resultado = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (resultado != null) {
                Toast.makeText(getApplicationContext(), "No se pudo escanear el código QR", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data != null) {
                String lectura = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                Toast.makeText(getApplicationContext(), "Leído: " + lectura, Toast.LENGTH_SHORT).show();
                ipPCv4 = lectura;

            }
        }

        // Grabacion de audio
        switch(requestCode){
            case REC_CODE_INPUT:{
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String s = "";
                    for(int i = 0 ; i < result.size() ; i++){
                        s += result.get(i);
                    }
                    // El audio grabado pasa a texto y se escribe el respectivo editText
                    editTextTest.setText(result.get(0));

                }
            }
        }

    }

    /**
     * Metodo donde podemos obtener la direccion Ip de cada respectivo celular
     * @return direccion IP del celular
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Obtenemos el modelo del dispoditivo
     * @return Modelo del dispositivo
     */
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
