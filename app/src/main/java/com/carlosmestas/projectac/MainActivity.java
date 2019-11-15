package com.carlosmestas.projectac;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText editTextTest;
    private static final int REC_CODE_INPUT = 100;
    ImageButton imageButtonMic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTest = findViewById(R.id.editTextTest);
        imageButtonMic = findViewById(R.id.imageButton);

        imageButtonMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startListening();
            }


        });

        Thread myThread = new Thread(new MyServerThread());
        myThread.start();
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
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
            catch (IOException e){

            }
        }
    }

    public void send(View v){
        MessageSender messageSender = new MessageSender();
        messageSender.execute(editTextTest.getText().toString());
    }

    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Dime la posición de ataque");
        try{
            startActivityForResult(intent,REC_CODE_INPUT);
        }
        catch(ActivityNotFoundException e){

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
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
                break;
            }
        }
    }
}
