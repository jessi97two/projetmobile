package projet.jet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import projet.jet.GeneralActivity;
import projet.jet.GlobalApp;
import projet.jet.R;
import projet.jet.classe.Contact;

/**
 * Created by Jess on 02/05/2017.
 */
public class SondageCreationActivityRecapitulatif extends AppCompatActivity {

    private Toolbar toolbar;
    private RestActivity restAct;

    private String titre;
    private String description;
    private List<String> datesList;
    private List<String> restaurantsList;
    private String groupe;
    private String nomgroupe;
    private String restaurants;
    private String dates;
    private String idsondageencours;

    private Button btnLancementSondage;
    private TextView txtTitre;
    private TextView txtDescriptif;
    private TextView txtDate;
    private TextView txtRestaurants;
    private TextView txtGroupe;

    private int index = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_sondage_part_5);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Création sondage");

        titre = getIntent().getStringExtra("titre");
        description = getIntent().getStringExtra("description");
        datesList = getIntent().getExtras().getStringArrayList("date");
        restaurantsList = getIntent().getExtras().getStringArrayList("restaurants");
        groupe = getIntent().getStringExtra("groupe");



        restAct = new RestActivity();

        txtTitre = (TextView) findViewById(R.id.textViewTitreLS);
        txtDescriptif = (TextView) findViewById(R.id.textViewDescriptionLS);
        txtDate = (TextView) findViewById(R.id.textViewDatesLS);
        txtRestaurants = (TextView) findViewById(R.id.textViewRestaurantsLS);
        txtGroupe = (TextView) findViewById(R.id.textViewGroupeLS);

        String req2 = "action=getGroupById&idgroup="+groupe;
        restAct.envoiRequete(req2,"getGroupById",(GlobalApp) getApplication(),null,this);



        btnLancementSondage = (Button) findViewById(R.id.buttonLancementSondage);
        btnLancementSondage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *  TO DO :
                 *  enregistrer l'envoi du sondage dans la bdd
                 *  get les participants du groupe et leur envoyé le sondage
                 */
                // récupérer les id sur le phone concernant le groupe dans la bdd


                getIdOnTelGroup();
                //lancerSondage();
            }
        });
    }

    private void getIdOnTelGroup() {
        String req = "action=getIdOnTelGroup&idgroup=" + groupe;
        restAct.envoiRequete(req,"getIdOnTelGroup",(GlobalApp) getApplication(),null,this);
    }

    public void majContactsGroupe(String idontelGroup) {

        List<Contact> contactsList = new ArrayList<Contact>();

        for(String id : idontelGroup.split(",")) {
            ContactsActivity contactsActivity = new ContactsActivity();
            contactsList = contactsActivity.fetchGroupMembers(id,this);

            for(Contact contact : contactsList) {
                String numero = contact.phNo;
                String nom = contact.name;

                String num = numero.replace("+33","0");

                String qs = "action=getContactByNumber&iduser=" + ((GlobalApp)getApplication()).prefs.getString("id","") + "&numero="
                        + num + "&name=" + nom + "&groupe=" + groupe;
                restAct.envoiRequete(qs,"getContactByNumber",(GlobalApp) getApplication(),null,this);
            }
        }

        lancerSondage();

    }

    public void lancerSondage() {

        String qs = "action=addSondage&iduser=" + ((GlobalApp)getApplication()).prefs.getString("id","") + "&titre="
                + titre + "&description=" + description + "&groupe=" + groupe;
        restAct.envoiRequete(qs,"addSondage",(GlobalApp) getApplication(),null,this);

    }

    public void displayResult(JSONArray json, String type) {
        if(type.equals("groupeNameReceived")) {
            try {
                nomgroupe = json.getJSONObject(0).getString("nom");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(String restaurant : restaurantsList) {
                String req1 = "action=getRestaurantById&idrestaurant="+restaurant;
                restAct.envoiRequete(req1,"getRestaurantById",(GlobalApp) getApplication(),null,this);
            }
        }
        else if(type.equals("restaurantNameReceived")) {
            try {
                if(index == 0) {
                    restaurants = json.getJSONObject(0).getString("nom");
                    index = 1;
                }
                else {
                    restaurants = restaurants + "," + json.getJSONObject(0).getString("nom");
                }

                complete();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void suiteEnregistrementRestaurant(String idsondage) {

        idsondageencours = idsondage;

        for(String restaurant : restaurantsList) {
            String req1 = "action=addProprositionRestaurant&idsondage=" + idsondage + "&idrestaurant="+restaurant;
            restAct.envoiRequete(req1,"addProprositionRestaurant",(GlobalApp) getApplication(),null,this);
        }

        suiteEnregistrementDate();
    }

    public void suiteEnregistrementDate() {

        for(String datep : datesList) {
            String req1 = "action=addProprositionDate&idsondage=" + idsondageencours + "&datepropose="+datep;
            restAct.envoiRequete(req1,"addProprositionDate",(GlobalApp) getApplication(),null,this);
        }

        Intent gotohomepage = new Intent(this, GeneralActivity.class);
        startActivity(gotohomepage);
        finish();
    }

    private void complete() {
        txtTitre.setText(titre);
        txtDescriptif.setText(description);

        int inx = 0;
        for(String datesondage : datesList) {
            if(inx == 0) {
                dates = datesondage;
                inx = 1;
            }
            else {
                dates = dates + " , " + datesondage;
            }

        }

        txtDate.setText(dates);
        txtRestaurants.setText(restaurants);
        txtGroupe.setText(nomgroupe);
    }
}

