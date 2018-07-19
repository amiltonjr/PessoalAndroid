package extras;

import android.content.Context;
import android.content.SharedPreferences;

// Classe Preferences
// Objetivo: Manipular as SharedPreferences para salvar e recuperar dados curtos do aplicativo
public class Preferences {

    // Atributos da classe
    private String PREF_KEY         = "PESSOAS_PREFS"; // Chave das preferências
    public String SERVER_KEY        = "server_host"; // Chave do atributo "server"
    public String PORT_KEY          = "server_port"; // Chave do atributo "port"
    private String DEFAULT_SERVER   = "192.168.0.107"; // Endereço IP padrão do servidor API
    private String DEFAULT_PORT     = "8080"; // Porta padrão do servidor API
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
        String server = getPreference(SERVER_KEY);

        // Se o servidor não estiver salvo ou for inválido, usa o padrão
        if (server.length() < 7) {
            server = DEFAULT_SERVER;

            setPreference(SERVER_KEY, server);
        }

        return server;
    }

    // Método que faz a leitura da porta do servidor da API
    // @param (void)
    // @return (String) - Número da porta
    public String getAPIServerPort() {
        String port = getPreference(PORT_KEY);

        // Se a porta não estiver salva ou for inválida, usa a padrão
        if (port.equals("") || Integer.valueOf(port) < 80 || Integer.valueOf(port)  > 49151) {
            port = DEFAULT_PORT;

            setPreference(PORT_KEY, port);
        }

        return port;
    }

    // Método que faz o salvamento de uma preferência
    // @param (String) key - Chave do atributo
    // @param (String) value - valor do atributo
    // @return (void)
    public void setPreference(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Método que faz a leitura de uma preferência em String
    // @param (String) key - Chave do atributo
    // @return (String) - Valor do atributo
    public String getPreference(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        return sharedPreferences.getString(key, "");
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
        this.setPreference("Amilton", "Junior");

        // Faz a leitura da preferência
        System.out.println("[Preference] Amilton = " + this.getPreference("Amilton"));

        // Remove todas as preferências
        this.clearPreferences();
    }

}
