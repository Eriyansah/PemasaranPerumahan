package com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.Model.Booking;
import com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.Prevalent.Prevalent;
import com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.ViewHolder.BookingViewHolder;

public class BookingActivity extends AppCompatActivity {

    private Button nextProcessBtn;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private TextView txtMsg1, txtBooking, txtBooking1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        recyclerView = findViewById(R.id.booking_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        txtMsg1 = (TextView) findViewById(R.id.msg1);
        txtBooking = (TextView) findViewById(R.id.txt_booking);
        txtBooking1 = (TextView) findViewById(R.id.txt_booking1);
        nextProcessBtn = (Button) findViewById(R.id.next_process_btn);

        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingActivity.this, FinalBookingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {

        final DatabaseReference bookingListRef = FirebaseDatabase.getInstance().getReference().child("Booking List");

        FirebaseRecyclerOptions<Booking> options =
                new FirebaseRecyclerOptions.Builder<Booking>()
                .setQuery(bookingListRef.child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone())
                        .child("Products"), Booking.class)
                        .build();

        FirebaseRecyclerAdapter<Booking, BookingViewHolder> adapter
                = new FirebaseRecyclerAdapter<Booking, BookingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BookingViewHolder holder, int position, @NonNull final Booking model) {

                holder.txtProductName.setText("Nama Produk : " +model.getPname());
                holder.txtProductLocation.setText(model.getLocation());
                holder.txtProductPrice.setText(model.getPrice());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] =  new CharSequence[]{

                           "Hapus"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this);
                        builder.setTitle("Booking Survey:");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                if (i == 0){
                                    bookingListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(BookingActivity.this, "Hapus Item Booking Berhasil.", Toast.LENGTH_SHORT).show();
                                                        Intent intent= new Intent(BookingActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }

                            }
                        });

                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_items_layout, parent, false);
                BookingViewHolder holder = new BookingViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

        super.onStart();

        CheckBookingState();

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

                        txtBooking.setText("SELAMAT. " + userName + "\n Booking Survey Berhasil Dikirim.");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setText("SELAMAT, Proses Booking Survey Anda Telah Sukses, Tunggu Konfimasi Selanjutnya.");
                        txtMsg1.setVisibility(View.VISIBLE);
                        nextProcessBtn.setVisibility(View.GONE);
                        txtBooking1.setVisibility(View.GONE);

                        Toast.makeText(BookingActivity.this, "Anda Bisa Booking Kembali Setelah Proses Booking Survey Pertama Anda Selesai", Toast.LENGTH_SHORT).show();

                    }

                    else if (shippingState.equals("not shipped")){

                        txtBooking.setText("Booking Survey  : Tidak Terkirim");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        nextProcessBtn.setVisibility(View.GONE);
                        txtBooking1.setVisibility(View.GONE);

                        Toast.makeText(BookingActivity.this, "Anda Bisa Booking Kembali Setelah Proses Booking Survey Pertama Anda Selesai", Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
