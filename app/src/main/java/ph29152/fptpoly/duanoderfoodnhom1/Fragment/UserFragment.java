package ph29152.fptpoly.duanoderfoodnhom1.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ph29152.fptpoly.duanoderfoodnhom1.Activity.LoginActivity;
import ph29152.fptpoly.duanoderfoodnhom1.Helper.Connection_SQL;
import ph29152.fptpoly.duanoderfoodnhom1.Model.UserSession;
import ph29152.fptpoly.duanoderfoodnhom1.Model.Users;
import ph29152.fptpoly.duanoderfoodnhom1.R;


public class UserFragment extends Fragment {
    private TextView tvNameUser,tvPhoneUser,tvPasswordUser,tvEmailUser,tvAddressUser,tvViTien;
    private ImageView imgAvatarUser;
    private Button btnUpdateUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNameUser=view.findViewById(R.id.tvNameUser);
        tvPhoneUser=view.findViewById(R.id.tvPhoneUser);
        tvPasswordUser=view.findViewById(R.id.tvPasswordUser);
        tvEmailUser=view.findViewById(R.id.tvEmailUser);
        tvAddressUser=view.findViewById(R.id.tvAddressUser);
        tvViTien=view.findViewById(R.id.tvViTien);
        imgAvatarUser=view.findViewById(R.id.imgAvatarUser);
        btnUpdateUser=view.findViewById(R.id.btnUpdateUser);

        Users users = UserSession.getCurrentUser();
        tvNameUser.setText("Tên người dùng: "+users.getTen());
        tvPhoneUser.setText("Số điện thoại: "+users.getSoDienThoai());
        tvPasswordUser.setText("Mật Khẩu: "+users.getMatKhau());
        tvEmailUser.setText("Email: "+users.getEmail());
        tvAddressUser.setText("Địa Chỉ: "+users.getDiaChi());
        tvViTien.setText("Số dư ví: $"+users.getViTien());
        tvPhoneUser.setText(users.getSoDienThoai());
        if(users.getHinhAnh()!=null){
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.RGBA_F16;
            opts.inMutable = true;
            byte[] decodedString = Base64.decode(users.getHinhAnh(), Base64.DEFAULT);
            Bitmap myBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length,opts);
            myBitmap.setHasAlpha(true);
            imgAvatarUser.setImageBitmap(myBitmap);
        }else{
            imgAvatarUser.setImageResource(R.drawable.imgadmin);
        }

        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDMK();
            }
        });

    }


    private void showDialogDMK(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setNegativeButton("Cập nhập", null).setPositiveButton("Hủy", null);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_doimatkhau, null);

        TextInputLayout ed_MatKhauCu_out,ed_matKhauMoi_out,ed_NhapLaiMatKhauMoi_out;
        TextInputEditText ed_matKhauCu,ed_matKhauMoi,ed_NhapLaiMatKhauMoi;

        ed_MatKhauCu_out = view.findViewById(R.id.ed_DMK_matKhauCu_out);
        ed_matKhauMoi_out = view.findViewById(R.id.ed_DMK_matKhauMoi_out);
        ed_NhapLaiMatKhauMoi_out = view.findViewById(R.id.ed_DMK_NhapLaiMatKhauMoi_out);
        ed_matKhauCu = view.findViewById(R.id.ed_DMK_matKhauCu);
        ed_NhapLaiMatKhauMoi = view.findViewById(R.id.ed_DMK_NhapLaiMatKhauMoi);
        ed_matKhauMoi = view.findViewById(R.id.ed_DMK_matkhauMoi);


        builder.setView(view);



        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String matKhauCu = ed_matKhauCu.getText().toString().trim();
                String matKhauMoi = ed_matKhauMoi.getText().toString().trim();
                String nhapLaiMK = ed_NhapLaiMatKhauMoi.getText().toString().trim();

                if (matKhauCu.equals("")){
                    ed_MatKhauCu_out.setError("Không được để trống");
                    ed_matKhauMoi_out.setError(null);
                    ed_NhapLaiMatKhauMoi_out.setError(null);
                }else if (matKhauMoi.equals("")){
                    ed_MatKhauCu_out.setError(null);
                    ed_matKhauMoi_out.setError("Không được để trống");
                    ed_NhapLaiMatKhauMoi_out.setError(null);
                }else if (nhapLaiMK.equals("")){
                    ed_MatKhauCu_out.setError(null);
                    ed_matKhauMoi_out.setError(null);
                    ed_NhapLaiMatKhauMoi_out.setError("Không được để trống");
                }else if (matKhauMoi.equals(nhapLaiMK)){
                    try {
                        Connection_SQL connection_sql = new Connection_SQL();
                        String sql = "UPDATE USERS " +
                                "SET MATKHAU = ? " +
                                "WHERE IDUSERS = ?";
                        PreparedStatement stm = connection_sql.SQLconnection().prepareStatement(sql);
                        stm.setString(1,matKhauMoi);
                        stm.setInt(2,UserSession.getCurrentUser().getIdUser());
                        int row = stm.executeUpdate();

                        if(row >0 ){
                            Toast.makeText(getContext(), "Doi mat khau thanh cong \n"+ "Mat khau moi : "+matKhauMoi, Toast.LENGTH_SHORT).show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

    }

}
