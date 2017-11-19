package lk.ac.mrt.cse.medipal.adaptor;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import lk.ac.mrt.cse.medipal.R;
import lk.ac.mrt.cse.medipal.constant.Common;
import lk.ac.mrt.cse.medipal.model.AllergyNotification;
import lk.ac.mrt.cse.medipal.model.Notification;
import lk.ac.mrt.cse.medipal.model.ShareNotification;

/**
 * Created by lakshan on 11/19/17.
 */

public class NotificationRecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Notification> notificationList;
    private Context context;

    public NotificationRecyclerViewAdaptor(Context context, ArrayList<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row_layout, parent, false);
                return new NotificationRecyclerViewAdaptor.NotificationRecyclerViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row_layout, parent, false);
                return new NotificationRecyclerViewAdaptor.NotificationRecyclerViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row_layout, parent, false);
                return new NotificationRecyclerViewAdaptor.NotificationRecyclerViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        switch (position) {
//            case 0:
//                NotificationRecyclerViewAdaptor.NotificationRecyclerViewHolder infoHolder = ((NotificationRecyclerViewAdaptor.NotificationRecyclerViewHolder)holder);
//                break;
//            case 1:
//                NotificationRecyclerViewAdaptor.NotificationRecyclerViewHolder medHolder = ((NotificationRecyclerViewAdaptor.NotificationRecyclerViewHolder)holder);
//        }
        NotificationRecyclerViewAdaptor.NotificationRecyclerViewHolder viewHolder = ((NotificationRecyclerViewAdaptor.NotificationRecyclerViewHolder)holder);
        Notification notification = notificationList.get(position);
        viewHolder.txt_msg.setText(notification.getMessage());
        viewHolder.txt_time.setText(getTimeElapsed(notification.getTime()));
        if (notificationList.get(position) instanceof ShareNotification){
            viewHolder.icon_user.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setOldController(viewHolder.icon_user.getController())
                            .setUri(notification.getPatient().getImage())
                            .build());
            viewHolder.icon_type.setIconResource("\uf1d8");
            viewHolder.icon_type.setBackgroundColor(context.getResources().getColor(R.color.met_baseColorBlue));
        } else if (notificationList.get(position) instanceof AllergyNotification){
            viewHolder.icon_user.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setOldController(viewHolder.icon_user.getController())
                            .setUri(notification.getPatient().getImage())
                            .build());
            viewHolder.icon_type.setIconResource("\uf071");
            viewHolder.icon_type.setBackgroundColor(context.getResources().getColor(R.color.red));
        } else {
            viewHolder.icon_user.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setOldController(viewHolder.icon_user.getController())
                            .setUri(notification.getPatient().getImage())
                            .build());
            viewHolder.icon_type.setIconResource("\uf044");
            viewHolder.icon_type.setBackgroundColor(context.getResources().getColor(R.color.met_primaryColor));
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotificationRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RelativeLayout notification_layout;
        private SimpleDraweeView icon_user;
        private TextView txt_msg;
        private TextView txt_time;
        private MaterialFancyButton icon_type;
        public NotificationRecyclerViewHolder(View itemView) {
            super(itemView);
            icon_user = itemView.findViewById(R.id.icon_user);
            txt_msg = itemView.findViewById(R.id.txt_msg);
            txt_time = itemView.findViewById(R.id.txt_time);
            icon_type = itemView.findViewById(R.id.icon_type);
            notification_layout = itemView.findViewById(R.id.notification_layout);

            notification_layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.notification_layout:

                    break;
            }
        }


    }
//    public class PrescribeRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//
//        public PrescribeRecyclerViewHolder(View itemView) {
//            super(itemView);
//        }
//
//        @Override
//        public void onClick(View view) {
//        }
//    }
//
//    public class AllergyRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//
//        public AllergyRecyclerViewHolder(View itemView) {
//            super(itemView);
//        }
//
//        @Override
//        public void onClick(View view) {
//        }
//    }
    private String getTimeElapsed(String timeString){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(Common.Time.DATE_FORMAT, Locale.ENGLISH);
        try {
            Date startDate = simpleDateFormat.parse(timeString);
            Date endDate = new Date();

            long different = endDate.getTime() - startDate.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            if (elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes == 0){
                timeString = String.format(Locale.ENGLISH,
                        Common.Time.SECONDS_AGO,elapsedSeconds);
            } else if (elapsedDays == 0 && elapsedHours == 0) {
                timeString = String.format(Locale.ENGLISH,
                        Common.Time.MINS_AGO,elapsedMinutes);
            } else if (elapsedDays == 0){
                timeString = String.format(Locale.ENGLISH,
                        Common.Time.HOURS_AGO,elapsedHours);
            } else {
                timeString = String.format(Locale.ENGLISH,
                        Common.Time.DAYS_AGO,elapsedDays);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeString;
    }
}

