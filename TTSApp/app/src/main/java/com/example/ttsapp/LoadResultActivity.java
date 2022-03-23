package com.example.ttsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadResultActivity extends AppCompatActivity {

    // Toast messages
    private static final String DATA_PARSE_ERROR = "Error: unable to parse student data passed to activity";

    // ArrayList to store list entries
    private ArrayList<ListEntry> listData;

    // RecyclerView reference
    private RecyclerView recyclerView;

    // Button reference
    private Button returnButton;

    // Nested class to model list entries
    private static class ListEntry {
        private final String name;
        private final String description;

        public ListEntry(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    // Adapter for RecyclerView to render entries in RecyclerView
    private static class ListEntryAdapter extends RecyclerView.Adapter<ListEntryAdapter.ViewHolder> {

        private final ArrayList<ListEntry> listData;

        private static class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView nameTextView, descriptionTextView;

            public ViewHolder(View view) {
                super(view);

                nameTextView = view.findViewById(R.id.name);
                descriptionTextView = view.findViewById(R.id.description);
            }

            public TextView getNameTextView() {
                return nameTextView;
            }

            public TextView getDescriptionTextView() {
                return descriptionTextView;
            }
        }

        public ListEntryAdapter(ArrayList<ListEntry> listData) {
            this.listData = listData;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int ViewType) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_load_result_recyclerview_element,viewGroup,false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.getNameTextView().setText(listData.get(position).getName());
            viewHolder.getDescriptionTextView().setText(listData.get(position).getDescription());
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_result);

        // Load list data from intent as a JSONArray string
        listData = new ArrayList<ListEntry>();
        String listDataRaw = getIntent().getStringExtra("listData");
        if (listDataRaw==null) listDataRaw = "[]";

        // Pass entries into ArrayList
        try {
            JSONArray listDataJSON = new JSONArray(listDataRaw);
            for (int i=0;i<listDataJSON.length();i++) {
                JSONObject listEntryJSON = listDataJSON.getJSONObject(i);
                String description = "ID: " + listEntryJSON.getInt("id") + "\n"
                        + "Year of matriculation: " + listEntryJSON.getInt("year") + "\n"
                        + "Fully vaccinated: " + (listEntryJSON.getBoolean("is_vaccinated")?"Y":"N") + "\n"
                        + "Undergraduate: " + (listEntryJSON.getBoolean("is_undergraduate")?"Y":"N");
                listData.add(new ListEntry(listEntryJSON.getString("name"),description));
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), DATA_PARSE_ERROR, Toast.LENGTH_SHORT).show();
        }

        // Get RecyclerView reference
        recyclerView = findViewById(R.id.activity_load_result_recyclerview);

        // Setup adapter to show results
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new ListEntryAdapter(listData));

        // Set listener for return button
        returnButton = findViewById(R.id.activity_load_result_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}