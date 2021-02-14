package com.android_group10.needy.ui.InNeed;

import android.os.Bundle;
import android.view.View;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.android_group10.needy.Post;
import com.android_group10.needy.R;
import com.android_group10.needy.ServiceType;
import com.android_group10.needy.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InNeedViewModel extends ViewModel {
  /*  private final MutableLiveData<Set<Filter>> filters = new MutableLiveData<>();

    private final LiveData<List<Post>> dataList;
  //  private final LiveData<List<Post>> filteredDataList;

    public LiveData<List<Post>> getFilteredDataList(){
   //     return filteredDataList;
        return dataList;
    }

    public LiveData<Set<Filter>> getFilters(){
        return filters;
    }

    public void addFilter(Filter filter){};

    public void removeFilter(Filter filter){}

    public InNeedViewModel(LiveData<List<Post>> dataList) {
       // dataList = new LiveData<List<Post>>(generateData());

       // mText.setValue("This is in need fragment");
        this.dataList = dataList;
    }*/

 /*   public ArrayList<Post> getData() {
        return generateData();
    }

    private ArrayList<Post> generateData(){
        ArrayList<Post> data = new ArrayList<>();
        User one = new User("Liliia", "07653248601", "blah-blah@mail.com");
        one.setImage(R.mipmap.ic_launcher);
        data.add(new Post(1, one, "buy me food", ServiceType.SHOPPING, "Kristianstad", "23001"));
        data.add(new Post(2, one, "clean my apartment", ServiceType.CLEANING, "Kristianstad", "23951"));
        data.add(new Post(3, one, "give me a break", ServiceType.OTHER, "Malmö", "24834"));
        data.add(new Post(3, one, "a ride to the hospital", ServiceType.TRANSPORTATION, "Jävle", "20634"));
        return data;
    }
}

 class RecyclerViewFragment extends Fragment {
    private InNeedViewModel viewModel;
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(InNeedViewModel.class);
        viewModel.getFilteredDataList().observe(getViewLifecycleOwner(), list -> {
            //update the list UI
        });
    }
}

 class FilterFragment extends Fragment {
    private InNeedViewModel viewModel;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(InNeedViewModel.class);
        viewModel.getFilters().observe(getViewLifecycleOwner(), set -> {
            // Update the selected filters UI
        });
    }

    public void onFilterSelected(Filter filter) {
        viewModel.addFilter(filter);
    }

    public void onFilterDeselected(Filter filter) {
        viewModel.removeFilter(filter);
    }*/
}