package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.content.Context;
import android.content.SharedPreferences;

// Classe Preferences
// Objetivo: Manipular as SharedPreferences para salvar e recuperar dados curtos do aplicativo
public class Preferences {

    // Atributos da classe
    private String PREF_KEY         = "PESSOAS_PREFS"; // Chave das preferências
    private String SERVER_KEY       = "server"; // Chave do atributo "server"
    private String PORT_KEY         = "port"; // Chave do atributo "port"
    private String DEFAULT_SERVER   = "192.168.0.107"; // Endereço IP padrão do servidor API
    private int DEFAULT_PORT        = 8080; // Porta padrão do servidor API
    private Context context;

    // Método construtor
    // @param (Context) context - Contexto da aplicação
    // @return (Preferences) - Objeto da classe
    public Preferences(Context context) {
        this.context = context;
    }

    // Método que faz a leitura do servidor da API
    // @param (void)
    // @return (String) - Host do servidor da API
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
    // @param (void)
    // @return (int) - Número da porta
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
    // @param (String) key - Chave do atributo
    // @param (String) value - valor do atributo
    // @return (void)
    public void setPreferenceString(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Método que faz o salvamento de uma preferência em int
    // @param (String) key - Chave do atributo
    // @param (int) value - valor do atributo
    // @return (void)
    public void setPreferenceInt(String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    // Método que faz a leitura de uma preferência em String
    // @param (String) key - Chave do atributo
    // @return (String) - Valor do atributo
    public String getPreferenceString(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        return sharedPreferences.getString(key, "");
    }

    // Método que faz a leitura de uma preferência em int
    // @param (String) key - Chave do atributo
    // @return (int) - Valor do atributo
    public int getPreferenceInt(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        return sharedPreferences.getInt(key, 0);
    }

    // Método que apaga todas as preferências
    // @param (void)
    // @return (void)
    public void clearPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Método que testa o funcionamento das preferências
    // @param (void)
    // @return (void)
    public void testPreferences() {
        // Define uma preferência
        this.setPreferenceString("Amilton", "Junior");

        // Faz a leitura da preferência
        System.out.println("[Preference] Amilton = " + this.getPreferenceString("Amilton"));

        // Remove todas as preferências
        this.clearPreferences();
    }

}
