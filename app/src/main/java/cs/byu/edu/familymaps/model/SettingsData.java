package cs.byu.edu.familymaps.model;

import android.graphics.Color;

import com.amazon.geo.mapsv2.model.BitmapDescriptorFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class SettingsData
{
    public int lifeStoryLineColor = Color.GREEN;
    public boolean lifeStoryLinesVisible = true;
    public int familyTreeLineColor = Color.BLUE;
    public boolean familyTreeLinesVisible = true;
    public int spouseLineColor = Color.RED;
    public boolean spouseLinesVisible = true;

    private Map<String, Float> colors;

    public float getHue(Event event)
    {
        if(colors == null)
        {
            setColors();
        }

        return colors.get(event.getDescription().toLowerCase());
    }

    private void setColors()
    {
        colors = new HashMap<>();
        Set<String> types = UserData.filterData.getEventTypes();
        Set<Float> usedColors = new HashSet<>();
        for(String type : types)
        {
            float color;
            switch(type)
            {
                    /*case "birth": color = BitmapDescriptorFactory.HUE_GREEN; break;
                    case "death": color = BitmapDescriptorFactory.HUE_ORANGE; break;
                    case "christening": color = BitmapDescriptorFactory.HUE_BLUE; break;
                    case "baptism": color = BitmapDescriptorFactory.HUE_CYAN; break;
                    case "marriage": color = BitmapDescriptorFactory.HUE_MAGENTA; break;
                    case "census": color = BitmapDescriptorFactory.HUE_YELLOW; break;*/

                default:
                    do
                    {
                        Random random = new Random();
                        color = random.nextInt(22) * 15;
                    } while(usedColors.contains(color));
            }
            usedColors.add(color);
            colors.put(type, color);
        }
    }
}
