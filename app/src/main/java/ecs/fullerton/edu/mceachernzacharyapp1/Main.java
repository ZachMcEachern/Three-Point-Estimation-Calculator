package ecs.fullerton.edu.mceachernzacharyapp1;

import android.icu.text.DecimalFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.content.SharedPreferences;

public class Main extends AppCompatActivity
implements OnEditorActionListener {

    //Declaring the variables for the widgets.
    private EditText optimisticEditText;
    private EditText nominalEditText;
    private EditText pessimisticEditText;
    private TextView meanAmountTextView;
    private TextView standardDeviationAmountTextView;

    float optimisticAmount;
    float nominalAmount;
    float pessimisticAmount;
    float standardDeviationAmount;
    float meanAmount;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencing the widgets.
        optimisticEditText = (EditText) findViewById(R.id.optimisticEditText);
        nominalEditText = (EditText) findViewById(R.id.nominalEditText);
        pessimisticEditText = (EditText) findViewById(R.id.pessimisticEditText);

        meanAmountTextView = (TextView) findViewById(R.id.meanAmountTextView);
        standardDeviationAmountTextView = (TextView) findViewById(R.id.standardDeviationAmountTextView);

        //Setting the listeners.
        optimisticEditText.setOnEditorActionListener(this);
        nominalEditText.setOnEditorActionListener(this);
        pessimisticEditText.setOnEditorActionListener(this);

        preferences = getSharedPreferences("MainActivity", MODE_PRIVATE);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
        {
            calculateAndDisplay();
        }

        return false;
    }

    public void calculateAndDisplay(){

        //Get the amounts from the user inputs in string form.
        String optimisticAmountString = optimisticEditText.getText().toString();
        String nominalAmountString = nominalEditText.getText().toString();
        String pessimisticAmountString = pessimisticEditText.getText().toString();

        if (optimisticAmountString.equals("")) {
            optimisticAmount = 0;
        }
        else {
            optimisticAmount = Float.parseFloat(optimisticAmountString);
        }

        if (nominalAmountString.equals("")) {
            nominalAmount = 0;
        }
        else {
            nominalAmount = Float.parseFloat(nominalAmountString);
        }

        if (pessimisticAmountString.equals("")) {
            pessimisticAmount = 0;
        }
        else {
            pessimisticAmount = Float.parseFloat(pessimisticAmountString);
        }

        //Calculate the mean and standard deviation.
        meanAmount = (optimisticAmount + 4 * nominalAmount + pessimisticAmount) / 6;
        standardDeviationAmount = (pessimisticAmount - optimisticAmount) / 6;

        //Display the calculations onto the screen with formatting.
        meanAmountTextView.setText(String.format("%.1f", meanAmount));
        standardDeviationAmountTextView.setText(String.format("%.1f", standardDeviationAmount));
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor e = preferences.edit();
        e.putFloat("optimisticAmount", optimisticAmount);
        e.putFloat("nominalAmount", nominalAmount);
        e.putFloat("pessimisticAmount", pessimisticAmount);

        e.putFloat("meanAmount", meanAmount);
        e.putFloat("standardDeviationAmount", standardDeviationAmount);


        e.commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        optimisticAmount = preferences.getFloat("optimisticAmount", 0);
        nominalAmount = preferences.getFloat("nominalAmount", 0);
        pessimisticAmount = preferences.getFloat("pesimisticAmount", 0);

        meanAmount = preferences.getFloat("meanAmount", 0);
        standardDeviationAmount = preferences.getFloat("standardDeviationAmount", 0);

        calculateAndDisplay();
    }
}
