package com.electro.electroestimulador;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity  {


    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private  Button btnSignIn;
    private TextView tvIsConnected;
    DataBaseHelper mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mydb=new DataBaseHelper(this);

        // Set up the login form.
        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvIsConnected = (TextView) findViewById(R.id.register_error);
    }
   public void login(View view)
    {
        //String url="http://192.168.0.16/api/ApiService/SignIn";
        String url="http://201.131.41.33/zeus/api/ApiService/SignIn2";
        //Toast.makeText(this,"login",Toast.LENGTH_SHORT).show();
        RequestQueue  requestQueue= Volley.newRequestQueue(this);
        //Toast.makeText(getApplicationContext(),"login",Toast.LENGTH_LONG).show();


        StringRequest jsArrayRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            //StringBuilder textViewData = new StringBuilder();

                            JSONArray obj=new JSONArray(response.toString());
                            if(obj.length()>0) {
                                JSONObject jsonObject = new JSONObject(obj.getJSONObject(0).toString());
                                //JSONArray jsonArray=new JSONArray(obj.getJSONArray(""));
                                String nombre = jsonObject.getString("first_name");
                                String apellido = jsonObject.getString("last_name");
                                String userid = jsonObject.getString("user_id");
                                String userac = jsonObject.getString("user_account");
                                String useractive = jsonObject.getString("active");

                                mydb.insertData(1, nombre, apellido, userid, userac, useractive);
                                //first_name last_name user_id user_acount active


                                Intent intent = new Intent(LoginActivity.this, MenuPrincipalActivity.class);
                                intent.putExtra("User", nombre);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrecta.",Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "error"+
                                    e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
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
                params.put("usr",mEmailView.getText().toString().trim());
                params.put("pwd",mPasswordView.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(jsArrayRequest);
    }
}

