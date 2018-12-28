package com.indirmehesapla.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indirmehesapla.Model.Tur;
import com.indirmehesapla.Model.TurAdapterList;
import com.indirmehesapla.R;

import java.util.List;


public class TurListAdapter extends RecyclerView.Adapter<TurListAdapter.MyViewHolder> {

    private Context mContext;
    private List<TurAdapterList> TurListele;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView TurAdi,TurHiz,TurSure;
        public MyViewHolder(View view) {
            super(view);

            TurAdi = view.findViewById(R.id.TurAdi);
            TurHiz = view.findViewById(R.id.TurHiz);
            TurSure = view.findViewById(R.id.TurSure);



        }


        @Override
        public void onClick(View v) {

        }
    }


    public TurListAdapter(Context mContext, List<TurAdapterList> TurListele) {
        this.mContext = mContext;
        this.TurListele = TurListele;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.turlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        TurAdapterList Veri = TurListele.get(position);


        holder.TurAdi.setText(Veri.Adi);
        holder.TurHiz.setText(Veri.kbps);
        holder.TurSure.setText(Veri.units);

    }


    @Override
    public int getItemCount() {
        return TurListele.size();
    }



}
