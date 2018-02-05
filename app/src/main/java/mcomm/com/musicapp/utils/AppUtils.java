package mcomm.com.musicapp.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.format.DateFormat;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import mcomm.com.musicapp.R;
import mcomm.com.musicapp.Song;

/**
 * Created by apple on 04/02/2018.
 */

public class AppUtils {
    private Context context;

    public AppUtils(Context context){
        this.context = context;
    }

    static Comparator<Song> albumComp = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            return o1.getAlbum().compareTo(o2.getAlbum());
        }
    };

    static Comparator<Song> artistComp = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            return o1.getArtist().compareTo(o2.getArtist());
        }
    };

    static Comparator<Song> titleComp = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    };

    static Comparator<Song> dateComp = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            return o1.getDateAdded().compareTo(o2.getDateAdded());
        }
    };

    public ArrayList<Song> getSongs(){
        ArrayList<Song> songs = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(musicUri, null, null, null, null);

        if(cursor!=null && cursor.moveToFirst()){
            //get columns
            int titleColumn = cursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int durationColumn = cursor.getColumnIndex(
                    MediaStore.Audio.Media.DURATION);
            int dateColumn = cursor.getColumnIndex
                    (MediaStore.Audio.Media.DATE_ADDED);
            int artColumn = cursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);
            int albumColumn = cursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            //add songs to list
            do {
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                String thisArtist = cursor.getString(artistColumn);
                String thisAlbum = cursor.getString(albumColumn);
                String thisDate = cursor.getString(dateColumn);
                String thisDuration = cursor.getString(durationColumn);
                long thisArt = cursor.getLong(artColumn);
                songs.add(new Song(thisId, thisArt, thisTitle, "", thisArtist, thisAlbum,
                        thisDuration, thisDate));
            }
            while (cursor.moveToNext());
        }
        return songs;
    }

    public String dateConvert(String date){
        if (date != null && !date.isEmpty()) {
            long millisecond = Long.parseLong(date);
            // or you already have long value of date, use this instead of milliseconds variable.
            return DateFormat.format("mm:ss", new Date(millisecond)).toString();
        }
        return "00:00";
    }

    public ArrayList<Song> sortSongs(ArrayList<Song> songs, String sortType){
        if (sortType.equalsIgnoreCase("title")) {
            Collections.sort(songs, titleComp);
            return songs;
        } else if (sortType.equalsIgnoreCase("album")) {
            Collections.sort(songs, albumComp);
            return  songs;
        } else if (sortType.equalsIgnoreCase("artist")) {
            Collections.sort(songs, artistComp);
            return songs;
        } else if (sortType.equalsIgnoreCase("shuffle_unchecked")) {
            Collections.shuffle(songs);
            return songs;
        } else if (sortType.equalsIgnoreCase("date")) {
            Collections.sort(songs, dateComp);
            return songs;
        }
        return null;
    }

    public Bitmap getSongImage(long albumId) {
        Bitmap bm;
        try
        {
            final Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);

            ParcelFileDescriptor pfd = context.getContentResolver()
                    .openFileDescriptor(uri, "r");

            if (pfd != null)
            {
                FileDescriptor fd = pfd.getFileDescriptor();
                bm = BitmapFactory.decodeFileDescriptor(fd);
            } else {
                bm = null;
            }
        } catch (Exception e) {
            bm = null;
        }
        return bm;
    }

}
