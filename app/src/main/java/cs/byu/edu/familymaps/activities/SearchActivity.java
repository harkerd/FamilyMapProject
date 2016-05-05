package cs.byu.edu.familymaps.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.util.ArrayList;
import cs.byu.edu.familymaps.R;
import cs.byu.edu.familymaps.model.SearchRecycleListAdapter;
import cs.byu.edu.familymaps.model.SearchRecycleListAdapter.Item;
import cs.byu.edu.familymaps.model.UserData;

public class SearchActivity extends AppCompatActivity
{
    private SearchRecycleListAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        context = this;

        RecyclerView listView = (RecyclerView) findViewById(R.id.search_list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchRecycleListAdapter(context, new ArrayList<Item>());
        listView.setAdapter(adapter);

        EditText box = (EditText) findViewById(R.id.search_box);
        box.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                adapter.updateList(UserData.searchData.find(s));
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

}
