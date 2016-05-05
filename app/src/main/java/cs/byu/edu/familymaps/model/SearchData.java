package cs.byu.edu.familymaps.model;

import java.util.ArrayList;
import java.util.List;

public class SearchData
{
    public List<SearchRecycleListAdapter.Item> find(CharSequence s)
    {
        Person[] persons = UserData.persons.data;
        Event[] events = UserData.events.data;
        List<SearchRecycleListAdapter.Item> items = new ArrayList<>();

        for(int i = 0; i < persons.length; i++)
        {
            if(persons[i].contains(s))
            {
                items.add(persons[i]);
            }
        }

        for(int i = 0; i < events.length; i++)
        {
            if(events[i].isVisible() && events[i].contains(s))
            {
                items.add(events[i]);
            }
        }

        return items;
    }
}
