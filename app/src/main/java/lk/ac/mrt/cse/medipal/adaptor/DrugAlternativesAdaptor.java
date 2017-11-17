package lk.ac.mrt.cse.medipal.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rilixtech.materialfancybutton.MaterialFancyButton;
import java.util.ArrayList;
import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.model.Drug;

/**
 * Created by lakshan on 11/18/17.
 */

public class DrugAlternativesAdaptor extends RecyclerView.Adapter<DrugAlternativesAdaptor.DrugAlternativesViewHolder>   {
    private Context context;
    private ArrayList<Drug> drugList;
    private PrescriptionDrugSelectionRecyclerAdaptor.PrescriptiontionDrugRecyclerViewHolder viewHolder;

    public DrugAlternativesAdaptor(Context context, ArrayList<Drug> drugList, PrescriptionDrugSelectionRecyclerAdaptor.PrescriptiontionDrugRecyclerViewHolder viewHolder)  {
        this.context = context;
        this.drugList = drugList;
        this.viewHolder = viewHolder;
    }

    @Override
    public DrugAlternativesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alt_drug_recycler,parent,false);
        return new DrugAlternativesAdaptor.DrugAlternativesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrugAlternativesViewHolder holder, int position) {
        holder.txt_alt_drug.setText(drugList.get(position).getDrug_name());
    }

    @Override
    public int getItemCount() {
        return drugList.size();
    }

    public class DrugAlternativesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private MaterialFancyButton txt_alt_drug;
        public DrugAlternativesViewHolder(View itemView) {
            super(itemView);
            txt_alt_drug = itemView.findViewById(R.id.txt_alt_drug);

            txt_alt_drug.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            viewHolder.exchangeDrug(drugList.get(getAdapterPosition()));
        }
    }
}
