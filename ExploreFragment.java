package com.sp.android_studio_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

public class ExploreFragment extends Fragment implements Explore_RecyclerViewInterface{

    RecyclerView recyclerView;
    ExploreAdapter exploreAdapter;
    ArrayList<ExploreModel> exploreModels = new ArrayList<>();

    int[] exploreImage = {R.drawable.explore_map, R.drawable.explore_recycle, R.drawable.explore_news,
            R.drawable.explore_friends, R.drawable.explore_progress, R.drawable.explore_games,
            R.drawable.explore_shop};

    EditText searchBar;
    ImageView searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        recyclerView = view.findViewById(R.id.exploreRecyclerView);
        recyclerView.setHasFixedSize(true); //added
        setUpExploreModel();

        exploreAdapter = new ExploreAdapter(getContext(), exploreModels, this);
        recyclerView.setAdapter(exploreAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        searchBar = view.findViewById(R.id.searchBar);
        searchButton = view.findViewById(R.id.searchButton);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String search = searchBar.getText().toString();

                if(search.isEmpty()){
                    filter(editable.toString());
                } else {
                    searchButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            filter(editable.toString());
                        }
                    });
                }
            }
        });

        return view;
    }

    private void setUpExploreModel() {
        String[] exploreTab = getResources().getStringArray(R.array.ui_tab);
        String[] exploreDesc = getResources().getStringArray(R.array.ui_desc);

        for (int i = 0; i < exploreTab.length; i++) {
            exploreModels.add(new ExploreModel(exploreTab[i],
                    exploreDesc[i],
                    exploreImage[i]));
        }
    }

    private void filter(String text) {
        ArrayList<ExploreModel> filteredList = new ArrayList<>();

        for(ExploreModel item : exploreModels) {
            if(item.getExploreTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        exploreAdapter.filterList(filteredList);
    }

    @Override
    public void onItemClick(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new ExploreFragment_Map();
                break;
            case 1:
                fragment = new ExploreFragment_Recycle();
                break;
            case 2:
                fragment = new ExploreFragment_News();
                break;
            case 3:
                fragment = new ExploreFragment_Friends();
                break;
            case 4:
                fragment = new ExploreFragment_Progress();
                break;
            case 5:
                fragment = new ExploreFragment_Games();
                break;
            case 6:
                fragment = new ExploreFragment_Shop();
                break;
            default:
                break;
        }
        if(fragment != null) {
            /*FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.nav_explore, fragment);
            ft.commit();*/

            getActivity().getSupportFragmentManager().beginTransaction()
                         .replace(R.id.fragment_container, fragment, "findThisFragment")
                         .addToBackStack(null)
                         .commit();
        }
    }
}