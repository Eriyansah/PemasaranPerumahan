package com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.Model.Products;
import com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productLocation, productPrice, productDescription;
    private String productID = "", state = "Normal";
    private Button aadToBookingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productLocation = (TextView) findViewById(R.id.product_location_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        aadToBookingBtn = (Button) findViewById(R.id.add_booking_btn);


        getProductDetails(productID);


        aadToBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToBookingList();

                if (state.equals("Booking Placed") || state.equals("Booking Booking Shipped")){

                    Toast.makeText(ProductDetailsActivity.this, "Anda Bisa Booking Kembali Setelah Proses Booking Survey Pertama Anda Selesai Atau Dikonfimasi", Toast.LENGTH_LONG).show();
                }

                else {
                    addingToBookingList();
                }

            }
        });
    }


    @Override
    protected void onStart() {

        super.onStart();

        CheckBookingState();;
    }

    private void addingToBookingList() {

        String saveCurentTime, saveCurentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference bookingListRef = FirebaseDatabase.getInstance().getReference().child("Booking List");

        final HashMap<String, Object> bookingMap = new HashMap<>();
        bookingMap.put("pid", productID);
        bookingMap.put("pname", productName.getText().toString());
        bookingMap.put("price", productPrice.getText().toString());
        bookingMap.put("date", saveCurentDate);
        bookingMap.put("time", saveCurentTime);
        bookingMap.put("location", productLocation.getText().toString());

        bookingListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(bookingMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            bookingListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(bookingMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(ProductDetailsActivity.this, "Ditambahkan ke Booking Survey , Silahkan Klik Tombol BOOKING Kanan Bawah Untuk Melanjutkan Proses.", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }

                                        }
                                    });
                        }

                    }
                });

    }

    private void getProductDetails(final String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText("Harga : Rp." + products.getPrice());
                    productLocation.setText("Lokasi : " + products.getLocation());
                    productDescription.setText("Keterangan : " + products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }

    private void CheckBookingState(){

        DatabaseReference bookingRef;
        bookingRef = FirebaseDatabase.getInstance().getReference().child("Bookings").child(Prevalent.currentOnlineUser.getPhone());

        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")){

                       state = ("Booking Shipped");
                    }

                    else if (shippingState.equals("not shipped")){

                        state = ("Booking Placed");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
