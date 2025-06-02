package com.example.map;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class personActivity extends AppCompatActivity {
    SharedPreferences sp;
    TextView name;

    TextView ID;

    ImageButton Head;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        sp = this.getSharedPreferences("username", this.MODE_PRIVATE);

        name = this.findViewById(R.id.Name);

        name.setText(sp.getString("Loginname", ""));   //获取用户名

        ID = this.findViewById(R.id.ID);

        ID.setText(sp.getString("Loginname", ""));   //获取用户名

        Head = this.findViewById(R.id.head);

        Head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(personActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });








    }

}
