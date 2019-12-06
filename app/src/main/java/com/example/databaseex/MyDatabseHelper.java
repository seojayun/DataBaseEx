package com.example.databaseex;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDatabseHelper extends SQLiteOpenHelper {

    // @Nullable annotation : Null로 넣을수 있다.
    // public MyDatabseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
    // public MyDatabseHelper(화면                     , Database name        , 외부로부터 받아올 경우????                    , 버전       )
    // public MyDatabseHelper(Context context)  // 부모로부터 Context(장소) 만 받아서 사용하려고한다.
    public MyDatabseHelper(Context context) {
        //Database를 생성자를 통해 생성한다.
        //클래스를 생성하면서 DB를 생성하도록 되어있다.
        super(context, "groupDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Table을 생성한다.
        db.execSQL("CREATE TABLE groupTBL(groupname TEXT PRIMARY KEY, numofmembers INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Table 값을 제거한다.
        // IF EXISTS 존재여부를 보고 삭제를 실행한다.
        db.execSQL("DROP TABLE IF EXISTS groupTBL");
        //Table을 다시 생성하기 위해 onCreate()매서드를 호출한다.
        onCreate(db);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }
}
