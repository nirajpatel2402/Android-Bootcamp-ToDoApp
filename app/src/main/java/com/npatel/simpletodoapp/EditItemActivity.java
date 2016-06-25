package com.npatel.simpletodoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText etTodo, etDueDate;
    Button bSave;
    int position;
    private DbHelper dbHelper;
    private Calendar calendar;
    private int year, month, day;
    Spinner pList;
    ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        dbHelper = new DbHelper(this);
        etTodo = (EditText) findViewById(R.id.editTodo);
        etDueDate = (EditText) findViewById(R.id.editDueDate);
        pList = (Spinner) findViewById(R.id.priority);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        bSave = (Button) findViewById(R.id.bEdit);
        bSave.setOnClickListener(this);
        pList.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<>();
        categories.add("High");
        categories.add("Medium");
        categories.add("Low");
        String priority = getIntent().getStringExtra("priority");
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pList.setAdapter(dataAdapter);
        if(priority.equals("0")){
            pList.setSelection(0);
        }else if(priority.equals("1")){
            pList.setSelection(1);
        }else if(priority.equals("2")){
            pList.setSelection(2);
        }else {
            pList.setSelection(2);
        }
        dataAdapter.notifyDataSetChanged();
        String todoText = getIntent().getStringExtra("todoText");
        String dueDate = getIntent().getStringExtra("dueDate");

        position = getIntent().getIntExtra("position", 0);
        etTodo.setText(todoText);
        etDueDate.setText(dueDate);
        etDueDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showDialog(1);
                return true;
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        etDueDate.setText(new StringBuilder().append(month).append("/").append(day).append("/").append(year));
    }
    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        String p;
        dbHelper.updateRecord(position, etTodo.getText().toString(), etDueDate.getText().toString(),String.valueOf(pList.getSelectedItemPosition()));
        setResult(200, i);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
