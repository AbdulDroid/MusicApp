package mcomm.com.musicapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mcomm.com.musicapp.HoritzAdapter;
import mcomm.com.musicapp.R;
import mcomm.com.musicapp.Song;
import mcomm.com.musicapp.VertAdapter;
import mcomm.com.musicapp.fragments.PlaceHolderFragment;
import mcomm.com.musicapp.utils.AppUtils;

public class CategoryActivity extends AppCompatActivity implements HoritzAdapter.Listener,
        VertAdapter.Listener{

    @BindView(R.id.top_list)
    RecyclerView topList;
    @BindView(R.id.bottom_list)
    RecyclerView bottomList;
    @BindView(R.id.vert_list)
    RecyclerView vertList;
    @BindView(R.id.in1)
    ImageView indicator0;
    @BindView(R.id.in2)
    ImageView indicator1;
    @BindView(R.id.in3)
    ImageView indicator2;
    @BindView(R.id.image_holder)
    ViewPager container;
    private ArrayList<Song> songs, vertSongs;
    public static final int WRITE = 10;
    int page = 0;
    Unbinder unbind;
    private AppUtils appUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        unbind = ButterKnife.bind(this);
        songs = new ArrayList<>();
        vertSongs = new ArrayList<>();
        appUtils = new AppUtils(this);
        container.setCurrentItem(page);
        FragmentManager fm = getSupportFragmentManager();
        container.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return PlaceHolderFragment.PlaceholderFragment.newInstance(position + 1);
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                updateIndicators(page);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        checkPermission();

        topList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<Song> topSongs = appUtils.sortSongs(songs, "artist");
        topList.setAdapter(new HoritzAdapter(topSongs, this));
        topList.setHasFixedSize(true);
        bottomList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<Song> albumSongs = appUtils.sortSongs(songs, "album");
        bottomList.setAdapter(new HoritzAdapter(albumSongs, this));
        bottomList.setHasFixedSize(true);
        vertList.setLayoutManager(new LinearLayoutManager(this));
        vertList.setHasFixedSize(true);
        addVert();
        vertList.setAdapter(new VertAdapter(appUtils.sortSongs(vertSongs, "shuffle_unchecked"), this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Not implemented", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
    }

    private void addVert(){
        if (songs.size() != 0 && !(songs.size()<6)) {
            for (int i = 0; i < 6; i++) {
                vertSongs.add(songs.get(i));
            }
        }
    }

    private void updateIndicators(int page) {
        if (page == 0){
            indicator0.setBackgroundResource(R.drawable.indicator_selected);
            indicator1.setBackgroundResource(R.drawable.indicator_unselected);
            indicator2.setBackgroundResource(R.drawable.indicator_unselected);
        } else if (page == 1){
            indicator0.setBackgroundResource(R.drawable.indicator_unselected);
            indicator1.setBackgroundResource(R.drawable.indicator_selected);
            indicator2.setBackgroundResource(R.drawable.indicator_unselected);
        } else {
            indicator0.setBackgroundResource(R.drawable.indicator_unselected);
            indicator1.setBackgroundResource(R.drawable.indicator_unselected);
            indicator2.setBackgroundResource(R.drawable.indicator_selected);
        }
    }


    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        WRITE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            songs = appUtils.getSongs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    appUtils.getSongs();

                    Log.e("Player", songs.toString());

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(CategoryActivity.this, "Failed to get songs, Access Denied!!", Toast.LENGTH_LONG);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onClickItem(Song song, int position) {
        startActivity(new Intent(this, PlayListActivity.class));
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        int[] headers = new int[]{R.string.title0, R.string.title1,
                R.string.title2};

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceHolderFragment.newInstance(position + 1);
        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(headers[0]);
                case 1:
                    return getString(headers[1]);
                case 2:
                    return getString(headers[2]);
            }
            return null;
        }
    }
}
