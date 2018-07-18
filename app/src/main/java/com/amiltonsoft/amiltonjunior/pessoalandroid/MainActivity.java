package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import extras.API;
import extras.DB;
import extras.Preferences;

public class MainActivity extends AppCompatActivity {

    // Atributos da classe
    private Preferences preferences;
    private DB db;
    private API api;
    private ProgressDialog pd;
    private int selectedItemId      = -1;
    private String USER_ID_KEY      = "ID"; // Chave do parâmetro para enviar entre as activities

    // Método que executa as rotinas de preparação e carregamento do aplicativo
    private void startApplication() {

        // Inicializa o objeto Preferences
        preferences = new Preferences(getBaseContext());

        // Inicializa o objeto DB
        db = new DB(getBaseContext());

        // Inicializa o objeto API
        api = new API(preferences.getAPIServerHost(), preferences.getAPIServerPort(), db);

        // Caso queira testar as funcionalidades, basta descomentar
        //preferences.testPreferences();
        //db.testDB(false);
        //api.testAPI();

        // Preenche a lista de pessoas
        fillPersonList((ListView) findViewById(R.id.listview));
    }

    // Método invocado ao resumir a visibilidade da activity
    @Override
    protected void onResume() {
        super.onResume();

        // Executa as rotinas de inicialização
        startApplication();
    }

    // Método invocado ao criar a Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o strict mode para permitir a execução das threads
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Botão transmitir
        Button transmit = (Button) findViewById(R.id.bntTransmit);
        // Listener do botão transmitir
        transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //System.out.println("Botão Transmitir clicado!");

                try {
                    // Envia os dados da API
                    sendAPIdata();
                } catch (Exception e) {
                    // Exibe uma mensagem
                    Toast.makeText(getBaseContext(), "Erro ao transmitir os dados!\nVerifique as configurações e tente novamente.", Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }
            }
        });

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

                // Índices das opções do menu de contexto
                int MENU_EDIT   = 0;
                int MENU_DELETE = 1;

                // Define os itens do menu de contexto
                contextMenu.add("Editar");
                contextMenu.add("Remover");

                //System.out.println("selectedItemId = " + selectedItemId);

                // Listener da opção Editar
                contextMenu.getItem(MENU_EDIT).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        //System.out.println("Clicou no Editar para o ID " + selectedItemId);

                        // Inicia a EditPersonActivity passando o ID do item como parâmetro
                        Intent intent = new Intent(MainActivity.this, EditPersonActivity.class);
                        Bundle b = new Bundle();
                        b.putInt(USER_ID_KEY, selectedItemId);
                        intent.putExtras(b);
                        startActivity(intent);

                        return false;
                    }
                });

                // Listener da opção Remover
                contextMenu.getItem(MENU_DELETE).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        //System.out.println("Clicou no Remover para o ID " + selectedItemId);

                        // Inicia a RemovePersonActivity passando o ID do item como parâmetro
                        Intent intent = new Intent(MainActivity.this, RemovePersonActivity.class);
                        Bundle b = new Bundle();
                        b.putInt(USER_ID_KEY, selectedItemId);
                        intent.putExtras(b);
                        startActivity(intent);

                        return false;
                    }
                });
            }
        });

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

    // Método que envia os dados da API e exibe um dialog para o usuário aguardar
    // @param (void)
    // @return (void)
    private void sendAPIdata() {

        // Exibe a mensagem de aguarde
        pd = ProgressDialog.show(MainActivity.this, "Transmitindo", "Aguarde...");

        //start a new thread to process job
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Faz o envio dos dados da API
                    api.sendData();
                } catch (Exception e) {
                    // Envia uma mensagem para sumir a mensagem de aguarde
                    handler.sendEmptyMessage(0);

                    e.printStackTrace();
                }

                // Aguarda a finalização da thread da API
                while (api.thread_running) {
                    try {
                        Thread.sleep(1000); // Aguarda 1 segundo
                    } catch (InterruptedException e) {
                        // Envia uma mensagem para sumir a mensagem de aguarde
                        handler.sendEmptyMessage(0);

                        e.printStackTrace();
                    }
                }

                // Envia uma mensagem para sumir a mensagem de aguarde
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    // Processa as mensagens entre as threads no Android
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // Some com a mensagem de aguarde
            pd.dismiss();

            // Exibe uma mensagem de acordo com a resposta do servidor
            if (api.response_code == api.OK_CODE)
                Toast.makeText(getBaseContext(), "Dados transmitidos com sucesso!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getBaseContext(), "Erro ao transmitir os dados!\nVerifique as configurações e tente novamente.", Toast.LENGTH_LONG).show();
        }
    };

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
    // @param (String) content - Conteúdo do item da lista
    // @return (int) - ID do item no banco de dados
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
