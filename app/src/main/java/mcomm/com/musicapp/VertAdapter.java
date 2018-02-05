package mcomm.com.musicapp;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import mcomm.com.musicapp.utils.AppUtils;

/**
 * Created by apple on 03/02/2018.
 */

public class VertAdapter extends RecyclerView.Adapter<VertAdapter.ViewHolder> {
    private ArrayList<Song> songs;
    private Context context;
    private Listener listener;
    private AppUtils utils;
    public VertAdapter(ArrayList<Song> songs, Context context){
        this.songs = songs;
        this.context = context;
        listener = (Listener) context;
        utils = new AppUtils(context);
    }

    public interface Listener{
        void onClickItem(Song song, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context)
                .inflate(R.layout.vert_list_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Song song = songs.get(position);
        try
        {
            final Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri, song.getAlbumId());

            ParcelFileDescriptor pfd = context.getContentResolver()
                    .openFileDescriptor(uri, "r");

            if (pfd != null)
            {
                FileDescriptor fd = pfd.getFileDescriptor();
                holder.songImage.setImageBitmap(BitmapFactory.decodeFileDescriptor(fd));
            } else {
                holder.songImage.setImageResource(R.drawable.error_view);
            }
        } catch (Exception e) {
            holder.songImage.setImageResource(R.drawable.error_view);
        }
        holder.duration.setText(utils.dateConvert(song.getDuration()));
        holder.artist.setText(song.getArtist());
        holder.title.setText(song.getTitle());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItem(song, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.song_image) ImageView songImage;
        @BindView(R.id.song_title) TextView title;
        @BindView(R.id.song_artist) TextView artist;
        @BindView(R.id.duration) TextView duration;
        View view;
        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            view = v;
        }
    }
}
