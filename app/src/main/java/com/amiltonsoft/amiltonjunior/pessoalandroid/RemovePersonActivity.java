package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class RemovePersonActivity extends AppCompatActivity {

    // Atributos da classe
    private String USER_ID_KEY  = "ID"; // Chave do parâmetro para enviar entre as activities
    private int userID          = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Recupera o ID do usuário passado via parâmetro
        Bundle b = getIntent().getExtras();
        if (b != null)
            userID = b.getInt(USER_ID_KEY);

        System.out.println("ID do usuário: " + userID);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
