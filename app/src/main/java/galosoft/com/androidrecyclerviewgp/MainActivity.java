package galosoft.com.androidrecyclerviewgp;


import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import galosoft.com.androidrecyclerviewgp.Adapter.MyItemGroupAdapter;
import galosoft.com.androidrecyclerviewgp.Interface.IFirebaseLoadListener;
import galosoft.com.androidrecyclerviewgp.Model.ItemData;
import galosoft.com.androidrecyclerviewgp.Model.ItemGroup;

public class MainActivity extends AppCompatActivity implements IFirebaseLoadListener {

    AlertDialog dialog;
    IFirebaseLoadListener iFirebaseLoadListener;
    RecyclerView my_recycler_view;
    DatabaseReference myData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        myData = FirebaseDatabase.getInstance().getReference("MyData");
        dialog = new SpotsDialog.Builder().setContext(this).build();
        iFirebaseLoadListener = this;

        my_recycler_view = findViewById(R.id.recycler_view);
        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this));


        //loadData
        getFirebaseData();
    }

    private void getFirebaseData() {

        dialog.show();

        myData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               List<ItemGroup> itemGroups = new ArrayList<>();
               for(DataSnapshot groupSnapShot: dataSnapshot.getChildren()){
                   ItemGroup itemGroup = new ItemGroup();
                   itemGroup.setHeaderTitle(groupSnapShot.child("headerTitle").getValue(true).toString());
                   GenericTypeIndicator<ArrayList<ItemData>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<ItemData>>(){};
                   itemGroup.setListItem(groupSnapShot.child("listItem").getValue(genericTypeIndicator));
                   itemGroups.add(itemGroup);
               }
               iFirebaseLoadListener.onFirebaseLoadSuccess(itemGroups);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadListener.onfirebaseLoadFailed(databaseError.getMessage());
            }
        });
    }

    @Override
    public void onFirebaseLoadSuccess(List<ItemGroup> itemGroupList) {

        MyItemGroupAdapter adapter = new MyItemGroupAdapter(this, itemGroupList);
        my_recycler_view.setAdapter(adapter);
        dialog.dismiss();
    }

    @Override
    public void onfirebaseLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
