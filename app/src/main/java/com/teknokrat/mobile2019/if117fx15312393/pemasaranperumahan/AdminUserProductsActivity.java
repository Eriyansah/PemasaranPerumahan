package com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.Model.Booking;
import com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.ViewHolder.BookingViewHolder;

public class AdminUserProductsActivity extends AppCompatActivity {

    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference bookingListsRef;
    private String userID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        userID = getIntent().getStringExtra("uid");

        productsList = findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);


        bookingListsRef = FirebaseDatabase.getInstance().getReference()
                .child("Booking List").child("Admin View").child(userID).child("Products");
    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<Booking> options =
                new FirebaseRecyclerOptions.Builder<Booking>()
                .setQuery(bookingListsRef, Booking.class)
                .build();

        FirebaseRecyclerAdapter<Booking, BookingViewHolder> adapter = new FirebaseRecyclerAdapter<Booking, BookingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BookingViewHolder holder, int position, @NonNull Booking model) {

                holder.txtProductName.setText("Nama Produk : " + model.getPname());
                holder.txtProductLocation.setText(model.getLocation());
                holder.txtProductPrice.setText(model.getPrice());

            }

            @NonNull
            @Override
            public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_items_layout, parent, false);
                BookingViewHolder holder = new BookingViewHolder(view);
                return holder;

            }
        };

        productsList.setAdapter(adapter);
        adapter.startListening();
    }
}
