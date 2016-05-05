package cs.byu.edu.familymaps.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;
import cs.byu.edu.familymaps.R;

public class FilterRecycleListAdapter extends RecyclerView.Adapter<FilterRecycleListAdapter.ItemHolder>
{
    private Context context;
    private List<Item> items;

    public FilterRecycleListAdapter(Context context, List<Item> items)
    {
        this.context = context;
        this.items = items;
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sub_text_description_switch, null);
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

    public class ItemHolder extends RecyclerView.ViewHolder
    {
        private Item item;

        public ItemHolder(final View itemView)
        {
            super(itemView);

            if(item != null)
            {
                setup();
            }
        }

        public void bindItem(Item item)
        {
            this.item = item;
            if(itemView != null)
            {
                setup();
            }
        }

        private void setup()
        {
            TextView textView = (TextView) itemView.findViewById(R.id.filter_item_text);
            TextView descriptionView = (TextView) itemView.findViewById(R.id.filter_item_description_text);
            Switch switchBox = (Switch) itemView.findViewById(R.id.filter_item_switch);

            textView.setText(item.text);
            descriptionView.setText(item.description);
            switchBox.setChecked(item.isChecked);
            switchBox.setOnCheckedChangeListener(item.listener);
        }
    }

    public static class Item
    {
        private String text;
        private String description;
        private boolean isChecked;
        private CompoundButton.OnCheckedChangeListener listener;

        public Item(String text, String description, boolean isChecked, CompoundButton.OnCheckedChangeListener listener)
        {
            this.text = text;
            this.description = description;
            this.isChecked = isChecked;
            this.listener = listener;
        }
    }
}

