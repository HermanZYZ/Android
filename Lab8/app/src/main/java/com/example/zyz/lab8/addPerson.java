package com.example.zyz.lab8;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addPerson extends AppCompatActivity {

    private EditText name;
    private EditText birth;
    private EditText gift;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        ElemBinding();
        EvenBinding();
    }

    private void ElemBinding()
    {
        name = (EditText) findViewById(R.id.nameInput);
        birth = (EditText) findViewById(R.id.birthInput);
        gift = (EditText) findViewById(R.id.giftInput);
        add = (Button) findViewById(R.id.add);
    }

    private void EvenBinding()
    {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                String msg;
                if(Name.isEmpty())
                    msg = "Failed to insert!";
                else
                {
                    String Birth = birth.getText().toString();
                    String Gift = gift.getText().toString();
                    Person person = new Person(Name, Birth, Gift);
                    DBHelper dbHelper = new DBHelper(getBaseContext());
                    dbHelper.insert(person);
                    msg = "Insert Successfully!";
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
