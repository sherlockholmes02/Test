package com.dave.test;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton addNote;
    private RelativeLayout parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parent = findViewById(R.id.parent);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addNote = findViewById(R.id.addNote);

        Intent intent = getIntent();

        if(intent.getBooleanExtra("isSaved", false)) {
            Util.snackToast(parent, "Note Saved!", Util.SNACK_TOAST_SUCCESS);
        }else if(intent.getBooleanExtra("isDeleted", false)){
            Util.snackToast(parent, "Note Deleted!", Util.SNACK_TOAST_SUCCESS);
        }

        getNotes();
        sendToAdd();
    }

    private void sendToAdd(){
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getNotes(){

        class GetANotes extends AsyncTask<Void, Void, List<Note>>{

            @Override
            protected List<Note> doInBackground(Void... voids) {
                List<Note> notelist = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().noteDao().getAll();
                return notelist;
            }

            @Override
            protected void onPostExecute(List<Note> noteList) {
                super.onPostExecute(noteList);

                NoteAdapter adapter = new NoteAdapter(MainActivity.this, noteList);
                recyclerView.setAdapter(adapter);

            }
        }

        GetANotes gn = new GetANotes();
        gn.execute();


    }




}
