package com.npatel.simpletodoapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvitems;
    private final int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        lvitems= (ListView) findViewById(R.id.lvTodo);
        items= new ArrayList<>();

        readItems();
        if(items.size()==0){
            items.add("First item");
            items.add("Second item");
        }


        itemsAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        lvitems.setAdapter(itemsAdapter);
        lvitems.setOnItemLongClickListener(this);
        lvitems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("position", position);
                i.putExtra("todoText", items.get(position));
                System.out.println("hello :"+ items.get(position));
                startActivityForResult(i, REQUEST_CODE );
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String todoText = data.getExtras().getString("todoText");
        int position = data.getExtras().getInt("position");
        System.out.println("xyz :"+position);
        items.set(position, todoText);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
        Toast.makeText(getApplicationContext(), "Item edited", Toast.LENGTH_SHORT).show();
    }

    public void onAddItems(View view) {
        EditText editText=(EditText) findViewById(R.id.addText);
        String itemText =editText.getText().toString();
        itemsAdapter.add(itemText);
        editText.setText("");
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        items.remove(position);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
        Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_SHORT).show();
        return true;
    }

    public void readItems(){
        File myDir = getFilesDir();
        File todoFile = new File(myDir, "todo.txt");
        try{
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void writeItems(){
        File myDir = getFilesDir();
        File todoFile = new File(myDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

