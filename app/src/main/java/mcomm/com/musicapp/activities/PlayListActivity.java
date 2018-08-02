package mcomm.com.musicapp.activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.parceler.Parcels;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mcomm.com.musicapp.MusicService;
import mcomm.com.musicapp.R;
import mcomm.com.musicapp.Song;
import mcomm.com.musicapp.SongAdapter;
import mcomm.com.musicapp.utils.AppUtils;

public class PlayListActivity extends AppCompatActivity implements SongAdapter.Listener,
        View.OnClickListener {

    private ArrayList<Song> songs;
    private Song song;
    private AppUtils appUtils;
    @BindView(R.id.vert_list1)
    RecyclerView vert1;
    @BindView(R.id.card)
    RelativeLayout layout;
    @BindView(R.id.play)
    RelativeLayout playButton;
    private SearchView searchView;
    public static final int WRITE = 10;
    @BindView(R.id.duration)
    TextView duration;
    @BindView(R.id.current_duration)
    TextView currentDuration;
    @BindView(R.id.current_song_title)
    TextView title;
    @BindView(R.id.current_song_artist)
    TextView artist;
    @BindView(R.id.current_song_image)
    ImageView songImage;
    @BindView(R.id.shuffle)
    ToggleButton shuffleBtn;
    @BindView(R.id.repeat)
    ImageView repeatBtn;
    @BindView(R.id.like)
    ImageView likeBtn;
    @BindView(R.id.next)
    ImageView nextBtn;
    @BindView(R.id.play_icon)
    ToggleButton playImage;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    SongAdapter adapter;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private int position;
    private Unbinder unbind;
    private boolean paused = false, playbackPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        unbind = ButterKnife.bind(this);
        appUtils = new AppUtils(this);
        repeatBtn.setOnClickListener(this);
        shuffleBtn.setOnClickListener(this);
        playButton.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        songImage.setOnClickListener(this);
        songs = new ArrayList<>();
        checkPermission();
        vert1.setLayoutManager(new LinearLayoutManager(this));
        vert1.setHasFixedSize(true);

        if (getSupportActionBar() != null){
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        }

        songs = appUtils.sortSongs(songs, "artist");

        adapter = new SongAdapter(songs, this);
        vert1.setAdapter(adapter);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                int pos = (int) (((float)progress/100) * Integer.parseInt(song.getDuration()));
                if (musicSrv != null && musicBound && musicSrv.isPlaying())
                    musicSrv.seekTo(pos);
            }
        });
        if (songs.size() > 0)
            this.song = songs.get(0);
            setViews(songs.get(0));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        unbind.unbind();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_search || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused){
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songs);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };



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

    private void play() {
        if (playbackPaused)
            playbackPaused = false;
        musicSrv.playCont();
    }

    private void pause(){
        if (!playbackPaused)
            playbackPaused = true;
        musicSrv.pauseSong();
    }

    private void playNext(){
        int pos = musicSrv.playNext();
        if (playbackPaused){
            playbackPaused = false;
        }
        song = songs.get(pos);
        setViews(songs.get(pos));
    }

    private void playPrev() {
        int pos = musicSrv.playPrev();
        if (playbackPaused){
            playbackPaused = false;
        }
        song = songs.get(pos);
        setViews(songs.get(pos));
    }

    private void setViews(Song song){
        duration.setText(appUtils.dateConvert(song.getDuration()));
        title.setText(song.getTitle());
        artist.setText(song.getArtist());
        currentDuration.setText(String.valueOf("00:00"));
        seekBar.setProgress(0);
        Bitmap bm = appUtils.getSongImage(song.getAlbumId());
        if (bm != null)
            songImage.setImageBitmap(bm);
        else
            songImage.setImageResource(R.drawable.error_view);

        if (layout.getVisibility() != View.VISIBLE)
            layout.setVisibility(View.VISIBLE);
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
                    songs = appUtils.getSongs();

                    Log.e("Player", songs.toString());

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(PlayListActivity.this, "Failed to get songs, Access Denied!!", Toast.LENGTH_LONG);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onClickItem(Song song, int position) {
        this.song = song;
        this.position = position;
        setViews(song);
        musicSrv.setSong(position);
        musicSrv.playSong();
    }

    @Override
    public void onSongsChanged(ArrayList<Song> songs) {
        musicSrv.setList(songs);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                if (playImage.isChecked()) {
                    musicSrv.playCont();
                    playImage.setChecked(false);
                } else {
                    musicSrv.pauseSong();
                    playImage.setChecked(true);
                }
                break;
            case R.id.next:
                playNext();
                break;
            case R.id.current_song_image:
                Intent intent = new Intent(this, PlayerActivity.class);
                intent.putExtra("song", Parcels.wrap(this.song));
                startActivity(intent);
                break;
            case R.id.shuffle:
                musicSrv.setShuffle();
                break;
        }
    }

    /*@Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void pause() {
        if (musicSrv != null)
            musicSrv.pauseSong();
        playbackPaused = true;
    }

    @Override
    public int getDuration() {
        if (musicSrv != null && musicBound && musicSrv.isPlaying()) {
            return musicSrv.getDur();

        } else {
            return 0;
        }
    }

    @Override
    public int getCurrentPosition() {
        if (musicSrv != null && musicBound && musicSrv.isPlaying()) {
            int progress = (int)(((float)musicSrv.getPosn() / musicSrv.getDur()) * 100);
            Log.e("Player", "" + progress + " " + musicSrv.getPosn() + " " + musicSrv.getDur());
            currentDuration.setText(appUtils.dateConvert(String.valueOf(musicSrv.getPosn())));
            seekBar.setProgress(progress);
            return musicSrv.getPosn();
        }else {
            return 0;
        }
    }

    @Override
    public void seekTo(int pos) {
        if (musicSrv != null && musicBound && musicSrv.isPlaying())
            musicSrv.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return musicSrv != null && musicBound && musicSrv.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }*/
}
