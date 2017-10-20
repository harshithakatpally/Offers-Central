package com.example.vamsi.firebaseauth1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by FALCONS on 5/21/2017.
 */

public class RedeemCoupon extends AppCompatActivity {

    private TextView pizza;
    private TextView breads;
    private TextView dips;
    private TextView beverages;
    private TextView more;
    private View line3;
    private TextView amount;
    private LinearLayout totalamount;
    private Button  checkout;
    private int sum=0;
    private int j;
    private ArrayList<String> breadsitems = new ArrayList<String>();
    private ArrayList<String> dipsitems = new ArrayList<String>();
    private ArrayList<String> beveragesitems = new ArrayList<String>();
    private ArrayList<String> moreitems = new ArrayList<String>();
    private AlertDialog.Builder builder;

    static HashMap<String,String> pizzaSize ;

    private TextView carttext;
    private View line1;
    private TableLayout carttable;
    private static String keyword;
    private static int minAmount;
    private int id;

    private DatabaseReference rootReference;
    private FirebaseAuth firebaseAuth;

    static ArrayList<String> items = new ArrayList<String>();
    private HashMap<String,Integer> prices = new HashMap<String, Integer>();

    private CouponDetails2 couponDetails;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeem_coupon_screen);

        pizzaSize = new HashMap<>();

        rootReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        pizza = (TextView) findViewById(R.id.dpizza);
        breads = (TextView) findViewById(R.id.dbreads);
        dips = (TextView) findViewById(R.id.ddips);
        beverages = (TextView) findViewById(R.id.dbeverages);
        more = (TextView) findViewById(R.id.dmore);

        prices.put("Paneer and Onion", 295);
        prices.put("Cheese and corn", 295);
        prices.put("Fresh Veggie", 295);
        prices.put("Pepper Chicken", 365);
        prices.put("Barbeque Chicken", 365);
        prices.put("Cheese and Pepperoni", 525);
        prices.put("Hot N Spicy Chicken", 365);
        prices.put("Cheesy Jalapeno dip", 25);
        prices.put("Cheesy dip", 25);
        prices.put("Sprite mobile", 60);
        prices.put("Coke mobile", 60);
        prices.put("Fanta mobile", 60);
        prices.put("Lava cake", 89);
        prices.put("Butterscotch Mouse cake", 85);
        prices.put("Choco pizza", 199);
        prices.put("Garlic breadsticks", 69);
        prices.put("Stuffed Garlic breadsticks", 129);
        prices.put("Chicken parcel", 35);
        prices.put("Veg parcel", 29);
        prices.put("Paneer and Onion", 150);
        prices.put("Cheese and Corn", 125);
        prices.put("Fresh Veggie", 135);


        Intent intent = this.getIntent();
        builder = new AlertDialog.Builder(RedeemCoupon.this);
  /* Obtain String from Intent  */
        if (intent != null) {
            Bundle b = intent.getExtras();
            String strdata = b.getString("source");


            if (strdata.equals("fromCoupon")) {

                rootReference.child("Coupons").child(firebaseAuth.getCurrentUser().getUid())
                        .child(b.getString("brandName")).child(b.getString("couponID")).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        couponDetails = dataSnapshot.getValue(CouponDetails2.class);
                        minAmount = couponDetails.getMinAmount();
                        keyword = couponDetails.getKeyword();
                        if(keyword.equals("Pizza")) {

                            builder.setTitle("Coupon Message");
                            builder.setMessage("Choose "+keyword+" from the menu to avail the coupon.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i;

                                        i = new Intent(RedeemCoupon.this,RedeemDominosPizza.class);
                                        i.putExtra("source","AvailCoupon");
                                        startActivity(i);

                                }
                            });

                            builder.show();
                        }

                        else if (keyword.equals("Garlic Breadsticks"))
                        {
                            builder.setTitle("Coupon Message");
                            builder.setMessage("Choose "+keyword+" from the menu to avail the coupon.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i;

                                        i = new Intent(RedeemCoupon.this,RedeemDominosBreads.class);
                                        i.putExtra("source","AvailCoupon");
                                        startActivity(i);

                                }
                            });

                            builder.show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                pizza.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent p = new Intent(getApplicationContext(), RedeemDominosPizza.class);
                        startActivity(p);
                    }
                });

                breads.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent b = new Intent(getApplicationContext(), RedeemDominosBreads.class);
                        startActivity(b);
                    }
                });

                dips.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent d = new Intent(getApplicationContext(), RedeemDominosDips.class);
                        startActivity(d);
                    }
                });

                beverages.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent b = new Intent(getApplicationContext(), RedeemDominosBeverages.class);
                        startActivity(b);
                    }
                });

                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent m = new Intent(getApplicationContext(), RedeemDominosMore.class);
                        startActivity(m);
                    }
                });
            } else {

                if (!items.isEmpty()) {
                    carttext = (TextView) findViewById(R.id.carttext);
                    carttext.setVisibility(View.VISIBLE);
                    line1 = findViewById(R.id.line1);
                    line1.setVisibility(View.VISIBLE);
                    carttable = (TableLayout) findViewById(R.id.carttable);
                    carttable.setVisibility(View.VISIBLE);


                    for (j = 0; j < items.size(); j++) {

                        //creating table for cart
                        if (items.get(j) != null) {

                            int itemPrice = prices.get(items.get(j));

                            //creating table for cart
                            if (items.get(j) != null) {
                                String size;
                                size = pizzaSize.get(items.get(j));
                                if (size != null) {
                                    if (size.equals("Regular"))
                                        ;
                                    else if (size.equals("Medium"))
                                        itemPrice = itemPrice + 100;
                                    else if (size.equals("Large"))
                                        itemPrice = itemPrice + 150;
                                }


                                TableRow row1 = new TableRow(this);
                                row1 = (TableRow) getLayoutInflater().inflate(R.layout.table_row_style, null);

                                TextView item = new TextView(this);
                                item = (TextView) getLayoutInflater().inflate(R.layout.table_text_style, null);
                                item.setText(items.get(j));
                                row1.addView(item);

                                TextView quantity = new TextView(this);
                                quantity = (TextView) getLayoutInflater().inflate(R.layout.table_text_style, null);
                                quantity.setPadding(10, 0, 0, 0);
                                quantity.setText("1");
                                row1.addView(quantity);

                                TextView price = new TextView(this);
                                price = (TextView) getLayoutInflater().inflate(R.layout.table_text_style, null);
                                price.setPadding(10, 0, 0, 0);
                                if (j==0){
                                    id = View.generateViewId();
                                    price.setId(id);
                                }

                                price.setText(Integer.toString(itemPrice));
                                if(j!=0)
                                    sum = sum + prices.get(items.get(j));

                                row1.addView(price);
//                                row1.setOnLongClickListener(new View.OnLongClickListener() {
//                                    @Override
//                                    public boolean onLongClick(View view) {
//                                        Toast.makeText(RedeemCoupon.this, "Inside Longlclick", Toast.LENGTH_SHORT).show();
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(RedeemCoupon.this);
//                                        builder.setTitle("Remove Item");
//                                        builder.setMessage("Do you want to remove " + items.get(j) + " from your cart?");
//                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                if (items.size() != 1) {
//                                                    items.remove(j);
//                                                    sum = sum - prices.get(items.get(j));
////                                                    Intent intent = new Intent(RedeemCoupon.this, RedeemCoupon.class);
////                                                    startActivity(intent);
//                                                } else {
//                                                    items.remove(j);
//                                                    sum = sum - prices.get(items.get(j));
//                                                    carttext.setVisibility(View.GONE);
//                                                    line1.setVisibility(View.GONE);
//                                                    carttable.setVisibility(View.GONE);
//                                                    line3 = findViewById(R.id.line3);
//                                                    line3.setVisibility(View.GONE);
//                                                    totalamount = (LinearLayout) findViewById(R.id.totalamount);
//                                                    totalamount.setVisibility(View.GONE);
//                                                    amount = (TextView) findViewById(R.id.amount);
//                                                    amount.setText(Integer.toString(sum));
//                                                    amount.setVisibility(View.GONE);
//
////                                                    Intent intent = new Intent(RedeemCoupon.this, RedeemCoupon.class);
////                                                    startActivity(intent);
//                                                }
//                                            }
//                                        });
//
//                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
////                                                Intent intent = new Intent(RedeemCoupon.this, RedeemCoupon.class);
////                                                startActivity(intent);
//                                            }
//                                        });
//
//                                        builder.show();
//                                        return true;
//                                    }
//                                });
                                carttable.addView(row1);


                            }


                            line3 = findViewById(R.id.line3);
                            line3.setVisibility(View.VISIBLE);
                            totalamount = (LinearLayout) findViewById(R.id.totalamount);
                            totalamount.setVisibility(View.VISIBLE);
                            amount = (TextView) findViewById(R.id.amount);
                            amount.setText(Integer.toString(sum));
                            amount.setVisibility(View.VISIBLE);


                            pizza.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent p = new Intent(getApplicationContext(), RedeemDominosPizza.class);
                                    startActivity(p);
                                }
                            });

                            breads.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent b = new Intent(getApplicationContext(), RedeemDominosBreads.class);
                                    startActivity(b);
                                }
                            });

                            dips.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent d = new Intent(getApplicationContext(), RedeemDominosDips.class);
                                    startActivity(d);
                                }
                            });

                            beverages.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent b = new Intent(getApplicationContext(), RedeemDominosBeverages.class);
                                    startActivity(b);
                                }
                            });

                            more.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent m = new Intent(getApplicationContext(), RedeemDominosMore.class);
                                    startActivity(m);
                                }
                            });

                        }
                    }

                    final AlertDialog ad;
                    if(minAmount>sum) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Alert !!!");
                        builder.setCancelable(false);
                        builder.setMessage("Add Rs" + (minAmount - sum)+" worth items more to avail the offer.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();

                    }
                    else{

                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Alert !!!");
                        builder.setMessage("Your "+keyword+" is now made free of cost");

                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView price  = (TextView)findViewById(id);
                                //amount.setText(String.valueOf(Integer.valueOf(amount.getText().toString())-Integer.valueOf(price.getText().toString())));
                                price.setText("0.00");

                            }
                        });
                        builder.show();


                    }


                }
            }

            checkout = (Button) findViewById(R.id.checkout);

            checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent c = new Intent(getApplicationContext(), RedeemCheckout.class);
                    c.putExtra("amount", sum);
                    startActivity(c);
                }
            });


        }
    }


}
