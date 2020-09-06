package com.example.dell.expensemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class SQLite_Login extends SQLiteOpenHelper {

    public SQLite_Login(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public boolean isEmpty(String tableName){

        SQLiteDatabase database = this.getReadableDatabase();
        int noOfRows = (int) DatabaseUtils.queryNumEntries(database,tableName);

        if (noOfRows == 0){
            return true;
        }else {
            return false;
        }
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String email, String password){
        SQLiteDatabase database = getWritableDatabase();
        String query = "INSERT INTO LoginDetail VALUES(?, ?)";
        SQLiteStatement sqLiteStatement = database.compileStatement(query);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1,email);
        sqLiteStatement.bindString(2,password);
        sqLiteStatement.execute();

    }

    public void deleteData(String email){
        SQLiteDatabase database = getWritableDatabase();
        SQLiteStatement statement = database.compileStatement("DELETE FROM LoginDetail WHERE email = ?");
        statement.clearBindings();
        statement.bindString(1,email);
        statement.execute();
    }

    public Cursor getData(String query){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(query,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
