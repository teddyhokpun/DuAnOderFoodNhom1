package ph29152.fptpoly.duanoderfoodnhom1.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import ph29152.fptpoly.duanoderfoodnhom1.AdminActivity;
import ph29152.fptpoly.duanoderfoodnhom1.Helper.Connection_SQL;
import ph29152.fptpoly.duanoderfoodnhom1.MainActivity;
import ph29152.fptpoly.duanoderfoodnhom1.Model.UserSession;
import ph29152.fptpoly.duanoderfoodnhom1.Model.Users;
import ph29152.fptpoly.duanoderfoodnhom1.R;

public class LoginActivity extends AppCompatActivity {
    private CardView btnLogin;
    private TextInputLayout edPhone,edPassword;
    Connection_SQL connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.cardLogin);
        edPhone = findViewById(R.id.layUserName);
        edPassword = findViewById(R.id.layPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();


            }
        });
    }
    private void login()  {
        try {
        connection = new Connection_SQL();
        if(connection!=null){
            String username = Objects.requireNonNull(edPhone.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(edPassword.getEditText()).getText().toString();
            String sql = "SELECT COUNT(*) as count,QUYEN FROM USERS WHERE SODIENTHOAI = ? AND MATKHAU = ? GROUP BY QUYEN";
            PreparedStatement statement = connection.SQLconnection().prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();
            String sql2 ="SELECT * FROM USERS WHERE SODIENTHOAI = ? AND MATKHAU = ?";
            PreparedStatement statement2 = connection.SQLconnection().prepareStatement(sql2);
            statement2.setString(1, username);
            statement2.setString(2, password);
            ResultSet result2 = statement2.executeQuery();
            if(result2.next()){
                int idUser = result2.getInt("IDUSERS");
                int quyen = result2.getInt("QUYEN");
                String name = result2.getString("TEN");
                String email = result2.getString("EMAIL");
                String diaChi=result2.getString("DIACHI");
                double viTien = result2.getDouble("VITIEN");
                String hinhAnh = result2.getString("HINHANH");
                Users user;
                if(hinhAnh==null) {
                    user = new Users(idUser, name, email, username, diaChi, password, quyen, viTien);
                }else{
                    user = new Users(idUser, name, email, username, diaChi, password, quyen, viTien,hinhAnh);
                }
                UserSession.setCurrentUser(user);

            }
            if (result.next()) {
                int count = result.getInt("count");
                int quyen = result.getInt("QUYEN");


                if (count == 1) {
                    if (quyen == 0) {
                        Toast.makeText(this, "Admin", Toast.LENGTH_SHORT).show();
                        // Chuyển đến trang chủ
                        Intent intent = new Intent(LoginActivity.this,AdminActivity.class);
                        startActivity(intent);
                    } else {
                        // Chuyển đến trang phụ
                        Intent intent = new Intent(LoginActivity.this, BurgerActivity.class);
                        startActivity(intent);
                        Toast.makeText(this, "User", Toast.LENGTH_SHORT).show();
                    }
//                    finishAffinity();
                    Toast.makeText(this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                } else {
                    // Đăng nhập thất bại
                    Toast.makeText(this, "Tai khoan hoac mat khau sai", Toast.LENGTH_SHORT).show();
                }
            }
            result.close();
            statement.close();
            statement2.close();
            connection.SQLconnection().close();

        }else{
            Toast.makeText(this, "Ket noi den co so du lieu khong thanh cong", Toast.LENGTH_SHORT).show();
        }
    }catch (Exception e){
            e.printStackTrace();
        }
    }





}