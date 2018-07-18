package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import extras.API;
import extras.DB;
import extras.Preferences;

public class MainActivity extends AppCompatActivity {

    // Atributos da classe
    private Preferences preferences;
    private DB db;
    private API api;

    // Método invocado ao criar a Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Inicializa o objeto Preferences
        preferences = new Preferences(getBaseContext());
        preferences.clearPreferences();
        // Inicializa o objeto DB
        db = new DB(getBaseContext());
        // Inicializa o objeto API
        api = new API(preferences.getAPIServerHost(), preferences.getAPIServerPort(), db);

        // Caso queira testar as funcionalidades, basta descomentar
        //preferences.testPreferences();
        //db.testDB(false);
        //api.testAPI();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Lista de pessoas
        ListView listview = (ListView) findViewById(R.id.listview);
        // Preenche a lista
        fillPersonList(listview);

        // Botão flutuante "+"
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // Listener do botão "+"
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre a AddPersonActivity
                openAddPersonActivity();
            }
        });
    }

    // Método que abre a activity AddPersonActivity
    // @param (void)
    // @return (void)
    public void openAddPersonActivity() {
        Intent intent = new Intent(this, AddPersonActivity.class);
        startActivity(intent);
    }

    // Método que abre a activity SettingsActivity
    // @param (void)
    // @return (void)
    public void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // Método que preenche a lista de pessoas
    // @param (ListView) listview - Ponteiro da lista
    // @return (void)
    public void fillPersonList(ListView listview) {
        ArrayList<String> dados = new ArrayList<String>();

        // Obtém os dados do banco de dados
        Cursor people = db.readAll();

        // Vai para o início do Cursor
        people.moveToFirst();

        // Percorre os resultados
        while (people.getCount() > 0) {
            // Obtém os dados da pessoa
            int id      = people.getInt(people.getColumnIndexOrThrow(DB.PersonEntry._ID));
            String name = people.getString(people.getColumnIndexOrThrow(DB.PersonEntry.COLUMN_NAME));
            int age     = people.getInt(people.getColumnIndexOrThrow(DB.PersonEntry.COLUMN_AGE));
            String sex  = people.getString(people.getColumnIndexOrThrow(DB.PersonEntry.COLUMN_SEX));

            // Adiciona os dados ao array String
            dados.add("ID: " + id + "\n" + name + " - " + age + " anos - Sexo " + sex);

            // Se era o último resultado, encerra o loop
            if (people.isLast())
                break;

            // Move para o próximo resultado
            people.moveToNext();
        }


        // Define o adapter e preenche a lista
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, dados);
        listview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Método listener do menu
    // @param (MenuItem) item - Item selecionado
    // @return (boolean) - Resultado a operação
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Se clicou na opção "Configurações" do menu
        if (id == R.id.action_settings) {
            // Abre a SettingsActivity
            openSettingsActivity();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
