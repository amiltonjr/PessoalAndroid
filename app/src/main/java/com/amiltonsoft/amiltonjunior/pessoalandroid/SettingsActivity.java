package com.amiltonsoft.amiltonjunior.pessoalandroid;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.webkit.URLUtil;
import android.widget.Toast;

import java.util.List;

import extras.Preferences;

// Classe das configurações
public class SettingsActivity extends AppCompatPreferenceActivity {

    // Atributo da classe
    private static Context context;

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            // Obtém o valor da preferência
            String stringValue = value.toString().trim();

            // Cria um objeto Preferences
            Preferences prefs = new Preferences(context);

            // Objeto auxiliar para validar a URL
            URLUtil urlUtil = new URLUtil();

            // Valida o valor digitado de acordo com o tipo da preferência

            // Se é o host do servidor
            if (preference.getKey().equals(prefs.SERVER_KEY) && (!stringValue.substring(0, 4).equals("http") || stringValue.length() < 14 || !urlUtil.isValidUrl(stringValue))) {
                Toast.makeText(context, "Caminho do servidor inválido!\nVerifique os dados digitados e tente novamente.", Toast.LENGTH_LONG).show();

                return false;
            }
            // Se é a porta do servidor
            else if (preference.getKey().equals(prefs.PORT_KEY) && (Integer.valueOf(stringValue) < 80 || Integer.valueOf(stringValue) > 49151)) {
                Toast.makeText(context, "Porta do servidor inválida!\nVerifique os dados digitados e tente novamente.", Toast.LENGTH_LONG).show();

                return false;
            }
            // Se é a caminho do servidor
            else if (preference.getKey().equals(prefs.PATH_KEY) && (stringValue.length() < 1 || (stringValue.length() == 1 && !stringValue.equals("/")))) {
                Toast.makeText(context, "Caminho/diretório do servidor inválido!\nVerifique os dados digitados e tente novamente.", Toast.LENGTH_LONG).show();

                return false;
            }

            // Remove alguma barra se houver no fim do host do servidor
            while (preference.getKey().equals(prefs.SERVER_KEY) && stringValue.charAt(stringValue.length() - 1) == '/')
                stringValue = stringValue.substring(0, stringValue.length() - 1);

            System.out.println("stringValue = <" + stringValue + ">");

            // Atualiza nas preferências globais do aplicativo
            prefs.setPreference(preference.getKey(), stringValue);

            // Atualiza nos valores da activity
            preference.setDefaultValue(stringValue);
            preference.setSummary(stringValue);

            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        // Salva o contexto no atributo da classe
        context = getBaseContext();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName) || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Obtém os dados das preferências
            Preferences prefs = new Preferences(getContext());

            // Define os valores padrão
            findPreference(prefs.SERVER_KEY).setDefaultValue(prefs.getAPIServerHost());
            findPreference(prefs.PORT_KEY).setDefaultValue(prefs.getAPIServerPort());
            findPreference(prefs.PATH_KEY).setDefaultValue(prefs.getAPIServerPath());

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(prefs.SERVER_KEY));
            bindPreferenceSummaryToValue(findPreference(prefs.PORT_KEY));
            bindPreferenceSummaryToValue(findPreference(prefs.PATH_KEY));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}
