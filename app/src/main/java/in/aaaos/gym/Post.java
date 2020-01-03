package in.aaaos.gym;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Post extends AppCompatActivity {
    private ProgressDialog dialog;
    Toolbar toolbar;
    TextView toolbartext,content;
    ImageView img;
    String id,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        dialog = new ProgressDialog(Post.this);
        id = getIntent().getExtras().getString("id");
        name=getIntent().getExtras().getString("name");
        content=(TextView)findViewById(R.id.content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        img=(ImageView)findViewById(R.id.wer);
        toolbartext = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
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
        if(isNetworkAvailable()){
            String url="http://online.vetsunpharma.com/wp-json/wp/v2/posts?categories="+id;
            StringRequest ste=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                    try {
                        JSONArray json=new JSONArray(response);
                        JSONObject jobj=json.getJSONObject(0);
                        JSONObject jo=jobj.getJSONObject("content");
                        String contentt=jo.getString("rendered");
                        JSONObject jj=jobj.getJSONObject("better_featured_image");
                        String source=jj.getString("source_url");
                        String sou=source.replace("\\","");
                        content.setText(Html.fromHtml(contentt));
                        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(img);
                        Glide.with(Post.this).load(sou).into(imageViewTarget);
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

                        Toast.makeText(Post.this, "No Network",Toast.LENGTH_SHORT).show();


                    }
                    if(error instanceof TimeoutError){
                        Toast.makeText(Post.this, "You are on slow network", Toast.LENGTH_SHORT).show();


                    }
                    else{
                        Toast.makeText(Post.this, "Some error occurred.Please try after some time ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            AppController.getInstance().addToRequestQueue(ste);
        }
        else{
            Toast.makeText(Post.this, "No Network",Toast.LENGTH_SHORT).show();
        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}
