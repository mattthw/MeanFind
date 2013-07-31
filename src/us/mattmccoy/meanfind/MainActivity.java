package us.mattmccoy.meanfind;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {

    //instantiate preferences & an editor
    SharedPreferences preferences;
    Editor edit;
    //Commented out becuae i was being LAZY
    //String a1utoclearKEY = "us.mattmccoy.meanfind.AUTOCLEAR";
    //String c1opyItKEY = "us.mattmccoy.meanfind.COPYIT";
    //String d1ataKEY = "us.mattmccoy.meanfind.DATA";

    //classwide variables
    boolean autoclear;
    boolean copyIt;
    String data;
    //textview is for input box etc
    TextView result_type;
    int dividend;
    String dataFixed;
    private CheckBox radio_clipboard, radio_autoclear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        preferences = this.getSharedPreferences("us.mattmccoy.meanfind", Context.MODE_PRIVATE);
        edit = preferences.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //start listening
        dataListener();
        //load options
        loadOptions();
    }



    public void loadOptions(){
        autoclear = preferences.getBoolean("us.mattmccoy.meanfind.AUTOCLEAR", false);
        copyIt = preferences.getBoolean("us.mattmccoy.meanfind.COPYIT", false);
        data = preferences.getString("us.mattmccoy.meanfind.DATA", "78");
        if (autoclear) {
            radio_autoclear.setChecked(true);
        } else {
            radio_autoclear.setChecked(false);
        }
        if (copyIt) {
            radio_clipboard.setChecked(true);
        } else {
            radio_clipboard.setChecked(false);
        }

        EditText numString = (EditText) findViewById(R.id.num_input);
        numString.setText(data, TextView.BufferType.EDITABLE);
        numString.setSelection(numString.length());

        result_type = (TextView) findViewById(R.id.ansType);
        result_type.setText("Answer:");
    }

    //Listen for changed needed in preferences file
    public void dataListener() {
        EditText numString = (EditText) findViewById(R.id.num_input);
        numString.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EditText numString = (EditText) findViewById(R.id.num_input);
                edit.putString("us.mattmccoy.meanfind.DATA", numString.getText().toString() ).commit();
            }
        });
        numString.setText(data, TextView.BufferType.EDITABLE);
        //instantiate autoclear checkbox
        radio_autoclear = (CheckBox) findViewById(R.id.chex2);
        radio_autoclear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //is radio_autoclear checked?
                if (((CheckBox) view).isChecked()) {
                    edit.putBoolean("us.mattmccoy.meanfind.AUTOCLEAR", true).commit();
                } else {
                    edit.putBoolean("us.mattmccoy.meanfind.AUTOCLEAR", false).commit();
                }
            }
        });

        //instantiate clipboard checkbox
        radio_clipboard = (CheckBox) findViewById(R.id.chex);
        radio_clipboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //is radio_clipboard checked?
                if (((CheckBox) view).isChecked()) {
                    edit.putBoolean("us.mattmccoy.meanfind.COPYIT", true).commit();


                } else {
                    edit.putBoolean("us.mattmccoy.meanfind.COPYIT", false).commit();

                }

            }
        });
    }

    //delete data if actually closing
    public void onStop() {
        edit.putString("us.mattmccoy.meanfind.DATA", "").commit();
        super.onStop();
    }
    public void clearNums(View view) {
        EditText nums = (EditText) findViewById(R.id.num_input);
        nums.setText("");
        edit.putString("us.mattmccoy.meanfind.DATA", "").commit();
    }
    public void addComma(View view) {
        EditText numString = (EditText) findViewById(R.id.num_input);
        numString.setText(numString.getText().toString() + ",", TextView.BufferType.EDITABLE);
        numString.setSelection(numString.length());
    }
    public void about() {
        Intent a = new Intent(this, AboutPage.class);
        startActivity(a);
    }
    public void learn() {
        Intent goLearn = new Intent(this, LearnActivity.class);
        startActivity(goLearn);
    }
    //mean => calculate
    public void Mean(View view) {
        calculate(view, 1);
    }
    //median => calculate
    public void Median(View view) {
        calculate(view, 2);
    }
    //mode => calculate
    public void Mode(View view) {
        calculate(view, 3);
    }
    //calculate
    public void calculate(View view, int type) {
        final EditText numString = (EditText) findViewById(R.id.num_input);
        String temp = numString.getText().toString();
        //no blank entries
        if(temp.equals("")) return;
        //remove extra commas if needed
        for(int i = 0; i < temp.length()-2;i++) {
            if(temp.substring(0,1).equals(",")){
                temp = temp.substring(1);
                numString.setText(temp, TextView.BufferType.EDITABLE);
                i = 0;
            }
            if (temp.substring(i, i + 2).equals(",,")) {
                temp = temp.substring(0, i) + "," + temp.substring(i + 2);
                numString.setText(temp, TextView.BufferType.EDITABLE);
                i = 0;
            }
        }
        //save string incase close app
        edit.putString("us.mattmccoy.meanfind.DATA", temp).commit();
        //put string in array for easier math
        String[] nums = temp.split(",");
        dataFixed = Arrays.toString(nums);//temp;
        double[] newNums = new double[nums.length];
        for (int i = 0; i < newNums.length; i++) {
            try {
                newNums[i] = Double.parseDouble(nums[i]);
            }
            catch (NumberFormatException nfe) {
            }
        }
        double ans = 0.0;
        double b = 0;
        dividend = newNums.length;
        //check mean, median, or mode
        if (type == 1) {       //mean
            for (int i = 0; i < newNums.length; i++) {
                ans += newNums[i];
                b++;
            }
            ans = ans / b;
            ans = (double) Math.round(ans * 100000) / 100000;
            final String ansString = Double.toString(ans);
            dispResults(ansString, type, "Mean:");
        } else if (type == 2) { //median
            Arrays.sort(newNums);
            double median = 0;
            if (newNums.length % 2 != 0) {
                median = newNums[newNums.length / 2];
            } else {
                median = (newNums[newNums.length / 2] + newNums[newNums.length / 2 - 1]) / 2;
            }
            String medianString = Double.toString(median);
            dispResults(medianString, type, "Median:");
        } else {               //mode
            double mode = 0;
            int modeFreq = 0;
            ArrayList<Double> multiMode = new ArrayList<Double>(0);
            for (int i = 0; i < newNums.length; i++) {
                //check if most frequent
                int freq = 0;
                for (int j = 0; j < newNums.length; j++) {
                    if (newNums[j] == newNums[i])
                        freq++;
                }
                if (freq >= modeFreq) {
                    if (freq == modeFreq) {
                        if (!(multiMode.contains(newNums[i]))) {
                            //multiMode.set(0, newNums[i]);//) = newNums[i];
                            multiMode.add(newNums[i]);
                        }
                        modeFreq = freq;
                    } else {
                        if (!(multiMode.contains(newNums[i]))) {
                            multiMode.clear();
                            multiMode.add(newNums[i]);
                        }
                        modeFreq = freq;
                    }
                }
            }
            String modeString = "";// = Double.toString(multiMode);
            for (int i = 0; i < multiMode.size(); i++) {
                if (i == 0)
                    modeString = multiMode.get(i).toString();
                else
                    modeString += ", " + multiMode.get(i).toString();
            }
            if (modeFreq <= 1)
                modeString = "No mode";
            dispResults(modeString, type, "Mode:");
        }
    }


    @SuppressLint("NewApi")
    public void dispResults(String nums, int type, String typeName) {
        final EditText numString = (EditText) findViewById(R.id.num_input);
        TextView dataV = (TextView) findViewById(R.id.dataID);
        dataV.setText(dataFixed);
        result_type.setText(typeName);
        TextView ansV = (TextView) findViewById(R.id.answer);
        ansV.setText(nums);
        //Check if clearing
        if (radio_autoclear.isChecked()) {
            numString.setText("");
            edit.putString("us.mattmccoy.meanfind.DATA", "").commit();
        }
        //check if copy
        if (radio_clipboard.isChecked()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", nums);
                clipboard.setPrimaryClip(clip);
                Toast copy = Toast.makeText(getApplicationContext(), typeName + " copied to clipboard", Toast.LENGTH_SHORT);
                copy.show();
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(nums);
                Toast copy = Toast.makeText(getApplicationContext(), typeName + " copied to clipboard", Toast.LENGTH_SHORT);
                copy.show();
            }
        }
    }

    //Menu creation for menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about_settings:
                about();
                return true;
            case R.id.learn_settings:
                learn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
