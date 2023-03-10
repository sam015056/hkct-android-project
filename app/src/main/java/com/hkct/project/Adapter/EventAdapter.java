package com.hkct.project.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hkct.project.CommentsActivity;
import com.hkct.project.Model.Event;
import com.hkct.project.Model.Users;
import com.hkct.project.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{
    private List<Event> mList;
    private List<Users> usersList;
    private Activity context;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String Uid;

    public EventAdapter(Activity context, List<Event> mList, List<Users> usersList) {
        this.mList = mList;
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_post, parent, false);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        Uid = auth.getCurrentUser().getUid();
        return new EventAdapter.EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, int position) {
        Event event = mList.get(position);
        holder.setEventPic(Event.getImage_event());
        holder.setEventCaption(Event.getCaption_event());

//        long milliseconds = Event.getTime_event().getTime();
////        String date = DateFormat.getDateInstance().format(new Date(milliseconds));
//        holder.setEventDate(date);

        String username = usersList.get(position).getName();
        String image = usersList.get(position).getImage();

        holder.setProfilePic(image);
        holder.setEventUsername(username);


        // like btn
        String EventId = event.EventId;
        String currentUserId = auth.getCurrentUser().getUid();
        holder.likePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("Events/" + EventId + "/Joins").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()) {
                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());
                            firestore.collection("Events/" + EventId + "/Joins").document(currentUserId).set(likesMap);
                        } else {
                            firestore.collection("Events/" + EventId + "/Joins").document(currentUserId).delete();
                        }
                    }
                });
            }
        });

        // like btn color change
        firestore.collection("Events/" + EventId + "/Joins").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    if (value.exists()) {
                        holder.likePic.setImageDrawable(context.getDrawable(R.drawable.after_liked));
                    } else {
                        holder.likePic.setImageDrawable(context.getDrawable(R.drawable.before_liked));
                    }
                }
            }
        });

        // likes count
        firestore.collection("Events/" + EventId + "/Joins").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    if (!value.isEmpty()) {
                        int count = value.size();
                        holder.setEventLikes(count);
                    } else {
                        holder.setEventLikes(0);
                    }
                }
            }
        });

        //comments implementation
//        holder.commentsPic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent commentIntent = new Intent(context, CommentsActivity.class);
//                commentIntent.putExtra("eventid", EventId);
//                context.startActivity(commentIntent);
//            }
//        });

        if (currentUserId.equals(event.getUser_event())) {

            firestore.collection("Users").document(Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            String memberShip = task.getResult().getString("membership");
                            if (memberShip.equals("1")) {
                                holder.membershipIcon.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });

            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setClickable(true);
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Delete")
                            .setMessage("Are you sure?")
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    firestore.collection("Events/" + EventId + "/Comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                firestore.collection("Events/" + EventId + "/Comments").document(snapshot.getId()).delete();
                                            }
                                        }
                                    });
                                    firestore.collection("Events/" + EventId + "/Joins").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                firestore.collection("Events/" + EventId + "/Joins").document(snapshot.getId()).delete();
                                            }
                                        }
                                    });
                                    firestore.collection("Events").document(EventId).delete();
                                    mList.remove(position);
                                    notifyDataSetChanged();
                                }
                            });
                    alert.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventPic, commentsPic, likePic;
        CircleImageView profilePic;
        TextView eventUsername, eventDate, eventCaption, eventLikes;
        ImageView deleteBtn;
        ImageView membershipIcon;

        View mView;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            likePic = mView.findViewById(R.id.like_btn);
            commentsPic = mView.findViewById(R.id.comments_event);
            deleteBtn = mView.findViewById(R.id.delete_btn);
            membershipIcon = mView.findViewById(R.id.membership_icon_post);
        }

        public void setEventLikes(int count) {
            eventLikes = mView.findViewById(R.id.like_count_tv);
            eventLikes.setText(count + " Likes");
        }

        public void setEventPic(String urlPost) {
            eventPic = mView.findViewById(R.id.user_post);
            Glide.with(context).load(urlPost).into(eventPic);
        }

        public void setProfilePic(String urlProfile) {
            profilePic = mView.findViewById(R.id.profile_pic);
            Glide.with(context).load(urlProfile).into(profilePic);
        }

        public void setEventUsername(String username) {
            eventUsername = mView.findViewById(R.id.username_tv);
            eventUsername.setText(username);
        }

        public void setEventDate(String date) {
            eventDate = mView.findViewById(R.id.date_tv);
            eventDate.setText(date);
        }

        public void setEventCaption(String caption) {
            eventCaption = mView.findViewById(R.id.caption_tv);
            eventCaption.setText(caption);
        }
    }
}
