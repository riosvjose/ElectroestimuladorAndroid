package com.electro.electroestimulador;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MasajeActivity extends AppCompatActivity {
    String intensity="0";
    ImageView imgWeb;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    private TextView tvNumIntensity;
    private Spinner spinnerBody;

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masaje);
        Intent newint = getIntent();
        address = newint.getStringExtra(Bluetooth2Activity.EXTRA_ADDRESS); //receive the address of the bluetooth device
        tvNumIntensity = findViewById(R.id.tvNumIntensity);
        tvNumIntensity.setText(intensity);
        new ConnectBT().execute(); //Call the class to connect

        imgWeb=findViewById(R.id.imageViewWeb);
        Picasso.get().load("http://201.131.41.33/zeus/img/electro/mano.jpg").into(imgWeb);
        LoadBodyPartsSpinner();


    }
    public void incrementar (View view)
    {
        tvNumIntensity = findViewById(R.id.tvNumIntensity);
        String straux = tvNumIntensity.getText().toString();
        int aux= Integer.parseInt(straux)+1;
        if(aux<10)
        {
            int aux1=Equivalences(aux);
            sendIntensity(aux1+"");
            tvNumIntensity.setText(aux+"");
        }
        else
            msg("Intensidad máxima alcanzada.");
        turnOnLed();
    }
    public void decrementar (View view)
    {
        tvNumIntensity = findViewById(R.id.tvNumIntensity);
        String straux = tvNumIntensity.getText().toString();
        int aux= Integer.parseInt(straux)-1;
        if(aux>=0){
            int aux1=Equivalences(aux);
            tvNumIntensity.setText(aux+"");
            sendIntensity(aux1+"");
        }
        //turnOffLed();
    }
    public void desconectar(View view)
    {
        Disconnect();
    }
    public void initTreatment (View view){
        sendTreatment();
    }
    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }
    private Integer Equivalences(int in){
        int aux=0;
        switch (in){
            case 0:
                aux=0;
                break;
            case 1:
                aux=50;
                break;
            case 2:
                aux=60;
                break;
            case 3:
                aux=70;
                break;
            case 4:
                aux=80;
                break;
            case 5:
                aux=90;
                break;
            case 6:
                aux=100;
                break;
            case 7:
                aux=110;
                break;
            case 8:
                aux=120;
                break;
            case 9:
                aux=128;
                break;
        }
        return aux;
    }
    private void turnOffLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("0".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void sendTreatment(){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("100;1;1;50;".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error al iniciar el masaje");
            }
        }
        else
            msg("El disositivo no se encuentra conectado.");
    }
    private void sendIntensity(String intensity){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write((intensity).getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void turnOnLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("1".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }


    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }

    public void LoadBodyPartsSpinner(){

        String url="http://201.131.41.33/zeus/api/ApiService/ListBody";
        //Toast.makeText(this,"Lista",Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                try {
                    String currentString = response;
                    String[] separated = currentString.split(";");
                    boolean logged= false;
                    String name="";
                    Toast.makeText(getApplicationContext(),"hola",Toast.LENGTH_LONG).show();
                    ArrayList<BodyParts> Body=new ArrayList<>();
                    Toast.makeText(getApplicationContext(),separated.length,Toast.LENGTH_LONG).show();
                    for(int i=0;i<separated.length;i++)
                    {
                        String currentString1 = separated[i];
                        String[] separated1 = currentString1.split(">");
                        Toast.makeText(getApplicationContext(),currentString1,Toast.LENGTH_LONG).show();
                        // if(separated1[0].toString().equalsIgnoreCase("Error")&&separated1[1].toString().equals("0"))
                        if(separated1.length==6)
                        {
                            BodyParts bod= new BodyParts(Integer.parseInt(separated1[0]),separated1[1],separated1[5]);
                            Body.add(bod);
                        }

                    }

                    for(int i=0;i<Body.size();i++) {
                        Toast.makeText(getApplicationContext(),Body.get(i).toString(),Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }



                //ArrayAdapter<String> karant_adapter = new ArrayAdapter<String>(this,
                  //      android.R.layout.simple_spinner_item, Body);

            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                //params.put("usr",mEmailView.getText().toString().trim());
                //params.put("pwd",mPasswordView.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }



    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(MasajeActivity.this, "Conectando...", "Espere un momento por favor.");  //avisa el estado del proceso
        }

        @Override
        protected Void doInBackground(Void... devices) //la conexión se realiza en segundo plano
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection fallida. Intentelo nuevamente.");
                finish();
            }
            else
            {
                msg("Conectado");
                isBtConnected = true;
            }
            progress.dismiss();
        }


    }


}
