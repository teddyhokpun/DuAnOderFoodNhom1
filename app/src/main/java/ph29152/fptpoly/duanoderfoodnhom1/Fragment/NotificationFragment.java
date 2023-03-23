package ph29152.fptpoly.duanoderfoodnhom1.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ph29152.fptpoly.duanoderfoodnhom1.Adapter.NotificationAdapter;
import ph29152.fptpoly.duanoderfoodnhom1.Helper.Connection_SQL;
import ph29152.fptpoly.duanoderfoodnhom1.Model.Notification;
import ph29152.fptpoly.duanoderfoodnhom1.Model.UserSession;
import ph29152.fptpoly.duanoderfoodnhom1.Model.Users;
import ph29152.fptpoly.duanoderfoodnhom1.R;

public class NotificationFragment extends Fragment {
    RecyclerView rcv;
    NotificationAdapter adapter;
    List<Notification> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcv = view.findViewById(R.id.rcvNotification);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        getListFromFireBase();
        adapter = new NotificationAdapter(getContext(),list);
        rcv.setAdapter(adapter);

    }


    private void getListFromFireBase() {
        Users user = UserSession.getCurrentUser();
        int idUsers = user.getIdUser();
        String stringIdUsers = String.valueOf(idUsers);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("ThongBao").child(stringIdUsers);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Notification notification = snapshot.getValue(Notification.class);
                if(notification!=null){
                    list.add(notification);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Notification notification = snapshot.getValue(Notification.class);
                if(list==null || list.isEmpty() || notification == null){
                    return;
                }
                for(int i = 0 ; i<list.size() ; i++ ){
                    if(notification.getIdThongBao()==list.get(i).getIdThongBao()){
                        list.set(i,notification);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Notification notification = snapshot.getValue(Notification.class);
                if(list==null || list.isEmpty() || notification == null){
                    return;
                }
                for (int i = 0 ; i < list.size() ; i++){
                    if(notification.getIdThongBao()==list.get(i).getIdThongBao()){
                        list.remove(list.get(i));
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
