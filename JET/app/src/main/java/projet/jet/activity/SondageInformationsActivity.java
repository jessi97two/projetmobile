package projet.jet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import projet.jet.GlobalApp;
import projet.jet.R;

/**
 * Created by Jess on 05/04/2017.
 */
public class SondageInformationsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RestActivity restAct;
    private TextView txtSondageTitre;
    private TextView txtSondePar;
    private TextView txtDescriptif;
    private RadioButton radioBtnDate1;
    private RadioButton radioBtnDate2;
    private RadioButton radioBtnDate3;
    private CheckBox cbRestaurant1;
    private CheckBox cbRestaurant2;
    private CheckBox cbRestaurant3;
    private Button btnSendAnswers;
    private String iddate;
    private String iddate1;
    private String iddate2;
    private String iddate3;
    private String idresto;
    private String idresto1;
    private String idresto2;
    private String idresto3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sondage_informations_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Informations sondage");

        Intent intent = getIntent();
        final String idsondage = intent.getStringExtra("idsondage");

        txtSondageTitre = (TextView) findViewById(R.id.txtTitreSondage);
        txtSondePar = (TextView) findViewById(R.id.txtSendBy);
        txtDescriptif = (TextView) findViewById(R.id.txtDescription);

        radioBtnDate1 = (RadioButton) findViewById(R.id.radioButtonDate1);
        radioBtnDate2 = (RadioButton) findViewById(R.id.radioButtonDate2);
        radioBtnDate3 = (RadioButton) findViewById(R.id.radioButtonDate3);

        radioBtnDate1.setVisibility(View.INVISIBLE);
        radioBtnDate2.setVisibility(View.INVISIBLE);
        radioBtnDate3.setVisibility(View.INVISIBLE);

        cbRestaurant1 = (CheckBox) findViewById(R.id.checkBoxRestaurant1);
        cbRestaurant2 = (CheckBox) findViewById(R.id.checkBoxRestaurant2);
        cbRestaurant3 = (CheckBox) findViewById(R.id.checkBoxRestaurant3);

        cbRestaurant1.setVisibility(View.INVISIBLE);
        cbRestaurant2.setVisibility(View.INVISIBLE);
        cbRestaurant3.setVisibility(View.INVISIBLE);

        btnSendAnswers = (Button) findViewById(R.id.btnSendAnswersSondage);

        restAct = new RestActivity();

        String qs = "action=getInfosSondage&idsondage=";
        restAct.envoiRequete(qs+idsondage,"getInfosSondage",(GlobalApp) getApplication(),null, this);



        btnSendAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!radioBtnDate1.isChecked() && !radioBtnDate2.isChecked() && !radioBtnDate3.isChecked()) {
                    Toast.makeText(getApplicationContext(),"You have to select a date",Toast.LENGTH_SHORT).show();
                }
                else if(!cbRestaurant1.isChecked() && !cbRestaurant2.isChecked() && !cbRestaurant3.isChecked()) {
                    Toast.makeText(getApplicationContext(),"You have to select a restaurant",Toast.LENGTH_SHORT).show();
                }
                else {
                    // enregistrer dans la bdd la réponse au sondage
                    if(radioBtnDate1.isChecked()) iddate = iddate1;
                    if(radioBtnDate2.isChecked()) iddate = iddate2;
                    if(radioBtnDate3.isChecked()) iddate = iddate3;

                    if(cbRestaurant1.isChecked()) idresto = idresto1;
                    if(cbRestaurant2.isChecked()) idresto = idresto2;
                    if(cbRestaurant3.isChecked()) idresto = idresto3;

                    String qs1 = "action=putReponsesSondage&iduser="+ ((GlobalApp)getApplication()).prefs.getString("id","")+"&idsondage="+idsondage+"&iddate="+iddate+"&idresto="+idresto;
                    restAct.envoiRequete(qs1,"putReponsesSondage",(GlobalApp) getApplication(),null, SondageInformationsActivity.this);
                }
            }
        });

        cbRestaurant1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbRestaurant1.isChecked()) {
                    cbRestaurant2.setChecked(false);
                    cbRestaurant3.setChecked(false);
                }
            }
        });

        cbRestaurant2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbRestaurant2.isChecked()) {
                    cbRestaurant1.setChecked(false);
                    cbRestaurant3.setChecked(false);
                }
            }
        });

        cbRestaurant3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbRestaurant3.isChecked()) {
                    cbRestaurant2.setChecked(false);
                    cbRestaurant3.setChecked(false);
                }
            }
        });
    }


    public void displayResult(String type , JSONArray jsonarray, JSONArray jsonarraydates, JSONArray jsonarrayrestos) {
        if(type.equals("infossondage")) {
            try {
                txtSondageTitre.setText(jsonarray.getJSONObject(0).getString("titre"));
                txtSondePar.setText(jsonarray.getJSONObject(0).getString("sondeur"));
                txtDescriptif.setText(jsonarray.getJSONObject(0).getString("descriptif"));

                for(int i=0; i<jsonarraydates.length(); i++) {
                    if(i == 0) {
                        radioBtnDate1.setText(jsonarraydates.getJSONObject(0).getString("datepropose"));
                        iddate1 = jsonarraydates.getJSONObject(0).getString("id");
                        radioBtnDate1.setVisibility(View.VISIBLE);
                    }
                    else if(i == 1) {
                        radioBtnDate2.setText(jsonarraydates.getJSONObject(1).getString("datepropose"));
                        iddate2 = jsonarraydates.getJSONObject(0).getString("id");
                        radioBtnDate2.setVisibility(View.VISIBLE);
                    }
                    else if(i == 2) {
                        radioBtnDate3.setText(jsonarraydates.getJSONObject(2).getString("datepropose"));
                        iddate3 = jsonarraydates.getJSONObject(0).getString("id");
                        radioBtnDate3.setVisibility(View.VISIBLE);
                    }

                }

                for(int i=0; i<jsonarrayrestos.length(); i++) {
                    if(i == 0) {
                        cbRestaurant1.setText(jsonarrayrestos.getJSONObject(0).getString("nom"));
                        idresto1 = jsonarrayrestos.getJSONObject(0).getString("id");
                        cbRestaurant1.setVisibility(View.VISIBLE);
                    }
                    else if(i == 1) {
                        cbRestaurant2.setText(jsonarrayrestos.getJSONObject(1).getString("nom"));
                        idresto2 = jsonarrayrestos.getJSONObject(1).getString("id");
                        cbRestaurant2.setVisibility(View.VISIBLE);
                    }
                    else if(i == 2) {
                        cbRestaurant3.setText(jsonarrayrestos.getJSONObject(2).getString("nom"));
                        idresto3 = jsonarrayrestos.getJSONObject(2).getString("id");
                        cbRestaurant3.setVisibility(View.VISIBLE);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
