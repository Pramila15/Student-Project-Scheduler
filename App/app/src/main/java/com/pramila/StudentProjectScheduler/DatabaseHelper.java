package com.pramila.StudentProjectScheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // columns of the subject table
    private static final String SUBJECT_TABLE = "subject_table";
    private static final String SUBJECT_ID = "SUBJECT_ID";
    private static final String SUBJECT_NAME = "SUBJECT_NAME";

    // columns of the task table
    private static final String TASK_TABLE = "task_table";
    private static final String TASK_ID = "task_id";
    private static final String TASK_DESC = "task_desc";
    private static final String TASK_DATE = "task_date";
    private static final String TASK_SUBJECT_ID = "subject_id_fk";
//    private static final String FLAG_TASK = "flag_task"
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 4;

    //SQL statement of the subjects table creation
    private static final String SQL_CREATE_TABLE_SUBJECTS = "CREATE TABLE " + SUBJECT_TABLE + " ("
            + SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SUBJECT_NAME + " VARCHAR NOT NULL )";

    //SQL statement of the task table creation
    private static final String SQL_CREATE_TABLE_TASKS = "CREATE TABLE " + TASK_TABLE + " ("
            + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TASK_SUBJECT_ID + " INTEGER, "
            + TASK_DESC + " TEXT, "
            + TASK_DATE + " TEXT, "

            + "FOREIGN KEY(" + TASK_SUBJECT_ID + ") REFERENCES " + SUBJECT_TABLE + " (" + SUBJECT_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_SUBJECTS);
        db.execSQL(SQL_CREATE_TABLE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("drop table if exists " + TASK_TABLE);
            db.execSQL("drop table if exists " + SUBJECT_TABLE);
            onCreate(db);
    }

    public void insertSubject(String subject) {
        SQLiteDatabase db = this.getWritableDatabase(); // just for testing
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUBJECT_NAME,subject);

        Log.d(TAG, "insertSubject: Adding " + subject + " to " + SUBJECT_TABLE);

        long result = db.insert(SUBJECT_TABLE,null,contentValues);
    }

    public boolean insertTask(String dbTaskDate, String dbTaskDesc, int dbSubjID) {
        SQLiteDatabase db = this.getWritableDatabase(); // just for testing
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_SUBJECT_ID,dbSubjID);
        contentValues.put(TASK_DESC,dbTaskDesc);
        contentValues.put(TASK_DATE,dbTaskDate);

        Log.d(TAG, "insertSubject: Adding " + dbTaskDesc + " to " + TASK_TABLE);

        long result = db.insert(TASK_TABLE,null,contentValues);
        db.close();
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + SUBJECT_TABLE;
        return db.rawQuery(query,null);
    }

    public Cursor getAllData2() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TASK_TABLE;
        return db.rawQuery(query,null);
    }
//
//    public Cursor checkstatus()
//    {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//      //  String q1="SELECT *  FROM " + TASK_TABLE +" WHERE " + TASK_DATE + " < '" + date + "'";
//        return db.rawQuery(q1,null);
//    }

    public Cursor getTaskData(int subj_id){

        String sub_id = Integer.toString(subj_id);

        SQLiteDatabase db = this.getWritableDatabase();

        return db.query(TASK_TABLE,
                new String[] {TASK_ID, TASK_DESC, TASK_DATE},
                TASK_SUBJECT_ID + " = '"+sub_id+"'",
                null,
                null,
                null,
                TASK_DATE + " ASC");
    }

    public Cursor getSingleTask(String task_id){

        SQLiteDatabase db = this.getWritableDatabase();

        return db.query(TASK_TABLE,
                new String[] {TASK_DESC, TASK_DATE},
                TASK_ID + " = '"+task_id+"'",
                null,
                null,
                null,
                null);
    }

    // Returns only the ID that matches the subject passed in
    public Cursor getItemID(String subj_name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + SUBJECT_ID  + " FROM " + SUBJECT_TABLE +
                " WHERE " + SUBJECT_NAME + " = '" + subj_name + "'";
        return db.rawQuery(query, null);
    }

    public void deleteTask(String taskID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TASK_TABLE, TASK_ID + " = ?", new String[]{taskID});
    }

    public boolean updateTask(String subj_id, String task_id, String task_desc, String task_date ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_ID,task_id);
        contentValues.put(TASK_DESC,task_desc);
        contentValues.put(TASK_DATE,task_date);
        db.update(TASK_TABLE, contentValues, TASK_ID +" = ? AND " + TASK_SUBJECT_ID+" = ?", new String[] {task_id,subj_id});
        return true;
    }

    public void deleteSubj(String subjID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TASK_TABLE, TASK_SUBJECT_ID + " = ?", new String[]{subjID});
        db.delete(SUBJECT_TABLE, SUBJECT_ID + " = ?", new String[]{subjID});
    }
}
