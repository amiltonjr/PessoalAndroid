package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class API {

    // Atributos da classe
    private String server;
    private int port;
    private DB db;

    // Método construtor
    public API(String server, int port, DB db) {
        this.server = server;
        this.port   = port;
        this.db     = db;

        System.out.println("Usando servidor da API, host = " + server + ", porta = " + port);
    }

    // Método que faz o envio dos dados da API
    public String sendData() throws JSONException, IOException {
        // Obtém os dados em formato JSON
        String json = getAllPersonJson().toString();

        System.out.println("String json = " + json);

        // Cria um objeto Client HTTP
        OkHttpClient client = new OkHttpClient();

        // Faz o envio dos dados e salva a resposta
        RequestBody body    = new FormBody.Builder().add("message", "Your message").build();
        Request request     = new Request.Builder().url(getAPIUrl()).method("post", body).build();
        Response response   = client.newCall(request).execute();

        System.out.print("Resposta do servidor: ");

        // Retorna com a resposta
        return response.body().string();
    }

    // Método que retorna com a URL do servidor da API
    public String getAPIUrl() {
        return "http://" + server + ":" + port + "/api";
    }

    // Método que converte todos os dados das pessoas no banco de dados para JSON
    public JSONObject getAllPersonJson() throws JSONException {
        return (cursorToJson(db.readAll()));
    }

    // Método que faz a conversão de um objeto Cursor com resultados de pessoas para objeto JSON
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

}
