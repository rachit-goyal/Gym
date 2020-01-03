package in.aaaos.gym;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by RACHIT GOYAL on 4/23/2018.
 */

public class HomeFragment extends Fragment {
    private String JSON_STRING;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<res> contactArrayList;
    private DataAdapter adapter;
    private ProgressDialog dialog;
    String id,desc,name;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment,container,false);
        dialog = new ProgressDialog(getActivity());
        contactArrayList = new ArrayList<res>();
        recyclerView = (RecyclerView)rootView.findViewById(R.id.cat);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if (isNetworkAvailable()) {
            loadData();

        } else {
            Toast.makeText(getActivity(), "No Network", Toast.LENGTH_SHORT).show();
        }
        return rootView;

    }

    private void loadData() {
        getdata();
    }

    private void getdata() {
        this.dialog.setMessage("Please Wait");
        this.dialog.show();
        JSON_STRING = "http://online.vetsunpharma.com/wp-json/wp/v2/categories?per_page=100&exclude=17,18,20,21,22,23,24,19,24,25,29,30,26,27,31,28";
        StringRequest stringreq=new StringRequest(Request.Method.GET, JSON_STRING, new Response.Listener<String>() {
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
                        id=jobj.getString("id");
                        desc=jobj.getString("description");
                        name=jobj.getString("name");
                        res.setId(id);
                        res.setTitle(name);
                        res.setImg(desc);
                        contactArrayList.add(res);
                    }
                    recyclerView.setHasFixedSize(true);

                    layoutManager = new LinearLayoutManager(getActivity());

                    // use a linear layout manager
                    recyclerView.setLayoutManager(layoutManager);

                    // create an Object for Adapter
                    adapter = new DataAdapter(getActivity(),contactArrayList);

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
                    if(isAdded()) {
                        Toast.makeText(getActivity(), "No Network",Toast.LENGTH_SHORT).show();
                        if ( dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }
                if(error instanceof TimeoutError){
                    if(isAdded()) {
                        Toast.makeText(getActivity(), "You are on slow network", Toast.LENGTH_SHORT).show();
                        if ( dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Some error occurred.Please try after some time ", Toast.LENGTH_SHORT).show();
                }

            }

        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(stringreq);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
