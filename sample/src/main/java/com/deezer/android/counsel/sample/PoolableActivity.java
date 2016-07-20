package com.deezer.android.counsel.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deezer.android.counsel.interfaces.Poolable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static butterknife.ButterKnife.bind;

/**
 * @author Xavier Gouchet
 */
public class PoolableActivity extends AppCompatActivity {

    private final List<Item> items = new ArrayList<>();

    @BindView(R.id.poolable_items_list)
    RecyclerView recyclerView;

    ItemsAdapter adapter = new ItemsAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_poolable);

        bind(this);
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.poolable_add_button)
    void onAddItem() {
        int insertedIndex = items.size();
        items.add(new Item());
        adapter.notifyItemInserted(insertedIndex);
    }

    void onRemoveItem(Item item) {
        int position = items.indexOf(item);
        if (position < 0) return;
        
        items.remove(position);
        adapter.notifyItemRemoved(position);
        item.releaseInstance();
    }

    private static class Item implements Poolable {

        static int instancesCount = 0;

        private final int index;

        public Item() {
            index = instancesCount++;
        }

        @Override
        public void releaseInstance() {
        }

        @Override
        public String toString() {
            return String.format(Locale.US, "Item #%d (@%x)", index, System.identityHashCode(this));
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(android.R.id.text1)
        TextView text;

        private Item item;

        public ItemViewHolder(View itemView) {
            super(itemView);
            bind(this, itemView);
        }

        public void bindItem(Item item) {
            this.item = item;
            text.setText(item.toString());
        }

        @OnClick(android.R.id.text1)
        public void onClickItem() {
            onRemoveItem(item);
        }
    }

    private class ItemsAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            Item item = items.get(position);
            holder.bindItem(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

}
