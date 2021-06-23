package ru.tishin.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final TimeInterpolator TIME_INTERPOLATOR = new DecelerateInterpolator(1);
    private ObjectAnimator animator = null;

    private MediaPlayer mediaPlayer = null;
    ArrayList<SongInfo> songList = new ArrayList<>();
    private int songIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);

        }
        else
            prepareSongList();
        ProgressBar progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        progress_bar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));

        songIndex = 0;
    }

    private void prepareSongList(){
        ContentResolver musicResolver = getContentResolver();
        Uri externalUri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection=new String[]{MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
                };
        String selection =null;
        String[] selectionArgs = null;
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = musicResolver.query(externalUri, projection, selection, selectionArgs, sortOrder);
        SongInfo info = null;
        if (cursor != null) {
            try {
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    int colIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                    int colTitleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                    int colArtistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                    int colFilePathIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                    int colAlbumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                    int colDurationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                    do {
                        info = new SongInfo();
                        info.setId(cursor.getInt(colIdIndex));
                        info.setTitle(cursor.getString(colTitleIndex));
                        info.setArtist(cursor.getString(colArtistIndex));
                        info.setFilePath(cursor.getString(colFilePathIndex));
                        info.setDuration(cursor.getInt(colDurationIndex));

                        int album_id = cursor.getInt(colAlbumIdIndex);
                        Uri artwork_uri = Uri
                                .parse("content://media/external/audio/albumart");
                        info.setAlbumArtUri(ContentUris.withAppendedId(artwork_uri, album_id));

                        songList.add(info);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                prepareSongList();
            }
        }
    }

    private void setData(){
        ImageView image_view = (ImageView) findViewById(R.id.cover);

        TextView text_artist = (TextView) findViewById(R.id.text_artist);
        TextView text_title = (TextView) findViewById(R.id.text_title);

        ProgressBar progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(songList.get(songIndex).getFilePath());
            text_artist.setText(songList.get(songIndex).getArtist());
            text_title.setText(songList.get(songIndex).getTitle());

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), songList.get(songIndex).getAlbumArtUri());
                image_view.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 400, 400, true));
            } catch (IOException e) {

            }

            progress_bar.setMax(songList.get(songIndex).getDuration());
            if(animator != null) {
                animator.end();
            }
            animator = ObjectAnimator.ofInt(progress_bar, "progress", 0, songList.get(songIndex).getDuration());
            animator.setDuration(songList.get(songIndex).getDuration());

            mediaPlayer.prepare();
            //mediaPlayer.seekTo(mediaPlayer.getDuration() - 20000);
        }
        catch (Exception e){
            Toast.makeText(this, "Ошибка воспроизведения файла: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void playClick(View view) {
        if(mediaPlayer == null) {
            setData();
        }

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            if(animator.isPaused()) {
                animator.resume();
            } else {
                animator.start();
            }
        }
    }

    public void pauseClick(View view) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            animator.pause();
        }
    }

    public void prevClick(View view) {
        if (songIndex > 0){
            --songIndex;
            if (mediaPlayer != null){
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.release();
            }

            setData();
            mediaPlayer.start();
            animator.start();
        }
    }

    public void nextClick(View view) {
        if (songIndex + 1 < songList.size()){
            ++songIndex;
            if (mediaPlayer != null){
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.release();
            }

            setData();
            mediaPlayer.start();
            animator.start();
        }
    }
}