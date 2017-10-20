package com.example.vamsi.firebaseauth1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by VAMSI on 10-03-2017.
 */

public class CouponsScreen extends AppCompatActivity {

    CouponDetails maxCountCoupon;
    DatabaseReference couponReference;
    String brandName,smallLogoUrl,time,description,hour,minutes;
    int maxCount=0;

    ImageView thumbnail, dots;
    TextView couponDescription,couponExpiry;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    DatabaseReference rootReference;
    FirebaseAuth firebaseAuth;
    boolean oneDay,twoDays,threeDays,status;
    ReminderSettings reminderSettings;
    int count=10;

    PopupMenu popup;
    RelativeLayout relativeLayout;
    String couponID, coupondescription, couponterms, couponexpiry, couponURL;
    //Boolean wishlist,reminder,nevershow;
    int couponcount;

    FirebaseRecyclerAdapter<CouponDetails,CouponDetailsViewHolder> adapter;
    boolean flag;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupons_layout);

        sharedPreferences = getSharedPreferences("MyPreferences",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        firebaseAuth = FirebaseAuth.getInstance();

        maxCountCoupon = new CouponDetails();
        Bundle b = getIntent().getExtras();
        brandName = b.getString("brandName");
        smallLogoUrl = b.getString("smallLogoUrl");

        editor.putString("brandName",brandName);
        editor.commit();

        //Views of maxCountCoupon
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        couponDescription = (TextView) findViewById(R.id.couponDescription);
        couponExpiry = (TextView) findViewById(R.id.couponExpiry);

        rootReference = FirebaseDatabase.getInstance().getReference();

        getMaxCountCoupon();

        Query couponOrder = couponReference.orderByChild("count");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));

        adapter = new FirebaseRecyclerAdapter<CouponDetails, CouponDetailsViewHolder>
                (CouponDetails.class,R.layout.coupons_card_view,CouponDetailsViewHolder.class,couponOrder) {
            @Override
            protected void populateViewHolder(final CouponDetailsViewHolder viewHolder, final CouponDetails model, final int position) {

                //when the count values of the coupons are initially '0', maxCountCoupon is not assigned a valid object value

                    if (!maxCountCoupon.getCouponId().equals(model.getCouponId())) {
                        StorageReference st = FirebaseStorage.getInstance().getReferenceFromUrl(smallLogoUrl);
                        viewHolder.setImageView(getApplicationContext(), st);
                        viewHolder.couponDescription.setText(model.getdescription());
                        viewHolder.couponExpiry.setText(model.getExpiry());



                        viewHolder.v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                couponReference.child(model.couponKeys[position]).child("count").setValue(model.getCount() + 1);
                                Intent i = new Intent(getApplicationContext(), CouponLayoutSingle.class);

                                i.putExtra("couponID", model.getCouponId());
                                i.putExtra("brandName", model.getdescription());
                                i.putExtra("terms", model.getTerms());
                                i.putExtra("url", model.getUrl());
                                startActivity(i);
                            }
                        });

                    }

                    else
                    {
                        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewHolder.v.getLayoutParams();
                        layoutParams.width = 0;
                        layoutParams.height = 0;
                        viewHolder.v.setLayoutParams(layoutParams);
                    }





            }

            @Override
            public CouponDetails getItem(int position) {

                 return super.getItem(getItemCount()-position-1);
            }
        };

        recyclerView.setAdapter(adapter);

        //adapter.notifyDataSetChanged();
    }

    public void getMaxCountCoupon()
    {
        maxCount = -1;
        couponReference = FirebaseDatabase.getInstance().getReference().child("Coupons").child(firebaseAuth.getCurrentUser().getUid())
                .child(brandName);
        couponReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CouponDetails couponDetails = dataSnapshot.getValue(CouponDetails.class);
                if(couponDetails.getCount() > maxCount) {
                    maxCount = couponDetails.getCount();
                    couponDetails.setCouponKey(dataSnapshot.getKey());
                    maxCountCoupon = couponDetails;
                }

                //when the count values of the coupons are initially '0',

                    StorageReference maxCouponUrl = FirebaseStorage.getInstance().getReferenceFromUrl(maxCountCoupon.getUrl());

                    Glide.with(getApplicationContext())
                            .using(new FirebaseImageLoader())
                            .load(maxCouponUrl)
                            .into(thumbnail);
                    couponID = couponDetails.getCouponId();
                    coupondescription = couponDetails.getdescription();
                    couponcount = couponDetails.getCount();
                    couponexpiry = couponDetails.getExpiry();
                    couponterms = couponDetails.getTerms();
                    couponURL = couponDetails.getUrl();
                    //wishlist = couponDetails.getWishlist();
                    //reminder = couponDetails.getReminder();
                    //nevershow = couponDetails.getNevershow();
                    couponDescription.setText(maxCountCoupon.getdescription());
                    couponExpiry.setText(maxCountCoupon.getExpiry());

            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //setMaxCountCoupon();
    }

    /*public void setMaxCountCoupon()
    {

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();

        rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid()).child(brandName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getMaxCountCoupon();
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout1);
        dots = (ImageView) findViewById(R.id.dots);

        final Query couponsOrder = couponReference.orderByChild("count");

        adapter = new FirebaseRecyclerAdapter<CouponDetails, CouponDetailsViewHolder>
                (CouponDetails.class,R.layout.coupons_card_view,CouponDetailsViewHolder.class,couponsOrder) {
            @Override
            protected void populateViewHolder(final CouponDetailsViewHolder viewHolder, final CouponDetails model, final int position) {
                if(!maxCountCoupon.getCouponId().equals(model.getCouponId())) {
                    StorageReference st = FirebaseStorage.getInstance().getReferenceFromUrl(smallLogoUrl);
                    viewHolder.setImageView(getApplicationContext(), st);
                    viewHolder.couponDescription.setText(model.getdescription());
                    viewHolder.couponExpiry.setText(model.getExpiry());

                    setReminderNotification(model.getExpiry(),model);

                    //For popup menu
                    viewHolder.dotsImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            popup = new PopupMenu(getApplicationContext(), viewHolder.dotsImageView);
                            popup.getMenuInflater().inflate(R.menu.actions, popup.getMenu());

                            //Inflating the Popup using xml file

                            //PopupMenu popup = new PopupMenu(getApplicationContext(), viewHolder.dotsImageView);
                            //Inflating the Popup using xml file
                            final Menu actions = popup.getMenu();

                            //TODO do the same for reminders too
//                            rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid())
//                                    .child(brandName).child(model.getCouponId()).child("wishList").addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    boolean wishlist = (boolean) dataSnapshot.getValue();
//                                    if(wishlist)
//                                    {
//                                        actions.findItem(R.id.add_to_wishlist).setVisible(false);
//                                        actions.findItem(R.id.removeFromWishList).setVisible(true);
//                                    }
//
//                                    if(!wishlist)
//                                    {
//                                        actions.findItem(R.id.add_to_wishlist).setVisible(true);
//                                        actions.findItem(R.id.removeFromWishList).setVisible(false);
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });

                            rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid()).child(brandName)
                                    .child(model.getCouponId()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            CouponDetails2 couponDetails = dataSnapshot.getValue(CouponDetails2.class);

                                            if(couponDetails.isWishlist())
                                            {
                                                actions.findItem(R.id.add_to_wishlist).setVisible(false);
                                                actions.findItem(R.id.removeFromWishList).setVisible(true);
                                            }

                                            else
                                            {
                                                actions.findItem(R.id.add_to_wishlist).setVisible(true);
                                                actions.findItem(R.id.removeFromWishList).setVisible(false);
                                            }

                                            if(couponDetails.isNevershow())
                                            {
                                                actions.findItem(R.id.never_show_again).setVisible(false);
                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                            popup.show();//showing popup menu*/

                            //registering popup with OnMenuItemClickListener
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {
                                    int id=item.getItemId();
                                    if(id==R.id.reminder){
                                        Intent r= new Intent(getApplicationContext(), SetReminder.class);
                                        r.putExtra("couponID",model.getCouponId());
                                        r.putExtra("type","specific");
                                        startActivity(r);
                                    }
                                    if(id== R.id.add_to_wishlist){

                                        editor.putString("couponID",model.getCouponId());
                                        editor.apply();
                                        addToWishList();
                                    }
                                    if(id== R.id.removeFromWishList) {

                                        rootReference.child("WishLists").child(firebaseAuth.getCurrentUser().getUid()).child(model.getCouponId()).removeValue();
                                        rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid()).child(brandName).
                                                child(model.getCouponId()).child("wishlist").setValue(false);
                                    }
                                    if(id== R.id.redeem_coupon){
                                        Intent rc =new Intent(getApplicationContext(), RedeemCoupon.class);
                                        rc.putExtra("couponID",model.getCouponId());
                                        rc.putExtra("source","fromCoupon");
                                        rc.putExtra("brandName",brandName);
                                        startActivity(rc);
                                    }
                                    if(id== R.id.never_show_again){
                                        rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid()).child(brandName).
                                                child(model.getCouponId()).removeValue();

                                    }

                                    return true;
                                }
                            });


                        }
                    });


                    //Coupon OnClick event
                    viewHolder.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //couponReference.child(model.couponKeys[position]).child("count").setValue(model.getCount()+1);
                            couponReference.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    if(dataSnapshot.getValue(CouponDetails.class).getCouponId().equals(model.getCouponId()))
                                        couponReference.child(dataSnapshot.getKey()).child("count").setValue(model.getCount()+1);
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            Intent i = new Intent(getApplicationContext(), CouponLayoutSingle.class);
                            i.putExtra("brandName", brandName);
                            i.putExtra("terms", model.getTerms());
                            i.putExtra("url", model.getUrl());
                            i.putExtra("couponID",model.getCouponId());
                            editor.putString("couponID",model.getCouponId());
                            editor.apply();
                            startActivity(i);

                        }
                    });
                }
                else {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewHolder.v.getLayoutParams();
                    layoutParams.width=0;
                    layoutParams.height= 0;
                    viewHolder.v.setLayoutParams(layoutParams);

                }
            }

            @Override
            public CouponDetails getItem(int position) {
                return super.getItem(getItemCount()-position-1);
            }
        };

        recyclerView.setAdapter(adapter);


       relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                couponReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.getValue(CouponDetails.class).getCouponId().equals(couponID))
                            couponReference.child(dataSnapshot.getKey()).child("count").setValue(couponcount+1);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Intent i = new Intent(getApplicationContext(), CouponLayoutSingle.class);
                i.putExtra("brandName", coupondescription);
                i.putExtra("terms", couponterms);
                i.putExtra("url", couponURL);
                i.putExtra("couponID",couponID);
                editor.putString("couponID",couponID);
                editor.apply();
                startActivity(i);

            }
        });

        dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final PopupMenu popup = new PopupMenu(getApplicationContext(), dots);
                //Inflating the Popup using xml file

                //PopupMenu popup = new PopupMenu(getApplicationContext(), viewHolder.dotsImageView);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.actions, popup.getMenu());
                //Inflating the Popup using xml file

                //PopupMenu popup = new PopupMenu(getApplicationContext(), viewHolder.dotsImageView);
                //Inflating the Popup using xml file
                final Menu actions = popup.getMenu();

                rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid()).child(brandName).child(couponID)
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        CouponDetails2 couponDetails = dataSnapshot.getValue(CouponDetails2.class);
                        if(couponDetails.isWishlist())
                        {
                            actions.findItem(R.id.add_to_wishlist).setVisible(false);
                            actions.findItem(R.id.removeFromWishList).setVisible(true);
                        }

                        else
                        {
                            actions.findItem(R.id.add_to_wishlist).setVisible(true);
                            actions.findItem(R.id.removeFromWishList).setVisible(false);
                        }

                        if(couponDetails.isNevershow())
                        {
                            actions.findItem(R.id.never_show_again).setVisible(false);
                            rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid()).child(brandName).
                                    child(couponID).removeValue();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                popup.show();//showing popup menu*/


                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id=item.getItemId();
                        if(id==R.id.reminder){
                            Intent r= new Intent(getApplicationContext(), SetReminder.class);
                            //r.putExtra()
                            startActivity(r);
                        }
                        if(id== R.id.add_to_wishlist){

                            editor.putString("couponID",couponID);
                            editor.apply();
                            addToWishList();
                        }
                        if(id== R.id.removeFromWishList){


                            rootReference.child("WishLists").child(firebaseAuth.getCurrentUser().getUid()).child(couponID).removeValue();
                            rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid()).child(brandName).
                                    child(couponID).child("wishlist").setValue(false);
