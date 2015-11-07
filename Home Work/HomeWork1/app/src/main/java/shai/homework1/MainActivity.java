package shai.homework1;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    //Shared Preferences constants
    private final String PREFS_NAME          = "app_memory";
    private final String LEFT_PREFS_KEY      = "left_time";
    private final String RIGHT_PREFS_KEY     = "right_time";
    private final String TIME_GAP_PREFS_KEY  = "time_gap";
    private final String BEST_TIME_PREFS_KEY = "best_time";

    //App time format
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("mm:ss:SS");

    //buttons
    private Button  _left_cmd;
    private Button  _right_cmd;
    private Button  _time_gap_cmd;
    private Button  _best_time_cmd;

    //is left or right keys ware pressed
    private Boolean isKeyOnePressed;

    //take the time for the first and second press
    private long startTime, endTime;

    //Shared Preferences vars
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    //================================================================
    //                      On Create
    //================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init shared preferences
        prefs  = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();

        //set screen objects
        _left_cmd       = (Button)findViewById(R.id.left_cmd);
        _right_cmd      = (Button)findViewById(R.id.right_cmd);
        _time_gap_cmd   = (Button)findViewById(R.id.time_gap_cmd);
        _best_time_cmd  = (Button)findViewById(R.id.best_time_cmd);

        //set listeners
        _left_cmd.setOnClickListener(this);
        _right_cmd.setOnClickListener(this);
        _time_gap_cmd.setOnClickListener(this);
        _best_time_cmd.setOnClickListener(this);

        //button boolean reset
        isKeyOnePressed = false;

        //load last operation values
        loadAll();
    }


    //================================================================
    //                      On Click
    //================================================================
    @Override
    public void onClick(View v)
    {
        if      (v.getId() == _left_cmd.getId()     )   leftButtonPressed();    //on left button press
        else if (v.getId() == _right_cmd.getId()    )   rightButtonPressed();   //on right button press
        else if (v.getId() == _time_gap_cmd.getId() )   timeGapPressed();       //on time gap button press
        else if (v.getId() == _best_time_cmd.getId())   bestTimePressed();      //on best time button press
    }


    private void leftButtonPressed()
    {
        timerManager(_left_cmd.getText().toString());
    }

    private void rightButtonPressed()
    {
        timerManager(_right_cmd.getText().toString());
    }

    private void timeGapPressed()
    {
        if(isKeyOnePressed)
            resetPress();

        setRandomKeys();

        _time_gap_cmd.setText(getResources().getString(R.string.time_gap_button));
    }

    private void bestTimePressed()
    {
        if(isKeyOnePressed)
            resetPress();
        _best_time_cmd.setText(getResources().getString(R.string.best_time_button));
        _time_gap_cmd.setText(getResources().getString(R.string.time_gap_button));

        saveAll();
    }

    //Generates the left and right buttons value randomly ( 1 or 2 )
    private void setRandomKeys()
    {
        int rand = (int)( Math.random() * 2 + 1);   //generates 1 or 2

        switch(rand)
        {
            case 1:
                _left_cmd.setText("1");             //left button
                _right_cmd.setText("2");            //right button
                break;
            case 2:
                _left_cmd.setText("2");             //left button
                _right_cmd.setText("1");            //right button
                break;
        }

        saveAll();      //save state
    }

    //Manages the presses on the left and right buttons
    private void timerManager(String buttonText)
    {
        if(buttonText.equals("1"))  //
        {
            if(isKeyOnePressed)
            {
                isKeyOnePressed = false;
            }
            else
            {
                isKeyOnePressed = true;
                takeStartTime();
            }
        }
        else if(isKeyOnePressed)    //if button 2 pressed after button 1 without interrupts
        {
            takeEndTime();
            _time_gap_cmd.setText(getTimeGap());
            if(isFaster())
                _best_time_cmd.setText(getTimeGap());
            saveAll();

            resetPress();
        }
    }

    //================================================================
    //                      Time
    //================================================================

    //Take the start time
    private void takeStartTime()
    {
        startTime = System.currentTimeMillis();
    }

    //Take the end time
    private void takeEndTime()
    {
        endTime = System.currentTimeMillis();
    }

    //Return String with the time gap between start and end time
    private String getTimeGap()
    {
        long milliseconds = endTime - startTime;
        Date date = new Date(milliseconds);

        return DATE_FORMAT.format(date);
    }

    //Check if the new time is faster then best time
    private Boolean isFaster()
    {
        String zeroString    = getResources().getString(R.string.time_gap_button);
        String bestTimeValue = _best_time_cmd.getText().toString();
        String timeGapValue  = _time_gap_cmd.getText().toString();

        String timeGapStr, bestTimeStr;
        Date timeGap, bestTime;

        if(bestTimeValue.equals(zeroString) && !timeGapValue.equals(zeroString))
        {
            return true;
        }
        else
        {
            try
            {
                //convert time gap value to Date object
                timeGapStr  = _time_gap_cmd.getText().toString();
                timeGap     = DATE_FORMAT.parse(timeGapStr);

                //convert best time value to Date object
                bestTimeStr = _best_time_cmd.getText().toString();
                bestTime    = DATE_FORMAT.parse(bestTimeStr);

                //compare the dates
                if (timeGap.compareTo(bestTime) < 0)
                {
                    return true;
                }
            }
            catch (ParseException e)
            {
                return false;
            }
        }

        return false;
    }


    //================================================================
    //                      Reset Press
    //================================================================
    private void resetPress()
    {
        isKeyOnePressed = false;
    }


    //================================================================
    //                      Save All
    //================================================================
    private void saveAll()
    {
        //write to shared preferences
        editor.putString(LEFT_PREFS_KEY,      _left_cmd.getText().toString()      );    //save left key
        editor.putString(RIGHT_PREFS_KEY,     _right_cmd.getText().toString()     );    //save right key
        editor.putString(TIME_GAP_PREFS_KEY,  _time_gap_cmd.getText().toString()  );    //save time gap key
        editor.putString(BEST_TIME_PREFS_KEY, _best_time_cmd.getText().toString() );    //save best time key

        editor.apply();
    }

    //================================================================
    //                      Load All
    //================================================================
    private void loadAll()
    {
        _left_cmd.setText(prefs.getString(LEFT_PREFS_KEY,           getResources().getString(R.string.left_button)      ));     //load left key
        _right_cmd.setText(prefs.getString(RIGHT_PREFS_KEY,         getResources().getString(R.string.right_button)     ));     //load right key
        _time_gap_cmd.setText(prefs.getString(TIME_GAP_PREFS_KEY,   getResources().getString(R.string.time_gap_button)  ));     //load time gap key
        _best_time_cmd.setText(prefs.getString(BEST_TIME_PREFS_KEY, getResources().getString(R.string.best_time_button) ));     //load best time key
    }
}
