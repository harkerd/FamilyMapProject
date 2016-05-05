package cs.byu.edu.familymaps.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FilterData
{
    private Map<String, Boolean> types;
    public boolean fathers = true;
    public boolean mothers = true;
    public boolean male = true;
    public boolean female = true;

    public void setChecked(String type, boolean isVisible)
    {
        types.put(type.toLowerCase(), isVisible);
    }

    public boolean isChecked(String type)
    {
        return types.get(type.toLowerCase());
    }

    public Set<String> getEventTypes()
    {
        if(types == null)
        {
            types = new TreeMap<>();
            Event[] events = UserData.events.data;
            for (int i = 0; i < events.length; i++)
            {
                if (!types.containsKey(events[i].getDescription().toLowerCase()))
                {
                    types.put(events[i].getDescription(), true);
                }
            }
        }
        return types.keySet();
    }

    public Event[] getEventsByStringDescription(String description)
    {
        ArrayList<Event> descriptionEvents = new ArrayList();
        Event[] events = UserData.events.data;
        for(int i = 0; i < events.length; i++)
        {
            if(events[i].getDescription().toLowerCase().equals(description))
            {
                descriptionEvents.add(events[i]);
            }
        }

        Event[] result = new Event[descriptionEvents.size()];
        for(int i = 0; i < descriptionEvents.size(); i++)
        {
            result[i] = descriptionEvents.get(i);
        }
        return result;
    }

    public Person[] getPersonsByGender(boolean male)
    {
        ArrayList<Person> genderPersons = new ArrayList();
        Person[] persons = UserData.persons.data;
        for(int i = 0; i < persons.length; i++)
        {
            if( (male && persons[i].isMale()) || (!male && !persons[i].isMale()) )
            {
                genderPersons.add(persons[i]);
            }
        }

        Person[] result = new Person[genderPersons.size()];
        for(int i = 0; i < genderPersons.size(); i++)
        {
            result[i] = genderPersons.get(i);
        }
        return result;
    }

    public Person[] getPersonsBySide(boolean fathers)
    {
        Person user = UserData.persons.data[UserData.persons.data.length - 1];
        Person parent;
        if(fathers)
        {
            parent = user.getFather();
        }
        else
        {
            parent = user.getMother();
        }

        return parent.getAncestors();
    }
}
