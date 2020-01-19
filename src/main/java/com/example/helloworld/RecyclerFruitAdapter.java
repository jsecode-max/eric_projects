package com.example.helloworld;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecyclerFruitAdapter extends RecyclerView.Adapter<RecyclerFruitAdapter.ViewHolder> {

    private List<Fruit> mFruitList;

    public RecyclerFruitAdapter(List<Fruit> fruitList) {
        // 构造函数
        mFruitList = fruitList;
    }

    /**
     * 增加数据，通用写法(第一行 不含)
     */
    public void addData(int position) {
        mFruitList.add(position, new Fruit("Watermelon", R.drawable.fruit_pic));
        notifyItemInserted(position);//注意这里，通知animator
    }

    /**
     * 移除数据(第一行 不含)
     */
    public void removeData(int position) {
        mFruitList.remove(position);
        notifyItemRemoved(position);//注意这里，通知animator
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // 保存控件
        ImageView fruitImage;
        TextView fruitText;

        // 用于注册事件监听
        View fruitView;

        public ViewHolder(View view) {
            // 构造，view从onCreateViewHolder中构造，并返回
            super(view);
            fruitView = view;
            fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            fruitText = (TextView) view.findViewById(R.id.fruit_name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);   // 构造ViewHolder，传入了子项的view，构造的时候把控件也加载进去
        holder.fruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取点击的position，getAdapterPosition来自RecyclerView
                int position = holder.getAdapterPosition();
                // 通过position获取相应的Fruit实例
                Fruit fruit = mFruitList.get(position);
                Toast.makeText(v.getContext(), "you clicked view " + fruit.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        holder.fruitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Fruit fruit = mFruitList.get(position);
                Toast.makeText(v.getContext(), "you clicked image " + fruit.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        return holder;  // 这个holder里面保存了控件，给后续的onBindViewHolder使用
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 类似 ListView的getView
        Fruit fruit = mFruitList.get(position);
        holder.fruitImage.setImageResource(fruit.getImageId());
        holder.fruitText.setText(fruit.getName());
    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }
}
