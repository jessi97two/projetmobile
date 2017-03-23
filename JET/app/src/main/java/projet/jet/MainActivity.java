package projet.jet;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_view);
        btnNewAccount = (Button) findViewById(R.id.buttonNewAccount);
        btnNewAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent versNewAccount = new Intent(MainActivity.this, NewAccountActivity.class);
                startActivity(versNewAccount);
            }
        });
    }

}
