package cs.byu.edu.familymaps.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import cs.byu.edu.familymaps.R;
import cs.byu.edu.familymaps.fragments.AmazonMapFragment;
import cs.byu.edu.familymaps.model.UserData;

public class MapActivity extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getFragmentManager().beginTransaction().add(R.id.map_fragment_container, AmazonMapFragment.get(), AmazonMapFragment.getTag()).commit();

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.map_person_container);
        ImageView image = (ImageView) findViewById(R.id.map_person_image);
        TextView person_name = (TextView) findViewById(R.id.map_person_name_view);
        TextView event_description = (TextView) findViewById(R.id.map_description_view);

        AmazonMapFragment.setPersonFragment(this, false, layout, image, person_name, event_description);
        UserData.personData.currentPerson = null;

        // Enable the Up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Drawable searchIcon = new IconDrawable(this, Iconify.IconValue.fa_angle_double_up).colorRes(R.color.standard).sizeDp(30);
        MenuItem searchItem = menu.add(R.string.top).setIcon(searchIcon).setVisible(true);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getTitle().equals(getString(R.string.top)))
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
            this.finish();
        }
        else
        {
            this.onBackPressed();
        }

        return true;
    }
}
