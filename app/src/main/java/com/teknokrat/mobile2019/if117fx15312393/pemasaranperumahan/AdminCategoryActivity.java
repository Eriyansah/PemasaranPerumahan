package com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminCategoryActivity extends AppCompatActivity {

    private Button rumahButton, rukoButton, kavlinganButton;

    private Button logoutBtn, checkBookingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        rumahButton = (Button) findViewById(R.id.rumah_btn);
        rukoButton = (Button) findViewById(R.id.ruko_btn);
        kavlinganButton = (Button) findViewById(R.id.kavling_btn);

        logoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        checkBookingBtn = (Button) findViewById(R.id.check_booking_btn);


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewBookingActivity.class);
                startActivity(intent);

            }
        });


        rumahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Rumah");
                startActivity(intent);
            }
        });

        rukoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Ruko");
                startActivity(intent);
            }
        });

        kavlinganButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Kavling");
                startActivity(intent);
            }
        });
    }
}
