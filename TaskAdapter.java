package com.act1.mytodolistapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnTaskSelectedListener {
        void onTaskSelected(Task task);
    }

    private OnTaskSelectedListener selectionListener;

    public void setOnTaskSelectedListener(OnTaskSelectedListener listener) {
        this.selectionListener = listener;
    }

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTitle.setText(task.getTitle());
        holder.tvContent.setText(task.getContent());
        holder.tvDateTime.setText(task.getDateTime());

        // Highlight selected item
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(0xFF333333);
        } else {
            holder.itemView.setBackgroundColor(0xFF1C1C1E);
        }

        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            if (selectedPosition == adapterPosition) {
                selectedPosition = RecyclerView.NO_POSITION;
            } else {
                selectedPosition = adapterPosition;
            }
            notifyDataSetChanged();

            if (selectionListener != null) {
                selectionListener.onTaskSelected(getSelectedTask());
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public Task getSelectedTask() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            return taskList.get(selectedPosition);
        }
        return null;
    }

    public void addTask(Task task) {
        taskList.add(task);
        notifyItemInserted(taskList.size() - 1);
    }

    public void removeSelectedTask() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            taskList.remove(selectedPosition);
            notifyItemRemoved(selectedPosition);
            selectedPosition = RecyclerView.NO_POSITION;
        }
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvDateTime;

        public TaskViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_task_title);
            tvContent = itemView.findViewById(R.id.tv_task_content);
            tvDateTime = itemView.findViewById(R.id.tv_task_datetime);
        }
    }
}
