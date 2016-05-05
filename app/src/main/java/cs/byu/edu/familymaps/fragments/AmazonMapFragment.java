package cs.byu.edu.familymaps.fragments;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amazon.geo.mapsv2.AmazonMap;
import com.amazon.geo.mapsv2.AmazonMapOptions;
import com.amazon.geo.mapsv2.CameraUpdateFactory;
import com.amazon.geo.mapsv2.MapFragment;
import com.amazon.geo.mapsv2.OnMapReadyCallback;
import com.amazon.geo.mapsv2.SupportMapFragment;
import com.amazon.geo.mapsv2.model.BitmapDescriptorFactory;
import com.amazon.geo.mapsv2.model.Marker;
import com.amazon.geo.mapsv2.model.MarkerOptions;
import com.amazon.geo.mapsv2.model.Polyline;
import com.amazon.geo.mapsv2.model.PolylineOptions;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import cs.byu.edu.familymaps.R;
import cs.byu.edu.familymaps.activities.PersonActivity;
import cs.byu.edu.familymaps.model.Event;
import cs.byu.edu.familymaps.model.Person;
import cs.byu.edu.familymaps.model.UserData;

public class AmazonMapFragment
{
    private static MapFragment mMapFragment;
    private static final String LOG_TAG = "mapFragment";
    public static int currentMapType = AmazonMap.MAP_TYPE_NORMAL;

    public static void sync()
    {
        sync(-1);
    }

