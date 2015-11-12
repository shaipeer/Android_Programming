package shai.homework1;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    //App time format
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("mm:ss:SS");

    //Shared Preferences constants and keys
    private final String PREFS_NAME          = "app_memory";
    private final String LEFT_PREFS_KEY      = "left_time";
    private final String RIGHT_PREFS_KEY     = "right_time";
    private final String TIME_GAP_PREFS_KEY  = "time_gap";
    private final String BEST_TIME_PREFS_KEY = "best_time";
    private final String BEST_TIME_PREFS_VAL = "best_time_val";

    //Upper buttons value
    private final String FIRST_BUTTON_VALUE  = "1";
    private final String SECOND_BUTTON_VALUE = "2";

    //Best time zero value
    private final int BEST_TIME_ZERO_VALUE = 0;

    //Buttons
    private Button  _left_cmd;
    private Button  _right_cmd;
    private Button  _time_gap_cmd;
    private Button  _best_time_cmd;

    //Is the key contains 1 pressed
    private Boolean _isKeyOnePressed;

    //Best time value
    long _bestTime;

    //Take the time for the first and second press
    private long _startTime, _endTime;

    //Shared Preferences vars
    private SharedPreferences _prefs;
    private SharedPreferences.Editor _editor;


    //================================================================
    //                      On Create
    //================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init shared preferences
        _prefs  = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        _editor = _prefs.edit();

        //Set screen objects
        _left_cmd       = (Button)findViewById(R.id.left_cmd);
        _right_cmd      = (Button)findViewById(R.id.right_cmd);
        _time_gap_cmd   = (Button)findViewById(R.id.time_gap_cmd);
        _best_time_cmd  = (Button)findViewById(R.id.best_time_cmd);

        //Set listeners
        _left_cmd.setOnClickListener(this);
        _right_cmd.setOnClickListener(this);
        _time_gap_cmd.setOnClickListener(this);
        _best_time_cmd.setOnClickListener(this);

        //Button boolean reset
        _isKeyOnePressed = false;

        //Reset best time
        _bestTime = BEST_TIME_ZERO_VALUE;

        //Load last operation values
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
        pressManager(_left_cmd.getText().toString());
    }

    private void rightButtonPressed()
    {
        pressManager(_right_cmd.getText().toString());
    }

    private void timeGapPressed()
    {

    }

    private void bestTimePressed()
    {
        //Cancel key one press
        if(_isKeyOnePressed)
            resetPress();

        //Reset fields
        _best_time_cmd.setText(getResources().getString(R.string.zero_time));
        _time_gap_cmd.setText(getResources().getString(R.string.zero_time));

        //Reset best time
        _bestTime = BEST_TIME_ZERO_VALUE;

        //Set random value for the left and right buttons
        setRandomKeys();

        //Save state
        saveAll();

        //Reset values massage
        printToast(getResources().getString(R.string.reset_values));
    }

    //Generates the left and right buttons value randomly ( 1 or 2 )
    private void setRandomKeys()
    {
        int minNumber = 1;
        int maxNumber = 2;

        int leftKeyValue = (int)( Math.random() * maxNumber + minNumber);   //generates 1 or 2

        switch(leftKeyValue)
        {
            case 1:
                _left_cmd.setText(FIRST_BUTTON_VALUE);      //left button
                _right_cmd.setText(SECOND_BUTTON_VALUE);    //right button
                break;
            case 2:
                _left_cmd.setText(SECOND_BUTTON_VALUE);     //left button
                _right_cmd.setText(FIRST_BUTTON_VALUE);     //right button
                break;
        }

        saveAll();      //save state
    }

    //Manages the presses on the left and right buttons
    private void pressManager(String buttonText)
    {
        if(buttonText.equals(FIRST_BUTTON_VALUE))  //If the first button pressed
        {
            if(_isKeyOnePressed)
            {
                resetPress();                       //Cancel Press
            }
            else
            {
                _isKeyOnePressed = true;            //Ok press on button one
                takeStartTime();                    //Start taking time
            }
        }
        else if(_isKeyOnePressed)                   //If button 2 pressed after button 1 without interrupts
        {
            takeEndTime();                          //Take the second time
            _time_gap_cmd.setText(getTimeGapStr()); 

            //Check if there is new best time
            if(isFaster())
            {
                _bestTime = getTimeGapMili();
                _best_time_cmd.setText(getTimeGapStr());
                printToast(getResources().getString(R.string.new_best_time));   //new best time massage
            }

            //Set random value for the left and right buttons
            setRandomKeys();

            //Save state
            saveAll();

            //Reset the press
            resetPress();
        }
    }

    //================================================================
    //                      Time
    //================================================================

    //Take the start time
    private void takeStartTime()
    {
        _startTime = System.currentTimeMillis();
    }

    //Take the end time
    private void takeEndTime()
    {
        _endTime = System.currentTimeMillis();
    }

    //Return String with the time gap between start and end time
    private String getTimeGapStr()
    {
        Date date = new Date(getTimeGapMili());

        return DATE_FORMAT.format(date);
    }

    private Long getTimeGapMili()
    {
        Long gap =_endTime - _startTime;
        return gap;
    }

    //Check if the new time is faster then best time
    //Return true if the time gap is faster than the best time
    private Boolean isFaster()
    {
        String timeGapStr;
        Date timeGap;

        if      (_bestTime == 0)                {   return true;    }   //Check if its the first measure
        else if (getTimeGapMili() < _bestTime)  {   return true;    }   //Compare the time
        else                                    {   return false;   }   //Not faster

    }

    //================================================================
    //                      Toast
    //================================================================
    private void printToast(String str)
    {
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
    }


    //================================================================
    //                      Reset Press
    //================================================================
    private void resetPress()
    {
        _isKeyOnePressed = false;
    }


    //================================================================
    //                      Save All
    //================================================================
    private void saveAll()
    {
        //write to shared preferences
        _editor.putString(LEFT_PREFS_KEY,      _left_cmd.getText().toString()      );    //save left key
        _editor.putString(RIGHT_PREFS_KEY,     _right_cmd.getText().toString()     );    //save right key
        _editor.putString(TIME_GAP_PREFS_KEY,  _time_gap_cmd.getText().toString()  );    //save time gap key
        _editor.putString(BEST_TIME_PREFS_KEY, _best_time_cmd.getText().toString() );    //save best time key
        _editor.putString(BEST_TIME_PREFS_VAL, String.valueOf(_bestTime)           );    //save best time value

        _editor.apply();
    }

    //================================================================
    //                      Load All
    //================================================================
    private void loadAll()
    {
        _left_cmd.setText(_prefs.getString(LEFT_PREFS_KEY,           getResources().getString(R.string.left_button)  ));        //load left key
        _right_cmd.setText(_prefs.getString(RIGHT_PREFS_KEY,         getResources().getString(R.string.right_button) ));        //load right key
        _time_gap_cmd.setText(_prefs.getString(TIME_GAP_PREFS_KEY,   getResources().getString(R.string.zero_time)    ));        //load time gap key
        _best_time_cmd.setText(_prefs.getString(BEST_TIME_PREFS_KEY, getResources().getString(R.string.zero_time)    ));        //load best time key
        _bestTime = Long.valueOf(_prefs.getString(BEST_TIME_PREFS_VAL, BEST_TIME_ZERO_VALUE + "")).longValue();                 //load best time value
    }
}
