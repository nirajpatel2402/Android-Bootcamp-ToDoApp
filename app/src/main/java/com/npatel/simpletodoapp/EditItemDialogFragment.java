package com.npatel.simpletodoapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditItemDialogFragment extends DialogFragment{

    EditText etTodoName, etDueDate;
    Button bSave;
    Calendar calendar;
    int year, month, day, position, id;
    DbHelper dbHelper;
    Spinner pList;
    ArrayAdapter<String> dataAdapter;

    public interface EditToDoListener {
        void onFinishEditDialog(int position, int id, String todoName, String dueDate, String priority);
    }


    public EditItemDialogFragment() {

    }

    public static EditItemDialogFragment newInstance(String title, int position, int id, String todoName, String dueDate, String priority) {
        EditItemDialogFragment frag = new EditItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("todoName", todoName);
        args.putInt("id", id);
        args.putInt("position", position);
        args.putString("dueDate", dueDate);
        args.putString("priority", priority);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTodoName = (EditText) view.findViewById(R.id.editTodo);
        bSave = (Button) view.findViewById(R.id.bEdit);
        etDueDate = (EditText) view.findViewById(R.id.editDueDate);
        dbHelper = new DbHelper(getActivity());
        pList = (Spinner) view.findViewById(R.id.priority);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        List<String> categories = new ArrayList<>();
        categories.add("High");
        categories.add("Medium");
        categories.add("Low");

        String todoTitle = getArguments().getString("title", "Undefined");
        String todoName = getArguments().getString("todoName", "Undefined");
        String priority = getArguments().getString("priority", "Undefined");
        String dueDate = getArguments().getString("dueDate", "Undefined");
        getDialog().setTitle(todoTitle);
        etDueDate.setText(dueDate);
        id = getArguments().getInt("id");
        position = getArguments().getInt("position");
        dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
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
        etTodoName.setText(todoName);
        etDueDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    DatePickerDialog dp = new DatePickerDialog(getActivity(), myDateListener, year, month, day);
                    dp.show();
                }
                return true;
            }
        });
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditToDoListener listener = (EditToDoListener) getActivity();
                dbHelper.updateRecord(id, etTodoName.getText().toString(), etDueDate.getText().toString(),String.valueOf(pList.getSelectedItemPosition()));
                listener.onFinishEditDialog(position, id, etTodoName.getText().toString(), etDueDate.getText().toString(), String.valueOf(pList.getSelectedItemPosition()));
                dismiss();
            }
        });
        pList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }
    public DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate(arg1, arg2+1, arg3);
        }
    };
    private void showDate(int year, int month, int day) {
        etDueDate.setText(new StringBuilder().append(month).append("/").append(day).append("/").append(year));
    }
}
