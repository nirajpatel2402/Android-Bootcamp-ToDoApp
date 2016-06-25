package com.npatel.simpletodoapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.npatel.simpletodoapp.model.ToDoModel;
import java.util.ArrayList;

public class ToDoAdapter extends ArrayAdapter<ToDoModel>{
    public ToDoAdapter(Context context, ArrayList<ToDoModel> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDoModel todo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_list_item, parent, false);
        }

        TextView tvId = (TextView) convertView.findViewById(R.id.tvId);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvTodo);
        TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.priority);

        tvId.setText(String.valueOf(todo.getId()));
        tvName.setText(todo.getName());
        tvDueDate.setText(todo.getDueDate());
        if(todo.getPriority().equals("0")){
            tvDueDate.setTextColor(Color.RED);
            tvPriority.setTextColor(Color.RED);
            tvPriority.setText("High");
        }else if(todo.getPriority().equals("1")){
            tvDueDate.setTextColor(Color.parseColor("#cc6600"));
            tvPriority.setTextColor(Color.parseColor("#cc6600"));
            tvPriority.setText("Medium");
        }else if(todo.getPriority().equals("2")){
            tvDueDate.setTextColor(Color.parseColor("#009933"));
            tvPriority.setTextColor(Color.parseColor("#009933"));
            tvPriority.setText("Low");
        }else{
            tvDueDate.setTextColor(Color.BLACK);
            tvPriority.setTextColor(Color.BLACK);
            tvPriority.setText("Not Prioritized");
        }
        notifyDataSetChanged();
        return convertView;
    }
}
