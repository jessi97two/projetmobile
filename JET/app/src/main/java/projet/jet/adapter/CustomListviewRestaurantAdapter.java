package projet.jet.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import projet.jet.R;
import projet.jet.activity.RestaurantsActivity;
import projet.jet.classe.Restaurant;

/**
 * Created by Jess on 16/05/2017.
 */
public class CustomListviewRestaurantAdapter extends BaseAdapter {

    private static final String tag = "RestaurantArrayAdapter";
    private Context mContext;
    private Map mcorrespond;
    private ArrayList<String> restaurants= new ArrayList<String>();

    public CustomListviewRestaurantAdapter(Context context, ArrayList<String> listResto, Map correspond) {
        mContext = context;
        restaurants = listResto;
        mcorrespond = correspond;

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return this.restaurants.size();
    }

    public String getItem(int index) {
        // TODO Auto-generated method stub
        return this.restaurants.get(index);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            // ROW INFLATION
            Log.d(tag, "Starting XML Row Inflation ... ");
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.activity_search_restaurant_listview, parent, false);
            Log.d(tag, "Successfully completed XML Row Inflation!");
        }
        String name = getItem(position);
        Restaurant resto = (Restaurant) mcorrespond.get(name);
        ImageView favImage= (ImageView) row.findViewById(R.id.imagefav);
        if (resto.isFavori(mContext))
            favImage.setImageResource(R.drawable.favoris_logo);
        else
            favImage.setImageResource(R.drawable.notinfav);


        favImage.setClickable(true);
        favImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("CUSTOM ADAPTER DEBUG :" + " : " + RestaurantsActivity.class.getName(), "Entered onClick method");
                Toast.makeText(v.getContext(),
                        "The favorite list would appear on clicking this icon",
                        Toast.LENGTH_LONG).show();
            }
        });
        TextView RestoName = (TextView) row.findViewById(R.id.RestoSearchName);
        RestoName.setText(name);

        return (row);
    }

}
