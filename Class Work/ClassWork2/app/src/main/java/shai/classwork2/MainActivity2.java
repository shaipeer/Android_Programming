package shai.classwork2;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener
{

    //===========  Views  =============
    private Button   _play_cmd;
    private Button   _show_playlist_cmd;
    private TextView _platlist_textView;

    //======  SharedPreferences  ======
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    //========  MediaPlayer  ==========
    private MediaPlayer mPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //set buttons
        _play_cmd          = (Button)findViewById(R.id.play_sound_cmd);
        _show_playlist_cmd = (Button)findViewById(R.id.show_playlist_cmd);
        _platlist_textView = (TextView) findViewById(R.id.playlist_textView);

        //set listeners
        _play_cmd.setOnClickListener(this);
        _show_playlist_cmd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if      (v.getId() == _play_cmd.getId())            //on Play button click
            playButtonPressed();
        else if (v.getId() == _show_playlist_cmd.getId())   //on show Playlist button click
            showPlaylistButtonPressed();
    }

    private void playButtonPressed()
    {
        mPlayer = MediaPlayer.create(this, R.raw.soundtrack);
        mPlayer.start();
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        //stop the music
        if(mPlayer != null)
            mPlayer.stop();
    }

    private void showPlaylistButtonPressed()
    {
        String playlistText = "";

        //init stream
        InputStream input = getResources().openRawResource(R.raw.soundtrack_list);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = "";

        try
        {
            //loop though the file line by line
            while ((line = reader.readLine()) != null)
            {
                playlistText += line + "\n";
            }

            _platlist_textView.setText(playlistText);

            input.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
