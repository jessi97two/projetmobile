package projet.jet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import projet.jet.R;

/**
 * Created by Jess on 01/05/2017.
 */
public class SondageCreationActivityDate extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnGoToPart3;
    private String titre;
    private String description;

    private EditText datechoice1;
    private EditText datechoice2;
    private EditText datechoice3;

    private List<String> listDates = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_sondage_part_2);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Création sondage");


        btnGoToPart3 = (Button) findViewById(R.id.btnGoToChoixRestos);
        datechoice1 = (EditText) findViewById(R.id.editTextDateChoice1);
        datechoice2 = (EditText) findViewById(R.id.editTextDateChoice2);
        datechoice3 = (EditText) findViewById(R.id.editTextDateChoice3);



        titre = getIntent().getStringExtra("titre");
        description = getIntent().getStringExtra("description");

        btnGoToPart3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(datechoice1.getText().toString().equals(""))) {
                    listDates.add(datechoice1.getText().toString());
                }
                if(!(datechoice2.getText().toString().equals(""))) {
                    listDates.add(datechoice2.getText().toString());
                }
                if(!(datechoice3.getText().toString().equals(""))) {
                    listDates.add(datechoice3.getText().toString());
                }

                Intent gotoPart3 = new Intent(SondageCreationActivityDate.this, SondageCreationActivityRestos.class);
                gotoPart3.putExtra("titre",titre);
                gotoPart3.putExtra("description",description);
                gotoPart3.putStringArrayListExtra("date",(ArrayList<String>) listDates);
                startActivity(gotoPart3);
            }
        });
    }

}
