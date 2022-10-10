package com.yo_travel.yo_travelguider.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.yo_travel.yo_travelguider.AdapterData.AdapterData1;
import com.yo_travel.yo_travelguider.DrawerActivity1.Activity1;
import com.yo_travel.yo_travelguider.R;

import java.util.ArrayList;

public class Adapter1 extends RecyclerView.Adapter<Adapter1.ViewHolder> implements View.OnClickListener{

    LayoutInflater inflater;
    ArrayList<AdapterData1> model;
    Context context;

    private View.OnClickListener listener;


    public Adapter1(Context context, ArrayList<AdapterData1> model){
        this.inflater = LayoutInflater.from(context);
        this.model = model;
        this.context = context;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_card1, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public void setOnClickListener (View.OnClickListener listener) {
        this.listener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = model.get(position).getName();
        String p_id = model.get(position).getId();
        String s_date = model.get(position).getS_date();
        String e_date = model.get(position).getE_date();
        String adults = model.get(position).getAdults();
        String child = model.get(position).getChild();
        String total = model.get(position).getTotal();
        String map = model.get(position).getMap();

        holder.name.setText(name);
        holder.sdate.setText(s_date);
        holder.edate.setText(e_date);
        holder.adult.setText(adults);
        holder.child.setText(child);
        holder.total.setText(total);

//        When user select item - new activity will open
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
//                Uri uriUrl = Uri.parse(map);
//                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
//                context.startActivity(launchBrowser);
                Intent intent = new Intent(context, Activity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("P_ID",p_id);
                intent.putExtra("MAP",map);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,sdate,edate,child,adult,total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView12);
            sdate = itemView.findViewById(R.id.textView4);
            edate = itemView.findViewById(R.id.textView13);
            child = itemView.findViewById(R.id.textView21);
            adult = itemView.findViewById(R.id.textView22);
            total = itemView.findViewById(R.id.textView23);
        }
    }
}
