package cs.byu.edu.familymaps.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import cs.byu.edu.familymaps.R;
import cs.byu.edu.familymaps.model.PersonsExpandableListAdapter;
import cs.byu.edu.familymaps.model.Person;
import cs.byu.edu.familymaps.model.UserData;

public class PersonActivity extends AppCompatActivity
{
    private Person currentPerson;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        context = this;

        if(currentPerson == null)
        {
            currentPerson = UserData.personData.currentPerson;
        }

        TextView first_name = (TextView) findViewById(R.id.first_name_text);
        first_name.setText(currentPerson.getFirstName());
        TextView last_name = (TextView) findViewById(R.id.last_name_text);
        last_name.setText(currentPerson.getLastName());
        TextView gender = (TextView) findViewById(R.id.gender_text);
        if(currentPerson.isMale())
        {
            gender.setText("Male");
        }
        else
        {
            gender.setText("Female");
        }

        ExpandableListView personDataList = (ExpandableListView) findViewById(R.id.person_data_list);
        PersonsExpandableListAdapter listAdapter = new PersonsExpandableListAdapter(this, currentPerson);

        personDataList.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                if(groupPosition == 0)
                {
                    UserData.personData.currentEvent = currentPerson.getEvent(childPosition);
                    Intent intent = new Intent(context, MapActivity.class);
                    context.startActivity(intent);
                }
                else if(groupPosition == 1)
                {
                    UserData.personData.currentPerson = currentPerson.getFamilyMember(childPosition);
                    Intent intent = new Intent(context, PersonActivity.class);
                    context.startActivity(intent);
                }
                return false;
            }
        });

        personDataList.setAdapter(listAdapter);

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Drawable searchIcon = new IconDrawable(this, Iconify.IconValue.fa_angle_double_up).colorRes(R.color.standard).sizeDp(30);
        MenuItem searchItem = menu.add("Go To Top").setIcon(searchIcon).setVisible(true);
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
