package com.example.cookdi.upload;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookdi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UploadActivity extends AppCompatActivity {
    EditText textIn,textInStep,stepTime;
    FloatingActionButton buttonAdd, addstepbutton;
    LinearLayout container, containerstep;
    TextView DisplayTime;
    int stephours, stepminutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        textIn = (EditText)findViewById(R.id.textin);
        stepTime = (EditText)findViewById(R.id.steptime);
        buttonAdd = (FloatingActionButton) findViewById(R.id.add);
        container = (LinearLayout)findViewById(R.id.container);
        containerstep = (LinearLayout)findViewById(R.id.containerstep);
        addstepbutton = (FloatingActionButton) findViewById(R.id.addstep);
        textInStep = (EditText)findViewById(R.id.textinstep);

        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.ingredient, null);
                TextView textOut = (TextView)addView.findViewById(R.id.textout);
                textOut.setText(textIn.getText().toString());
                Button buttonRemove = (Button)addView.findViewById(R.id.remove);

                final View.OnClickListener thisListener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout)addView.getParent()).removeView(addView);
                    }
                };

                buttonRemove.setOnClickListener(thisListener);
                container.addView(addView);

            }
        });
        DisplayTime = (EditText) findViewById(R.id.steptime);
        DisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog pickerDialog = new TimePickerDialog(UploadActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        stephours = hourOfDay;
                        stepminutes = minute;
                        DisplayTime.setText(stephours+":"+stepminutes);
                    }
                },24,0,true);
                pickerDialog.updateTime(stephours,stepminutes);
                pickerDialog.show();
            }
        });

        addstepbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.steps, null);
                TextView textOutStep = (TextView)addView.findViewById(R.id.textoutstep);
                textOutStep.setText(textInStep.getText().toString());
                TextView steptime = (TextView)addView.findViewById(R.id.texttime);
                steptime.setText(stepTime.getText().toString());
                Button buttonRemove = (Button)addView.findViewById(R.id.removestep);

                final View.OnClickListener thisListener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout)addView.getParent()).removeView(addView);
                    }
                };

                buttonRemove.setOnClickListener(thisListener);
                containerstep.addView(addView);
            }
        });

        findViewById(R.id.activity_upload).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyBoard();
                return true;
            }
        });
    }

    public void hideKeyBoard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

}
