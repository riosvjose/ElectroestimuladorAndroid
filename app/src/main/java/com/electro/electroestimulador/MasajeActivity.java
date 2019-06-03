package com.electro.electroestimulador;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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
    Spinner spinnerBody;
    Spinner spinnerInjuries;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    String DatoSpinnerInjuries="",DatoSpinnerBody="";
    String[] lis={""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masaje);
        Intent newint = getIntent();
        address = newint.getStringExtra(Bluetooth2Activity.EXTRA_ADDRESS); //receive the address of the bluetooth device
        tvNumIntensity = findViewById(R.id.tvNumIntensity);
        tvNumIntensity.setText(intensity);
        new ConnectBT().execute(); //Call the class to connect



        //************************************************
        //mostrar usuario sqlite
        String Master="";
        DataBaseHelper mydb=new DataBaseHelper(this);
        Cursor res=mydb.getAllData();
        if (res.getCount()==0)
        {
            //Toast.makeText(this,"ERROR FATAL "+" NO SE ENCONTRO NADA",Toast.LENGTH_SHORT).show();
            return;
        }

        while (res.moveToNext()) {
            StringBuffer stringBuffer1=new StringBuffer();
            stringBuffer1.append(res.getString(0));
            Master=stringBuffer1.toString();
        }

       // Toast.makeText(this, "Master es ="+Master,Toast.LENGTH_LONG).show();

        imgWeb=findViewById(R.id.imageViewWeb);

        spinnerInjuries=findViewById(R.id.spinnerInjuries);


        //********************************************************************

        String url="http://201.131.41.33/zeus/api/ApiService/ListInjuries";
        //Toast.makeText(this,"Lista",Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                try {
                    JSONArray obj=new JSONArray(response);
                    final String[] lista=new String[obj.length()];
                    final String[] listaInjuries=new String[obj.length()];
                    for (int i=0;i<obj.length();i++)
                    {
                        JSONObject jsonObject=new JSONObject(obj.getJSONObject(i).toString());
                        //JSONArray jsonArray=new JSONArray(obj.getJSONArray(""));
                        lista[i]=jsonObject.getString("name");
                        listaInjuries[i]=jsonObject.getString("injury_id");
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    }

                    spinnerInjuries.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.text_view_grande,lista));

                    spinnerInjuries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                           // Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                            DatoSpinnerInjuries=parent.getItemAtPosition(position).toString();
                            DatoSpinnerInjuries =DatoSpinnerInjuries+" "+listaInjuries[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

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
                //params.get("usr");
                //params.put("pwd",mPasswordView.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
        //********************************************************************


        spinnerBody=findViewById(R.id.spinnerBodyParts);
        //**********************************************************************

        String url2="http://201.131.41.33/zeus/api/ApiService/Listbody";
        //Toast.makeText(this,"Lista",Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue2= Volley.newRequestQueue(this);
        StringRequest stringRequest2=new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                try {
                    JSONArray obj=new JSONArray(response);
                    final String[] lista=new String[obj.length()];
                    final String[] listaBodyParts=new String[obj.length()];
                    for (int i=0;i<obj.length();i++)
                    {
                        JSONObject jsonObject=new JSONObject(obj.getJSONObject(i).toString());
                        //JSONArray jsonArray=new JSONArray(obj.getJSONArray(""));
                        lista[i]=jsonObject.getString("name");
                        listaBodyParts[i]=jsonObject.getString("body_part_id");
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    }

                    spinnerBody.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.text_view_grande,lista));

                    spinnerBody.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            //Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                            String aaa=parent.getItemAtPosition(position).toString().toLowerCase();
                            Picasso.get().load("http://201.131.41.33/zeus/Img/Electro/"+aaa+".jpg").into(imgWeb);
                            DatoSpinnerBody=parent.getItemAtPosition(position).toString();
                            DatoSpinnerBody=DatoSpinnerBody+" "+listaBodyParts[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

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
                //params.get("usr");
                //params.put("pwd",mPasswordView.getText().toString().trim());
                return params;
            }
        };
        requestQueue2.add(stringRequest2);
        //**********************************************************************



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
    }
    public void desconectar(View view)
    {
        Disconnect();
    }
    public void initTreatment (View view)
    {
        final Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                tvNumIntensity.setText("3");
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        tvNumIntensity.setText("2");
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                tvNumIntensity.setText("1");







                            }
                        },1000);
                    }
                },1000);
            }
        },1000);

        Toast.makeText(this,DatoSpinnerBody+" "+DatoSpinnerInjuries,Toast.LENGTH_LONG).show();
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

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }

    public int longlista()
    {
        final int[] a = {0};
        String url="http://201.131.41.33/zeus/api/ApiService/ListInjuries";
        //Toast.makeText(this,"Lista",Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                try {
                    JSONArray obj=new JSONArray(response.toString());
                    JSONObject jsonObject=new JSONObject(obj.getJSONObject(0).toString());
                    //JSONArray jsonArray=new JSONArray(obj.getJSONArray(""));
                    a[0] = obj.length();

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
               // params.get("usr");
                //params.put("pwd",mPasswordView.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);

        return a[0];
    }
    public  String[] obtenerlista()
    {
        final String[] lista=new String[100];
        String url="http://201.131.41.33/zeus/api/ApiService/ListInjuries";
        //Toast.makeText(this,"Lista",Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                try {
                    JSONArray obj=new JSONArray(response);
                    for (int i=0;i<obj.length();i++)
                    {
                        JSONObject jsonObject=new JSONObject(obj.getJSONObject(i).toString());
                        //JSONArray jsonArray=new JSONArray(obj.getJSONArray(""));
                        lista[i]=jsonObject.getString("name");
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
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
                //params.get("usr");
                //params.put("pwd",mPasswordView.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);

        return lista;
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
                    //obtiene los datos del adaptador bluetood del teléfono
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    //Conecta con el dispositivo destino  en caso de que esté disponible
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    //Crea conexíon SPP
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    //Inicia la conección
                    btSocket.connect();
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
               // msg("Conectado");
                isBtConnected = true;
            }
            progress.dismiss();
        }


    }


}
