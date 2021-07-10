package com.mcgod.newtowndb;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemDecorationMonth extends RecyclerView.ItemDecoration {

    private final int headerOffset;
    private final boolean sticky;
    private final SectionCallback sectionCallback;
    RecyclerView rvs = null;

    private final SimpleDateFormat sdf = new SimpleDateFormat("M");

    private View headerView;
    private TextView day, week;

    public ItemDecorationMonth(int headerHeight, boolean sticky, SectionCallback sectionCallback) {
        headerOffset = headerHeight;
        this.sticky = sticky;
        this.sectionCallback = sectionCallback;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);


        int pos = parent.getChildAdapterPosition(view);
        if (sectionCallback.isSection(pos)) {
            outRect.top = headerOffset;
        }else{

        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {


        super.onDrawOver(c,
                parent,
                state);


        this.rvs = parent;

        if (headerView == null) {
            headerView = inflateHeaderView(parent);
            day = headerView.findViewById(R.id.day);
            week = headerView.findViewById(R.id.week);

            fixLayoutSize(headerView,
                    parent);
        }

        CharSequence previousHeader = "";
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);

            CharSequence title = sectionCallback.getSectionHeader(position);

          /*  if(new Dates(title.toString()).getMonthYear().equals(
                    new Dates(new Date()).getMonthYear())){

                day.setTextColor(Color.parseColor("#000077"));
                week.setTextColor(Color.parseColor("#3c3f41"));

            }else{

                day.setTextColor(Color.parseColor("#c5c5f4"));
                week.setTextColor(Color.parseColor("#8c9194"));


            }*/

            if(current(title.toString())){
                Spannable spannable = new SpannableString(new DateUtils(title.toString()).getDaySpecial());
                spannable.setSpan(new RelativeSizeSpan(0.7f),1, 3,  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // set size


                day.setText(spannable);
                week.setText("Wk.");

            }else{
                day.setText(new DateUtils(title.toString()).getDay());
                week.setText(new DateUtils(title.toString()).getWeek());
            }




            if (!(previousHeader.equals(title.toString()) || sameWeek(previousHeader.toString(), title.toString()))


                    || sectionCallback.isSection(position)) {
                drawHeader(c,
                        child,
                        headerView);
                previousHeader = title.toString();
            }



        }
    }

    private void drawHeader(Canvas c, View child, View headerView) {
        c.save();



        if (sticky) {
            c.translate(0,
                    Math.max(0,
                            child.getTop() - headerView.getHeight()));
        } else {





            c.translate(0,
                    child.getTop() - headerView.getHeight());
        }
        headerView.draw(c);
        c.restore();
    }

    private View inflateHeaderView(RecyclerView parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_row_dates,
                        parent,
                        false);
    }

    /**
     * Measures the header view to make sure its size is greater than 0 and will be drawn
     * https://yoda.entelect.co.za/view/9627/how-to-android-recyclerview-item-decorations
     */

    private void fixLayoutSize(View view, ViewGroup parent) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(),
                View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(),
                View.MeasureSpec.UNSPECIFIED);

        int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                parent.getPaddingLeft() + parent.getPaddingRight(),
                view.getLayoutParams().width);
        int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                parent.getPaddingTop() + parent.getPaddingBottom(),
                view.getLayoutParams().height);

        view.measure(childWidth,
                childHeight);

        view.layout(0,
                0,
                view.getMeasuredWidth(),
                view.getMeasuredHeight());
    }

    public interface SectionCallback {

        boolean isSection(int position);

        CharSequence getSectionHeader(int position);
    }


    public boolean current(String c1){

        return (new DateUtils(c1).getMonth().equals(sdf.format(new Date())));

    }

    public boolean sameWeek(String c1, String c2){

        return (current(c1) && current(c2)) && (new DateUtils(c1).getWeekNumber() == ( new DateUtils(c2).getWeekNumber()));

    }







}