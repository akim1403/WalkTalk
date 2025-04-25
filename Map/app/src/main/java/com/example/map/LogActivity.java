package com.example.map;

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

public class LogActivity extends AppCompatActivity {
    EditText name, pwd;
    Button btnlogin, btnreg;
    Mysql mysql;
    SQLiteDatabase db;
    SharedPreferences sp1, sp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
        name = this.findViewById(R.id.name);            //User name input box
        pwd = this.findViewById(R.id.pwd);              //Password input box
        btnlogin = this.findViewById(R.id.login);         //Login button
        btnreg = this.findViewById(R.id.reg);               //Registration button
        sp1 = this.getSharedPreferences("useinfo", this.MODE_PRIVATE);
        sp2 = this.getSharedPreferences("username", this.MODE_PRIVATE);

        name.setText(sp1.getString("usname", null));
        pwd.setText(sp1.getString("uspwd", null));
        mysql = new Mysql(this, "Userinfo", null, 1);      //Create or retrieve a database
        db = mysql.getReadableDatabase();
        btnlogin.setOnClickListener(new View.OnClickListener() {                //Logon events
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                String password = pwd.getText().toString();                 //Obtain the username and password entered by the user
                //Query data with the same username and password
                Cursor cursor = db.query("logins", new String[]{"usname", "uspwd"}, " usname=? and uspwd=?", new String[]{username, password}, null, null, null);

                int flag = cursor.getCount();                            //The number of record items found, if there is no such user, it is 0
                if (flag != 0) {                                            //If the queried record is not 0, perform a jump operation
                    Intent intent = new Intent();
                    intent.setClass(LogActivity.this, Welcome.class);
                    //Set Page Jump
                    SharedPreferences.Editor editor = sp2.edit();
                    cursor.moveToFirst();                                   //Move the cursor to a position of 0, with a default position of -1
                    String loginname = cursor.getString(0);
                    editor.putString("Loginname", loginname);
                    editor.commit();                                        //Save username in SharedPreferences
                    startActivity(intent);
                } else {
                    //Prompt for incorrect user information or lack of account
                    Toast.makeText(LogActivity.this, "Incorrect username or password！", Toast.LENGTH_LONG).show();
                }

            }
        });
        btnreg.setOnClickListener(new View.OnClickListener() {                  //Register Event
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(LogActivity.this, Register.class);          //Jump to the registration page
                startActivity(intent);
                Toast.makeText(LogActivity.this, "Go to register!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
