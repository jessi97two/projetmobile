package projet.jet.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
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
    private String dateSelected;

    private EditText datechoice1;
    private EditText datechoice2;
    private EditText datechoice3;

    private int day;
    private int month;
    private int year;

    static final int DATE_PICKER_ID = 999;

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

        datechoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSelected = "date 1";
                SelectDate();
            }
        });

        datechoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSelected = "date 2";
                SelectDate();
            }
        });

        datechoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSelected = "date 3";
                SelectDate();
            }
        });

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

    private void SelectDate() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        showDialog(DATE_PICKER_ID);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            // Show selected date
            if(dateSelected.equals("date 1")) {
                datechoice1.setText(new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" "));
            }
            else if(dateSelected.equals("date 2")) {
                datechoice2.setText(new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" "));
            }
            else if(dateSelected.equals("date 3")) {
                datechoice3.setText(new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" "));
            }
        }
    };

}
