package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import extras.DB;

public class EditPersonActivity extends AppCompatActivity {

    // Atributos da classe
    private String USER_ID_KEY      = "ID"; // Chave do parâmetro para enviar entre as activities
    private int userID              = -1;
    private String[] arraySpinner; // Tipos de sexo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cria os ponteiros para os elementos da activity
        final EditText name   = (EditText) findViewById(R.id.editTextName);
        final EditText age    = (EditText) findViewById(R.id.editTextAge);
        final Spinner sex     = (Spinner) findViewById(R.id.spinnerSex);
        Button save     = (Button) findViewById(R.id.btnSave);
        Button cancel   = (Button) findViewById(R.id.btnCancel);
        Button remove   = (Button) findViewById(R.id.btnRemove);

        // Recupera o ID do usuário passado via parâmetro
        Bundle b = getIntent().getExtras();
        if (b != null)
            userID = b.getInt(USER_ID_KEY);

        //System.out.println("ID do usuário: " + userID);

        // Preenche com os dados do usuário no banco de dados
        final DB db = new DB(getBaseContext());
        name.setText(db.getPersonName(userID));
        age.setText(String.valueOf(db.getPersonAge(userID)));

        // Adiciona as opções ao spinner
        if (db.getPersonSex(userID).equals("M"))
            arraySpinner   = new String[] { "M", "F" }; // Tipos de sexo
        else
            arraySpinner   = new String[] { "F", "M" }; // Tipos de sexo

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(adapter);

        // Listener do botão "Salvar"
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //System.out.println("Botão Salvar clicado!");

                // Atualiza a informação no banco de dados
                db.update(userID, name.getText().toString(), Integer.valueOf(age.getText().toString()), sex.getSelectedItem().toString());

                // Encerra a activity
                finish();

            }
        });

        // Listener do botão "Cancelar"
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //System.out.println("Botão Cancelar clicado!");

                // Encerra a activity
                finish();

            }
        });

        // Listener do botão "Excluir"
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //System.out.println("Botão Excluir clicado!");

                // Inicia a RemovePersonActivity passando o ID do item como parâmetro
                Intent intent = new Intent(EditPersonActivity.this, RemovePersonActivity.class);
                Bundle b = new Bundle();
                b.putInt(USER_ID_KEY, userID);
                intent.putExtras(b);
                startActivity(intent);

                // Encerra a activity atual
                finish();

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
