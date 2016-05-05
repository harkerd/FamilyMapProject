package cs.byu.edu.familymaps.model;

public class UserData
{
    public static boolean loggedIn = false;

    public static String username;
    public static Events events;
    public static Persons persons;

    public static SettingsData settingsData = new SettingsData();
    public static FilterData filterData = new FilterData();
    public static SearchData searchData = new SearchData();
    public static PersonData personData = new PersonData();

    public static void reset()
    {
        events = null;
        persons = null;
        settingsData = new SettingsData();
        filterData = new FilterData();
        searchData = new SearchData();
        personData = new PersonData();
    }

    public static Event getEventFromMarker(String id)
    {
        Event clickEvent = null;

        for(int i = 0; i < events.data.length; i++)
        {
            if(events.data[i].getMarkerId().equals(id))
            {
                clickEvent = events.data[i];
                break;
            }
        }
        return clickEvent;
    }

    public static Person getPersonById(String personId)
    {
        Person clickedPerson = null;

        for(int i = 0; i < persons.data.length; i++)
        {
            if(persons.data[i].getId().equals(personId))
            {
                clickedPerson = persons.data[i];
                break;
            }
        }
        return clickedPerson;
    }
}
