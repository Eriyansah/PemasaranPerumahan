package com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class FinalBookingActivity extends AppCompatActivity {
    
    private EditText nameEditText, phoneEditText, addressEditText, cityEditText, dateEditText;
    private Button confirmBookingBtn, dateBtn;
    private DatePickerDialog picker;
    private TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_booking);
        
        confirmBookingBtn = (Button) findViewById(R.id.confirm_booking_final);
        
        nameEditText = (EditText) findViewById(R.id.booking_name_final);
        phoneEditText = (EditText) findViewById(R.id.booking_phone_final);
        addressEditText = (EditText) findViewById(R.id.booking_address_final);
        cityEditText = (EditText) findViewById(R.id.booking_city_final);
        dateEditText = (EditText) findViewById(R.id.booking_date_final) ;
        tvDate = (TextView) findViewById(R.id.tv_date_final2);
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateBtn = (Button) findViewById(R.id.btn_date);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDate.setText("Tanggal DiPilih : "+ dateEditText.getText());
            }
        });

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(FinalBookingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        dateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, year, month, day);
                picker.show();
            }
        });
        
        
        confirmBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });
        
    }

    private void Check() {
        
        if (TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this, "Input Nama Lengkap Anda.", Toast.LENGTH_SHORT).show();
        }
        else  if (TextUtils.isEmpty(phoneEditText.getText().toString())){
            Toast.makeText(this, "Input Nomor Telepon Anda.", Toast.LENGTH_SHORT).show();
        }
        else  if (TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this, "Input Alamat Anda.", Toast.LENGTH_SHORT).show();
        }
        else  if (TextUtils.isEmpty(cityEditText.getText().toString())){
            Toast.makeText(this, "Input Kota Anda.", Toast.LENGTH_SHORT).show();
        }

        else  if (TextUtils.isEmpty(dateEditText.getText().toString())){
            Toast.makeText(this, "Input Tanggal Survey Anda.", Toast.LENGTH_SHORT).show();
        }
        else {
            ConfirmBooking();
        }
    }

    private void ConfirmBooking() {

        final String saveCurentDate, saveCurentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference()
                .child("Bookings")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> finalBookingMap = new HashMap<>();

        finalBookingMap.put("name", nameEditText.getText().toString());
        finalBookingMap.put("phone", phoneEditText.getText().toString());
        finalBookingMap.put("address", addressEditText.getText().toString());
        finalBookingMap.put("date", saveCurentDate);
        finalBookingMap.put("time", saveCurentTime);
        finalBookingMap.put("city", cityEditText.getText().toString());
        finalBookingMap.put("dateBooking", dateEditText.getText().toString());
        finalBookingMap.put("state", "not shipped");

        
        bookingRef.updateChildren(finalBookingMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Booking List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    
                                    if (task.isSuccessful()){
                                        Toast.makeText(FinalBookingActivity.this, "Proses Terakhir Booking Survey Anda Sukses", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(FinalBookingActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
                
            }
        });
    }
}
