package ph29152.fptpoly.duanoderfoodnhom1.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ph29152.fptpoly.duanoderfoodnhom1.Adapter.ProductAdapter;
import ph29152.fptpoly.duanoderfoodnhom1.Helper.Connection_SQL;
import ph29152.fptpoly.duanoderfoodnhom1.Model.Hamburger;
import ph29152.fptpoly.duanoderfoodnhom1.R;

public class BurgerActivity extends AppCompatActivity {
    RecyclerView rcvBurger;
    ProductAdapter adapter;
    List<Hamburger> list =new ArrayList<>();
    CardView cardCartBurger;
    Connection_SQL connection;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burger);
        rcvBurger = findViewById(R.id.rcvBurger);
        cardCartBurger=findViewById(R.id.cardCartBurger);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        gridLayoutManager.setSpanCount(3);
        getAllListProduct();
        adapter = new ProductAdapter(list,this);
        rcvBurger.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
        cardCartBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BurgerActivity.this,CartActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public List<Hamburger> getAllListProduct() {

        try {
            connection = new Connection_SQL();
            String sql = "SELECT * FROM HAMBURGER";
            PreparedStatement statement = connection.SQLconnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("IDHAMBURGER");
                String ten = resultSet.getString("TEN");
                String moTaNgan = resultSet.getString("MOTANGAN");
                String moTaChiTiet = resultSet.getString("MOTACHITIET");
                double giaTien = resultSet.getDouble("GIATIEN");
                int soLuong = resultSet.getInt("SOLUONG");
                String hinhAnh = resultSet.getString("HINHANH");
                Hamburger hamburger = new Hamburger(id,ten,moTaNgan,moTaChiTiet,soLuong,giaTien,hinhAnh);
                list.add(hamburger);
            }
            resultSet.close();
            statement.close();
            connection.SQLconnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


//    public Connection connectionSQLSever (){
//        Connection connection = null;
//        try{
//            //khai bao che do ket noi muc dich la de lay quyen ket noi
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);// thiet lap chinh sach ket noi bao gom tat ca cac quyen
//            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();//đăng ký driver JDBC và kết nối tới cơ sở dữ liệu
//            String connectString = "jdbc:jtds:sqlserver://192.168.0.102:1433;databaseName=YummyApp;user=sa;password=Password.1";
//            connection = DriverManager.getConnection(connectString);
//            Log.i("Thong bao", "Ket noi thanh cong den sql server su dung com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return connection;
//    }


}