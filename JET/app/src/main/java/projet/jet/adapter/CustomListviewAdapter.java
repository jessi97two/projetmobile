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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import projet.jet.GlobalApp;
import projet.jet.R;
import projet.jet.activity.ContactsActivity;
import projet.jet.activity.InformationsEventActivity;
import projet.jet.activity.InvitationInformationsActivity;
import projet.jet.activity.ResultatsSondageActivity;
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
    String type;

    public  CustomListviewAdapter (String type, Context context, ArrayList<String> data, Application app, Activity a)
    {
        this.con = context;
        this.data = data;
        this.ga = (GlobalApp) app;
        this.activity = a;
        this.type = type;
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

        if(type.equals("sondage") || type.equals("invitation") || type.equals("sondageevents") || type.equals("eventsevents")) {
            convertView= inflater.inflate(R.layout.home_fragment_listview, parent, false);

            final TextView textTitre = (TextView) convertView.findViewById(R.id.hometitre);
            final Button btnInfo = (Button) convertView.findViewById(R.id.btninfo);

            final String titre = data.get(position).split("_")[0];
            final String id = data.get(position).split("_")[1];
            textTitre.setText(titre);

            if(type.equals("sondage")) {
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
            }
            else if(type.equals("invitation")) {
                btnInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("idinvitation", id);
                        Intent gotoinvit = new Intent(activity, InvitationInformationsActivity.class);
                        gotoinvit.putExtras(bundle);
                        activity.startActivity(gotoinvit);
                    }
                });
            }
            else if(type.equals("sondageevents")) {
                btnInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("idsondage", id);
                        bundle.putString("nomsondage", titre);
                        Intent gotoresultatsondage = new Intent(activity, ResultatsSondageActivity.class);
                        gotoresultatsondage.putExtras(bundle);
                        activity.startActivity(gotoresultatsondage);
                    }
                });
            }
            else if(type.equals("eventsevents")) {
                btnInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("idevent", id);
                        Intent gotoinfosevents = new Intent(activity, InformationsEventActivity.class);
                        gotoinfosevents.putExtras(bundle);
                        activity.startActivity(gotoinfosevents);
                    }
                });
            }
        }
        else if(type.equals("groups")) {
            convertView= inflater.inflate(R.layout.groups_contacts_list, parent, false);

            final TextView txtGroup = (TextView) convertView.findViewById(R.id.groupname);


            final String titre = data.get(position).split("_")[0];
            final String idontel = data.get(position).split("_")[1];
            final String groupnameid = data.get(position);
            txtGroup.setText(titre);


            txtGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putString("groupe", groupnameid);
                    Intent gotocontact = new Intent(activity, ContactsActivity.class);
                    gotocontact.putExtras(bundle);
                    activity.startActivity(gotocontact);
                }
            });
        }
        else if(type.equals("contactsgroups")) {
            convertView= inflater.inflate(R.layout.contacts_listview, parent, false);

            TextView txtContact = (TextView) convertView.findViewById(R.id.nomContact);
            ImageView imgJet = (ImageView) convertView.findViewById(R.id.imageViewJET);

            txtContact.setText(data.get(position).split("_")[0]);
            String presence = data.get(position).split("_")[1];

            if(presence.equals("0")) {
                imgJet.setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

}
