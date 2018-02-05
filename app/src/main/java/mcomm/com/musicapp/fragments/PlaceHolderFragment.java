package mcomm.com.musicapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mcomm.com.musicapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaceHolderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaceHolderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceHolderFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    public PlaceHolderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment PlaceHolderFragment.
     */
    public static PlaceHolderFragment newInstance(int position) {
        PlaceHolderFragment fragment = new PlaceHolderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_place_holder, container, false);


        return v;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private ImageView indicator0, indicator1, indicator2;

        ImageView imageView;

        int[] backgrounds = new int[]{R.drawable.image0, R.drawable.image1,
                R.drawable.image2};
        int[] headers = new int[]{R.string.title0, R.string.title1,
                R.string.title2};
        int[] contents = new int[]{R.string.subtitle0, R.string.subtitle1,
                R.string.subtitle2};

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_place_holder, container, false);
            TextView textView = rootView.findViewById(R.id.title);
            TextView content = rootView.findViewById(R.id.subtitle);
            textView.setText(getString(headers[getArguments().getInt(ARG_SECTION_NUMBER)-1]));
            content.setText(getString(contents[getArguments().getInt(ARG_SECTION_NUMBER)-1]));

            imageView = rootView.findViewById(R.id.image);
            imageView.setBackgroundResource(backgrounds[getArguments().getInt(ARG_SECTION_NUMBER)-1]);

            return rootView;
        }
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

    }
}
