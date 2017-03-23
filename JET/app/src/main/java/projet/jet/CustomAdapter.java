package projet.jet;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jess on 22/12/2016.
 */
public class CustomAdapter extends BaseAdapter {

    Context con;
    ArrayList<String> data;
    GlobalApp ga;
    Boolean found = false;
    String listjson;

    public  CustomAdapter (Context context, ArrayList<String> data, Application app, String list)
    {
        this.con = context;
        this.data = data;
        this.ga = (GlobalApp) app;
        this.listjson = list;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView= inflater.inflate(R.layout.activity_listview, parent, false);
        final TextView text = (TextView) convertView.findViewById(R.id.labelview); //recognize your view like this
        Button button = (Button) convertView.findViewById(R.id.btnlistview);
        text.setText(data.get(position));

        final ConnectivityManager cnMngr = (ConnectivityManager) con.getSystemService(con.CONNECTIVITY_SERVICE);
        final UserFunctions uf = new UserFunctions();

        List<String> listFollowers = null;

        Log.i("DEBUG TEST REP", text.getText().toString());
        if(listjson.contains(text.getText().toString())) {
            found = true;
        }

        if(found == true) {
            button.setBackgroundResource(R.drawable.user_checked_icons);
            found = false;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uf.AddNewLien(text.getText().toString(),cnMngr, ga.prefs.getString("id",""),ga);
            }
        });
        return convertView;
    }

}
