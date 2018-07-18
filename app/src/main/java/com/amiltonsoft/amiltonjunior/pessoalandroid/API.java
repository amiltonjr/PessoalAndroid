package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
