package com.electro.electroestimulador;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MenuPrincipalActivity extends AppCompatActivity {

    ImageButton btnBluetooth;
    BluetoothSocket btSocket = null;
    ImageButton btnMasaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        String user=getIntent().getExtras().getString("User");
        Toast.makeText(this,"Bienvenido/a "+user,Toast.LENGTH_LONG).show();

        btnBluetooth = (ImageButton) findViewById(R.id.imageButtonBluetooth);
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //attemptLogin();
                Intent PrincipalMenu= new Intent(getApplicationContext(), Bluetooth2Activity.class);
                startActivity(PrincipalMenu);
            }
        });
        /*btnMasaje = (ImageButton) findViewById(R.id.imageButtonTratamiento);
        btnMasaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //attemptLogin();
                Intent MasajeView= new Intent(getApplicationContext(), MasajeActivity.class);
                startActivity(MasajeView);
            }
        });*/

    }

}
