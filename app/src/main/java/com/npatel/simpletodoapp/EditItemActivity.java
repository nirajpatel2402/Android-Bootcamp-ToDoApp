package com.npatel.simpletodoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etTodo;
    Button bSave;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etTodo = (EditText) findViewById(R.id.editTodo);
        bSave = (Button) findViewById(R.id.bEdit);
        bSave.setOnClickListener(this);
        String todoText = getIntent().getStringExtra("todoText");
        position = getIntent().getIntExtra("position", 0);

        etTodo.setText(todoText);
    }

    @Override
    public void onClick(View v) {

        Intent i = new Intent();
        i.putExtra("todoText", etTodo.getText().toString());
        i.putExtra("position", position );
        setResult(200, i);
        finish();
    }
}
