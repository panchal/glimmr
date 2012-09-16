package com.bourke.glimmrpro.tasks;

import android.os.AsyncTask;

import android.util.Log;

import com.bourke.glimmrpro.common.Constants;
import com.bourke.glimmrpro.common.FlickrHelper;
import com.bourke.glimmrpro.event.Events.IGroupListReadyListener;
import com.bourke.glimmrpro.fragments.base.BaseFragment;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.groups.Group;
import com.googlecode.flickrjandroid.groups.GroupList;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;

import java.io.IOException;

import java.util.Collection;

public class LoadGroupsTask extends AsyncTask<OAuth, Void, Collection<Group>> {

    private static final String TAG = "Glimmr/LoadGroupsTask";

    private IGroupListReadyListener mListener;
    private BaseFragment mBaseFragment;

    public LoadGroupsTask(BaseFragment a, IGroupListReadyListener listener) {
        mBaseFragment = a;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mBaseFragment.showProgressIcon(true);
    }

    @Override
    protected Collection<Group> doInBackground(OAuth... arg0) {
        OAuthToken token = arg0[0].getToken();
        Flickr f = FlickrHelper.getInstance().getFlickrAuthed(
                token.getOauthToken(), token.getOauthTokenSecret());
        User user = arg0[0].getUser();

        try {
            return f.getPoolsInterface().getGroups();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FlickrException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(final Collection<Group> result) {
        GroupList ret = new GroupList();
        if (result == null) {
            if (Constants.DEBUG)
                Log.e(TAG, "Error fetching groups, result is null");
        } else {
            ret.addAll(result);
        }
        mListener.onGroupListReady(ret);
        mBaseFragment.showProgressIcon(false);
    }

    @Override
    protected void onCancelled(final Collection<Group> result) {
        if (Constants.DEBUG)
            Log.d(TAG, "onCancelled");
    }
}
