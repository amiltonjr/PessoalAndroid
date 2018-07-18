package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddPersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cria os ponteiros para os elementos da activity
        EditText name   = (EditText) findViewById(R.id.editTextName);
        EditText age    = (EditText) findViewById(R.id.editTextAge);
        Spinner sex     = (Spinner) findViewById(R.id.spinnerSex);
        Button save     = (Button) findViewById(R.id.btnSave);
        Button cancel   = (Button) findViewById(R.id.btnCancel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
