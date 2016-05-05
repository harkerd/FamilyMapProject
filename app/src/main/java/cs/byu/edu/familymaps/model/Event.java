package cs.byu.edu.familymaps.model;

import com.amazon.geo.mapsv2.model.LatLng;
import com.amazon.geo.mapsv2.model.Marker;

public class Event implements SearchRecycleListAdapter.Item
{
    private String eventID;
    private String personID;
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String description;
    private int year;
    private String descendant;

    private Marker marker;
    private Person person;
    private int visibilityCount = 0;

    public LatLng getLatLng()
    {
        return new LatLng(latitude, longitude);
    }

    public String getLocation()
    {
        return country + ", " + city;
    }

    public String getDescription()
    {
        return description;
    }

    public void setMarker(Marker marker)
    {
        this.marker = marker;
    }

    public void setVisible(boolean visible)
    {
        if(visible)
        {
            visibilityCount--;
        }
        else
        {
            visibilityCount++;
        }

        if(visibilityCount == 0)
        {
            marker.setVisible(true);
        }
        else
        {
            marker.setVisible(false);
        }
    }

    public Person getPerson()
    {
        if(person == null)
        {
            person = UserData.getPersonById(personID);
        }
        return person;
    }

    public String getPersonId()
    {
        return personID;
    }

    public int getYear()
    {
        return year;
    }

    public String getMarkerId()
    {
        return marker.getId();
    }

    public boolean isVisible()
    {
        if(marker == null)
        {
            return true;
        }
        return marker.isVisible();
    }

    @Override
    public boolean contains(CharSequence text)
    {
        if(country.contains(text))
        {
            return true;
        }
        else if(city.contains(text))
        {
            return true;
        }
        else if(description.contains(text))
        {
            return true;
        }
        else if(String.valueOf(year).contains(text))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return description + ": " + city + ", " + country + " (" + year + ")";
    }
}
