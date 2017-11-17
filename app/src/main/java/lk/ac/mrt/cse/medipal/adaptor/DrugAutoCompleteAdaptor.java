package lk.ac.mrt.cse.medipal.adaptor;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.model.Drug;
import lk.ac.mrt.cse.medipal.model.DrugCategory;

/**
 * Created by lakshan on 11/15/17.
 */

public class DrugAutoCompleteAdaptor extends ArrayAdapter<Drug> {
    private final Context context;
    private final List<Drug> drugs;
    private final List<DrugCategory> drugCategories;
    private final List<Drug> all_drugs;
    private final List<Drug> drug_suggestions;
    private final int layout_resource_id;

    public DrugAutoCompleteAdaptor(@NonNull Context context, @LayoutRes int resource, List<Drug> drugList, ArrayList<DrugCategory> categories) {
        super(context, resource, drugList);
        this.context = context;
        this.layout_resource_id = resource;
        this.drugs = new ArrayList<>();
        this.all_drugs = new ArrayList<>(drugList);
        this.drug_suggestions = new ArrayList<>();
        this.drugCategories = categories;
    }

    @Override
    public int getCount() {
        return drugs.size();
    }

    @Nullable
    @Override
    public Drug getItem(int position) {
        return drugs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(layout_resource_id, parent, false);
            }
            Drug drug = getItem(position);
            TextView drug_txt = (TextView) convertView.findViewById(R.id.drug_txt);
            TextView image_txt = (TextView) convertView.findViewById(R.id.image_txt);
            TextView drug_category_txt = (TextView) convertView.findViewById(R.id.drug_category_txt);
            image_txt.setText(drug.getDrug_name().substring(0, 1).toUpperCase());
            drug_txt.setText(drug.getDrug_name());
            if (!drugCategories.isEmpty()) {
                drug_category_txt.setText(drugCategories.get(Integer.parseInt(drug.getCategory_id())).getCategory_name());
            } else {
                drug_category_txt.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if (charSequence != null) {
                    drug_suggestions.clear();
                    for (Drug drug : all_drugs) {
                        if (drug.getDrug_name().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                            drug_suggestions.add(drug);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = drug_suggestions;
                    filterResults.count = drug_suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                drugs.clear();
                if (filterResults != null && filterResults.count > 0) {
                    List<?> result = (List<?>) filterResults.values;
                    for (Object object : result) {
                        if (object instanceof Drug) {
                            drugs.add((Drug) object);
                        }
                    }
                } else if (charSequence == null) {
                    drugs.addAll(all_drugs);
                }
                notifyDataSetChanged();
            }
        };
    }

    public List<Drug> getDrug_suggestions() {
        return drug_suggestions;
    }
}