//                            rootReference.child("Images").child(brandName).addChildEventListener(new ChildEventListener() {
//                                @Override
//                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                                    final CouponDetails couponDetails = dataSnapshot.getValue(CouponDetails.class);
//                                    {
//                                        if (couponDetails.getCouponId().equals(couponID)) {
//                                            //Toast.makeText(CouponLayoutSingle.this, "Clicked CouponID : "+couponDetails.getCouponId(), Toast.LENGTH_SHORT).show();
//
//                                            Toast.makeText(CouponsScreen.this, "Removed from wishlist", Toast.LENGTH_SHORT).show();
//
//                                            rootReference.child("Images").child(brandName).child(dataSnapshot.getKey()).child("wishlist").setValue(false);
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                                }
//
//                                @Override
//                                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                                }
//
//                                @Override
//                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
                        }
                        if(id== R.id.redeem_coupon){
                            Intent rc =new Intent(getApplicationContext(), RedeemCoupon.class);
                            rc.putExtra("source","fromCoupon");
                            rc.putExtra("couponID",couponID);
                            rc.putExtra("brandName",brandName);
                            startActivity(rc);
                        }
                        if(id== R.id.never_show_again){

                            rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid()).child(brandName).child(couponID).removeValue();
                        }

                        //Toast.makeText(CouponsScreen.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

            }
        });

    }

    private void addToWishList() {


                rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid()).
                        child(sharedPreferences.getString("brandName","none"))
                        .child(sharedPreferences.getString("couponID","none")).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        CouponDetails2 couponDetails = dataSnapshot.getValue(CouponDetails2.class);

                        rootReference.child("WishLists").child(firebaseAuth.getCurrentUser().getUid()).child(sharedPreferences.getString("couponID","none"))
                                .setValue(couponDetails);
                        rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid()).
                                child(sharedPreferences.getString("brandName","none"))
                                .child(sharedPreferences.getString("couponID","none")).child("wishlist").setValue(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void setReminderNotification(final String expiry, final CouponDetails couponDetails)
    {

        //To get general settings :
        oneDay=twoDays=threeDays = false;

        //To get specific settings
        rootReference.child("Reminders").child(firebaseAuth.getCurrentUser().getUid())
                .child("SpecificSettings").child(couponDetails.getCouponId()).addValueEventListener(new ValueEventListener() {
            @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
                reminderSettings = dataSnapshot.getValue(ReminderSettings.class);

                if(reminderSettings != null)
                {
                    Toast.makeText(CouponsScreen.this, "Specific Settings : "+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();

                }
                else
                {
                    rootReference.child("Reminders").child(firebaseAuth.getCurrentUser().getUid())
                            .child("GeneralSettings").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            reminderSettings = dataSnapshot.getValue(ReminderSettings.class);

                            if(reminderSettings!=null)
                            {
                                String days[] = reminderSettings.getDays().split(",");

                                if(days[0].equals("true"))
                                    oneDay = true;
                                if(days[1].equals("true"))
                                    twoDays = true;
                                if(days[2].equals("true"))
                                    threeDays = true;

                                status = reminderSettings.isStatus();

                                time = reminderSettings.getTime();

                                String t[] = time.split(":");
                                hour = t[0];
                                minutes = t[1];

                                description = reminderSettings.getDescription();

                                Calendar calendar = Calendar.getInstance();

                                String exprirydate[] = expiry.split("-");


                                Calendar expirycalender = Calendar.getInstance();
                                expirycalender.set(Calendar.DATE,Integer.valueOf(exprirydate[0]));
                                expirycalender.set(Calendar.MONTH,Integer.valueOf(exprirydate[1]));
                                expirycalender.set(Calendar.YEAR,Integer.valueOf(exprirydate[2]));
                                expirycalender.set(Calendar.HOUR_OF_DAY,Integer.valueOf(hour));
                                expirycalender.set(Calendar.MINUTE,Integer.valueOf(minutes));
                                expirycalender.add(Calendar.MONTH,-1);
                                expirycalender.set(Calendar.SECOND,0);


                                if(status && calendar.get(Calendar.HOUR_OF_DAY)<=Integer.valueOf(hour) && calendar.get(Calendar.MINUTE)<=Integer.valueOf(minutes)) {
                                    if (oneDay) {

//                                        if(yearDiff ==0 && monthDiff ==1 && dayDiff==1)
                                        //calendar.add(Calendar.SECOND,5);
                                        expirycalender.add(Calendar.DATE,-1);
                                        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getBaseContext().ALARM_SERVICE);

                                        Intent intent = new Intent(CouponsScreen.this, AlarmBroadcastReceiver.class);
                                        intent.putExtra("description", description);
                                        intent.putExtra("name", couponDetails.getdescription());
                                        intent.putExtra("terms", couponDetails.getTerms());
                                        intent.putExtra("url", couponDetails.getUrl());
                                        intent.putExtra("couponID", couponDetails.getTerms());
                                        intent.putExtra("count", count);


                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), count++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        alarmManager.set(AlarmManager.RTC_WAKEUP, expirycalender.getTimeInMillis(), pendingIntent);
                                        expirycalender.add(Calendar.DATE,1);


                                        //}
                                    }


                                    if (twoDays) {

                                            expirycalender.add(Calendar.DATE, -2);

                                            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getBaseContext().ALARM_SERVICE);

                                            Intent intent = new Intent(CouponsScreen.this, AlarmBroadcastReceiver.class);
                                            intent.putExtra("description", description);
                                            intent.putExtra("name", couponDetails.getdescription());
                                            intent.putExtra("terms", couponDetails.getTerms());
                                            intent.putExtra("url", couponDetails.getUrl());
                                            intent.putExtra("couponID", couponDetails.getTerms());
                                            intent.putExtra("count", count);

                                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), count++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            alarmManager.set(AlarmManager.RTC_WAKEUP, expirycalender.getTimeInMillis(), pendingIntent);

                                            expirycalender.add(Calendar.DATE,2);


                                    }


                                    if (threeDays) {

                                            expirycalender.add(Calendar.DATE, -3);

                                            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getBaseContext().ALARM_SERVICE);

                                            Intent intent = new Intent(CouponsScreen.this, AlarmBroadcastReceiver.class);
                                            intent.putExtra("description", description);
                                            intent.putExtra("name", couponDetails.getdescription());
                                            intent.putExtra("terms", couponDetails.getTerms());
                                            intent.putExtra("url", couponDetails.getUrl());
                                            intent.putExtra("couponID", couponDetails.getTerms());
                                            intent.putExtra("count", count);

                                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), count++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            alarmManager.set(AlarmManager.RTC_WAKEUP, expirycalender.getTimeInMillis(), pendingIntent);

                                            expirycalender.add(Calendar.DATE,3);


                                    }


                                    //calendar.add(Calendar.SECOND, 5);

                                    //Code to fire send the alarm to the broadcast receiver

                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
