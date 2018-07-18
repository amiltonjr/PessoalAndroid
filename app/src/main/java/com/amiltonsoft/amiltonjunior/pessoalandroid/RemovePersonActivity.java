package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import extras.DB;

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

        // Define os ponteiros dos elementos
        TextView msg    = (TextView) findViewById(R.id.lblRemovePerson);
        Button confirm  = (Button) findViewById(R.id.bntConfirm);
        Button cancel   = (Button) findViewById(R.id.btnCancel);

        // Recupera o ID do usuário passado via parâmetro
        Bundle b = getIntent().getExtras();
        if (b != null)
            userID = b.getInt(USER_ID_KEY);

        //System.out.println("ID do usuário: " + userID);

        // Obtém o nome da pessoa
        DB db = new DB(getBaseContext());
        String name = db.getPersonName(userID);

        // Exibe a mensagem de confirmação
        msg.setText("Tem certeza que deseja remover o usuário #" + userID + " de nome '" + name + "'?\nEssa operação não poderá ser desfeita!");

        // Listener do botão "Confirmar"
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //System.out.println("Botão Confirmar clicado!");

                // Remove a pessoa do banco de dados
                DB db = new DB(getBaseContext());
                db.delete(userID);

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
