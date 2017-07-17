package recruitment.iiitd.edu.microphone_app;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import recruitment.iiitd.edu.mew.R;


public class FloorOverlayFragment extends Fragment {
    private static final String TAG = "ShowCustomRL";


    public static FloorOverlayFragment newInstance() {
        Log.i(TAG, "Creating fragment");
        FloorOverlayFragment fragment
                = new FloorOverlayFragment();
        return  fragment;
    }

    public FloorOverlayFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_floor_overlay, container, false);
        return view;
    }


    @Override

    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        ((FloorActivity)getActivity()).colorMap();

        // Getting view for WingA overlay
        String wingA_color=((FloorActivity)getActivity()).WingAColor;
        final ImageView image= (ImageView)getView().findViewById(R.id.outside_imageview);
        colorView(image,wingA_color);

        // Getting view for WingB overlay
        String wingB_color=((FloorActivity)getActivity()).WingBColor;
        final ImageView imageB= (ImageView)getView().findViewById(R.id.one_imageview);
        colorView(imageB,wingB_color);

        // Getting view for Lobby overlay
        String Lobby_color=((FloorActivity)getActivity()).LobbyColor;
        final ImageView image_lob= (ImageView)getView().findViewById(R.id.two_imageview);
        colorView(image_lob,Lobby_color);


    }

    //Coloring view according to respective colors assigned
    private void colorView(ImageView image, String color) {
        Resources res=this.getResources();
        if(color.equals("green"))
        {
            final int newColor=res.getColor(R.color.green_color);
            image.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        }
        else if(color.equals("yellow"))
        {
            final int newColor=res.getColor(R.color.orange_color);
            image.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        }
        else if(color.equals("red"))
        {
            final int newColor=res.getColor(R.color.red_color);
            image.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        }
        else if(color.equals("grey"))
        {
            final int newColor=res.getColor(R.color.grey_color);
            image.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        }
    }
}
