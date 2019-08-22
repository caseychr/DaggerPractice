package com.codingwithmitch.daggerpractice.ui.main.posts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codingwithmitch.daggerpractice.R;
import com.codingwithmitch.daggerpractice.models.Post;
import com.codingwithmitch.daggerpractice.ui.main.Resource;
import com.codingwithmitch.daggerpractice.util.VerticalSpacingItemDecoration;
import com.codingwithmitch.daggerpractice.viewmodels.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerFragment;

public class PostsFragment extends DaggerFragment {
    private static final String TAG = "PostsFragment";

    private PostsViewModel mPostsViewModel;
    private RecyclerView mRecyclerView;

    @Inject PostsRecyclerAdapter mAdapter;
    @Inject ViewModelProviderFactory mViewModelProviderFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mPostsViewModel = ViewModelProviders.of(this, mViewModelProviderFactory).get(PostsViewModel.class);
        initRecyclerView();
        subscribeObservers();
    }

    private void subscribeObservers() {
        mPostsViewModel.observePosts().removeObservers(getViewLifecycleOwner());
        mPostsViewModel.observePosts().observe(getViewLifecycleOwner(), new Observer<Resource<List<Post>>>() {
            @Override
            public void onChanged(Resource<List<Post>> listResource) {
                if(listResource != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            Log.i(TAG, "onChanged: LOADING...");
                            break;
                        }
                        case ERROR: {
                            Log.e(TAG, "onChanged: ERROR..."+listResource.message);
                            break;
                        }
                        case SUCCESS: {
                            Log.i(TAG, "onChanged: SUCCESS...");
                            mAdapter.setPosts(listResource.data);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpacingItemDecoration verticalSpacingItemDecoration = new VerticalSpacingItemDecoration(15);
        mRecyclerView.addItemDecoration(verticalSpacingItemDecoration);
        mRecyclerView.setAdapter(mAdapter);

    }
}
