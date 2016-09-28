package com.mansetong;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ChineseCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManseCalendar extends AppCompatActivity
{
    /*연/월 텍스트뷰*/
    private TextView tvDate;

    /*그리드뷰 어댑터*/
    private GridAdapter gridAdapter;

    /* 일 저장 할 리스트*/
    private ArrayList<String> dayList;

    /*그리드뷰*/
    private GridView gridView;

    /* 캘린더 변수*/
    private java.util.Calendar mCal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manse_calendar);

        gridView = (GridView)findViewById(R.id.gridView);

        tvDate = (TextView)findViewById(R.id.YearAndMonth);

        // 오늘에 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        // 현재 날짜 텍스트뷰에 뿌려줌
        tvDate.setText(curYearFormat.format(date) + "년 " + curMonthFormat.format(date) + "월");

        //gridview 요일 표시
        dayList = new ArrayList<String>();

        mCal = java.util.Calendar.getInstance();

        // 이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);

        int dayNum = mCal.get(java.util.Calendar.DAY_OF_WEEK);

        // 1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++)
            dayList.add("");

        setCalendarDate(mCal.get(java.util.Calendar.MONTH) + 1);

        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);

    }

    /* 해당 월에 표시할 일 수 구함 */
    private void setCalendarDate(int month)
    {
        mCal.set(java.util.Calendar.MONTH, month - 1);

        for (int i = 0; i < mCal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH); i++)
            dayList.add("" + (i + 1));
    }

    /*그리드뷰 어댑터*/
    private class GridAdapter extends BaseAdapter
    {
        private final List<String> list;
        private final LayoutInflater inflater;

        /* 생성자*/
        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                holder = new ViewHolder();
                holder.tvItemGridView = (TextView)convertView.findViewById(R.id.tv_item_gridview);
                convertView.setTag(holder);
            }
            else
                holder = (ViewHolder)convertView.getTag();

            holder.tvItemGridView.setText("" + getItem(position));

            //해당 날짜 텍스트 컬러,배경 변경
            mCal = java.util.Calendar.getInstance();

            //오늘 day 가져옴
            Integer today = mCal.get(java.util.Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);

            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvItemGridView;
    }
}