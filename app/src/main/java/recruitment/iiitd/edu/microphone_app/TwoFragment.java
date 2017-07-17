package recruitment.iiitd.edu.microphone_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;

import recruitment.iiitd.edu.mew.R;


public class TwoFragment extends Fragment implements View.OnClickListener{
    private int flag=0,flag1=0;
    private Button mshowmap;
    private Spinner mdatespin,mday_floorspin;
    private List<String> list=new ArrayList<String>();
    private List<String > day_list=new ArrayList<String>();

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_two, container, false);


        mshowmap = (Button) view.findViewById(R.id.showMapButton);

        mdatespin=(Spinner)view.findViewById(R.id.datespinner);
        mday_floorspin=(Spinner)view.findViewById(R.id.day_FloorSpinner);
        addItemsOnSpinnerDay();
        addItemsOnFloorSpinner();
       // AnalogClock simpleAnalogClock = (AnalogClock) view.findViewById(R.id.simpleAnalogClock);
        mshowmap.setOnClickListener(this);


        return view;
    }

    //Floor options on spinner
    private void addItemsOnFloorSpinner() {
        day_list.add("First");
        day_list.add("Second");
        day_list.add("Third");


        ArrayAdapter<String> dataadapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, day_list);
        dataadapter
                .setDropDownViewResource(R.layout.spinner_dropdown_list);
        mday_floorspin.setAdapter(dataadapter);
         int spinnerPosition = dataadapter.getPosition("Third");

        mday_floorspin.setSelection(spinnerPosition);

    }
//Day options on spinner
    private void addItemsOnSpinnerDay() {

        list.add("Monday");
        list.add("Tuesday");
        list.add("Wednesday");
        list.add("Thursday");
        list.add("Friday");
        list.add("Saturday");
        list.add("Sunday");



        ArrayAdapter<String> dataadapter = new ArrayAdapter<String>(getActivity(),
               R.layout.spinner_item, list);
        dataadapter
                .setDropDownViewResource(R.layout.spinner_dropdown_list);
        mdatespin.setAdapter(dataadapter);

    }
    @Override
    public void onClick(View v) {
        showSoundMap();
    }


    // Method to move to FloorActivity
    private void showSoundMap() {

        flag=0;
        flag1=0;
       //checking if nothing is selected in spinner
        if(mdatespin.getSelectedItem().toString()==null)
        {
            AlertDialog.Builder builder;
            flag=1;
            builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            builder.setTitle("Invalid,did not select day");
            builder.setCancelable(false);
            builder.setMessage("Please select a day ");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete

                    Log.e("FLAG", String.valueOf(flag));

                }
            });
            builder.show();


        }

//checking if third floor option is selected in spinner
        if(!mday_floorspin.getSelectedItem().toString().equals("Third"))
        {
            AlertDialog.Builder builder;
            flag1=1;
            builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater


            builder.setTitle("Data Unavailable");
            builder.setCancelable(false);
            builder.setMessage("Sorry data not available for selected floor.Please select another floor. ");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete

                    Log.e("FLAG", String.valueOf(flag));

                }
            });
            builder.show();


        }
       //If both above constraints are satisfied then moving to floor activity by day
        if(flag==0 && flag1==0) {
            Intent I = new Intent(getActivity(), FloorActivity.class);

            I.putExtra("Type","Day");

            I.putExtra("Day",mdatespin.getSelectedItem().toString());



            startActivity(I);
        }
    }

}
