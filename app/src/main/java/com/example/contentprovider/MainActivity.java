package com.example.contentprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String ProviderName = "com.example.android.contentprovidersample.provider";
    private static final Uri Content_Data = Uri.parse("content://" + ProviderName + "/cheeses");
    private static final String Data_Name = "name";
    private static final String Data_Id = "_id";
    private NewAdapter newAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));

        Cursor c = getCheeseData();
        newAdapter = new NewAdapter();
        newAdapter.setCheeses(c);
        list.setAdapter(newAdapter);
    }

    private Cursor getCheeseData(){
        Uri allCheeses = Uri.parse("content://" +  ProviderName + "/cheeses");
        String[] projection = new String[]
                {Data_Id, Data_Name};
        Cursor c;
        CursorLoader cursorLoader = new CursorLoader(
                this,
                allCheeses,
                projection,
                null,
                null,
                "name" + " ASC");
        c = cursorLoader.loadInBackground();
        return c;
    }

    private static class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder> {

        private Cursor mCursor;

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if(mCursor.moveToPosition(position)){
                holder.mText.setText(mCursor.getString(mCursor.getColumnIndex(Data_Name)));
            }
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        void setCheeses(Cursor cursor) {
            mCursor = cursor;
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            final TextView mText;

            ViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(
                        android.R.layout.simple_expandable_list_item_2, parent, false));
                mText = itemView.findViewById(android.R.id.text1);
            }

        }

    }
}
