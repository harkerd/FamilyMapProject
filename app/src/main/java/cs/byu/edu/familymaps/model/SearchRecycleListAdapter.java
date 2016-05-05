package cs.byu.edu.familymaps.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import java.util.List;
import cs.byu.edu.familymaps.R;
import cs.byu.edu.familymaps.activities.MapActivity;
import cs.byu.edu.familymaps.activities.PersonActivity;

public class SearchRecycleListAdapter extends RecyclerView.Adapter<SearchRecycleListAdapter.ItemHolder>
{
    private Context context;
    private List<Item> items;

    public SearchRecycleListAdapter(Context context, List<Item> items)
    {
        this.context = context;
        this.items = items;
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sub_text_icon_text, null);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position)
    {
        holder.bindItem(items.get(position));
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public void updateList(List<Item> data) {
        items = data;
        notifyDataSetChanged();
    }


    public class ItemHolder extends RecyclerView.ViewHolder
    {
        private Item item;

        public ItemHolder(final View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (item != null)
                    {
                        if (item instanceof Person)
                        {
                            UserData.personData.currentPerson = (Person) item;
                            Intent personActivity = new Intent(context, PersonActivity.class);
                            context.startActivity(personActivity);
                        }
                        else if (item instanceof Event)
                        {
                            UserData.personData.currentEvent = (Event) item;
                            Intent personActivity = new Intent(context, MapActivity.class);
                            context.startActivity(personActivity);
                        }
                    }
                }
            });
        }

        public void bindItem(Item item)
        {
            this.item = item;

            ImageView iconView = (ImageView) itemView.findViewById(R.id.list_item_icon);
            TextView topTextView = (TextView) itemView.findViewById(R.id.list_item_text_top);
            TextView bottomTextView = (TextView) itemView.findViewById(R.id.list_item_text_bottom);

            Drawable icon = null;
            String topLine = null;
            String bottomLine = null;

            if(item instanceof Event)
            {
                Event event = (Event) item;
                icon = new IconDrawable(context, Iconify.IconValue.fa_map_marker).colorRes(R.color.gray).sizeDp(45);
                topLine = event.toString();
                bottomLine = event.getPerson().toString();
            }
            else if(item instanceof Person)
            {
                Person person = (Person) item;
                if(person.isMale())
                {
                    icon = new IconDrawable(context, Iconify.IconValue.fa_male).colorRes(R.color.male).sizeDp(45);
                }
                else
                {
                    icon = new IconDrawable(context, Iconify.IconValue.fa_female).colorRes(R.color.female).sizeDp(45);
                }
                topLine = person.toString();
                bottomLine = "";
            }

            iconView.setImageDrawable(icon);
            topTextView.setText(topLine);
            bottomTextView.setText(bottomLine);
        }
    }

    public interface Item
    {
        boolean contains(CharSequence text);
    }
}
