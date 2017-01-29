package com.example.zsss.note_addnote;

import android.app.ListActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.example.zsss.note_addnote.db.NotesDB;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AtyAddNote extends ListActivity {

    private EditText etName;
    private EditText etContent;
    private NotesDB db;
    private SQLiteDatabase dbRead, dbWrite;

    private int noteId = -1;



    public static final String EXTRA_NOTE_ID = "noteId";
    public static final String EXTRA_NOTE_NAME = "noteName";
    public static final String EXTRA_NOTE_CONTENT = "noteContent";



    private OnClickListener btnClickHandler = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                //将数据保存到数据库里
                case R.id.btnSave:
                    saveNote();
                    setResult(RESULT_OK);
                    finish();
                    break;
                case R.id.btnCancel:

                    setResult(RESULT_CANCELED);
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_add_note);
        db = new NotesDB(this);
        dbRead = db.getReadableDatabase();
        dbWrite = db.getWritableDatabase();

        etName = (EditText) findViewById(R.id.etName);
        etContent = (EditText) findViewById(R.id.etContent);


        noteId = getIntent().getIntExtra(EXTRA_NOTE_ID,-1);
        if(noteId>-1){
            etName.setText(getIntent().getStringExtra(EXTRA_NOTE_NAME));
            etContent.setText(getIntent().getStringExtra(EXTRA_NOTE_CONTENT));
        }

        findViewById(R.id.btnSave).setOnClickListener(btnClickHandler);
        findViewById(R.id.btnCancel).setOnClickListener(btnClickHandler);
    }



    public int saveNote() {
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.COLUMN_NAME_NOTE_NAME, etName.getText().toString());
        cv.put(NotesDB.COLUMN_NAME_NOTE_CONTENT, etContent.getText().toString());
        cv.put(NotesDB.COLUMN_NAME_NOTE_DATE, new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss").format(new Date()));


        if ( noteId> -1) {
            dbWrite.update(NotesDB.TABLE_NAME_NOTES, cv, NotesDB.COLUMN_NAME_ID
                    + "=?", new String[] { noteId + "" });
            return noteId;
        } else {
            return (int) dbWrite.insert(NotesDB.TABLE_NAME_NOTES, null, cv);
        }
    }

    @Override
    protected void onDestroy() {
        dbRead.close();
        dbWrite.close();
        super.onDestroy();
    }
}
