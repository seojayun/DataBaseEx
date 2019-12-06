package com.example.databaseex;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edtGroupName, edtNumofMember;
    Button btnInit, btnInsert, btnUpdate, btnDelete, btnSearch;
    TextView tvGroupNameResult, tvNumofMemberResult;

    MyDatabseHelper myDB;  //DB 생성, 테이블 생성을 담당할 객체 선언
    SQLiteDatabase sqlDB; // 4대 쿼리(Insert, Select, Update, Delete)등 모든 SQL 명령 수행할 객체 변수 선언
    String myTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtGroupName = (EditText) findViewById(R.id.edtGroupName);
        edtNumofMember = (EditText) findViewById(R.id.edtNumofMember);
        btnInit = (Button) findViewById(R.id.btnInit);
        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        tvGroupNameResult = (TextView) findViewById(R.id.tvGroupName);
        tvNumofMemberResult = (TextView) findViewById(R.id.tvNumofMember);

        myDB = new MyDatabseHelper(this);
        myTable = "groupTBL";

        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Database 가져오기
                sqlDB = myDB.getWritableDatabase();
                //onUpgrade 매서드 호출해서 초기화
                myDB.onUpgrade(sqlDB, 1, 2);
                sqlDB.close();

                Toast.makeText(getApplicationContext(), "자료가 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                btnSearch.callOnClick();
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myDB.getWritableDatabase();

                String myFieldValue1 = "'" + edtGroupName.getText().toString() + "'";
                String myFieldValue2 = edtNumofMember.getText().toString();
                if (myFieldValue2 == null || myFieldValue2.equals("")) {
                    myFieldValue2 = "0";
                }
                String sqlInsert = "INSERT INTO " + myTable + " VALUES(" + myFieldValue1 + "," + myFieldValue2 + ");";
                Log.d("SQL INSERT Statement : ", sqlInsert);
                sqlDB.execSQL(sqlInsert);

                sqlDB.close();
                edtGroupName.setText("");
                edtNumofMember.setText("");
                Toast.makeText(getApplicationContext(), "그룹명 : " + myFieldValue1 + ", " + myFieldValue2 + "명 이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                btnSearch.callOnClick();

//                productTBL 사용시 예제
//                myTable = "productTBL";
//                myField1 = "'" + edt01.getText().toString() + "'";   // productCode text  ex> 'SS3455'
//                myField2 = "'" + edt02.getText().toString() + "'";   // productName text  ex> '냉장고'
//                myField3 = edt03.getText().toString();               // amount integer    ex> 20
//                myField4 = edt04.getText().toString();               // price integer     ex> 2500000
//                myField5 = "'" + edt05.getText().toString() + "'";   // maker text        ex> '삼성'
//                myField6 = "'" + edt06.getText().toString() + "'";   // etc text          ex> '200리터'
//
//                sqlInsert = "INSERT INTO " + myTable + " VALUES(" + myField1 + "," + myField2 + "," + myField3+ "," + myField4+ "," + myField5+ "," + myField6+ ");";
//                sqlDB.execSQL(sqlInsert);

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myDB.getWritableDatabase();
                String myFieldValue1 = "'" + edtGroupName.getText().toString() + "'";
                String myFieldValue2 = edtNumofMember.getText().toString();
                String sqlUpdate = "UPDATE " + myTable + " SET groupname = " + myFieldValue1 + ", numofmembers = " + myFieldValue2 + " WHERE groupname = " + myFieldValue1 + ";";
                Log.d("SQL UPDATE Statement : ", sqlUpdate);
                sqlDB.execSQL(sqlUpdate);
                edtGroupName.setText("");
                edtNumofMember.setText("");
                Toast.makeText(getApplicationContext(), "그룹명 : " + myFieldValue1 + "의 인원이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                sqlDB.close();

                btnSearch.callOnClick();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myDB.getWritableDatabase();
                String myFieldValue1 = "'" + edtGroupName.getText().toString() + "'";
                String myFieldValue2 = edtNumofMember.getText().toString();
                String sqlDelete = "DELETE FROM " + myTable + " WHERE groupname = " + myFieldValue1 + ";";
                Log.d("SQL DELETE Statement : ", sqlDelete);
                sqlDB.execSQL(sqlDelete);
                edtGroupName.setText("");
                edtNumofMember.setText("");
                Toast.makeText(getApplicationContext(), "그룹명 : " + myFieldValue1 + "이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                sqlDB.close();

                btnSearch.callOnClick();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDB = myDB.getReadableDatabase();
                Cursor cursor;
                String sqlSelecct;
                String myFieldValue1 = edtGroupName.getText().toString();
                String myFieldValue2 = edtNumofMember.getText().toString();

                if (myFieldValue1.length() == 1 && myFieldValue2.length() == 0) {
                    sqlSelecct = "SELECT * FROM " + myTable + " WHERE groupname Like '" + myFieldValue1 + "%';";
                } else if (myFieldValue1.length() == 0 && myFieldValue2.length() > 0) {
                    sqlSelecct = "SELECT * FROM " + myTable + " WHERE numofmembers = " + myFieldValue2 + ";";
                } else {
                    sqlSelecct = "SELECT * FROM " + myTable + ";";
                }

                Log.d("SQL SELECT Statement : ", sqlSelecct);
                cursor = sqlDB.rawQuery(sqlSelecct, null);
                String strNames = "그룹 이름\n---------------\n";
                String strMembers = "인원\n---------------\n";

                while (cursor.moveToNext()) {
                    //첫번째 필드의 값 columnIndex = 0
                    strNames += cursor.getString(0) + "\n";
                    //두번째 필드의 값 columnIndex = 1
                    strMembers += cursor.getString(1) + "\n";
                }


                tvGroupNameResult.setText(strNames);
                tvNumofMemberResult.setText(strMembers);
                cursor.close();
                sqlDB.close();
            }
        });
    }
}
