package com.mete.template;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mete.template.DTO.ToDo;
import com.mete.template.DTO.ToDoItem;

import java.util.ArrayList;


//const values



public class DBhandler extends SQLiteOpenHelper {

    public static final String DB_NAME = "ToDoList";
    public static final int DB_VERSION =  1;
    public static final String TABLE_TODO = "ToDo";
    public static final String COL_ID = "Id";
    public static final String COL_NAME = "Name";
    public static final String COL_DUE_DATE = "DueDate";

    public static final String TABLE_TODO_ITEM = "ToDoItem";
    public static final String COL_TODO_ID = "ToDoId";
    public static final String COL_ITEM_NAME = "ItemName";
    public static final String COL_IS_COMPLETED = "IsCompleted";

    public DBhandler(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String crateToDoTable = "CREATE TABLE " + TABLE_TODO + " (" +
                COL_ID + " integer PRIMARY KEY AUTOINCREMENT," +
                COL_DUE_DATE + " datetime," +
                COL_NAME + " varchar)";

        String crateToDoItemTable = "CREATE TABLE " + TABLE_TODO_ITEM + " (" +
                COL_ID + " integer PRIMARY KEY AUTOINCREMENT," +
                COL_TODO_ID + " integer," +
                COL_ITEM_NAME + " varchar," +
                COL_IS_COMPLETED + " integer)";

        db.execSQL(crateToDoTable);
        db.execSQL(crateToDoItemTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



    }

    public boolean addTodo(ToDo toDo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME , toDo.getName());
        long result = db.insert(TABLE_TODO,null,cv);
        return result != -1L;
    }

    public ArrayList<ToDo> getToDos() {
        ArrayList<ToDo> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * from " + TABLE_TODO, null);
        if (queryResult.moveToFirst()) {
            do {
                ToDo toDo = new ToDo();
                toDo.setId(queryResult.getLong(queryResult.getColumnIndex(COL_ID)));
                toDo.setName(queryResult.getString(queryResult.getColumnIndex(COL_NAME)));
                result.add(toDo);
            }while (queryResult.moveToNext());

        }
        queryResult.close();
        return result;
    }
}
