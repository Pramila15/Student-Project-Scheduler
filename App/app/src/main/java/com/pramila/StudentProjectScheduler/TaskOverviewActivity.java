package com.pramila.StudentProjectScheduler;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskOverviewActivity extends AppCompatActivity {

    DatabaseHelper myDB;

    private String selectedSubject;
    private int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_overview);
        Toolbar toolbar = (Toolbar)findViewById(R.id.edit_task_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView subjectTitle = findViewById(R.id.overview_subj);
        Button btnAddTask = findViewById(R.id.add_task_done);
        TextView overDue = findViewById(R.id.overdue_count);
        TextView notOverdue = findViewById(R.id.task_count);
        Button deleteSubj = findViewById(R.id.delete_subj);
        myDB = new DatabaseHelper(this);
        int taskCnt = 0;
        int taskCntO = 0;

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTask = new Intent(TaskOverviewActivity.this,NewTaskActivity.class);
                newTask.putExtra("id",selectedID);
                newTask.putExtra("name",selectedSubject);
                startActivity(newTask);
            }
        });

        deleteSubj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.deleteSubj(Integer.toString(selectedID));
                Intent afterSubjDel = new Intent(TaskOverviewActivity.this,SubjectListActivity.class);
                startActivity(afterSubjDel);
            }
        });

        // get the intent extra from the SubjectListActivity
        Intent receivedIntent = getIntent();
        //now get the subjectID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1);
        //now get the name we passed as an extra
        selectedSubject = receivedIntent.getStringExtra("name");

        //set the text to show the current selected subject
      subjectTitle.setText(selectedSubject);
        ListView taskListView = (ListView) findViewById(R.id.task_list);
        List<Task> mProductList = new ArrayList<>();
        Cursor tasks = myDB.getTaskData(selectedID);




        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        if (tasks.getCount() == 0){
            return;
        } else {
            while (tasks.moveToNext()) {
                // Convert tasks.getString(2) to format DD-MM-YYYY (from YYYY-MM-DD HH:MM:SS.SSS)
                String dbDate = tasks.getString(2);
                String dbDay = dbDate.substring(8,10);
                String dbMonth = dbDate.substring(5,7);
                String dbYear = dbDate.substring(0,4);
                String dbTime = dbDate.substring(11,16);
                String convertedDate = dbDay + "-" + dbMonth + "-" + dbYear + "    " + dbTime;
                mProductList.add(new Task(tasks.getInt(0), tasks.getString(1), convertedDate));
                Date strDate;
                Integer flag =0;
                try {
                    strDate = sdf.parse(tasks.getString(2));
                    if (new Date().after(strDate)){
                        taskCntO += 1;
                        flag=99;
                        //myDB.checkstatus(flag);
                    } else {
                        taskCnt +=1;
                        flag=100;
                      //  myDB.checkstatus(flag);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }

            overDue.setText(Integer.toString(taskCntO));
            notOverdue.setText(Integer.toString(taskCnt));

        }

        TaskListAdapter adapter = new TaskListAdapter(getApplicationContext(), mProductList);
        taskListView.setAdapter(adapter);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent editTask = new Intent(TaskOverviewActivity.this,EditTaskActivity.class);
                Object taskID = view.getTag();
                editTask.putExtra("taskID",taskID.toString());
                editTask.putExtra("subID",selectedID);
                editTask.putExtra("name",selectedSubject);
                startActivity(editTask);
                Toast.makeText(getApplicationContext(), "Task ID = " + view.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Comparator for Descending Date
}
