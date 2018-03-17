package mcomm.com.musicapp.activities;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.parceler.Parcels;

import java.io.FileDescriptor;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mcomm.com.musicapp.R;
import mcomm.com.musicapp.Song;
import mcomm.com.musicapp.utils.AppUtils;

public class PlayerActivity extends AppCompatActivity {

    @BindView(R.id.duration) TextView duration;
    @BindView(R.id.current_duration) TextView currentDuration;
    @BindView(R.id.current_song_title) TextView title;
    @BindView(R.id.current_song_artist) TextView artist;
    @BindView(R.id.current_song_image) ImageView songImage;
    @BindView(R.id.shuffle) ToggleButton shuffleBtn;
    @BindView(R.id.repeat) ToggleButton repeatBtn;
    @BindView(R.id.like) ImageView likeBtn;
    @BindView(R.id.next) ImageView nextBtn;
    @BindView(R.id.seek_bar) SeekBar seekBar;
    @BindView(R.id.play) RelativeLayout play;
    @BindView(R.id.play_icon) ToggleButton playButton;
    AppUtils appUtils;
    Unbinder unbind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        unbind = ButterKnife.bind(this);
        Intent intent = getIntent();
        appUtils = new AppUtils(this);
        if (getSupportActionBar() != null){
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        }
        if (intent.getExtras() != null) {
            Song song = Parcels.unwrap(intent.getParcelableExtra("song"));
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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind.unbind();
    }
}
