package com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.Model.AdminBookings;

public class AdminNewBookingActivity extends AppCompatActivity {

    private RecyclerView bookingList;
    private DatabaseReference bookingRef;
    private String userID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_booking);



        bookingRef = FirebaseDatabase.getInstance().getReference().child("Bookings");

        bookingList = findViewById(R.id.order_list);

        bookingList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {

        super.onStart();


        FirebaseRecyclerOptions<AdminBookings> options =
                new FirebaseRecyclerOptions.Builder<AdminBookings>()
                .setQuery(bookingRef, AdminBookings.class)
                .build();

        FirebaseRecyclerAdapter<AdminBookings, AdminBookingViewHolder > adapter =
                new FirebaseRecyclerAdapter<AdminBookings, AdminBookingViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminBookingViewHolder holder, final int position, @NonNull final AdminBookings model) {

                        holder.userName.setText("Nama : " + model.getName());
                        holder.userPhoneNumber.setText("Nomor Telepon : " + model.getPhone());
                        holder.userDateTime.setText("Booking Pada : " + model.getDate() + " " + model.getTime());
                        holder.userShippingAddress.setText("Alamat : " + model.getAddress() + "Kota" + model.getCity());
                        holder.dateBooking.setText("Request survey Pada Tanggal : " + model.getDateBooking());


                        holder.showBookingBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String uID = getRef(position).getKey();

                                Intent intent = new Intent(AdminNewBookingActivity.this, AdminUserProductsActivity.class);
                                intent.putExtra("uid", uID);
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]{
                                        "Yes",
                                        "No"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewBookingActivity.this);
                                builder.setTitle("Apakah Anda Sudah Mengkonfimasi Booking Survey Produk ini ?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if (i == 0){

                                            String uID = getRef(position).getKey();

                                            RemoveBooking(uID);
                                        }
                                        else {
                                            finish();
                                        }
                                    }
                                });

                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookings_layout, parent, false);
                        return new AdminBookingViewHolder(view);

                    }
                };
        bookingList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class AdminBookingViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userPhoneNumber, userDateTime, userShippingAddress, dateBooking;

        public Button showBookingBtn;

        public AdminBookingViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            showBookingBtn = itemView.findViewById(R.id.show_all_product_btn);
            dateBooking = itemView.findViewById(R.id.order_date_booking);


        }
    }

    private void RemoveBooking(String uID) {

        bookingRef.child(uID).removeValue();
    }
}
