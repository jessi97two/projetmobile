package projet.jet.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import projet.jet.R;

/**
 * Created by Jess on 05/04/2017.
 */
public class SondageCreationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnGoToPart2;
    private EditText titre;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_sondage_part_1);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Création sondage");

        btnGoToPart2 = (Button) findViewById(R.id.btnGoToChoixDates);
        titre = (EditText) findViewById(R.id.editTexttitreCreationSondage);
        description = (EditText) findViewById(R.id.editTextDescriptionCreationSondage);


        btnGoToPart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoPart2 = new Intent(SondageCreationActivity.this, SondageCreationActivityDate.class);
                gotoPart2.putExtra("titre",titre.getText().toString());
                gotoPart2.putExtra("description",description.getText().toString());
                startActivity(gotoPart2);
            }
        });
    }

}
