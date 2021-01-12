package com.pramila.StudentProjectScheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubjectAddActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    private EditText editSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_add);

        myDB = new DatabaseHelper(this);

        editSubject = findViewById(R.id.input_subj);
        Button btnAddSubj = findViewById(R.id.subj_done);

        btnAddSubj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newSubject = editSubject.getText().toString();
                if (editSubject.length() != 0){
                    AddData(newSubject);
                    editSubject.setText("");
                    Intent intent = new Intent(SubjectAddActivity.this,SubjectListActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(SubjectAddActivity.this, "Please enter a subject name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void AddData(String subjEntry){
        myDB.insertSubject(subjEntry);
    }


}
