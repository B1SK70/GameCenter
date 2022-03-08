package com.example.gamecenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.DataViewHolder> {

    ArrayList<String[]> dataList;

    public ScoresAdapter(ArrayList<String[]> data) {
        this.dataList = data;
    }

    @Override
    public ScoresAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element, null, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScoresAdapter.DataViewHolder holder, int position) {
        holder.assignData(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        TextView user_name;
        TextView user_score;

        public DataViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_score = (TextView) itemView.findViewById(R.id.user_score);

        }

        public void assignData(String[] s) {
            user_name.setText(s[0]);
            user_score.setText(s[1]);
        }
    }
}