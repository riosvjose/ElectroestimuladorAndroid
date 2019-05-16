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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;
import static com.android.volley.Request.Method.POST;

public class LoginActivity extends AppCompatActivity  {


    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private  Button btnSignIn;
    private TextView tvIsConnected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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
        String url="http://201.131.41.33/zeus/api/ApiService/SignIn ";
        Toast.makeText(this,"login",Toast.LENGTH_SHORT).show();
        RequestQueue  requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response)
            {

                try {
                    Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                    //JSONArray obj=new JSONArray(response.toString());
                    //JSONObject obj = new JSONObject(response);    // se tranforma el string en un objeto json

                    //JSONObject person = (JSONObject) obj.getJSONObject("usuario");   // como la operacion es correcta
                    // en data se envia los datos del usuario
                    //Toast.makeText(getApplicationContext(),person.toString(),Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(),obj.toString(),Toast.LENGTH_LONG).show();
                    String currentString = response;
                    String[] separated = currentString.split(">");
                    boolean logged= false;
                    String name="";
                    for(int i=0;i<separated.length;i++)
                    {
                        String currentString1 = separated[i];
                        String[] separated1 = currentString1.split(":");
                        Toast.makeText(getApplicationContext(),currentString1,Toast.LENGTH_LONG).show();
                        // if(separated1[0].toString().equalsIgnoreCase("Error")&&separated1[1].toString().equals("0"))
                        if(i==0&&separated1[1].toString().equals("0"))
                        {
                            logged=true;
                        }
                        if(separated1[0].toString().equalsIgnoreCase("First_name")||separated1[0].toString().equalsIgnoreCase("Last_name")){
                            name+=separated1[1]+" ";
                        }
                    }




                    //String name =first_name;                   // extraemos los datos que se encontraba en data
                    //String id = last_name;
                    if(logged){
                        Intent intent=new Intent(LoginActivity.this,MenuPrincipalActivity.class);
                        intent.putExtra("User",name );
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Usuario o conraseña incorrectos",Toast.LENGTH_LONG).show();
                    }
                    //intent.putExtra("Master",id);



                    //String last = person.getString("last");
                    //String user = person.getString("user");
                    //String email = person.getString("email");

                    //Toast.makeText(getApplicationContext(),"usuario: "+name+" "+last+" con id: "+id,Toast.LENGTH_LONG).show();

                    //save in sqlite
                    /**
                    boolean isInsterted= mydb.insertData(id,name,last,user,email,message);

                    Intent intent=new Intent(getApplicationContext(),BnvActivity.class);
                    intent.putExtra("User",etxtUser.getText().toString());
                    intent.putExtra("Master",id);
                    startActivity(intent);
                    finish();
                     */

                } catch (Exception e) {
                    e.printStackTrace();
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
        requestQueue.add(stringRequest);
    }





















       private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }





        private void SignIn(View view) {
            String url="http://201.131.41.33/zeus/api/ApiService/SignIn";
            JSONObject manJson = new JSONObject();
            try {
                manJson.put("usr", mEmailView);
                manJson.put("pwd", mPasswordView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request=new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray mJsonArray = response.getJSONArray("usuario");
                        JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                        String userid = mJsonObject.getString("user_id");
                        Toast.makeText(LoginActivity.this, "Nombre:"+userid,Toast.LENGTH_SHORT).show();
                        if(mJsonArray.length()>0){
                                //finish();
                                Intent PrincipalMenu= new Intent(LoginActivity.this, MenuPrincipalActivity.class);
                                startActivity(PrincipalMenu);
                            } else {
                                mPasswordView.setError(getString(R.string.error_incorrect_password));
                                mPasswordView.requestFocus();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            //queue.add(request);
        }

}

