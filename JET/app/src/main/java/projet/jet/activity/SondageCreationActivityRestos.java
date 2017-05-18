package projet.jet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import projet.jet.GlobalApp;
import projet.jet.R;
import projet.jet.adapter.CustomListviewCheckboxAdapter;
import projet.jet.classe.PropositionsSondages;

/**
 * Created by Jess on 01/05/2017.
 */
public class SondageCreationActivityRestos extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnGoToPart4;
    private List<String> listRestosChecked;
    RestActivity restAct;
    private ListView listRestaurantsSondage;
    ArrayList<PropositionsSondages> propositionsSondagesList = new ArrayList<PropositionsSondages>();
    private String titre;
    private String description;
    private List<String> datesList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_sondage_part_3);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Création sondage");

        btnGoToPart4 = (Button) findViewById(R.id.btnGotoSelectDestinataire);
        listRestaurantsSondage = (ListView) findViewById(R.id.listVieRestaurantsCreationSondage);

        titre = getIntent().getStringExtra("titre");
        description = getIntent().getStringExtra("description");
        datesList = getIntent().getExtras().getStringArrayList("date");

        restAct = new RestActivity();
        String qs = "action=getRestaurants&iduser=";
        restAct.envoiRequete(qs+ ((GlobalApp)getApplication()).prefs.getString("id",""),"getRestaurants",((GlobalApp)getApplication()),null,this);


        btnGoToPart4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listRestosChecked.size() == 0) {
                    Toast.makeText(getApplicationContext(),"Au moins un restaurant doit être sélectionné !",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent gotoPart4 = new Intent(SondageCreationActivityRestos.this, SondageCreationActivityGroupes.class);
                    gotoPart4.putExtra("titre",titre);
                    gotoPart4.putExtra("description", description);
                    gotoPart4.putStringArrayListExtra("date",(ArrayList<String>) datesList);
                    gotoPart4.putStringArrayListExtra("restaurants",(ArrayList<String>)listRestosChecked);
                    startActivity(gotoPart4);
                }

            }
        });
    }

    public void displayResult(JSONArray json, String type) {
        if(type.equals("restaurantsReceived")) {
            try {
                PropositionsSondages propositionsSondages;
                for (int l=0; l < json.length(); l++) {
                    propositionsSondages = new PropositionsSondages(json.getJSONObject(l).getString("nom"),json.getJSONObject(l).getString("id"),0);
                    propositionsSondagesList.add(propositionsSondages);
                }

                CustomListviewCheckboxAdapter adapter = new CustomListviewCheckboxAdapter(this, propositionsSondagesList);
                listRestosChecked = adapter.getListPropositionsChecked();
                listRestaurantsSondage.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
