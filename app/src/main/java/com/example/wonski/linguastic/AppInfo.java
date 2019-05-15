package com.example.wonski.linguastic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AppInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        TextView gora = (TextView)findViewById(R.id.textView2);
        TextView dol = (TextView)findViewById(R.id.textView4);
        gora.setText("Aplikacja PhoneBase została przygotowana na potrzeby projektu z przedmiotu Języki Programowania Obiektowego. Aplikacja ta jest " +
                "bazą danych telefonów dostępnych w sieci Play, umożliwia porównanie ich oraz pomaga w wyborze odpowiedniego urządzenia");
        dol.setText("Twórca aplikacji: Krzysztof WIlewski, IMT WIMiR, 292564");


    }





}
