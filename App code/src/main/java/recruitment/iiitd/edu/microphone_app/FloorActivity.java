package recruitment.iiitd.edu.microphone_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import recruitment.iiitd.edu.mew.R;


public class FloorActivity extends FragmentActivity {


    private  int st_hour;
    private int st_min;
    private int end_hour;
    private int end_min;


    private Map<String, ArrayList<Double>> wing_micdata = new HashMap<String, ArrayList<Double>>();
    private  Map<String,Double> micdata_distribution = new HashMap<String,Double>();

    private String WingA_APS[]={"c4:0a:cb:25:bf:77"}; //access points for WingA of third floor in academic block
    private String WingB_APS[]={"c4:0a:cb:5c:07:0a","c4:0a:cb:5c:07:07"};//access points for WingB of third floor in academic block
    private String Lobby_APS[]={"c4:0a:cb:25:a8:37","c4:0a:cb:2d:75:57"}; //access points for Lobby  of third floor in academic block

    double green_val=5,yellow_val=10; //Range for colors on map
    String WingAColor="",WingBColor="",LobbyColor="";
    private String day_selected,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor);
        setupFragment();
        Intent in = getIntent();
        type=in.getStringExtra("Type"); //checking whether intent is from  time acivity is selected or by day activity
        if(type.equals("Time"))
        {
            TimeBasedAnalysis(in);
        }
        else if(type.equals("Day"))
        {
            DayBasedAnalysis(in);
        }

    }


    //Method to display map based on day selected
    private void DayBasedAnalysis(Intent in) {

        day_selected=in.getStringExtra("Day");


        File parentdirectory = new File(Environment.getExternalStorageDirectory() + "/Mew/ReceivedFile/");
        File[] foldernames = parentdirectory.listFiles();
        Log.d("FOLDERS", String.valueOf(foldernames.length));
        for (int i = 0; i < foldernames.length; i++) {
            String fname = foldernames[i].getName();

            String time=fname.substring(16);
            Log.d("TIME  DAY FOLDER",time);

            long millis=Long.parseLong(time);

            Calendar c= Calendar.getInstance();
            c.setTimeInMillis(millis);

            String day= getDate(c);

            if(!day.equals(day_selected.trim())) continue;

            File childDirectory = new File(Environment.getExternalStorageDirectory() + "/Mew/ReceivedFile/" + fname + "/");

            File[] listOfFiles = childDirectory.listFiles();
            Log.d("List Files ", String.valueOf(listOfFiles) + childDirectory.exists() + childDirectory.isDirectory() + childDirectory.getAbsolutePath());
            for (int j = 0; j < listOfFiles.length; j++) {
                File file = listOfFiles[j];

                Log.d("Before Calling Read CSV", file.getName());
                ReadCSV(fname + "/" + file.getName());

            }

        }
        calculate_sounddistribution();
    }

    // Displaying map based on time range
    private void TimeBasedAnalysis(Intent in) {
        st_hour = in.getIntExtra("Start Hour",0);
        end_hour =in.getIntExtra("End Hour",0);

        st_min = in.getIntExtra("Start Min",0);
        end_min = in.getIntExtra("End Min",0);

        File parentdirectory = new File(Environment.getExternalStorageDirectory() + "/Mew/ReceivedFile/");
        File[] foldernames = parentdirectory.listFiles();
        for (int i = 0; i < foldernames.length; i++) {
            String fname = foldernames[i].getName();

            String time=fname.substring(16);

            long millis=Long.parseLong(time);

            Calendar c= Calendar.getInstance();
            c.setTimeInMillis(millis);
            int t = Integer.parseInt(String.valueOf(c.get(Calendar.HOUR_OF_DAY)));
            Log.e("TIME ANALYSIS",String.valueOf(t));
  //checking if file is in selected time range
            if (t >= (st_hour) && t <= (end_hour)) {
             //take file
                File childDirectory = new File(Environment.getExternalStorageDirectory() + "/Mew/ReceivedFile/" + fname + "/");

                File[] listOfFiles = childDirectory.listFiles();
                Log.d("List Files ", String.valueOf(listOfFiles) + childDirectory.exists() + childDirectory.isDirectory() + childDirectory.getAbsolutePath());
                for (int j = 0; j < listOfFiles.length; j++) {
                    File file = listOfFiles[j];

                    Log.d("Before Calling Read CSV", file.getName());
                    ReadCSV(fname + "/" + file.getName());

                }
            }
        }
        calculate_sounddistribution();

    }


    private void setupFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        Fragment customlayout = FloorOverlayFragment.newInstance();
        ft.replace(R.id.fragment_container, FloorOverlayFragment.newInstance(),
                "FloorOverlayFragment").commit();
    }

    //setting colors in map based on avg values

    public void colorMap() {
        int i=1;
        double sumA=0,sumB=0,sum_lob=0,numA=0,numB=0,num_lob=0;
        for (Map.Entry<String, Double> entry : micdata_distribution.entrySet())
        {
            String access_key=entry.getKey();
            double avg_val=entry.getValue();
            Log.e("AVG VALUES " ,access_key + "%%%%%%%" + avg_val);
            //   Toast.makeText(getApplicationContext(),i+ ": "+access_key+"####" + avg_val,Toast.LENGTH_SHORT).show();
            Log.e("outside condition ",access_key);
            i++;
            if(Arrays.asList(WingA_APS).contains(access_key.trim()))
            {
                //Toast.makeText(getApplicationContext(),"In A",Toast.LENGTH_SHORT).show();
                sumA=sumA+avg_val;
                numA=numA+1;
            }
            else if(Arrays.asList(WingB_APS).contains(access_key.trim()))
            {
                sumB=sumB+avg_val;
                numB=numB+1;


            }
            else if (Arrays.asList(Lobby_APS).contains(access_key.trim()))
            {
                sum_lob=sum_lob+avg_val;
                num_lob=num_lob+1;
                //color Lobby
                //  mimageview.setColorFilter(Color.GREEN);

            }



        }


        sumA=sumA/numA;
        sumB=sumB/numB;
        sum_lob=sum_lob/num_lob;

        if(numA==0 && numB==0 && num_lob==0)
        {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setTitle("NO DATA");
            builder.setCancelable(false);
            builder.setMessage("Sorry Data is not available.Please Select another time slot");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();

                }
            });
            builder.show();

        }

        if(sumA<=green_val)  WingAColor="green";
        else if(sumA>green_val && sumA<=yellow_val) WingAColor="yellow";
        else WingAColor="red";

        if(sumB<=green_val)  WingBColor="green";
        else if(sumB>green_val && sumB<=yellow_val) WingBColor="yellow";
        else WingBColor="red";



        if(sum_lob<=green_val)  LobbyColor="green";
        else if(sum_lob>green_val && sum_lob<=yellow_val) LobbyColor="yellow";
        else LobbyColor="red";

        if(numA==0)
            WingAColor="grey";
        if(numB==0)
            WingBColor="grey";
        if(num_lob==0)
            LobbyColor="grey";


        Log.e("Colors A ",WingAColor + sumA);
        Log.e("Colors B ",WingBColor +sumB);
        Log.e("Colors Lobby", LobbyColor+sum_lob);

    }

    // Finding avg for each access point and saving it in hash map
    private void calculate_sounddistribution() {
        Log.d("In sound distribution", String.valueOf(wing_micdata.size()));
        for (Map.Entry<String, ArrayList<Double>> entry : wing_micdata.entrySet())
        {
            micdata_distribution.put(entry.getKey(),findavg(entry.getValue()));
        }
    }



    private double findavg(ArrayList<Double> value) {
        double sum=0;
        for(int i=0;i<value.size();i++)
        {
            sum=sum+value.get(i);
        }
        return sum/value.size();
    }

    // Reading data from csv
    private void ReadCSV(String fname) {

        StringBuilder csvdata = new StringBuilder();
        File yourFile = new File(Environment.getExternalStorageDirectory(), "/Mew/ReceivedFile/"+fname);
        CSVReader reader;

        try {

            reader = new CSVReader(new FileReader(yourFile));
            String[]  nextLine;

            while ((nextLine = reader.readNext()) != null) {
                String access_point = nextLine[7];
                if(Double.parseDouble(nextLine[1])<-Double.MAX_VALUE) { continue;}
                Log.d("Access_point ",access_point);
                if(wing_micdata.get(access_point)==null) //No data for that wing.
                {
                    ArrayList<Double> values=new ArrayList<Double>();

                    values.add(Double.parseDouble(nextLine[1]));
                    wing_micdata.put(access_point,values);

                }
                else
                {
                    ArrayList<Double> values =  wing_micdata.get(access_point);
                    values.add(Double.parseDouble(nextLine[1]));
                    Log.d("Adding to Wing",nextLine[1]);
                    wing_micdata.put(access_point,values);

                }
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();

        } catch (IOException e1) {e1.printStackTrace();
        }


    }


//Getting day of the week from the date given
    public static String getDate(Calendar date) {

        Date curr_date= date.getTime();
        Log.d("curr date", String.valueOf(curr_date));
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayOfTheWeek = sdf.format(curr_date);
        return dayOfTheWeek;
    }
}