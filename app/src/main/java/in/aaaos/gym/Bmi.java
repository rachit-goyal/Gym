package in.aaaos.gym;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Bmi extends AppCompatActivity {
EditText height,weight;
    TextView check,yourbmi;
    ImageView img;
    Toolbar toolbar;
    TextView toolbartext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        height=(EditText)findViewById(R.id.height);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbartext = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbartext.setText("BMI Calculator");

        weight=(EditText)findViewById(R.id.weight);
        check=(TextView)findViewById(R.id.bmi_check);
        yourbmi=(TextView)findViewById(R.id.bmi);
        img=(ImageView)findViewById(R.id.bbn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(height.getText().toString().trim().isEmpty()){
                    height.setError("Field cannot be blank");
                }
                else if(weight.getText().toString().trim().isEmpty()){
                    weight.setError("Field cannot be blank");
                }
                else{
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    double val= Math.pow(Integer.parseInt(height.getText().toString().trim()),2);
                    double val1=val/10000;
                    double bm=Integer.parseInt(weight.getText().toString().trim())/val1;
                    String bmq=String.format("%.2f", bm);
                    if(bm <=18.5){
                        yourbmi.setBackgroundColor(Color.parseColor("#E1930E"));
                    }
                    else if(bm>18.5 && bm<22.9){
                        yourbmi.setBackgroundColor(Color.parseColor("#EBCC00"));
                    }
                    else if(bm>23 && bm<24.9){
                        yourbmi.setBackgroundColor(Color.parseColor("#D6551E"));
                    }
                    else if(bm>25 && bm<29.9){
                        yourbmi.setBackgroundColor(Color.parseColor("#ED3751"));
                    }
                    else if(bm>30){
                        yourbmi.setBackgroundColor(Color.parseColor("#F41414"));
                    }
                    yourbmi.setText("Your bmi is : "+bmq);
                    if(isNetworkAvailable()) {
                        Picasso.with(Bmi.this).load("http://online.vetsunpharma.com/wp-content/uploads/2018/04/bmi_image.png").into(img);
                        img.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(Bmi.this,"Unable to fetch image.You don't have internet connection",Toast.LENGTH_SHORT).show();
                    }
                    yourbmi.setVisibility(View.VISIBLE);


                }
            }
        });

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
