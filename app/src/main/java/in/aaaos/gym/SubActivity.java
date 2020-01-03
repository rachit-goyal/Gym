package in.aaaos.gym;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    Toolbar toolbar;
    TextView toolbartext;
    private SubAdapter adapter;
    String id,name,subid,subname,subdesc;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<res> contactarraylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        dialog = new ProgressDialog(SubActivity.this);
        id = getIntent().getExtras().getString("id");
        name=getIntent().getExtras().getString("name");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView)findViewById(R.id.subrecycler);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }
        layoutManager = new LinearLayoutManager(SubActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        toolbartext = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        contactarraylist=new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbartext.setText(name);
        getdata();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getdata() {
        loaddata();
    }

    private void loaddata() {
        if(isNetworkAvailable()){
            dialog.setMessage("Please Wait...");
            dialog.setCancelable(false);
            dialog.show();
            String url = "http://online.vetsunpharma.com/wp-json/wp/v2/categories?parent="+id+"&per_page=20";
            /*http://online.vetsunpharma.com/wp-json/wp/v2/categories?per_page=20&exclude=19,24*/
            StringRequest str=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    try {
                        JSONArray jsonarray=new JSONArray(response);
                        for(int i=0;i<jsonarray.length();i++){
                            res res=new res();
                            JSONObject jobj=jsonarray.getJSONObject(i);
                            subid=jobj.getString("id");
                            subdesc=jobj.getString("description");
                            subname=jobj.getString("name");
                            res.setId(subid);
                            res.setTitle(subname);
                            res.setImg(subdesc);
                            contactarraylist.add(res);
                        }
                        recyclerView.setHasFixedSize(true);

                        layoutManager = new LinearLayoutManager(SubActivity.this);

                        // use a linear layout manager
                        recyclerView.setLayoutManager(layoutManager);

                        // create an Object for Adapter
                        adapter = new SubAdapter(SubActivity.this,contactarraylist);

                        // set the adapter object to the Recyclerview
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                    if(error instanceof NoConnectionError){

                            Toast.makeText(SubActivity.this, "No Network",Toast.LENGTH_SHORT).show();


                    }
                    if(error instanceof TimeoutError){
                            Toast.makeText(SubActivity.this, "You are on slow network", Toast.LENGTH_SHORT).show();


                    }
                    else{
                        Toast.makeText(SubActivity.this, "Some error occurred.Please try after some time ", Toast.LENGTH_SHORT).show();
                    }
                }
            });

AppController.getInstance().addToRequestQueue(str);
        }else{
            Toast.makeText(SubActivity.this, "No Network",Toast.LENGTH_SHORT).show();

        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}
