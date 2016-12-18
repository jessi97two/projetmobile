package projet.jet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Jess on 18/12/2016.
 */
public class LoginActivity extends AppCompatActivity {

    private Button btnNewAccount;
    private Button buttonSignIn;
    private EditText edtLogin;
    private EditText edtPwd;

    GlobalApp ga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_view);
        ga = (GlobalApp) getApplication();

        edtLogin = (EditText) findViewById(R.id.editTextLoginAccueil);
        edtPwd = (EditText) findViewById(R.id.editTextPwd);

        btnNewAccount = (Button) findViewById(R.id.buttonNewAccount);
        btnNewAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent versNewAccount = new Intent(LoginActivity.this, NewAccountActivity.class);
                startActivity(versNewAccount);
            }
        });

        buttonSignIn = (Button)  findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(edtLogin.getText().toString().equals("")) && !(edtPwd.getText().toString().equals(""))) {
                    NetAsync(v);
                }
            }
        });
    }

    private class NetCheck extends AsyncTask<String, Integer, Boolean> {

        String txtReponse = "";

        String login = edtLogin.getText().toString();
        String password = edtPwd.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                try {
                    String urlData = "http://192.168.1.12/2i/APP2/projetmobile/data.php";
                    String qs = "action=connexion" + "&login=" + login + "&password=" + password;
                    URL url = new URL(urlData + "?" + qs );
                    Log.i("DEBUG CONNEXION","url utilisée : " + url.toString());
                    HttpURLConnection urlConnection = null;
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    if (urlConnection.getResponseCode() == 200) {
                        InputStream in = null;
                        in = new BufferedInputStream(urlConnection.getInputStream());
                        txtReponse = CommonsFunctions.convertStreamToString(in);
                        urlConnection.disconnect();
                        return Boolean.TRUE;
                    }
                }
                catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return Boolean.FALSE;
        }

        @Override
        protected void onPostExecute(Boolean th) {
            if(th == true){
                if(txtReponse != "") {
                    try {
                        Log.i("DEBUG TEST REP", txtReponse);
                        JSONObject json = new JSONObject(txtReponse);
                        Log.i("DEBUG TEST","pour le user " + json.getString("idUser") + " connecte vaut : " + json.getString("connecte"));
                        if(Integer.parseInt(json.getString("connecte")) == 0) {
                          //  registerErrorMsg.setText("Login ou Mot de passe incorrect");
                        }
                        else if(Integer.parseInt(json.getString("connecte")) == 1) {
                          //  registerErrorMsg.setText("Success");
                            SharedPreferences.Editor editor = ga.prefs.edit();
                            editor.clear();
                            editor.putString("login",login);
                            editor.putString("password",password);
                            editor.commit();

                            Intent versGeneralPAge = new Intent(LoginActivity.this,GeneralActivity.class);
                            startActivity(versGeneralPAge);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void NetAsync(View view){
        new NetCheck().execute();
    }

}
