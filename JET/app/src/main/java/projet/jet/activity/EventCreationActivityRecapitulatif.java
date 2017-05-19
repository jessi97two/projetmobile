package projet.jet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import projet.jet.GlobalApp;
import projet.jet.R;

/**
 * Created by Jess on 08/05/2017.
 */
public class EventCreationActivityRecapitulatif extends AppCompatActivity {

    private Toolbar toolbar;
    private RestActivity restAct;
    private EditText titreEvent;
    private TextView dateEvent;
    private EditText heureEvent;
    private TextView restaurantEvent;
    private TextView groupeEvent;
    private Button btnEnvoiEvent;

    private String titre;
    private String heure;
    private String datee;
    private String restaurant;
    private String idgroupe;
    private String idrestaurant;
    private String idsondage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.creation_event_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Création evenement");

        Intent intent = getIntent();
        String restaurantSelected = intent.getStringExtra("restaurant");
        String dateSelected = intent.getStringExtra("dateselected");
        idsondage = intent.getStringExtra("idsondage");

        titreEvent = (EditText) findViewById(R.id.editTextTitreCreationEvent);
        dateEvent = (TextView) findViewById(R.id.textViewDateEvent);
        heureEvent = (EditText) findViewById(R.id.editTextHeureEvent);
        restaurantEvent = (TextView) findViewById(R.id.textViewRestaurantEvent);
        groupeEvent = (TextView) findViewById(R.id.textViewGroupeEvent);
        btnEnvoiEvent = (Button) findViewById(R.id.buttonEnvoyerEvent);

        btnEnvoiEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                envoiEvent();
            }
        });

        restaurantEvent.setText(restaurantSelected);
        dateEvent.setText(dateSelected);

        restAct = new RestActivity();

        titre = titreEvent.getText().toString();
        datee = dateEvent.getText().toString();
        heure = heureEvent.getText().toString();
        restaurant = restaurantEvent.getText().toString();;

        // recuperer le groupe
        String req = "action=getGroupByIdSondage&idsondage="+idsondage;
        restAct.envoiRequete(req,"getGroupByIdSondage",(GlobalApp) getApplication(),null, this);

        // récupérer l'id du restaurant
        String resto = restaurant.replace(" ","__");
        String req2 = "action=getRestaurantBySondageName&idsondage="+idsondage+"&nom=" + resto;
        restAct.envoiRequete(req2,"getRestaurantBySondageName",(GlobalApp) getApplication(),null, this);
    }

    private void envoiEvent() {
        titre = titreEvent.getText().toString();
        heure = heureEvent.getText().toString().replace(".",":");
        String nom = titre.replace(" ","__");
        String req = "action=addEvent&nom="+nom+"&dateEvent="+datee+"&heureEvent="+heure+"&restaurant="
                +idrestaurant+"&iduser="+((GlobalApp)getApplication()).prefs.getString("id","") + "&idgroupe=" + idgroupe;
        restAct.envoiRequete(req,"addEvent",(GlobalApp) getApplication(),null, this);
    }

    public void getGroupName(JSONArray jsongroup) {
        try {
            groupeEvent.setText(jsongroup.getJSONObject(0).getString("nom"));
            idgroupe = jsongroup.getJSONObject(0).getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getIdRestaurant(JSONArray jsongroup) {
        try {
            idrestaurant = jsongroup.getJSONObject(0).getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void eventSent(String res) {
        if(Integer.parseInt(res) > 0) {
            String req = "action=archiverSondage&idsondage="+idsondage;
            restAct.envoiRequete(req,"archiverSondage",(GlobalApp) getApplication(),null, this);

            String req2 = "action=sendInvitation&idevent="+res+"&idgroupe="+idgroupe ;
            restAct.envoiRequete(req2,"sendInvitation",(GlobalApp) getApplication(),null, this);


            Intent gotogeneral = new Intent(this, GeneralActivity.class);
            startActivity(gotogeneral);
            this.finish();
        }
        else {
            Toast.makeText(getApplicationContext(),"Something is wrong !",Toast.LENGTH_SHORT).show();
        }
    }
}
