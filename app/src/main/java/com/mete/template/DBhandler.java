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

    private static final String DB_NAME = "ToDoList";
    private static final int DB_VERSION =  1;
    private static final String TABLE_TODO = "ToDo";
    private static final String COL_ID = "Id";
    private static final String COL_NAME = "Name";
    private static final String COL_DUE_DATE = "DueDate";

    private static final String TABLE_TODO_ITEM = "ToDoItem";
    private static final String COL_TODO_ID = "ToDoId";
    private static final String COL_ITEM_NAME = "ItemName";
    private static final String COL_IS_COMPLETED = "IsCompleted";



    DBhandler(Context context) {
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

    boolean addTodo(ToDo toDo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME , toDo.getName());
        long result = db.insert(TABLE_TODO,null,cv);
        return result != -1;
    }

    void updateToDo(ToDo todo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, todo.getName());
        db.update(TABLE_TODO, cv, COL_ID + "=?", new String[]{String.valueOf(todo.getId())});
    }

    void updateToDoItemCompletedStatus(Long todoId, Boolean isCompleted) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * FROM " + TABLE_TODO_ITEM + " WHERE " + COL_TODO_ID + "=" + todoId, null);
        if (queryResult.moveToFirst()) {
            do {
                ToDoItem item = new ToDoItem();
                item.setId(queryResult.getLong(queryResult.getColumnIndex(COL_ID)));
                item.setToDoId(queryResult.getLong(queryResult.getColumnIndex(COL_TODO_ID)));
                item.setItemName(queryResult.getString(queryResult.getColumnIndex(COL_ITEM_NAME)));
                item.setCompleted(isCompleted);
                updateToDoItem(item);
            } while (queryResult.moveToNext());
        }
        queryResult.close();
    }

    public void updateToDoItem(ToDoItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ITEM_NAME, item.getItemName());
        cv.put(COL_TODO_ID, item.getToDoId());
        cv.put(COL_IS_COMPLETED, item.isCompleted());
        db.update(TABLE_TODO_ITEM, cv, COL_ID + "=?", new String[]{String.valueOf(item.getId())});
    }

    void deleteToDo(Long todoId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TODO_ITEM, COL_TODO_ID + "=?", new String[]{String.valueOf(todoId)});
        db.delete(TABLE_TODO, COL_ID + "=?", new String[]{String.valueOf(todoId)});
    }

    ArrayList<ToDo> getToDos() {
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

    public boolean addToDoItem(ToDoItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ITEM_NAME, item.getItemName());
        cv.put(COL_TODO_ID, item.getToDoId());
        cv.put(COL_IS_COMPLETED, item.isCompleted());

        long result = db.insert(TABLE_TODO_ITEM, null, cv);
        return result != -1;
    }

    public void deleteToDoItem(long itemId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TODO_ITEM, COL_ID + "=?", new String[]{String.valueOf(itemId)});

    }

    public ArrayList<ToDoItem> getToDoItems(long todoId) {
        ArrayList<ToDoItem> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * FROM " + TABLE_TODO_ITEM + " WHERE " + COL_TODO_ID + "=" + todoId, null);
        if (queryResult.moveToFirst()) {
            do {
                ToDoItem item = new ToDoItem();
                item.setId(queryResult.getLong(queryResult.getColumnIndex(COL_ID)));
                item.setToDoId(queryResult.getLong(queryResult.getColumnIndex(COL_TODO_ID)));
                item.setItemName(queryResult.getString(queryResult.getColumnIndex(COL_ITEM_NAME)));
                item.setCompleted(queryResult.getInt(queryResult.getColumnIndex(COL_IS_COMPLETED)) == 1);
                result.add(item);
            } while (queryResult.moveToNext());
        }
        queryResult.close();
        return result;
    }
}
