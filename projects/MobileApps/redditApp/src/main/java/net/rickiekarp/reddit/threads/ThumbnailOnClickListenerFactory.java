package net.rickiekarp.reddit.threads;

import net.rickiekarp.reddit.things.ThingInfo;

import android.app.Activity;
import android.view.View.OnClickListener;

public interface ThumbnailOnClickListenerFactory {
	OnClickListener getThumbnailOnClickListener(ThingInfo threadThingInfo, Activity activity);
}