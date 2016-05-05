package cs.byu.edu.familymaps.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import cs.byu.edu.familymaps.R;
import cs.byu.edu.familymaps.fragments.AmazonMapFragment;
import cs.byu.edu.familymaps.fragments.LoginFragment;
import cs.byu.edu.familymaps.model.UserData;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener
{
    private static final String LOG_TAG = "MainActivity";
    private static boolean loggedIn = false;

    private MenuItem searchItem;
    private MenuItem filterItem;
    private MenuItem settingsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate()");
        setContentView(R.layout.activity_main);


        if(loggedIn)
        {
            if(getFragmentManager().findFragmentById(R.id.fragment_container) == null)
            {
                addMap();
            }
            setPerson();
        }
        else
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
            if(fragment == null)
            {
                fragmentManager.beginTransaction().add(R.id.fragment_container, new LoginFragment()).commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Drawable searchIcon = new IconDrawable(this, Iconify.IconValue.fa_search).colorRes(R.color.standard).sizeDp(20);
        searchItem = menu.add("Search").setIcon(searchIcon);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);


        Drawable filterIcon = new IconDrawable(this, Iconify.IconValue.fa_filter).colorRes(R.color.standard).sizeDp(20);
        filterItem = menu.add("Filter").setIcon(filterIcon);
        filterItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        Drawable settingsIcon = new IconDrawable(this, Iconify.IconValue.fa_gear).colorRes(R.color.standard).sizeDp(20);
        settingsItem = menu.add("Settings").setIcon(settingsIcon);
        settingsItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        if(!UserData.loggedIn)
        {
            searchItem.setVisible(false);
            filterItem.setVisible(false);
            settingsItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getTitle() == "Search")
        {
            Intent intent = new Intent(this, SearchActivity.class);
            this.startActivity(intent);

            return true;
        }
        else if(item.getTitle() == "Filter")
        {
            Intent intent = new Intent(this, FilterActivity.class);
            this.startActivity(intent);

            return true;
        }
        else if(item.getTitle() == "Settings")
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);

            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(!UserData.loggedIn && loggedIn)
        {
            logout();
        }
        if(loggedIn)
        {
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
            getFragmentManager().beginTransaction().add(R.id.fragment_container, AmazonMapFragment.get(), AmazonMapFragment.getTag()).commit();
        }
    }

    private void addMap()
    {
        getFragmentManager().beginTransaction().add(R.id.fragment_container, AmazonMapFragment.get(), AmazonMapFragment.getTag()).commit();

        setPerson();
    }

    private void setPerson()
    {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.person_container);
        ImageView image = (ImageView) findViewById(R.id.person_image);
        TextView person_name = (TextView) findViewById(R.id.person_name_view);
        TextView event_description = (TextView) findViewById(R.id.description_view);

        AmazonMapFragment.setPersonFragment(this, true, layout, image, person_name, event_description);
    }

    @Override
    public void login()
    {
        loggedIn = true;
        UserData.loggedIn = true;
        //InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        addMap();

        searchItem.setVisible(true);
        filterItem.setVisible(true);
        settingsItem.setVisible(true);
    }

    public void logout()
    {
        loggedIn = false;
        UserData.loggedIn = false;

        AmazonMapFragment.removePersonFragment();

        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragment_container)).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LoginFragment()).commit();

        searchItem.setVisible(false);
        filterItem.setVisible(false);
        settingsItem.setVisible(false);
    }
}
