package com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;
import com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.Model.Users;
import com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.Prevalent.Prevalent;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPhoneNumber, inputPassword;
    private Button loginButton;
    private ProgressDialog loadingBar;
    private TextView adminLink, notAdminLink;

    private String parentDbName = "Users";
    private CheckBox chbkrememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button)findViewById(R.id.login_btn);
        inputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        inputPassword = (EditText) findViewById(R.id.login_password_input);
        adminLink = (TextView) findViewById(R.id.admin_panel_link);
        notAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        loadingBar = new ProgressDialog(this);

        chbkrememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }

    private void loginUser() {
        String phone = inputPhoneNumber.getText().toString();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Masukkan Nomor Telepon Anda.", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Masukkan Password Anda.", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Akun Login");
            loadingBar.setMessage("Mohon Tunggu, Sementara Kami Memeriksa Kredensial.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            chbkrememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
            Paper.init(this);

            AllowAccesToAccount(phone, password);

        }
    }

    private void AllowAccesToAccount(final String phone, final String password) {

        if (chbkrememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPaswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()){

                    Users usersdata = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersdata.getPhone().equals(phone)){

                        if (usersdata.getPassword().equals(password)){
                           if (parentDbName.equals("Admins")){

                               Toast.makeText(LoginActivity.this, "Selamat Datang Admin, Login Sukses ...", Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                               startActivity(intent);
                           }
                           else {
                               if (parentDbName.equals("Users")){
                                   Toast.makeText(LoginActivity.this, "Login Sukses ...", Toast.LENGTH_SHORT).show();
                                   loadingBar.dismiss();

                                   Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                   Prevalent.currentOnlineUser = usersdata;
                                   startActivity(intent);
                               }
                           }

                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password Salah", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else {
                    Toast.makeText(LoginActivity.this, "Akun Dengan Nomor Ini" +phone+ "Nomor Tidak Ada", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Anda Perlu Membuat Akun Baru", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
