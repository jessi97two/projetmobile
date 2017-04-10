package projet.jet.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import projet.jet.GlobalApp;
import projet.jet.R;
import projet.jet.activity.SondageActivity;
import projet.jet.activity.SondageInformationsActivity;

/**
 * Created by Jess on 05/04/2017.
 */
public class CustomListviewAdapter extends BaseAdapter {

    Context con;
    ArrayList<String> data;
    GlobalApp ga;
    Activity activity;
    Boolean found = false;
    String listjson;


    public  CustomListviewAdapter (Context context, ArrayList<String> data, Application app, Activity a)
    {
        this.con = context;
        this.data = data;
        this.ga = (GlobalApp) app;
        this.activity = a;
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
        convertView= inflater.inflate(R.layout.home_fragment_listview, parent, false);

        final TextView textTitre = (TextView) convertView.findViewById(R.id.hometitre);
        final Button btnInfo = (Button) convertView.findViewById(R.id.btninfo);

        String titre = data.get(position).split("_")[0];
        final String id = data.get(position).split("_")[1];
        textTitre.setText(titre);

        btnInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("idsondage", id);
                Intent gotosondage = new Intent(activity, SondageInformationsActivity.class);
                gotosondage.putExtras(bundle);
                activity.startActivity(gotosondage);
            }
        });

        return convertView;
    }

}
