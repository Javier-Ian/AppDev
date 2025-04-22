package com.example.flextrack_ianation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flextrack_ianation.R;
import com.example.flextrack_ianation.models.WorkoutSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WorkoutSessionAdapter extends RecyclerView.Adapter<WorkoutSessionAdapter.WorkoutSessionViewHolder> {

    private final List<WorkoutSession> sessions;
    private final Context context;
    private final OnSessionClickListener listener;

    public interface OnSessionClickListener {
        void onSessionClick(WorkoutSession session, int position);
    }

    public WorkoutSessionAdapter(Context context, List<WorkoutSession> sessions, OnSessionClickListener listener) {
        this.context = context;
        this.sessions = sessions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutSessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_workout_session, parent, false);
        return new WorkoutSessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutSessionViewHolder holder, int position) {
        WorkoutSession session = sessions.get(position);
        holder.bind(session, position);
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public class WorkoutSessionViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivWorkoutTypeIcon;
        private final TextView tvWorkoutName;
        private final TextView tvWorkoutType;
        private final TextView tvWorkoutTime;
        private final TextView tvDuration;
        private final TextView tvCalories;
        private final TextView tvExerciseCount;
        private final ProgressBar progressWorkout;
        private final TextView tvProgressPercentage;

        public WorkoutSessionViewHolder(@NonNull View itemView) {
            super(itemView);
            ivWorkoutTypeIcon = itemView.findViewById(R.id.ivWorkoutTypeIcon);
            tvWorkoutName = itemView.findViewById(R.id.tvWorkoutName);
            tvWorkoutType = itemView.findViewById(R.id.tvWorkoutType);
            tvWorkoutTime = itemView.findViewById(R.id.tvWorkoutTime);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvCalories = itemView.findViewById(R.id.tvCalories);
            tvExerciseCount = itemView.findViewById(R.id.tvExerciseCount);
            progressWorkout = itemView.findViewById(R.id.progressWorkout);
            tvProgressPercentage = itemView.findViewById(R.id.tvProgressPercentage);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onSessionClick(sessions.get(position), position);
                }
            });
        }

        public void bind(WorkoutSession session, int position) {
            // Set workout name
            tvWorkoutName.setText(session.getWorkoutName());
            
            // Set workout type
            tvWorkoutType.setText(session.getWorkoutType());
            
            // Set workout time
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, h:mm a", Locale.getDefault());
            String formattedTime = sdf.format(new Date(session.getStartTimeMillis()));
            tvWorkoutTime.setText(formattedTime);
            
            // Set workout duration
            String duration = formatDuration(session.getDurationMinutes());
            tvDuration.setText(duration);
            
            // Set calories
            String calories = session.getCaloriesBurned() + " cal";
            tvCalories.setText(calories);
            
            // Set exercise count
            int exerciseCount = session.getExercises().size();
            String exerciseText = exerciseCount + " " + (exerciseCount == 1 ? "exercise" : "exercises");
            tvExerciseCount.setText(exerciseText);
            
            // Set progress
            int completionPercentage = session.getCompletionPercentage();
            progressWorkout.setProgress(completionPercentage);
            tvProgressPercentage.setText(completionPercentage + "% Complete");
            
            // Set icon based on workout type
            setWorkoutTypeIcon(session.getWorkoutType());
        }
        
        private void setWorkoutTypeIcon(String workoutType) {
            // Default icon
            int iconResource = R.drawable.ic_fitness;
            
            // Set icon based on workout type
            if (workoutType != null) {
                if (workoutType.toLowerCase().contains("strength") || workoutType.toLowerCase().contains("muscle")) {
                    iconResource = R.drawable.ic_strength;
                } else if (workoutType.toLowerCase().contains("cardio") || workoutType.toLowerCase().contains("endurance")) {
                    iconResource = R.drawable.ic_cardio;
                } else if (workoutType.toLowerCase().contains("yoga") || workoutType.toLowerCase().contains("flexibility")) {
                    iconResource = R.drawable.ic_yoga;
                }
            }
            
            ivWorkoutTypeIcon.setImageResource(iconResource);
        }
        
        private String formatDuration(int minutes) {
            if (minutes < 60) {
                return minutes + " min";
            } else {
                long hours = TimeUnit.MINUTES.toHours(minutes);
                long remainingMinutes = minutes - TimeUnit.HOURS.toMinutes(hours);
                return hours + "h " + remainingMinutes + "m";
            }
        }
    }
    
    public void updateSessions(List<WorkoutSession> newSessions) {
        this.sessions.clear();
        this.sessions.addAll(newSessions);
        notifyDataSetChanged();
    }
} 