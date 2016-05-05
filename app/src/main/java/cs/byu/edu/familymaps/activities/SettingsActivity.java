package cs.byu.edu.familymaps.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.geo.mapsv2.AmazonMap;

import cs.byu.edu.familymaps.R;
import cs.byu.edu.familymaps.backgroundClasses.SyncDataTask;
import cs.byu.edu.familymaps.fragments.AmazonMapFragment;
import cs.byu.edu.familymaps.model.UserData;

public class SettingsActivity extends AppCompatActivity
{
    private static final String LOG_TAG = "SettingsActivity";
    private static final Integer[] colors = {
        Color.GREEN,
        Color.BLUE,
        Color.RED,
        Color.YELLOW,
        Color.rgb(102,51,153),//purple
        Color.rgb(255,102,0), //orange
        Color.CYAN,
        Color.rgb(51,102,153),//azure
        Color.rgb(239,89,123), //rose
        Color.MAGENTA
    };
    private static final Integer[] mapTypes = {
            AmazonMap.MAP_TYPE_NORMAL,
            AmazonMap.MAP_TYPE_HYBRID,
            AmazonMap.MAP_TYPE_SATELLITE,
            AmazonMap.MAP_TYPE_TERRAIN
    };

    private static int getColorIndexOf(int key)
    {
        int index = 0;
        for (int value : colors)
        {
            if(key == value)
            {
                return index;
            }
            index++;
        }
        return -1;
    }

    private static int getMapTypeIndexOf(int key)
    {
        int index = 0;
        for (float value : mapTypes)
        {
            if(key == value)
            {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Spinner life_story_spinner = (Spinner) findViewById(R.id.life_story_spinner);
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this,
                R.array.colors, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        life_story_spinner.setAdapter(colorAdapter);
        life_story_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                UserData.settingsData.lifeStoryLineColor = colors[position];
                AmazonMapFragment.sync();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        life_story_spinner.setSelection(getColorIndexOf(UserData.settingsData.lifeStoryLineColor));
        Switch life_story_switch = (Switch) findViewById(R.id.life_story_switch);
        life_story_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                UserData.settingsData.lifeStoryLinesVisible = isChecked;
            }
        });
        life_story_switch.setChecked(UserData.settingsData.lifeStoryLinesVisible);


        Spinner family_tree_spinner = (Spinner) findViewById(R.id.family_tree_spinner);
        family_tree_spinner.setAdapter(colorAdapter);
        family_tree_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                UserData.settingsData.familyTreeLineColor = colors[position];
                AmazonMapFragment.sync();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        family_tree_spinner.setSelection(getColorIndexOf(UserData.settingsData.familyTreeLineColor));
        Switch family_tree_switch = (Switch) findViewById(R.id.family_tree_switch);
        family_tree_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                UserData.settingsData.familyTreeLinesVisible = isChecked;
            }
        });
        family_tree_switch.setChecked(UserData.settingsData.familyTreeLinesVisible);


        Spinner spouse_spinner = (Spinner) findViewById(R.id.spouse_spinner);
        spouse_spinner.setAdapter(colorAdapter);
        spouse_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                UserData.settingsData.spouseLineColor = colors[position];
                AmazonMapFragment.sync();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        spouse_spinner.setSelection(getColorIndexOf(UserData.settingsData.spouseLineColor));
        Switch spouse_switch = (Switch) findViewById(R.id.spouse_switch);
        spouse_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                UserData.settingsData.spouseLinesVisible = isChecked;
            }
        });
        spouse_switch.setChecked(UserData.settingsData.spouseLinesVisible);


        Spinner map_type_spinner = (Spinner) findViewById(R.id.map_type_spinner);
        ArrayAdapter<CharSequence> mapTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.mapType, android.R.layout.simple_spinner_item);
        mapTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        map_type_spinner.setAdapter(mapTypeAdapter);
        map_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                AmazonMapFragment.sync(mapTypes[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        map_type_spinner.setSelection(getMapTypeIndexOf(AmazonMapFragment.currentMapType));

        TextView resyncButton = (TextView) findViewById(R.id.resync_button);
        resyncButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserData.reset();
                sync();
            }
        });
        TextView logoutButton = (TextView) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserData.loggedIn = false;
                finish();
            }
        });
    }

    private void sync()
    {
        new SyncDataTask(this, LOG_TAG)
        {
            @Override
            protected void onPostExecute(Boolean successful)
            {
                if(!successful)
                {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AmazonMapFragment.sync();
                    finish();
                }
            }
        }.execute();
    }
}
