package mcomm.com.musicapp;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import org.parceler.transfuse.annotations.Bind;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import mcomm.com.musicapp.utils.AppUtils;

/**
 * Created by apple on 03/02/2018.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>
        implements Filterable {
    private ArrayList<Song> songs;
    private ArrayList<Song> filteredSongs;
    private Context context;
    private Listener listener;
    private AppUtils utils;
    public SongAdapter(ArrayList<Song> songs, Context context){
        this.songs = songs;
        this.filteredSongs = songs;
        this.context = context;
        listener = (Listener) context;
        utils = new AppUtils(context);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charStr = constraint.toString();
                if (charStr.isEmpty()){
                    filteredSongs = songs;
                } else {
                    ArrayList<Song> filtered = new ArrayList<>();

                    for ( Song song : songs) {
                        if (song.getTitle().toLowerCase().contains(charStr) ||
                                song.getAlbum().toLowerCase().contains(charStr) ||
                                song.getArtist().toLowerCase().contains(charStr)) {
                            filtered.add(song);
                        }
                    }
                    filteredSongs = filtered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredSongs;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredSongs = (ArrayList<Song>) results.values;
                listener.onSongsChanged(filteredSongs);
                notifyDataSetChanged();
            }
        };
    }

    public interface Listener{
        void onClickItem(Song song, int position);
        void onSongsChanged(ArrayList<Song> songs);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context)
                .inflate(R.layout.song_detail_list_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Song song = filteredSongs.get(position);
        final int pos = position;
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
                listener.onClickItem(song, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredSongs.size();
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
