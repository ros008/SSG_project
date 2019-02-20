package com.example.kimsujin.ssg_project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){

        this.context = context;

    }

    //Arrays
    public int[] slide_images = {
            R.drawable.online_shop,
            R.drawable.good,
            R.drawable.diagram
    };

    public String[] slide_headings = {
            "편리한 장보기",
            "맞춤 상품 추천",
            "맞춤 소비자 물가 지수"
    };

    public String[] slide_descs = {
            "최근 상품의 등락률이 컸던 제품과 할인률이 높은 제품을 추천해드립니다.",
            "선호 품목을 선택하면, 이를 바탕으로 소비자 맞춤 상품을 추천해드립니다.",
            "소비자의 선호 품목을 기반으로 직접 체감할 수 있는 소비자 지수를 계산해드립니다."
    };


    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    };

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((RelativeLayout)object);
    }
}
