package com.example.helloworld;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FruitAdapter extends ArrayAdapter<Fruit> {

    private int resourceId;

    //初始化FruitAdapter会将Fruit对象传进来
    public FruitAdapter(Context context, int textViewResourceId, List<Fruit> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;    //R.layout.fruit_item，列表数据展示的内容
    }

    /**
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //getView的作用是，拿到数据和布局，将数据放到布局中，返回布局（view）

        //在滑动list展示到某一条的时候，getView将被调用，这里返回的是一个view，用于展示
        Fruit fruit = getItem(position); //获取当前的fruit实例

        //获取view（初始化FruitAdapter时，对应的是R.layout.fruit_item）
        View view;
        if (convertView == null) {
            //convertView用于将之前加载好的布局进行缓存，提高ListView的展示效率
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }
        //从R.layout.fruit_item布局文件获取控件
        //每次getView()的时候，都要调用findViewById，可以通过ViewHolder优化，可以参考新的代码
        ImageView fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
        TextView fruitText = (TextView) view.findViewById(R.id.fruit_name);
        //给控件动态设置参数
        fruitImage.setImageResource(fruit.getImageId());
        fruitText.setText(fruit.getName());

        //滑动展示列表下一行的时候，展示这个view，也就是R.layout.fruit_item
        return view;
    }**/

    //下面加入了ViewHolder方式，优化findViewById
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //getView的作用是，拿到数据和布局，将数据放到布局中，返回布局（view）

        //在滑动list展示到某一条的时候，getView将被调用，这里返回的是一个view，用于展示
        Fruit fruit = getItem(position); //获取当前的fruit实例
        //获取view（初始化FruitAdapter时，对应的是R.layout.fruit_item）
        View view;
        //初始化内部类ViewHolder，用于保存控件
        ViewHolder viewHolder = null;

        if (convertView == null) {
            //convertView用于将之前加载好的布局进行缓存，提高ListView的展示效率
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.fruit_image);
            viewHolder.textView = (TextView) view.findViewById(R.id.fruit_name);
            view.setTag(viewHolder);    //将viewHolder存储到view中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();    //只要view存在，重新获取ViewHolder
        }
        //给控件动态设置参数
        viewHolder.imageView.setImageResource(fruit.getImageId());
        viewHolder.textView.setText(fruit.getName());

        //滑动展示列表下一行的时候，展示这个view，也就是R.layout.fruit_item
        return view;
    }

    class ViewHolder{
        //内部类
        ImageView imageView;
        TextView textView;
    }


}
