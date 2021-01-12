package com.pramila.StudentProjectScheduler;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class status extends AppCompatActivity {
    DatabaseHelper myDB;
    private ListView taskListView;
  //  private ListView subjectListView;
    private static final String TAG = "status";
    int taskCnt = 0;
    int taskCntO = 0;

    String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        TextView overDue = findViewById(R.id.overdue_count);
        TextView notOverdue = findViewById(R.id.task_count);

        taskListView = findViewById(R.id.task_list);
      //  subjectListView = findViewById(R.id.subj_list);
        myDB = new DatabaseHelper(this);


        populateTaskList();
        overDue.setText(Integer.toString(taskCntO));
        notOverdue.setText(Integer.toString(taskCnt));


    }


    public void populateTaskList() {
        Log.d(TAG, "populateTaskList: Displaying Tasks in the ListView.");
        Cursor tasks = myDB.getAllData2();
        Cursor subjects = myDB.getAllData();
        ArrayList<String> listTasks = new ArrayList<>();
        ArrayList<String> listSubjects = new ArrayList<>();

        if (tasks.getCount() == 0) {
            return;
        }else{
        while (tasks.moveToNext()) {
            Date strDate;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            try {
                strDate = sdf.parse(tasks.getString(3));
                if (new Date().after(strDate)) {
                    taskCntO += 1;
                    flag = "Overdue";

                    //myDB.checkstatus(flag);
                } else {
                    taskCnt += 1;
                    flag = "Upcoming";

                    //  myDB.checkstatus(flag);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            listTasks.add(tasks.getString(2) + "\n" + flag);
        }

//        while(subjects.moveToNext()){
//            listSubjects.add(subjects.getString(2));
//        }

        }

       // ListAdapter adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, listSubjects);
      //  subjectListView.setAdapter(adapter1);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, listTasks);
        taskListView.setAdapter(adapter);

        Button myButton = findViewById(R.id.add_task_done1);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(status.this, SubjectListActivity.class));
            }


        });
    }


    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



}
