package com.huiseong.blockphone.adaters;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Toast;

import com.huiseong.blockphone.DB.DBHelper;
import com.huiseong.blockphone.R;
import com.huiseong.blockphone.item.AlarmListItem;
import com.huiseong.blockphone.models.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    //private final List<AlarmListItem> items;
    List<AlarmListItem> items;
    private SQLiteDatabase sqlDB;
    Cursor cursor;

    public interface MyRecyclerViewClickListener{
        void onItemClicked(int position);
        void onSwitchClicked(int position, int alarmID);
        void onMenuClicked(int position);
    }

    private  MyRecyclerViewClickListener mListener;

    public  void setOnClickListener(MyRecyclerViewClickListener listener ){
        mListener=listener;
    }

        public MyAdapter(List<AlarmListItem> dataItems) {
            items = dataItems;
        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.alarm_list_item,viewGroup, false);
        return new ViewHolder(view);
    }
    //여기서 position값이 있어서 해당 아이템에 일일이 뿌려주는 역할을 한다. 여기서 numbering을 한다.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) { //포지션은 1,2,3 순서이다.
        AlarmListItem item = items.get(position);//개별 아이템과 홀더에 묶어준다. // 홀더는 아이템을 담는 틀이다.

        viewHolder.timeRange.setText(item.getTimeRange2());//item class에서 title값을 담아서 holde.title에 대입해준다.
        viewHolder.day.setText(item.getDay2());

        final int alarmID=item.getAlarmID();

        int swiOnoff=item.getSwiOnoff();

//        DBHelper myHelper = new DBHelper(this);
//        sqlDB = myHelper.getWritableDatabase();
//        cursor = sqlDB.rawQuery("SELECT * FROM alarmList where id='+alarmID+';",null);
//        //지금 cursor안에 아무것도 담기지 않은 것 같다. sqlite3로 체크를 해보자.
//        cursor.moveToFirst();

//        int swiOnoff2=cursor.getInt(0);

        if(swiOnoff==1){
            viewHolder.alarmOnoff.setChecked(true);//on이면 on해준다.
        }else {
            viewHolder.alarmOnoff.setChecked(false);
        }//

        // 클릭 이벤트 // 지금 내가 viewHolder의 원리를 이해 못하고 있기 때문에 잘못된 값을 건네주고 있는 것 같다.
        if (mListener != null) {
            // 현재 위치
            final int pos = viewHolder.getAdapterPosition(); // 클릭하는 순간 pos를 받는 것이다.
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(pos);
                }
            });
            viewHolder.alarmOnoff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Toast.makeText(MainActivity.this, "시작시간과 종료시간이 동일합니다", Toast.LENGTH_LONG).show();
                    mListener.onSwitchClicked(pos, alarmID);
                }
            });
            viewHolder.alarmMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onMenuClicked(pos);
                }
            });
        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView timeRange;
        TextView day;
        Switch alarmOnoff;
        ImageButton alarmMenu;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeRange = (TextView)itemView.findViewById(R.id.timeRange);
            day = (TextView) itemView.findViewById(R.id.day);
            alarmOnoff=(Switch) itemView.findViewById(R.id.alarmOnoff);
            alarmMenu=(ImageButton) itemView.findViewById(R.id.alarmMenu);
        }
    }

    public void add(int position, AlarmListItem item) {
        items.add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, items.size());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //내부 아이템을 클릭할 경우 이벤트가 발생해야한다. // 3가지 정도의 버튼이 있다.
    RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };

}//end
