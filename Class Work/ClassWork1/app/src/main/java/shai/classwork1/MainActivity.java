package shai.classwork1;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private EditText _editText;
    private TextView _textView;
    private Button   _button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _editText = (EditText)findViewById(R.id.textEnter);
        _textView = (TextView) findViewById(R.id.textShow);
        _button   = (Button)findViewById(R.id.button);

        _button.setOnClickListener(this);
        _button.setOnLongClickListener(this);

    }



    public void onClick(View v)
    {
        _textView.setText(_editText.getText());
        _editText.setText("");
    }


    public boolean onLongClick(View v)
    {
        _textView.setText("");
        _editText.setText("");

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
