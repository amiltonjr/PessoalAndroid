package extras;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.amiltonsoft.amiltonjunior.pessoalandroid.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import extras.DB;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// Classe de manipulação da API
public class API {

    // Atributos da classe
    private String server;
    private int port;
    private DB db;
    private String json_string      = ""; // String que será transmitida ao servidor da API
    public boolean thread_running   = false; // Flag que indica se a thread de transmitir está rodando
    public int response_code        = 0; // Código de resposta do servidor
    public int OK_CODE              = 200; // Código de sucesso
    public int ERROR_CODE           = 405; // Código de erro

    // Método construtor
    // @param (String) server - Host do servidor
    // @param (int) port - Número da porta do servidor
    // @param (DB) db - Objeto do banco de dados
    // @return (API) - Objeto da classe
    public API(String server, int port, DB db) {
        this.server     = server;
        this.port       = port;
        this.db         = db;

        System.out.println("Usando servidor da API, host = " + server + ", porta = " + port);
    }

    // Método que faz o envio dos dados da API
    // @param (void)
    // @return (void)
    public void sendData() throws Exception {
        // Faz a transmissão dos dados
        new APIthread().run();
    }

    // Método que retorna com a URL do servidor da API
    // @param (void)
    // @return (String) - URL formatada do servidor
    public String getAPIUrl() {
        return "http://" + server + ":" + port + "/api";
    }

    // Método que converte todos os dados das pessoas no banco de dados para JSON
    // @param (void)
    // @return (JSONObject) - Objeto JSON
    public JSONObject getAllPersonJson() throws JSONException {
        return (cursorToJson(db.readAll()));
    }

    // Método que faz a conversão de um objeto Cursor com resultados de pessoas para objeto JSON
    // @param (Cursor) cursor - Objeto Cursor com os dados da pessoa
    // @return (JSONObject) - Objeto JSON
    public JSONObject cursorToJson(Cursor cursor) throws JSONException {
        // Cria os objetos JSON
        JSONObject json     = new JSONObject();
        JSONArray people    = new JSONArray();

        // Vai para o início do Cursor
        cursor.moveToFirst();

        // Percorre os resultados
        while (cursor.getCount() > 0) {
            // Obtém os dados da pessoa
            int id      = cursor.getInt(cursor.getColumnIndexOrThrow(DB.PersonEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DB.PersonEntry.COLUMN_NAME));
            int age     = cursor.getInt(cursor.getColumnIndexOrThrow(DB.PersonEntry.COLUMN_AGE));
            String sex  = cursor.getString(cursor.getColumnIndexOrThrow(DB.PersonEntry.COLUMN_SEX));

            // Cria um objeto JSON
            JSONObject person = new JSONObject();
            person.put(DB.PersonEntry.COLUMN_NAME, name);
            person.put(DB.PersonEntry.COLUMN_AGE, age);
            person.put(DB.PersonEntry.COLUMN_SEX, sex);
            // Adiciona "person" em "people"
            people.put(person);

            // Se era o último resultado, encerra o loop
            if (cursor.isLast())
                break;

            // Move para o próximo resultado
            cursor.moveToNext();
        }

        // Concatena "people" com "json"
        json.put("person", people);

        return json;
    }

    // Método que faz o teste do envio dos dados da API
    // @param (void)
    // @return (void)
    public void testAPI() {
        db.testDB(false);

        System.out.println("\nEnviando dados para o servidor API...\n");
        try {
            // Envia os dados da API
            this.sendData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.deleteAll();
    }

    // Classe que faz o envio assíncrono dos dados da API
    public final class APIthread {

        // Método que executa a thread
        public void run() throws Exception {

            // Reseta o código de resposta do servidor
            response_code = 0;

            // Marca que a thread está rodando
            thread_running = true;

            // Obtém os dados em formato JSON
            json_string = getAllPersonJson().toString();

            System.out.println("Transmitindo dados da API...");

            // Cria um objeto Client HTTP
            OkHttpClient client = new OkHttpClient();

            // Prepara o envio dos dados
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("data", json_string).build();
            Request request         = new Request.Builder().url(getAPIUrl()).post(requestBody).build();

            // Faz o envio dos dados
            try (Response response = client.newCall(request).execute()) {

                // Em caso de erro, dispara uma exceção
                if (!response.isSuccessful()) {
                    // Marca que a thread não está rodando
                    thread_running = false;

                    throw new Exception();
                }

                // Converte a resposta para JSON
                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());

                    // Salva o código de resposta de acordo com o recebido do servidor
                    response_code = jsonResponse.getJSONObject("response").getInt("code");

                    // Marca que a thread não está rodando
                    thread_running = false;
                } catch (JSONException e) {
                    // Marca que a thread não está rodando
                    thread_running = false;

                    throw new Exception();
                }
            }

        }

    }

}
