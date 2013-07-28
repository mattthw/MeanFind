package us.mattmccoy.meanfind;

//import java.util.Arrays;


import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
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

public class MainActivity extends Activity {


    //saved data stuff needed
    SharedPreferences preferences;// = this.getSharedPreferences("us.mattmccoy.meanfind", Context.MODE_PRIVATE);
    Editor edit;
    boolean autoclear;
    boolean copyIt;
    String data;
    public String theme;
    TextView typeV;
    //private data using saved data
    String autoclearKEY = "us.mattmccoy.meanfind.AUTOCLEAR";
    String copyItKEY = "us.mattmccoy.meanfind.COPYIT";
    String dataKEY = "us.mattmccoy.meanfind.DATA";
    String themeKEY = "us.mattmccoy.meanfind.THEME";

    //normal private data
    int dividend;
    String dataFixed;
    private CheckBox myCBox, acBox, myThemeBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        preferences = this.getSharedPreferences("us.mattmccoy.meanfind", Context.MODE_PRIVATE);
        edit = preferences.edit();
        theme = preferences.getString(themeKEY, "light");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoclear = preferences.getBoolean(autoclearKEY, false);
        copyIt = preferences.getBoolean(copyItKEY, false);
        data = preferences.getString(dataKEY, "");
        //set boxes checked on start
        dataListener();
        addListenerBoxes();
        if (autoclear == true) {
            acBox.setChecked(true);
        } else {
            acBox.setChecked(false);
        }
        if (copyIt) {
            myCBox.setChecked(true);
        } else {
            myCBox.setChecked(false);
        }
        typeV = (TextView) findViewById(R.id.ansType);
        typeV.setText("Answer:");
    }

    //MENU CREATION **************************MENU*MENU*MENU*
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


    /*public void comingSoon() {

    }*/

    //delete info on close
    public void onStop() {
        edit.putString(dataKEY, "").commit();
        super.onStop();
    }


    //load edittext's previous data
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
                //String[] nums = obj.removeCommas(numString.getText().toString());
                String temp = numString.getText().toString().replaceAll(",,", ",");
                edit.putString(dataKEY, temp).commit();
            }
        });
        numString.setText(data, TextView.BufferType.EDITABLE);
    }

    //check box listeners
    public void addListenerBoxes() {
        //instantiate elements
        acBox = (CheckBox) findViewById(R.id.chex2);
        acBox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //is acBox checked?
                if (((CheckBox) v).isChecked()) {
                    edit.putBoolean(autoclearKEY, true).commit();
                } else {
                    edit.putBoolean(autoclearKEY, false).commit();
                }
            }
        });
        myCBox = (CheckBox) findViewById(R.id.chex);
        myCBox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //is myCBox checked?
                if (((CheckBox) v).isChecked()) {
                    edit.putBoolean(copyItKEY, true).commit();


                } else {
                    edit.putBoolean(copyItKEY, false).commit();

                }

            }
        });


    }


    public void clearNums(View view) {
        EditText nums = (EditText) findViewById(R.id.num_input);
        nums.setText("");
        edit.putString(dataKEY, "").commit();
    }
    public void addComma(View view){
        EditText numString = (EditText) findViewById(R.id.num_input);
        String temp = numString.getText().toString();
        numString.setText(temp+",", TextView.BufferType.EDITABLE);
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

    //calculate start mean
    public void Mean(View view) {
        calculate(view, 1);
    }

    //calculate start median
    public void Median(View view) {
        calculate(view, 2);
    }

    //calculate start mode
    public void Mode(View view) {
        calculate(view, 3);
    }

    //calculate
    public void calculate(View view, int type) {
        final EditText numString = (EditText) findViewById(R.id.num_input);
        //String[] nums = obj.removeCommas(numString.getText().toString());
        String temp = numString.getText().toString().replaceAll(",,", ",");
        edit.putString(dataKEY, temp).commit();
        String[] nums = temp.split(",");
        //prevent calculation if user has no data entered
        for (int i = 0; i < nums.length; i++) {
            if (nums[i].equals("") && temp != "") {
                Toast err = Toast.makeText(getApplicationContext(), "WRONG COMMA USAGE, IDIOT!", Toast.LENGTH_LONG);
                err.show();
                return;
            }
        }
        if (temp.equals("")) {
            return;
        }
        //remove double commas
        //temp.replace(",,", ",");
        //String[] nums = temp.split(",");
        dataFixed = Arrays.toString(nums);//temp;
        double[] newNums = new double[nums.length];
        for (int i = 0; i < newNums.length; i++) {
            try {
                newNums[i] = Double.parseDouble(nums[i]);
            } catch (NumberFormatException nfe) {
            }
            ;
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
            ans = (double)Math.round(ans * 100000) / 100000;
            final String ansString = Double.toString(ans);
            showDialog(ansString, type, "Mean:");
        } else if (type == 2) { //median
            Arrays.sort(newNums);
            double median = 0;
            if (newNums.length % 2 != 0) {
                median = newNums[newNums.length / 2];
            } else if (newNums.length % 2 == 0) {
                median = (newNums[newNums.length / 2] + newNums[newNums.length / 2 - 1]) / 2;
            } else {

            }
            String medianString = Double.toString(median);
            showDialog(medianString, type, "Median:");
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
                            ;
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
            showDialog(modeString, type, "Mode:");
        }


    }


    @SuppressLint("NewApi")
    public void showDialog(String nums, int type, String typeName) {
        final EditText numString = (EditText) findViewById(R.id.num_input);
        AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
        TextView dataV = (TextView) findViewById(R.id.dataID);
        dataV.setText(dataFixed);

        typeV.setText(typeName);
        TextView ansV = (TextView) findViewById(R.id.answer);
        ansV.setText(nums);

        //perform options
        if (acBox.isChecked()) {
            numString.setText("");
            edit.putString(dataKEY, "").commit();
        }
        //check if copy
        if (myCBox.isChecked()) {
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
        //setup alert dialog
        alert1.setTitle(R.string.dialog_title)
                .setMessage(("\tDATA: \n") + dataFixed + "   \n(" + dividend + " numbers)\n\n\t" + typeName + " \n" + nums)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = alert1.create();
        //dialog.show();

    }


}
