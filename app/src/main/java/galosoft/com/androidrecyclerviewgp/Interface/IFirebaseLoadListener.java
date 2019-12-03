package galosoft.com.androidrecyclerviewgp.Interface;

import java.util.List;

import galosoft.com.androidrecyclerviewgp.Model.ItemGroup;

public interface IFirebaseLoadListener {

    void onFirebaseLoadSuccess(List<ItemGroup> itemGroupList);
    void onfirebaseLoadFailed(String message);
}
