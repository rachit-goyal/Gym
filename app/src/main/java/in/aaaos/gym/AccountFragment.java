package in.aaaos.gym;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by RACHIT GOYAL on 4/23/2018.
 */

public class AccountFragment extends Fragment {
    LinearLayout bmi;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.account_fragment,container,false);

        bmi=(LinearLayout)rootView.findViewById(R.id.bmi_click);
        bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),Bmi.class);
                startActivity(i);
            }
        });
        return rootView;
    }

}
