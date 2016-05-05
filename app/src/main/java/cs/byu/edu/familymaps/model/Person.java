package cs.byu.edu.familymaps.model;

import java.util.ArrayList;
import java.util.LinkedList;

public class Person implements SearchRecycleListAdapter.Item
{
    private String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String descendant;

    //Can be null values
    private String father;
    private String mother;
    private String spouse;

    private Person fatherPerson;
    private Person motherPerson;
    private Person spousePerson;
    private Event[] events;
    private Person[] ancestors;
    private Person[] family;
    private String[] familyRelations;

    public String getId()
    {
        return personID;
    }

    public Person getSpouse()
    {
        if(spouse != null && spousePerson == null)
        {
            spousePerson = UserData.getPersonById(spouse);
        }
        return spousePerson;
    }

    public Person getFather()
    {
        if(father != null && fatherPerson == null)
        {
            fatherPerson = UserData.getPersonById(father);
        }
        return fatherPerson;
    }

    public Person getMother()
    {
        if(mother != null && motherPerson == null)
        {
            motherPerson = UserData.getPersonById(mother);
        }
        return motherPerson;
    }

    public Person[] getAncestors()
    {
        if(ancestors == null)
        {
            Person father = getFather();
            Person[] fathers = new Person[0];
            if(father != null)
            {
                fathers = father.getAncestors();
            }

            Person mother = getMother();
            Person[] mothers = new Person[0];
            if(mother != null)
            {
                mothers = mother.getAncestors();
            }

            ancestors = new Person[mothers.length + fathers.length + 1];
            int index = 0;

            for(int i = 0; i < fathers.length; i++)
            {
                ancestors[index] = fathers[i];
                index++;
            }

            for(int i = 0; i < mothers.length; i++)
            {
                ancestors[index] = mothers[i];
                index++;
            }

            ancestors[index] = this;
        }

        return ancestors;
    }

    public Event[] getOrderedEvents()
    {
        ArrayList<Event> visibleEvents = new ArrayList<>();
        if(events == null)
        {
            events = getOrderedEventsFromPerson(personID);
        }

        for(int i = 0; i < events.length; i++)
        {
            if(events[i].isVisible())
            {
                visibleEvents.add(events[i]);
            }
        }

        Event[] eventsToReturn = new Event[visibleEvents.size()];
        for(int i = 0; i < visibleEvents.size(); i++)
        {
            eventsToReturn[i] = visibleEvents.get(i);
        }
        return eventsToReturn;
    }

    public Event getEarlyEvent() throws NoEventForPersonException
    {
        if(events == null)
        {
            events = getOrderedEventsFromPerson(personID);
        }

        for(int i = 0; i < events.length; i++)
        {
            if(events[i].isVisible())
            {
                return events[i];
            }
        }

        throw new NoEventForPersonException();
    }

    public void setVisible(boolean visible)
    {
        Event[] events = getOrderedEvents();
        for(int i = 0; i < events.length; i++)
        {
            events[i].setVisible(visible);
        }
    }

    public boolean isMale()
    {
        return gender.equals("m");
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public Person getFamilyMember(int familyPosition)
    {
        if(family == null)
        {
            getFamily();
        }
        return family[familyPosition];
    }

    public int getFamilySize()
    {
        if(family == null)
        {
            getFamily();
        }
        return family.length;
    }

    public String getFamilyMemberRelationship(int childPosition)
    {
        if(family == null)
        {
            getFamily();
        }
        return familyRelations[childPosition];
    }

    private void getFamily()
    {
        ArrayList<Person> familyBuilder = new ArrayList<>();
        ArrayList<String> relationsBuilder = new ArrayList<>();

        if(getFather() != null)
        {
            familyBuilder.add(fatherPerson);
            relationsBuilder.add("Father");
        }
        if(getMother() != null)
        {
            familyBuilder.add(motherPerson);
            relationsBuilder.add("Mother");
        }
        if(getSpouse() != null)
        {
            familyBuilder.add(spousePerson);
            relationsBuilder.add("Spouse");
        }
        Person[] children = getChildren();
        for(int i = 0; i < children.length; i++)
        {
            familyBuilder.add(children[i]);
            relationsBuilder.add("Child");
        }

        family = new Person[familyBuilder.size()];
        for(int i = 0; i < familyBuilder.size(); i++)
        {
            family[i] = familyBuilder.get(i);
        }

        familyRelations = new String[relationsBuilder.size()];
        for(int i = 0; i < relationsBuilder.size(); i++)
        {
            familyRelations[i] = relationsBuilder.get(i);
        }
    }

    public Event getEvent(int childPosition)
    {
        Event[] events = getOrderedEvents();
        return events[childPosition];
    }

    @Override
    public boolean contains(CharSequence text)
    {
        if(firstName.contains(text))
        {
            return true;
        }
        else if(lastName.contains(text))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public class NoEventForPersonException extends Exception {}

    private Event[] getOrderedEventsFromPerson(String personId)
    {
        LinkedList<Event> personsEvents = new LinkedList();
        Event[] events =  UserData.events.data;
        for(int i = 0; i < events.length; i++)
        {
            if(events[i].getPersonId().equals(personId))
            {
                personsEvents.add(events[i]);
            }
        }

        for(int index = 0; index < personsEvents.size() - 1; index++)
        {
            int earliestEventIndex = -1;
            int earliestEventYear = -1;
            for(int i = index; i < personsEvents.size(); i++)
            {
                Event currentEvent = personsEvents.get(i);
                if(!currentEvent.getDescription().toLowerCase().equals("death"))
                {
                    if(earliestEventIndex == -1 || currentEvent.getYear() < earliestEventYear)
                    {
                        earliestEventIndex = i;
                        earliestEventYear = currentEvent.getYear();
                        if (index == 0 && currentEvent.getDescription().toLowerCase().equals("birth"))
                        {
                            break;
                        }
                    }
                }
            }
            personsEvents.add(index, personsEvents.remove(earliestEventIndex));
        }

        Event[] result = new Event[personsEvents.size()];
        for(int i = 0; i < personsEvents.size(); i++)
        {
            result[i] = personsEvents.get(i);
        }
        return result;
    }

    private Person[] getChildren()
    {
        ArrayList<Person> childrenList = new ArrayList<>();
        Person[] persons =  UserData.persons.data;
        for(int i = 0; i < persons.length; i++)
        {
            if(isMale() && persons[i].getFather() != null)
            {
                if(persons[i].father.equals(personID))
                {
                    childrenList.add(persons[i]);
                }
            }
            else if(!isMale() && persons[i].getMother() != null)
            {
                if(persons[i].mother.equals(personID))
                {
                    childrenList.add(persons[i]);
                }
            }
        }

        Person[] children = new Person[childrenList.size()];
        for(int i = 0; i < childrenList.size(); i++)
        {
            children[i] = childrenList.get(i);
        }
        return children;
    }

    @Override
    public String toString()
    {
        return firstName + " " + lastName;
    }
}
