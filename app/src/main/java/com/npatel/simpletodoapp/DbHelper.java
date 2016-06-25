package com.npatel.simpletodoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Niraj on 6/23/2016.
 */
public class DbHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "myTodoDb";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_TODOS = "toDo";
    public static final String TODO_ID = "id";
    public static final String TODO_DUE_DATE = "due_date";
    public static final String TODO_PRIORITY = "priority";
    public static final String TODO_NAME = "name";

    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TODOS_TABLE = "CREATE TABLE "+ TABLE_TODOS + "("+TODO_ID+ " INTEGER PRIMARY KEY,"+TODO_NAME+ " TEXT, "+TODO_DUE_DATE+" TEXT, "+TODO_PRIORITY+" TEXT"+")";
        System.out.println(CREATE_TODOS_TABLE);
        sqLiteDatabase.execSQL(CREATE_TODOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
            onCreate(sqLiteDatabase);
        }
    }

    public Cursor readData() {
        String TODOS_SELECT_QUERY = "SELECT * FROM "+ TABLE_TODOS + " ORDER BY "+TODO_PRIORITY + " ASC";
        SQLiteDatabase myData = this.getReadableDatabase();
        Cursor res = myData.rawQuery(TODOS_SELECT_QUERY, null);
        return res;
    }

    public boolean writeData(String toDo, String dueDate, String priority) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TODO_NAME, toDo);
        cv.put(TODO_DUE_DATE, dueDate);
        cv.put(TODO_PRIORITY, priority);
        db.insert(TABLE_TODOS, null, cv);
        return true;
    }

    public boolean deleteData(int id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete(TABLE_TODOS, "id = ?", new String[] {String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateRecord(int position, String name, String dueDate, String priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.TODO_NAME, name);
        contentValues.put(DbHelper.TODO_DUE_DATE, dueDate);
        contentValues.put(DbHelper.TODO_PRIORITY, priority);

        try{
            db.update(TABLE_TODOS, contentValues, "id = ? ", new String[] { Integer.toString(position) } );
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
