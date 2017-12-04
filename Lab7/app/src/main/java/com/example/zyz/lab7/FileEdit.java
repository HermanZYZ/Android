package com.example.zyz.lab7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileEdit extends AppCompatActivity {

    private Button Save;
    private Button Load;
    private Button Clear;
    private Button Delete;

    private EditText fileName;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filewindows);

        ElemBinding();
        EvenBinding();
    }

    private void ElemBinding()
    {
        Save = (Button) findViewById(R.id.save);
        Load = (Button) findViewById(R.id.load);
        Clear = (Button) findViewById(R.id.clear);
        Delete = (Button) findViewById(R.id.delete);

        fileName = (EditText) findViewById(R.id.filename);
        editText = (EditText) findViewById(R.id.editText);
    }

    private void EvenBinding()
    {
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        Load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });

        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

    private void save()
    {
        String msg;
        String filename = fileName.getText().toString();
        String context = editText.getText().toString();
        FileOutputStream file = null;
        if(filename.isEmpty())
            msg = "File name can't be empty!";
        else
        {
            try {
                file = openFileOutput(filename, MODE_PRIVATE);
                file.write(context.getBytes());
                msg = "Save file successfully!";
            }catch (IOException e){
                msg = "Failed to save file!";
                e.printStackTrace();
            }finally {
                if (file != null) {
                    try {
                        file.close();// 关闭文件输入流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void load()
    {
        String msg;
        String filename = fileName.getText().toString();
        FileInputStream file = null;
        if(filename.isEmpty())
            msg = "File name can't be empty!";
        else {
            try {
                file = openFileInput(filename);// 获得文件输入流
                byte[] buffer = new byte[file.available()];// 定义保存数据的数组
                file.read(buffer);// 从输入流中读取数据
                String data = new String(buffer);
                editText.setText(data);
                msg = "Load file successfully!";
            } catch (IOException e) {
                msg = "Failed to load file!";
                e.printStackTrace();
            } finally {
                if (file != null) {
                    try {
                        file.close();// 关闭文件输入流
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void delete()
    {
        String msg;
        String filename = fileName.getText().toString();
        if(filename.isEmpty())
            msg = "File name can't be empty!";
        else
        {
            boolean isSuccessed = deleteFile(filename);
            if(isSuccessed)
                msg = "Delete file successfully!";
            else
                msg = "No such file!";
        }
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
