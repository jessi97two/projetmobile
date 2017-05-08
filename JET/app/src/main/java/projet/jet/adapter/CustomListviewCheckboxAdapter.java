package projet.jet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import projet.jet.R;
import projet.jet.classe.PropositionsSondages;

/**
 * Created by Jess on 01/05/2017.
 */
public class CustomListviewCheckboxAdapter extends ArrayAdapter<PropositionsSondages> {

    ArrayList<PropositionsSondages> propositionsSondagesList = null;
    Context context;
    ArrayList<String> listPropositionsChecked = new ArrayList<String>();

    public CustomListviewCheckboxAdapter(Context context, ArrayList<PropositionsSondages> resource) {
        super(context, R.layout.list_restaurants_sondages,resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.propositionsSondagesList = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_restaurants_sondages, parent, false);

        final TextView name = (TextView) convertView.findViewById(R.id.txtrestaurantSondage);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBoxRestaurantsSondage);

        name.setText(propositionsSondagesList.get(position).getName());
        final String id = propositionsSondagesList.get(position).getId();

        if(propositionsSondagesList.get(position).getValue() == 1)
            cb.setChecked(true);
        else
            cb.setChecked(false);

        cb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox cbv = (CheckBox) v ;
                if(cbv.isChecked()) {
                    listPropositionsChecked.add(id);
                }
            }
        });

        return convertView;
    }

    public ArrayList<String> getListPropositionsChecked() {
        return listPropositionsChecked;
    }
}
