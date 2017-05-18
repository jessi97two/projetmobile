package projet.jet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import projet.jet.GeneralActivity;
import projet.jet.GlobalApp;
import projet.jet.R;

/**
 * Created by Jess on 07/05/2017.
 */
public class ResultatsSondageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RestActivity restAct;
    private TableLayout tableChoixJours;
    private TableLayout tableChoixRestaurants;
    private TextView titreSondage;
    private Button btnCreationEvenement;
    private String idsondage;
    private String iddate;
    private String nombreVotantsDates;
    private String nombreVotantsRestaurants;
    private String choixdate;
    private String idrestaurant;
    private String choixrestaurant;
    private String dateSelected;
    private String restaurantSelected;
    private List<String> listDateChecked;
    private List<String> listRestaurantChecked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultats_sondage_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Résultats sondage");

        restAct = new RestActivity();

        listDateChecked = new ArrayList<String>();
        listRestaurantChecked = new ArrayList<String>();

        titreSondage = (TextView) findViewById(R.id.textViewtitreSondageReusltat);
        tableChoixJours = (TableLayout) findViewById(R.id.table_choixJours);
        tableChoixRestaurants = (TableLayout) findViewById(R.id.table_choixRestaurants);
        btnCreationEvenement = (Button) findViewById(R.id.buttonCreationEvent);

        Intent intent = getIntent();
        idsondage = intent.getStringExtra("idsondage");
        final String nomsondage = intent.getStringExtra("nomsondage");

        titreSondage.setText(nomsondage);


        String req = "action=getResultatsDateSondage&idsondage=" + idsondage;
        restAct.envoiRequete(req,"getResultatsDateSondage",(GlobalApp) getApplication(),null, this);


        btnCreationEvenement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listDateChecked.size() > 1 || listDateChecked.size() == 0) {
                    Toast.makeText(getApplicationContext(),"Only one date must be selected !",Toast.LENGTH_SHORT).show();
                }
                else if(listRestaurantChecked.size() > 1 || listRestaurantChecked.size() == 0) {
                    Toast.makeText(getApplicationContext(),"Only one restaurant must be selected !",Toast.LENGTH_SHORT).show();
                }
                else {
                    gotoCreationEvent();
                }
            }
        });

    }

    private void gotoCreationEvent() {
        dateSelected = listDateChecked.get(0);
        restaurantSelected = listRestaurantChecked.get(0);

        Intent gotocreationevent = new Intent(this, EventCreationActivityRecapitulatif.class);
        gotocreationevent.putExtra("dateselected",dateSelected);
        gotocreationevent.putExtra("restaurant", restaurantSelected);
        gotocreationevent.putExtra("idsondage", idsondage);
        startActivity(gotocreationevent);
    }

    public void loadUsersResultatsDatesSondage(JSONArray json) {
        try {

            tableChoixJours.setStretchAllColumns(true);
            tableChoixJours.bringToFront();

            for(int i = 0; i < json.length(); i++) {
                iddate = json.getJSONObject(i).getString("iddate");
                nombreVotantsDates = json.getJSONObject(i).getString("nbre");
                choixdate = json.getJSONObject(i).getString("choixdate");

                String req = "action=getUsersResultatsDateSondage&idsondage=" + idsondage + "&iddate=" + iddate;
                restAct.envoiRequete(req,"getUsersResultatsDateSondage",(GlobalApp) getApplication(),null, this);

            }

            String req2 = "action=getResultatsRestaurantSondage&idsondage=" + idsondage;
            restAct.envoiRequete(req2,"getResultatsRestaurantSondage",(GlobalApp) getApplication(),null, this);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadUsersResultatsRestaurantsSondage(JSONArray json) {
        try {
            tableChoixRestaurants.setStretchAllColumns(true);
            tableChoixRestaurants.bringToFront();

            for(int i = 0; i < json.length(); i++) {
                idrestaurant = json.getJSONObject(i).getString("idrestaurant");
                nombreVotantsRestaurants = json.getJSONObject(i).getString("nbre");
                choixrestaurant = json.getJSONObject(i).getString("choixresto");

                String req = "action=getUsersResultatsRestaurantSondage&idsondage=" + idsondage + "&idrestaurant=" + idrestaurant;
                restAct.envoiRequete(req,"getUsersResultatsRestaurantSondage",(GlobalApp) getApplication(),null, this);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void completeTableChoixDates(JSONArray json) {
        try {
            String personnes = "";

            for(int i=0; i<json.length(); i++) {
                personnes = personnes + " " + json.getJSONObject(i).getString("login");
            }

            TableRow tr =  new TableRow(this);
            tr.setBackgroundResource(R.drawable.table_row_bg);
            tr.setPadding(10,0,10,0);

            TextView c1 = new TextView(this);
            c1.setText(nombreVotantsDates);
            c1.setBackgroundResource(R.drawable.table_cell_bg);
            c1.setPadding(5,5,5,5);
            c1.setGravity(Gravity.CENTER);

            CheckBox c2 = new CheckBox(this);
            c2.setBackgroundResource(R.drawable.table_cell_bg);
            c2.setPadding(10,10,10,0);
            c2.setGravity(Gravity.CENTER);
            c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        listDateChecked.add(choixdate);
                    }
                    else {
                        listDateChecked.remove(choixdate);
                    }
                }
            });

            TextView c3 = new TextView(this);
            c3.setText(choixdate);
            c3.setBackgroundResource(R.drawable.table_cell_bg);
            c3.setPadding(10,0,10,0);

            TextView c4 = new TextView(this);
            c4.setText(personnes);
            c4.setBackgroundResource(R.drawable.table_cell_bg);
            c4.setPadding(10,0,10,0);

            tr.addView(c1);
            tr.addView(c2);
            tr.addView(c3);
            tr.addView(c4);

            tableChoixJours.addView(tr);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void completeTableChoixRestaurants(JSONArray json) {
        try {
            String personnes = "";

            for(int i=0; i<json.length(); i++) {
                personnes = personnes + " " + json.getJSONObject(i).getString("login");
            }

            TableRow tr =  new TableRow(this);
            tr.setBackgroundResource(R.drawable.table_row_bg);
            tr.setPadding(10,0,10,0);

            TextView c1 = new TextView(this);
            c1.setText(nombreVotantsRestaurants);
            c1.setBackgroundResource(R.drawable.table_cell_bg);
            c1.setPadding(5,5,5,5);
            c1.setGravity(Gravity.CENTER);

            CheckBox c2 = new CheckBox(this);
            c2.setBackgroundResource(R.drawable.table_cell_bg);
            c2.setPadding(10,10,10,0);
            c2.setGravity(Gravity.CENTER);
            c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        listRestaurantChecked.add(choixrestaurant);
                    }
                    else {
                        listRestaurantChecked.remove(choixrestaurant);
                    }
                }
            });

            TextView c3 = new TextView(this);
            c3.setText(choixrestaurant);
            c3.setBackgroundResource(R.drawable.table_cell_bg);
            c3.setPadding(10,0,10,0);

            TextView c4 = new TextView(this);
            c4.setText(personnes);
            c4.setBackgroundResource(R.drawable.table_cell_bg);
            c4.setPadding(10,0,10,0);

            tr.addView(c1);
            tr.addView(c2);
            tr.addView(c3);
            tr.addView(c4);

            tableChoixRestaurants.addView(tr);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

 }
