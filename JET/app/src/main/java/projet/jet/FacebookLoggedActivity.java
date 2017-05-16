package projet.jet;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jess on 19/12/2016.
 */
public class FacebookLoggedActivity extends Activity {

    private Button btnFindFriends;
    private Button btnLogout;

    /*
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.facebook_account_user_activity,container,false);

        Activity a = getActivity();
        Bundle inBundle = a.getIntent().getExtras();
        String name =  this.getArguments().getString("name");
        String surname =  this.getArguments().getString("surname");
        String imageUrl =  this.getArguments().getString("imageUrl");



        TextView nameView = (TextView)v.findViewById(R.id.nameAndSurname);
        nameView.setText("" + name + " " + surname);

        new DownloadImageFacebookProfile((ImageView)v.findViewById(R.id.profileImage)).execute(imageUrl);


        btnFindFriends = (Button) v.findViewById(R.id.btnFindFriends);
        btnFindFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FriendsFacebookFragment friendsFacebookFragment = new FriendsFacebookFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framebis,friendsFacebookFragment);
                fragmentTransaction.commit();
            }
        });

        return v;
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.facebook_account_user_activity);
        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.get("name").toString();
        String surname = inBundle.get("surname").toString();
        String imageUrl = inBundle.get("imageUrl").toString();
        final String jsondataT = getIntent().getStringExtra("jsondata");

        TextView nameView = (TextView)findViewById(R.id.nameAndSurname);
        nameView.setText("" + name + " " + surname);

        new DownloadImageFacebookProfile((ImageView)findViewById(R.id.profileImage)).execute(imageUrl);

        btnFindFriends = (Button) findViewById(R.id.btnFindFriends);
        btnFindFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent versFriendsFbActivity = new Intent(FacebookLoggedActivity.this, FriendsFacebookActivity.class);
                versFriendsFbActivity.putExtra("jsondata", jsondataT);
                startActivity(versFriendsFbActivity);
            }
        });
    }

}
