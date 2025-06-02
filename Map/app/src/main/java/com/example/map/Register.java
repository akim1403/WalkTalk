package com.example.map;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    EditText usename, usepwd, usepwd2;
    Button submit;
    Mysql mysql;
    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usename = this.findViewById(R.id.usename);                //User name edit box
        usepwd = this.findViewById(R.id.usepwd);                //Set initial password editing box
        usepwd2 = this.findViewById(R.id.usepwd2);                //Second input password edit box
        submit = this.findViewById(R.id.submit);                //Registration button
        mysql = new Mysql(this, "Userinfo", null, 1);      //database construction
        db = mysql.getReadableDatabase();
        sp = this.getSharedPreferences("useinfo", this.MODE_PRIVATE);


        submit.setOnClickListener(new View.OnClickListener() {
            boolean flag = true;            //Flag bit to determine if the user already exists

            @Override
            public void onClick(View v) {
                String name = usename.getText().toString();                //user name
                String pwd01 = usepwd.getText().toString();                //passward
                String pwd02 = usepwd2.getText().toString();            //Second input password
                String sex = "";                                        //gender
                if (name.equals("") || pwd01.equals("") || pwd02.equals("")) {
                    Toast.makeText(Register.this, "用户名或密码不能为空!！", Toast.LENGTH_LONG).show();
                } else {
                    Cursor cursor = db.query("logins", new String[]{"usname"}, null, null, null, null, null);

                    while (cursor.moveToNext()) {
                        if (cursor.getString(0).equals(name)) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag == true) {                                             //Determine if the user already exists
                        if (pwd01.equals(pwd02)) {
                            //Determine whether the passwords entered twice are consistent. If they are consistent, continue,
                            // and if they are inconsistent, prompt for inconsistent passwords
                            ContentValues cv = new ContentValues();
                            cv.put("usname", name);
                            cv.put("uspwd", pwd01);
                            db.insert("logins", null, cv);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("usname", name);
                            editor.putString("uspwd", pwd01);
                            editor.commit();
                            Intent intent = new Intent();
                            intent.setClass(Register.this, LogActivity.class);      //Jump to login page
                            startActivity(intent);
                            Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Register.this, "Password inconsistency！", Toast.LENGTH_LONG).show();            //Password inconsistency
                        }
                    } else {
                        Toast.makeText(Register.this, "User already exists!", Toast.LENGTH_LONG).show();            //Prompt for inconsistent passwords
                    }

                }
            }


        });
    }
}
