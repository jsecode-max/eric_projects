package com.example.helloworld;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewsTitleFragment extends Fragment {

    private Boolean isTwoPane;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_title_frag, container, false);

        RecyclerView newsTitleRecyclerView = (RecyclerView) view.findViewById
                (R.id.news_title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()); //Frame中用getActivity获取Context
        newsTitleRecyclerView.setLayoutManager(layoutManager);
        NewsAdapter adapter = new NewsAdapter(getNews());
        newsTitleRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //当关联的Activity创建完成。判断是否双页，主要是判断是否存在news_content_layout（在sw600dp/acvitity_main9.xml定义的双页）
        if (getActivity().findViewById(R.id.news_content_layout) != null) {
            isTwoPane = true;
        } else {
            isTwoPane = false;
        }
    }

    private List<News> getNews() {
        //初始化新闻
        List<News> newsList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            News news = new News();
            news.setTitle("This is news title " + i);
            news.setContent(getRandomLengthContent("This is news content" + i + ". "));
            newsList.add(news);
        }
        return newsList;
    }

    private String getRandomLengthContent(String content) {
        //新闻内容的随机长度
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(content);
        }
        return builder.toString();
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

        private List<News> mNewsList;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView newsTitleText;

            public ViewHolder(@NonNull View view) {
                super(view);
                newsTitleText = (TextView) view.findViewById(R.id.news_title);
            }
        }

        public NewsAdapter(List<News> mNewsList) {
            this.mNewsList = mNewsList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //获取子项view
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,
                    parent, false);
            //子项view传给ViewHolder初始化，获取ViewHolder实例
            final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //根据点击的ViewHolder中子项的位置
                    News news = mNewsList.get(holder.getAdapterPosition());
                    if (isTwoPane) {
                        //如果是双页
                        //通过getFragmentManager，在Fragment中获取其他Fragment的实例（在Activity中使用的是getSupportFragmentManager）
                        NewsContentFragment newsContentFragment =
                                (NewsContentFragment) getFragmentManager()
                                        .findFragmentById(R.id.news_content_fragment);
                        newsContentFragment.refresh(news.getTitle(), news.getContent());
                    } else {
                        //如果是单页
                        NewsContentActivity.actionStart(getActivity(),
                                news.getTitle(), news.getContent());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            News news = mNewsList.get(position);
            holder.newsTitleText.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }
}
