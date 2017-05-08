package projet.jet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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
public class SondageCreationActivityGroupes extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnGoToPartRecap;
    RestActivity restAct;
    private List<String> groupeChecked;
    private ListView listGroupesSondage;
    ArrayList<PropositionsSondages> propositionsSondagesList = new ArrayList<PropositionsSondages>();

    private String titre;
    private String description;
    private List<String> datesList;
    private List<String> restaurantsList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_sondage_part_4);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Création sondage");

        btnGoToPartRecap = (Button) findViewById(R.id.btnGoToRecapSondage);
        listGroupesSondage = (ListView) findViewById(R.id.listViewGroupsCreationSondage);

        titre = getIntent().getStringExtra("titre");
        description = getIntent().getStringExtra("description");
        datesList = getIntent().getExtras().getStringArrayList("date");
        restaurantsList = getIntent().getExtras().getStringArrayList("restaurants");

        restAct = new RestActivity();
        String qs = "action=getGroups&iduser=";
        restAct.envoiRequete(qs+ ((GlobalApp)getApplication()).prefs.getString("id",""),"getGroups",((GlobalApp)getApplication()),null,this);


        btnGoToPartRecap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupechcked = groupeChecked.get(0);
                Intent gotoPart5 = new Intent(SondageCreationActivityGroupes.this, SondageCreationActivityRecapitulatif.class);
                gotoPart5.putExtra("titre",titre);
                gotoPart5.putExtra("description", description);
                gotoPart5.putStringArrayListExtra("date",(ArrayList<String>) datesList);
                gotoPart5.putStringArrayListExtra("restaurants",(ArrayList<String>)restaurantsList);
                gotoPart5.putExtra("groupe", groupechcked);
                startActivity(gotoPart5);
            }
        });

    }

    public void displayResult(JSONArray json, String type) {
        if(type.equals("groupsReceived")) {
            try {
                PropositionsSondages propositionsSondages;
                for (int l=0; l < json.length(); l++) {
                    propositionsSondages = new PropositionsSondages(json.getJSONObject(l).getString("nom"),json.getJSONObject(l).getString("id"),0);
                    propositionsSondagesList.add(propositionsSondages);
                }

                CustomListviewCheckboxAdapter adapter = new CustomListviewCheckboxAdapter(this, propositionsSondagesList);
                groupeChecked = adapter.getListPropositionsChecked();
                listGroupesSondage.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
