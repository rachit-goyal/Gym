package in.aaaos.gym;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by RACHIT GOYAL on 4/24/2018.
 */

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ViewHolder> {

    Context context;

    ArrayList<res> singleUser;

    public SubAdapter(Context context, ArrayList<res> singleUser) {

        this.context = context;
        this.singleUser = singleUser;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView img;


        public ViewHolder(View v) {

            super(v);
            img = (ImageView) v.findViewById(R.id.imageviewsub);
            name = (TextView) v.findViewById(R.id.textviewsub);

        }
    }

    @Override
    public SubAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view1 = LayoutInflater.from(context).inflate(R.layout.sub_singel, parent, false);

        SubAdapter.ViewHolder vh = new SubAdapter.ViewHolder(view1);
        return vh;
    }

    @Override
    public void onBindViewHolder(SubAdapter.ViewHolder holder, final int position) {
        Picasso.with(this.context).load(singleUser.get(position).getImg()).into(holder.img);
        holder.name.setText(singleUser.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable()){
                    Intent intent = new Intent(context, Post.class);
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
