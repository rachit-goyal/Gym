package in.aaaos.gym;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by RACHIT GOYAL on 4/24/2018.
 */

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    Context context;

    ArrayList<res> singleUser;

    public DataAdapter(Context context, ArrayList<res> singleUser) {

        this.context = context;
        this.singleUser = singleUser;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView img;


        public ViewHolder(View v) {

            super(v);
            img = (ImageView) v.findViewById(R.id.myImageView);
            name = (TextView) v.findViewById(R.id.myImageViewText);

        }
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view1 = LayoutInflater.from(context).inflate(R.layout.single_layout, parent, false);

        ViewHolder vh = new ViewHolder(view1);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(this.context).load(singleUser.get(position).getImg()).into(holder.img);
        holder.name.setText(singleUser.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable()){
                    Intent intent = new Intent(context, SubActivity.class);
                    intent.putExtra("id",singleUser.get(position).getId());
                    intent.putExtra("name",singleUser.get(position).getTitle());
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context,"Please switch on internet",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {

        return singleUser.size();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}