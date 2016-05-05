package cs.byu.edu.familymaps.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Set;
import cs.byu.edu.familymaps.R;
import cs.byu.edu.familymaps.model.Event;
import cs.byu.edu.familymaps.model.FilterRecycleListAdapter;
import cs.byu.edu.familymaps.model.FilterRecycleListAdapter.Item;
import cs.byu.edu.familymaps.model.Person;
import cs.byu.edu.familymaps.model.SearchRecycleListAdapter;
import cs.byu.edu.familymaps.model.UserData;

public class FilterActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Set<String> types = UserData.filterData.getEventTypes();
        ArrayList<Item> items = new ArrayList<>();


        for(final String type : types)
        {
            items.add(new Item(type.toUpperCase() + " Events", "FILTER BY " + type.toUpperCase() + " EVENTS",
                    UserData.filterData.isChecked(type), new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    UserData.filterData.setChecked(type, isChecked);

                    Event[] events = UserData.filterData.getEventsByStringDescription(type);
                    for (int i = 0; i < events.length; i++)
                    {
                        events[i].setVisible(isChecked);
                    }
                }
            }
            ));
        }

        items.add(new Item("Father's Side", "FILTER BY FATHER'S SIDE OF THE FAMILY",
            UserData.filterData.fathers, new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    UserData.filterData.fathers = isChecked;

                    Person[] persons = UserData.filterData.getPersonsBySide(true);
                    for (int i = 0; i < persons.length; i++)
                    {
                        persons[i].setVisible(isChecked);
                    }
                }
            }
        ));

        items.add(new Item("Mother's Side", "FILTER BY MOTHER'S SIDE OF THE FAMILY",
            UserData.filterData.mothers, new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    UserData.filterData.mothers = isChecked;

                    Person[] persons = UserData.filterData.getPersonsBySide(false);
                    for (int i = 0; i < persons.length; i++)
                    {
                        persons[i].setVisible(isChecked);
                    }
                }
            }
        ));

        items.add(new Item("Male Events", "FILTER EVENTS BASED ON GENDER",
            UserData.filterData.male, new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    UserData.filterData.male = isChecked;

                    Person[] persons = UserData.filterData.getPersonsByGender(true);
                    for (int i = 0; i < persons.length; i++)
                    {
                        persons[i].setVisible(isChecked);
                    }
                }
            }
        ));

        items.add(new Item("Female Events", "FILTER EVENTS BASED ON GENDER",
                UserData.filterData.female, new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    UserData.filterData.female = isChecked;

                    Person[] persons = UserData.filterData.getPersonsByGender(false);
                    for (int i = 0; i < persons.length; i++)
                    {
                        persons[i].setVisible(isChecked);
                    }
                }
            }
        ));

        RecyclerView listView = (RecyclerView) findViewById(R.id.filter_layout);
        listView.setLayoutManager(new LinearLayoutManager(this));
        FilterRecycleListAdapter adapter = new FilterRecycleListAdapter(this, items);
        listView.setAdapter(adapter);
    }
}
