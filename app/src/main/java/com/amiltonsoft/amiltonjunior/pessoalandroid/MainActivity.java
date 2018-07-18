package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import extras.API;
import extras.DB;
import extras.Preferences;

public class MainActivity extends AppCompatActivity {

    // Método invocado ao criar a Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Inicializa o objeto Preferences
        final Preferences preferences = new Preferences(getBaseContext());
        preferences.clearPreferences();
        // Inicializa o objeto DB
        final DB db = new DB(getBaseContext());
        // Inicializa o objeto API
        final API api = new API(preferences.getAPIServerHost(), preferences.getAPIServerPort(), db);

        // Caso queira testar as funcionalidades, basta descomentar
        //preferences.testPreferences();
        //db.testDB(true);
        //api.testAPI();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
