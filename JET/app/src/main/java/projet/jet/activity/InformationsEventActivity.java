package projet.jet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import projet.jet.GlobalApp;
import projet.jet.R;

/**
 * Created by Jess on 11/05/2017.
 */
public class InformationsEventActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RestActivity restAct;
    private TextView txtNomEvent;
    private TextView txtDate;
    private TextView txtLieu;
    private TextView txtHeure;
    private TextView txtParticipants;
    private TextView txtParticipantsPeutEtre;
    private TextView txtParticipantsPas;
    private TextView txtPasRepondu;
    private String idevent;
    private String participants;
    private String participantspeutetre;
    private String participantspas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reponses_event_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Informations event");

        restAct = new RestActivity();

        txtNomEvent = (TextView) findViewById(R.id.textViewNomEvent);
        txtDate = (TextView) findViewById(R.id.textViewDate);
        txtLieu = (TextView) findViewById(R.id.textViewLieu);
        txtHeure = (TextView) findViewById(R.id.textViewHeure);
        txtParticipants = (TextView) findViewById(R.id.textViewPersonnesParticipe);
        txtParticipantsPeutEtre = (TextView) findViewById(R.id.textViewPersonnesParticipePeutEtre);
        txtParticipantsPas = (TextView) findViewById(R.id.textViewPersonnesParticipantPas);
        txtPasRepondu = (TextView) findViewById(R.id.textViewPersonnesPasRepondus);

        Intent intent = getIntent();
        idevent = intent.getStringExtra("idevent");

        String req = "action=getInfosEvent&idevent=" + idevent;
        restAct.envoiRequete(req,"getInfosEvent",(GlobalApp) getApplication(),null, this);

        String req2 = "action=getReponsesUsersEvent&idevent=" + idevent;
        restAct.envoiRequete(req2,"getReponsesUsersEvent",(GlobalApp) getApplication(),null, this);

    //    String req3 = "action=getUsersNonReponduEvent&idevent=" + idevent;
    //    restAct.envoiRequete(req3,"getUsersNonReponduEvent",(GlobalApp) getApplication(),null, this);

    }

    public void displayResult(String type, JSONArray json) {
        if(type.equals("infos")) {
            try {
                txtNomEvent.setText(json.getJSONObject(0).getString("nomEvent"));
                txtDate.setText(json.getJSONObject(0).getString("dateEvent"));
                txtLieu.setText(json.getJSONObject(0).getString("restaurant"));
                txtHeure.setText(json.getJSONObject(0).getString("heureDebut"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("reponsesevent")) {
            try {
                for(int i=0; i<json.length(); i++) {
                    String reponse = json.getJSONObject(i).getString("reponse");
                    String user = json.getJSONObject(i).getString("login");

                    if(reponse.equals("1")) {
                        if(participants != null) {
                            participants = participants + ", " + user;
                        }
                        else {
                            participants =  user;
                        }
                    }
                    else if(reponse.equals("2")) {
                        if(participantspeutetre != null) {
                            participantspeutetre = participantspeutetre + ", " + user;
                        }
                        else {
                            participants =  user;
                        }
                    }
                    else if(reponse.equals("3")) {
                        if(participantspas != null) {
                            participantspas = participantspas + ", " + user;
                        }
                        else {
                            participantspas =  user;
                        }
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            txtParticipants.setText(participants);
            txtParticipantsPeutEtre.setText(participantspeutetre);
            txtParticipantsPas.setText(participantspas);
        }
    }
}
