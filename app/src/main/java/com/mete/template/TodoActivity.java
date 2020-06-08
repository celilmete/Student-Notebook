package com.mete.template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mete.template.DTO.ToDo;

import java.util.ArrayList;

public class TodoActivity extends AppCompatActivity {
    DBhandler dBhandler;
    TodoActivity activity;
    RecyclerView recyclerView;
    FloatingActionButton add_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        add_task = findViewById(R.id.add_task);
        dBhandler = new DBhandler(activity);
        activity = this;



        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle("Add To Do");
                View view = getLayoutInflater().inflate(R.layout.diaolog_todo,null);
                final EditText toDoName = findViewById(R.id.dialog_text);
                dialog.setView(view);
                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!toDoName.getText().toString().isEmpty()) {
                            ToDo toDo = new ToDo();
                            toDo.setName(toDoName.getText().toString());
                            dBhandler.addTodo(toDo);
                            refreshList();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });


    }

    @Override
    protected void onResume() {
        refreshList();
        super.onResume();
    }

    public void refreshList() {
        recyclerView.setAdapter(new TodoAdapter(activity,dBhandler.getToDos()));
    }

    class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>{
        ArrayList<ToDo> list;
        TodoActivity activity;

        public TodoAdapter(TodoActivity activity, ArrayList<ToDo> list) {
            this.list = list;
            this.activity = activity;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_todo,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            holder.toDoName.setText( list.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView toDoName;
            //ImageView menu;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                toDoName = itemView.findViewById(R.id.todo_name);
                // menu = itemView.findViewById(R.id.iv_menu);
            }
        }
    }


}
