package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.content.SharedPreferences;
import android.content.Context;

public class Preferences {

    // Atributos da classe
    private String PREF_KEY         = "PESSOAS_PREFS";
    private String SERVER_KEY       = "server";
    private String PORT_KEY         = "port";
    private String DEFAULT_SERVER   = "192.168.100.218";
    private int DEFAULT_PORT        = 80;
    private Context context;

    // Método construtor
    public Preferences(Context context) {
        this.context = context;
    }

    // Método que faz a leitura do servidor da API
    public String getAPIServerHost() {
        String server = getPreferenceString(SERVER_KEY);

        // Se o servidor não estiver salvo ou for inválido, usa o padrão
        if (server.length() < 7) {
            server = DEFAULT_SERVER;

            setPreferenceString(SERVER_KEY, server);
        }

        return server;
    }

    // Método que faz a leitura da porta do servidor da API
    public int getAPIServerPort() {
        int port = getPreferenceInt(PORT_KEY);

        // Se a porta não estiver salva ou for inválida, usa a padrão
        if (port < 80 || port  > 49151) {
            port = DEFAULT_PORT;

            setPreferenceInt(PORT_KEY, port);
        }

        return port;
    }

    // Método que faz o salvamento de uma preferência em String
    public void setPreferenceString(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Método que faz o salvamento de uma preferência em int
    public void setPreferenceInt(String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    // Método que faz a leitura de uma preferência em String
    public String getPreferenceString(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        return sharedPreferences.getString(key, "");
    }

    // Método que faz a leitura de uma preferência em int
    public int getPreferenceInt(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        return sharedPreferences.getInt(key, 0);
    }

    // Método que apaga todas as preferências
    public void clearPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Método que testa o funcionamento das preferências
    public void testPreferences() {
        // Define uma preferência
        this.setPreferenceString("Amilton", "Junior");

        // Faz a leitura da preferência
        System.out.println("[Preference] Amilton = " + this.getPreferenceString("Amilton"));

        // Remove todas as preferências
        this.clearPreferences();
    }

}
