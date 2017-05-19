package projet.jet.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import projet.jet.R;

/**
 * Created by Jess on 02/12/2016.
 */
public class NewAccountActivity extends AppCompatActivity {

    EditText edtnom;
    EditText edtprenom;
    EditText edtlogin;
    EditText edtpassword;
    EditText edtpasswordConf;
    EditText edtmail;
    EditText edttel;
    Button btnEnregistrerInsc;
    TextView registerErrorMsg;
    String urlData = getApplication().getString(R.string.url_data_php);

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_compte);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Création compte JET");


        edtnom = (EditText) findViewById(R.id.editTextNom);
        edtprenom = (EditText) findViewById(R.id.editTextPrenom);
        edtlogin = (EditText) findViewById(R.id.editTextLoginInsc);
        edtpassword = (EditText) findViewById(R.id.editTextPassword);
        edtpasswordConf = (EditText) findViewById(R.id.editTextPasswordConf);
        edtmail = (EditText) findViewById(R.id.editTextMail);
        edttel = (EditText) findViewById(R.id.editTextTel);

        registerErrorMsg = (TextView) findViewById(R.id.register_error);


        btnEnregistrerInsc = (Button) findViewById(R.id.buttonEnregistrerInsc);
        btnEnregistrerInsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((!edtnom.getText().toString().equals("")) && (!edtprenom.getText().toString().equals("")) && (!edtlogin.getText().toString().equals(""))
                        && (!edtpassword.getText().toString().equals("")) && (!edtpasswordConf.getText().toString().equals("")) &&
                        (!edtmail.getText().toString().equals("")) && (!edttel.getText().toString().equals(""))) {
                    if(edtpassword.getText().toString().equals(edtpasswordConf.getText().toString())) {
                        Log.i("LONG TEL", String.valueOf(edttel.getText().toString().length()));
                        if(edttel.getText().toString().length() == 10) {
                            if(edtlogin.getText().toString().length() < 25) {
                                if((edtmail.getText().toString().contains("@")) || (edtmail.getText().toString().contains(".com"))
                                        || (edtmail.getText().toString().contains(".fr")) || (edtmail.getText().toString().contains(".net"))) {
                                    // si tout les controle de sparametres sont effectué et que tout est bon
                                    NetAsync(view);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Mail is incorrect",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Length login incorrect ( must be inferior than 20 characters )",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Tel is incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Password must be the same as the confirmation of password",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String convertStreamToString(InputStream in) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class NetCheck extends AsyncTask<String, Integer, Boolean> {

        String nom = edtnom.getText().toString();
        String prenom = edtprenom.getText().toString();
        String login = edtlogin.getText().toString();
        String password = edtpassword.getText().toString();
        String mail = edtmail.getText().toString();
        String tel = edttel.getText().toString();

        String txtReponse = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("DEBUG TEST","onPreExecute");
        }

        @Override
        protected Boolean doInBackground(String... args) {


            ConnectivityManager cnMngr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                try {
                    String qs = "action=inscription" + "&nom=" + nom + "&prenom=" + prenom + "&login=" + login + "&password=" + password + "&mail=" + mail + "&tel=" + tel;
                    URL url = new URL(urlData + "?" + qs );
                    Log.i("DEBUG INSCRIPTION","url utilisée : " + url.toString());
                    HttpURLConnection urlConnection = null;
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    if (urlConnection.getResponseCode() == 200) {
                        InputStream in = null;
                        in = new BufferedInputStream(urlConnection.getInputStream());
                        txtReponse = convertStreamToString(in);
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
            Log.i("DEBUG TEST","onPostExecute,  res =" + txtReponse);
            if(th == true){
                if(txtReponse != "") {
                    try {
                        JSONObject json = new JSONObject(txtReponse);
                        Log.i("DEBUG TEST"," staut inscritpton vaut : " + json.getString("statutinscription"));
                        if(Integer.parseInt(json.getString("statutinscription")) == 0) {
                            registerErrorMsg.setText("User already exists");
                        }
                        else if(Integer.parseInt(json.getString("statutinscription")) == 1) {
                            registerErrorMsg.setText("Successfully Registered");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            else{
                registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }


    public void NetAsync(View view){
        new NetCheck().execute();
    }
}