    public static void sync(int mapType)
    {
        if(mapType != -1)
        {
            currentMapType = mapType;
        }

        AmazonMapOptions mapOpt = new AmazonMapOptions();
        mMapFragment = MapFragment.newInstance(mapOpt);

        mMapFragment.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(final AmazonMap amazonMap)
            {
                amazonMap.getUiSettings().setZoomControlsEnabled(true);
                amazonMap.setTrafficEnabled(false);
                amazonMap.setMyLocationEnabled(false);
                amazonMap.setMapType(currentMapType);
                if(UserData.personData.currentEvent != null)
                {
                    clickedEvent(UserData.personData.currentEvent, amazonMap);
                    amazonMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UserData.personData.currentEvent.getLatLng(), 2));
                    UserData.personData.currentEvent = null;
                }
                else if(isTop && UserData.personData.topEvent != null)
                {
                    clickedEvent(UserData.personData.topEvent, amazonMap);
                    amazonMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UserData.personData.topEvent.getLatLng(), 2));
                }


                drawMarkers(amazonMap);
                amazonMap.setOnMarkerClickListener(new AmazonMap.OnMarkerClickListener()
                {
                    @Override
                    public boolean onMarkerClick(Marker marker)
                    {
                        clickedEvent(UserData.getEventFromMarker(marker.getId()), amazonMap);
                        return false;
                    }
                });
            }
        });
    }

    private static void clickedEvent(Event clickedEvent, AmazonMap amazonMap)
    {
        Person clickedPerson = clickedEvent.getPerson();

        person_name.setText(clickedPerson.toString());
        event_description.setText(clickedEvent.toString());
        if (clickedPerson.isMale())
        {
            person_icon.setImageDrawable(new IconDrawable(context, Iconify.IconValue.fa_male).colorRes(R.color.male).sizeDp(45));
        }
        else
        {
            person_icon.setImageDrawable(new IconDrawable(context, Iconify.IconValue.fa_female).colorRes(R.color.female).sizeDp(45));
        }
        UserData.personData.currentPerson = clickedPerson;
        if(isTop)
        {
            UserData.personData.topEvent = clickedEvent;
        }

        amazonMap.clear();
        drawMarkers(amazonMap);
        if (UserData.settingsData.spouseLinesVisible)
        {
            Person spouse = clickedPerson.getSpouse();
            if (spouse != null)
            {
                try
                {
                    Event spouseEvent = spouse.getEarlyEvent();
                    PolylineOptions opt = new PolylineOptions()
                            .visible(true)
                            .add(clickedEvent.getLatLng()).add(spouseEvent.getLatLng())
                            .color(UserData.settingsData.spouseLineColor);
                    Polyline p = amazonMap.addPolyline(opt);

                    Log.d(LOG_TAG, String.valueOf(opt.getWidth()));
                } catch (Person.NoEventForPersonException e)
                {
                    //Do nothing
                }
            }
        }
        if (UserData.settingsData.familyTreeLinesVisible)
        {
            familyLines(clickedPerson, clickedEvent, 10, amazonMap);
        }
        if (UserData.settingsData.lifeStoryLinesVisible)
        {
            Event[] chronologicalEvents = clickedPerson.getOrderedEvents();
            PolylineOptions opt = new PolylineOptions().visible(true);
            for (Event event : chronologicalEvents)
            {
                if (event.isVisible())
                {
                    opt.add(event.getLatLng());
                }
            }
            opt.color(UserData.settingsData.lifeStoryLineColor);

            Polyline p = amazonMap.addPolyline(opt);
        }

    }

    private static void drawMarkers(AmazonMap amazonMap)
    {
        Event[] events = UserData.events.data;
        for (int i = 0; i < events.length; i++)
        {
            Event event = events[i];
            if(event.isVisible())
            {
                float hue = UserData.settingsData.getHue(event);
                MarkerOptions opt = new MarkerOptions()
                        .position(event.getLatLng())
                        .title(event.getLocation())
                        .snippet(event.getDescription())
                        .icon(BitmapDescriptorFactory.defaultMarker(hue));
                Marker marker = amazonMap.addMarker(opt);
                event.setMarker(marker);
            }
        }
    }

    private static void familyLines(Person person, Event event, float lineWidth, AmazonMap amazonMap)
    {
        Person father = person.getFather();
        Person mother = person.getMother();

        if (father != null)
        {
            try
            {
                Event fatherEvent = father.getEarlyEvent();
                PolylineOptions opt = new PolylineOptions()
                        .visible(true)
                        .add(event.getLatLng()).add(fatherEvent.getLatLng())
                        .color(UserData.settingsData.familyTreeLineColor)
                        .width(lineWidth);
                Polyline p = amazonMap.addPolyline(opt);

                familyLines(father, fatherEvent, lineWidth/2, amazonMap);
            } catch (Person.NoEventForPersonException e)
            {
                //Do nothing
            }
        }

        if (mother != null)
        {
            try
            {
                Event motherEvent = mother.getEarlyEvent();
                PolylineOptions opt = new PolylineOptions()
                        .visible(true)
                        .add(event.getLatLng()).add(motherEvent.getLatLng())
                        .color(UserData.settingsData.familyTreeLineColor)
                        .width(lineWidth);
                Polyline p = amazonMap.addPolyline(opt);

                familyLines(mother, motherEvent, lineWidth/2, amazonMap);
            } catch (Person.NoEventForPersonException e)
            {
                //Do nothing
            }
        }
    }

    public static MapFragment get()
    {
        sync();
        return mMapFragment;
    }

    public static String getTag()
    {
        return LOG_TAG;
    }


    private static Context context;
    private static RelativeLayout personFragment;
    private static ImageView person_icon;
    private static TextView person_name;
    private static TextView event_description;
    private static boolean isTop = true;

    public static void setPersonFragment(Context c, boolean isMain, RelativeLayout fragment, ImageView icon, TextView name, TextView description)
    {
        context = c;
        personFragment = fragment;
        person_icon = icon;
        person_name = name;
        event_description = description;
        isTop = isMain;

        person_icon.setImageDrawable(new IconDrawable(context, Iconify.IconValue.fa_android).colorRes(R.color.android).sizeDp(45));
        person_name.setText("Click on a marker");
        event_description.setText("to see event details");

        personFragment.setVisibility(View.VISIBLE);
        personFragment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(UserData.personData.currentPerson != null)
                {
                    Intent intent = new Intent(context, PersonActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    public static void removePersonFragment()
    {
        personFragment.setVisibility(View.INVISIBLE);

        personFragment = null;
        person_icon = null;
        person_name = null;
        event_description = null;
    }
}
