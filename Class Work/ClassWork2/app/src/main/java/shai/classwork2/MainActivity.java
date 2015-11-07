package shai.classwork2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private final String PREFS_NAME         = "main_page_memory";
    private final String USERNAME_PREFS_KEY = "username";


    private TextView _username_TextView;
    private EditText _username_EditText;
    private Button   _save_cmd;
    private Button   _nextScreen_cmd;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init shared preferences
        prefs =  getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();

        //set buttons;
        _username_TextView = (TextView) findViewById(R.id.username_textView);
        _username_EditText = (EditText)findViewById(R.id.username_editText);
        _save_cmd          = (Button)findViewById(R.id.save_username_cmd);
        _nextScreen_cmd    = (Button)findViewById(R.id.next_screen_cmd);

        //set listeners
        _save_cmd.setOnClickListener(this);
        _nextScreen_cmd.setOnClickListener(this);

        //load username to username text view
        _username_TextView.setText(prefs.getString(USERNAME_PREFS_KEY, ""));

    }


    @Override
    public void onClick(View v)
    {
        if      (v.getId() == _save_cmd.getId())        //on Save button press
            saveButtonPressed();
        else if (v.getId() == _nextScreen_cmd.getId())  //on nextScreen button press
            nextScreenButtonPressed();

    }

    private void saveButtonPressed()
    {
        //write to shared preferences
        editor.putString(USERNAME_PREFS_KEY, _username_EditText.getText().toString() );
        editor.apply();

        //set username text view to show the username
        _username_TextView.setText(_username_EditText.getText());
    }

    private void nextScreenButtonPressed()
    {
        //create an intent to connect this Context with new Activity
        Intent intent = new Intent(this, MainActivity2.class);

        //start new activity
        startActivity(intent);
    }

}
