package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private int selectedItemId = -1;

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
        final ListView listview = (ListView) findViewById(R.id.listview);

        // Define o listener de click da lista
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Define o ID do item selecionado
                selectedItemId = getItemId(parent.getItemAtPosition(position).toString());

                // Mostra o menu de contexto ao clicar no item
                listview.showContextMenu();

            }

        });

        // Define o listener de click longo da lista
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Define o ID do item selecionado
                selectedItemId = getItemId(parent.getItemAtPosition(position).toString());

                // Mostra o menu de contexto ao clicar no item
                listview.showContextMenu();

                return true;
            }

        });

        // Define o menu de contexto do item da lista
        listview.setOnCreateContextMenuListener(new AdapterView.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

                // Define os itens do menu de contexto
                contextMenu.add(Menu.NONE, 1, Menu.NONE, "Editar");
                contextMenu.add(Menu.NONE, 2, Menu.NONE, "Remover");

                //System.out.println("selectedItemId = " + selectedItemId);
            }
        });

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

    // Método que retorna com o ID de um registro baseado nos dados de uma listview
    public int getItemId(String content) {
        String lines[] = content.split("\n");
        return Integer.valueOf(lines[0].substring(4));
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
