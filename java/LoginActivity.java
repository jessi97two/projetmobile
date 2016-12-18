package projet.jet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Jess on 18/12/2016.
 */
public class LoginActivity extends AppCompatActivity {

    private Button btnNewAccount;
    private Button buttonSignIn;
    private EditText edtLogin;
    private EditText edtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_view);

        edtLogin = (EditText) findViewById(R.id.editTextLoginAccueil);
        edtPwd = (EditText) findViewById(R.id.editTextPwd);

        btnNewAccount = (Button) findViewById(R.id.buttonNewAccount);
        btnNewAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent versNewAccount = new Intent(LoginActivity.this, NewAccountActivity.class);
                startActivity(versNewAccount);
            }
        });

        buttonSignIn = (Button)  findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }


}
