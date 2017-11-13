package lk.ac.mrt.cse.medipal.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.model.Disease;

/**
 * Created by lakshan on 11/13/17.
 */

public class DiseaseSpinnerAdaptor extends BaseAdapter implements Filterable{
    Context context;
    ArrayList<Disease> diseaseList;
    ArrayList<Disease> fullDiseaseList;
    LayoutInflater inflter;

    public DiseaseSpinnerAdaptor(Context applicationContext, ArrayList<Disease> diseaseList) {
        this.context = applicationContext;
        this.diseaseList = diseaseList;
        this.fullDiseaseList = diseaseList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return diseaseList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.simple_spinner_dropdown_item, null);
        TextView image_txt = view.findViewById(R.id.image_txt);
        TextView disease_txt =  view.findViewById(R.id.disease_txt);
        image_txt.setText(diseaseList.get(i).getDisease_name().substring(0,1).toUpperCase());
        disease_txt.setText(diseaseList.get(i).getDisease_name());
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                if (charSequence == null || charSequence.length() == 0) {
                    results.values = fullDiseaseList;
                    results.count = fullDiseaseList.size();
                }
                else {
                    ArrayList<Disease> newDiseaseList = new ArrayList<Disease>();

                    for (Disease disease : fullDiseaseList) {
                        if (disease.getDisease_name().toUpperCase()
                                .startsWith(charSequence.toString().toUpperCase()))
                            newDiseaseList.add(disease);
                    }

                    results.values = newDiseaseList;
                    results.count = newDiseaseList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.count == 0)
                    notifyDataSetInvalidated();
                else {
                    diseaseList = (ArrayList<Disease>) filterResults.values;
                    notifyDataSetChanged();
                }
            }
        };
    }
}
