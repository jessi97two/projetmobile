package projet.jet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import projet.jet.GlobalApp;
import projet.jet.R;

/**
 * Created by Jess on 12/04/2017.
 */
public class InvitationInformationsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RestActivity restAct;
    private TextView txtInvitNom;
    private TextView txtOrganisateur;
    private TextView txtDateInvit;
    private TextView txtLieuRdv;
    private TextView txtHeureRdv;
    private Button btnParticipe;
    private Button btnPeutetre;
    private Button btnNeParticipePas;
    private String idevent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.invitation_informations_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Informations invitation");

        Intent intent = getIntent();
        final String idinvitation = intent.getStringExtra("idinvitation");

        txtInvitNom = (TextView) findViewById(R.id.txtTitreEventInvit);
        txtOrganisateur = (TextView) findViewById(R.id.txtNomOrganisateurEventinvit);
        txtDateInvit = (TextView) findViewById(R.id.txtDateEventInvit);
        txtLieuRdv = (TextView) findViewById(R.id.txtLieuRdvInvit);
        txtHeureRdv = (TextView) findViewById(R.id.txtHeureRdvInvit);
        btnParticipe = (Button) findViewById(R.id.btnParticipe);
        btnPeutetre = (Button) findViewById(R.id.btnPeutEtre);
        btnNeParticipePas = (Button) findViewById(R.id.btnNeParticipePas);

        restAct = new RestActivity();

        String qs = "action=getInfosInvitation&idinvitation=";
        restAct.envoiRequete(qs+idinvitation,"getInfosInvitation",(GlobalApp) getApplication(),null, this);

        btnParticipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qs1 = "action=putReponsesInvitation&iduser="+ ((GlobalApp)getApplication()).prefs.getString("id","")+"&idevent="+idevent+"&reponseinvit=1";
                restAct.envoiRequete(qs1,"putReponsesInvitation",(GlobalApp) getApplication(),null, InvitationInformationsActivity.this);
            }
        });

        btnPeutetre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qs1 = "action=putReponsesInvitation&iduser="+ ((GlobalApp)getApplication()).prefs.getString("id","")+"&idevent="+idevent+"&reponseinvit=2";
                restAct.envoiRequete(qs1,"putReponsesInvitation",(GlobalApp) getApplication(),null, InvitationInformationsActivity.this);
            }
        });

        btnNeParticipePas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qs1 = "action=putReponsesInvitation&iduser="+ ((GlobalApp)getApplication()).prefs.getString("id","")+"&idevent="+idevent+"&reponseinvit=3";
                restAct.envoiRequete(qs1,"putReponsesInvitation",(GlobalApp) getApplication(),null, InvitationInformationsActivity.this);
            }
        });

    }

    public void displayResult(JSONArray jsonarray) {

        try {

            txtInvitNom.setText(jsonarray.getJSONObject(0).getString("nomevent"));
            String organisateur = jsonarray.getJSONObject(0).getString("nom") + " " + jsonarray.getJSONObject(0).getString("prenom");
            txtOrganisateur.setText(organisateur);
            txtDateInvit.setText(jsonarray.getJSONObject(0).getString("dateEvent"));
            txtLieuRdv.setText(jsonarray.getJSONObject(0).getString("restaurant"));
            txtHeureRdv.setText(jsonarray.getJSONObject(0).getString("heureDebut"));
            idevent = jsonarray.getJSONObject(0).getString("idevent");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
