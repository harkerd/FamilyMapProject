package cs.byu.edu.familymaps.model;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import cs.byu.edu.familymaps.R;

public class PersonsExpandableListAdapter extends BaseExpandableListAdapter
{
    private Event[] lifeData;
    private Person currentPerson;
    private Context context;

    public PersonsExpandableListAdapter(Context context, Person person)
    {
        this.context = context;
        this.currentPerson = person;
        lifeData = person.getOrderedEvents();
    }

    @Override
    public int getGroupCount()
    {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        if(groupPosition == 0) return lifeData.length;
        else if(groupPosition == 1) return currentPerson.getFamilySize();
        else return 0;
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        if(groupPosition == 0) return "LIFE EVENTS";
        else if(groupPosition == 1) return "FAMILY";
        else return 0;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        if(groupPosition == 0) return lifeData[childPosition];
        else if(groupPosition == 1) return currentPerson.getFamilyMember(childPosition);
        else return null;
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sub_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.list_header);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        Drawable icon = null;
        String topLine = null;
        String bottomLine = null;

        if(groupPosition == 0)
        {
            icon = new IconDrawable(context, Iconify.IconValue.fa_map_marker).colorRes(R.color.gray).sizeDp(45);
            topLine = lifeData[childPosition].toString();
            bottomLine = UserData.personData.currentPerson.toString();
        }
        else if(groupPosition == 1)
        {
            Person familyMember = currentPerson.getFamilyMember(childPosition);
            if(familyMember.isMale())
            {
                icon = new IconDrawable(context, Iconify.IconValue.fa_male).colorRes(R.color.male).sizeDp(45);
            }
            else
            {
                icon = new IconDrawable(context, Iconify.IconValue.fa_female).colorRes(R.color.female).sizeDp(45);
            }
            topLine = familyMember.toString();
            bottomLine = currentPerson.getFamilyMemberRelationship(childPosition);
        }

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sub_text_icon_text, null);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.list_item_icon);
        TextView topTextView = (TextView) convertView.findViewById(R.id.list_item_text_top);
        TextView bottomTextView = (TextView) convertView.findViewById(R.id.list_item_text_bottom);

        iconView.setImageDrawable(icon);
        topTextView.setText(topLine);
        bottomTextView.setText(bottomLine);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
