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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import recruitment.iiitd.edu.mew.R;


public class OneFragment extends Fragment implements View.OnClickListener{

    private int start_hour,start_minute,end_hour,end_minute,flag=0,flag1=0;
    private Button mshowmap;
    private EditText mstartEditText,mendEditText;
    private TimePicker mstartTimePicker, mEndTimePicker;
    private Spinner mfloorspin;
    private TextView mStartTimeView,mEndTimeView;

    private List<String> floor_list=new ArrayList<String>();

    public OneFragment() {
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
        View view= inflater.inflate(R.layout.fragment_one, container, false);

        mshowmap = (Button) view.findViewById(R.id.showMapButton);
        mfloorspin=(Spinner)view.findViewById(R.id.floorSpinner);
        addItemsOnFloorSpinner();
        mStartTimeView=(TextView)view.findViewById(R.id.startTimeTextView);
        mEndTimeView=(TextView)view.findViewById(R.id.endTimeTextView);
      //  AnalogClock simpleAnalogClock = (AnalogClock) view.findViewById(R.id.simpleAnalogClock);
        mshowmap.setOnClickListener(this);

        mstartEditText=(EditText)view.findViewById(R.id.startTimeEditText);
        mstartEditText.setOnClickListener(this);

        mendEditText=(EditText)view.findViewById(R.id.endTimeEditText);
        mendEditText.setOnClickListener(this);


        return view;
    }


    // Floor spinner options
    private void addItemsOnFloorSpinner() {
        floor_list.add("First");
        floor_list.add("Second");
        floor_list.add("Third");


        ArrayAdapter<String> dataadapter = new ArrayAdapter<String>(getActivity(),
               R.layout.spinner_item, floor_list);
        dataadapter
                .setDropDownViewResource(R.layout.spinner_dropdown_list);
        mfloorspin.setAdapter(dataadapter);
        int spinnerPosition = dataadapter.getPosition("Third");

        mfloorspin.setSelection(spinnerPosition);

    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.startTimeEditText:
                                    flag=0;
                                    CollectStartTime();
                                    break;

            case R.id.endTimeEditText:
                                   flag=0;
                                   CollectEndTime();
                                  break;
            case R.id.showMapButton:
                                  showSoundMap();
                                  break;
        }
    }

    // Method to move to FloorActivity
    private void showSoundMap() {

        flag=0;
        flag1=0;


//checking for validity of entered start time and end time.
        if( (end_hour<start_hour) || (start_hour==end_hour)&& end_minute<=start_minute)
        {
            AlertDialog.Builder builder;
            flag=1;
            builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater


            builder.setTitle("Invalid Time");
            builder.setCancelable(false);
            builder.setMessage("Please select correct time range");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete

                    Log.e("FLAG", String.valueOf(flag));

                }
            });
            builder.show();


        }


        if(!mfloorspin.getSelectedItem().toString().equals("Third"))
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
        //If both above constraints are satisfied then moving to floor activity by time

        if(flag==0 && flag1==0) {
            Intent I = new Intent(getActivity(), FloorActivity.class);
            Log.e("TAG", String.valueOf(start_hour));

            I.putExtra("Type","Time");
            I.putExtra("Start Hour", start_hour);
            I.putExtra("End Hour", end_hour);
            I.putExtra("Start Minute",start_minute);
            I.putExtra("End Minute",end_minute);




            startActivity(I);
            mstartEditText.setText("");
            mendEditText.setText("");

        }

    }


    //Method to get the end hour and end minute from the Time Picker
    private void CollectEndTime()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle("Time");
        builder.setCancelable(false);
        final View dialogview=inflater.inflate(R.layout.dialog, null);
        builder.setView(dialogview);
        // Add action buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                mEndTimePicker= (TimePicker)dialogview.findViewById(R.id.dialogtimePicker);

                mEndTimePicker.setIs24HourView(false);

                end_hour=mEndTimePicker.getCurrentHour();
                end_minute=mEndTimePicker.getCurrentMinute();
                mendEditText.setText(end_hour + " : " + end_minute );

            }

        });

        builder.show();

    }

    //Method to get the start hour and start minute from the Time Picker
    private void CollectStartTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = (getActivity()).getLayoutInflater();

        builder.setTitle("Time");
        builder.setCancelable(false);
        final View dialogview=inflater.inflate(R.layout.dialog, null);
        builder.setView(dialogview);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                mstartTimePicker= (TimePicker)dialogview.findViewById(R.id.dialogtimePicker);

                mstartTimePicker.setIs24HourView(false);

                start_hour=mstartTimePicker.getCurrentHour();
                start_minute=mstartTimePicker.getCurrentMinute();
                mstartEditText.setText(start_hour + " : " + start_minute );
            }

        });

        builder.show();


    }
}
