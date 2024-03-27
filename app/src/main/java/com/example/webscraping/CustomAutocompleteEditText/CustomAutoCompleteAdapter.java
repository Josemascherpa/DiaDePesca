package com.example.webscraping.CustomAutocompleteEditText;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class CustomAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private List<String> mData;
    private List<String> mFilteredData;

    public CustomAutoCompleteAdapter(Context context, List<String> data) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        mData = new ArrayList<>(data);
        mFilteredData = new ArrayList<>(data);
    }

    @Override
    public int getCount() {
        return mFilteredData.size();
    }

    @Override
    public String getItem(int position) {
        return mFilteredData.get(position);
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<String> results = new ArrayList<>();

                if (constraint != null) {
                    for (String item : mData) {
                        if (item.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            results.add(item);
                        }
                    }

                    filterResults.values = results;
                    filterResults.count = results.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredData.clear();
                if (results != null && results.count > 0) {
                    mFilteredData.addAll((List<String>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }
}
