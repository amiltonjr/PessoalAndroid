package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.content.SharedPreferences;
import android.content.Context;

public class Preferences {

    // Atributos da classe
    private String PREF_KEY = "PESSOAS_PREFS";
    private Context context;

    // Método construtor
    public Preferences(Context context) {
        this.context = context;
    }

    // Método que faz o salvamento de uma preferência
    public void setPreference(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Método que faz a leitura de uma preferência
    public String getPreference(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        return sharedPreferences.getString(key, "");
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
        this.setPreference("Amilton", "Junior");

        // Faz a leitura da preferência
        System.out.println("[Preference] Amilton = " + this.getPreference("Amilton"));

        // Remove todas as preferências
        this.clearPreferences();
    }

}
