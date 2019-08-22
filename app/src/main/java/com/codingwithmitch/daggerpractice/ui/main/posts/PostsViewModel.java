package com.codingwithmitch.daggerpractice.ui.main.posts;

import android.content.res.Resources;
import android.util.Log;

import com.codingwithmitch.daggerpractice.SessionManager;
import com.codingwithmitch.daggerpractice.models.Post;
import com.codingwithmitch.daggerpractice.network.main.MainApi;
import com.codingwithmitch.daggerpractice.ui.main.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PostsViewModel extends ViewModel {
    private static final String TAG = "PostsViewModel";

    private final SessionManager mSessionManager;
    private final MainApi mMainApi;

    MediatorLiveData<Resource<List<Post>>> posts;

    @Inject
    public PostsViewModel(SessionManager sessionManager, MainApi mainApi) {
        mSessionManager = sessionManager;
        mMainApi = mainApi;
        Log.i(TAG, "PostsViewModel: viewmodel is working...");
    }

    public LiveData<Resource<List<Post>>> observePosts() {
        if(posts == null) {
            posts = new MediatorLiveData<>();
            posts.setValue(Resource.loading((List<Post>)null));
            /**
             * LiveDataReactoveStreams converts the RxJava object to LiveData object
             * fromPublisher makes the request to the API
             */
            final LiveData<Resource<List<Post>>> source = LiveDataReactiveStreams
                    .fromPublisher(mMainApi.getPostsFromUser(mSessionManager.getAuthUser().getValue().data.getId())
                            // Should the request return an error
                            .onErrorReturn(new Function<Throwable, List<Post>>() {
                                @Override
                                public List<Post> apply(Throwable throwable) throws Exception {
                                    Log.e(TAG, "apply: ", throwable);
                                    Post post = new Post();
                                    post.setId(1);
                                    ArrayList<Post> posts = new ArrayList<>();
                                    posts.add(post);
                                    return posts;
                                }
                            }).map(new Function<List<Post>, Resource<List<Post>>>() {
                                @Override
                                public Resource<List<Post>> apply(List<Post> posts) throws Exception {
                                    if(posts.size() > 0) {
                                        if(posts.get(0).getId() == -1) {
                                            return Resource.error("Something went wrong", null);
                                        }
                                    }
                                    return Resource.success(posts);
                                }
                            }).subscribeOn(Schedulers.io())
                    );
            posts.addSource(source, new Observer<Resource<List<Post>>>() {
                @Override
                public void onChanged(Resource<List<Post>> listResource) {
                    posts.setValue(listResource);
                    posts.removeSource(source);
                }
            });
        }
        return posts;
    }
}
