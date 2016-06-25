package com.npatel.simpletodoapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.npatel.simpletodoapp.model.ToDoModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    ArrayList<ToDoModel> items = new ArrayList<ToDoModel>();
    ToDoAdapter itemsAdapter;
    private DbHelper dbHelper;

    ListView lvitems;
    private final int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        lvitems= (ListView) findViewById(R.id.lvTodo);

        dbHelper = new DbHelper(this);

        readItems();
        itemsAdapter = new ToDoAdapter(this, items);
        lvitems.setAdapter(itemsAdapter);
        lvitems.invalidate();

        lvitems.setOnItemLongClickListener(this);
        lvitems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("position", items.get(position).getId());
                i.putExtra("todoText", items.get(position).getName());
                i.putExtra("dueDate", items.get(position).getDueDate());
                i.putExtra("priority", items.get(position).getPriority());
                startActivityForResult(i, REQUEST_CODE );
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        readItems();
        itemsAdapter.notifyDataSetChanged();
    }

    public void onAddItems(View view) {
        EditText editText=(EditText) findViewById(R.id.addText);
        String itemText =editText.getText().toString();
        writeItems(itemText, "Due date not set", "3");
        readItems();
        editText.setText("");
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        deleteItem(items.get(position).getId());
        itemsAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void deleteItem(int id) {
        dbHelper.deleteData(id);
        readItems();
    }

    public void readItems(){
        items.clear();
        Cursor rs = dbHelper.readData();
        try{
            if(rs.moveToFirst()){
                do{
                    ToDoModel td= new ToDoModel(rs.getInt(rs.getColumnIndex(DbHelper.TODO_ID)),rs.getString(rs.getColumnIndex(DbHelper.TODO_NAME)), rs.getString(rs.getColumnIndex(DbHelper.TODO_DUE_DATE)),rs.getString(rs.getColumnIndex(DbHelper.TODO_PRIORITY)));
                    items.add(td);
                }while(rs.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        }

//        if(items.size()==0){
//            writeItems("write First item");
//            writeItems("write Second item");
//            readItems();
//        }

    }
    public void writeItems(String toDo, String dueDate, String priority){
        dbHelper.writeData(toDo, dueDate, priority);
    }
}

