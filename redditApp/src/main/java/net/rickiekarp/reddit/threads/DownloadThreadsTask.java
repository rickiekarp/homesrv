package net.rickiekarp.reddit.threads;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rickiekarp.reddit.common.CacheInfo;
import net.rickiekarp.reddit.common.Common;
import net.rickiekarp.reddit.common.Constants;
import net.rickiekarp.reddit.common.ProgressInputStream;
import net.rickiekarp.reddit.common.util.StringUtils;
import net.rickiekarp.reddit.filters.RedditFilterEngine;
import net.rickiekarp.reddit.settings.RedditSettings;
import net.rickiekarp.reddit.things.Listing;
import net.rickiekarp.reddit.things.ListingData;
import net.rickiekarp.reddit.things.ThingInfo;
import net.rickiekarp.reddit.things.ThingListing;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Given a subreddit name string, starts the threadlist-download-thread going.
 * 
 * @param subreddit The name of a subreddit ("android", "gaming", etc.)
 *        If the number of elements in subreddit is >= 2, treat 2nd element as "after" 
 */
public abstract class DownloadThreadsTask extends AsyncTask<Void, Long, Boolean> implements PropertyChangeListener {

	static final String TAG = "DownloadThreadsTask";

	protected Context mContext;
	protected final HttpClient mClient;
	private ObjectMapper mOm;

	protected String mSubreddit;
	protected String mSortByUrl = Constants.ThreadsSort.SORT_BY_HOT_URL;
	protected String mSortByUrlExtra = "";
	protected String mAfter;
	protected String mBefore;
	protected int mCount;
	protected String mLastAfter = null;
	protected String mLastBefore = null;
	protected int mLastCount = 0;
	protected RedditSettings mSettings = new RedditSettings();

	//the GET parameters to be passed when performing a search
	//just get it to recognize the query first, get sort working later.
	protected String mSearchQuery;
	protected String mSortSearch; //not implemented yet

	Uri mDTTSavedURI;

	protected String mUserError = "Error retrieving subreddit info.";

	protected RedditFilterEngine mFilterEngine;
	// Progress bar
	protected long mContentLength = 0;

	// Downloaded data
	protected ArrayList<ThingInfo> mThingInfos = new ArrayList<ThingInfo>();
	protected String mModhash = null;

	public DownloadThreadsTask(Context context, HttpClient client, ObjectMapper om,
							   String sortByUrl, String sortByUrlExtra,
							   String subreddit, String query, String sort) {
		this(context, client, om, sortByUrl, sortByUrlExtra, subreddit, null, null, Constants.DEFAULT_THREAD_DOWNLOAD_LIMIT);
		mSearchQuery = query;
		mSortSearch = sort;
	}

	public DownloadThreadsTask(Context context, HttpClient client, ObjectMapper om,
							   String sortByUrl, String sortByUrlExtra,
							   String subreddit, Uri redditURI) {
		this(context, client, om, sortByUrl, sortByUrlExtra, subreddit, null, null, Constants.DEFAULT_THREAD_DOWNLOAD_LIMIT);
		mDTTSavedURI = redditURI;
	}

	public DownloadThreadsTask(Context context, HttpClient client, ObjectMapper om,
							   String sortByUrl, String sortByUrlExtra,
							   String subreddit, Uri redditURI, String after, String before, int count) {
		this(context, client, om, sortByUrl, sortByUrlExtra, subreddit, after, before, count);
		mDTTSavedURI = redditURI;
	}

	public DownloadThreadsTask(Context context, HttpClient client, ObjectMapper om,
							   String sortByUrl, String sortByUrlExtra,
							   String subreddit) {
		this(context, client, om, sortByUrl, sortByUrlExtra, subreddit, null, null, Constants.DEFAULT_THREAD_DOWNLOAD_LIMIT);
	}

	public DownloadThreadsTask(Context context, HttpClient client, ObjectMapper om,
							   String sortByUrl, String sortByUrlExtra,
							   String subreddit, String after, String before, int count) {
		mSettings.loadRedditPreferences(context, null);
		mContext = context;
		mClient = client;
		mOm = om;
		mSortByUrl = sortByUrl;
		mSortByUrlExtra = sortByUrlExtra;
		mDTTSavedURI = null;
		if (subreddit != null) {
			mSubreddit = subreddit;
		} else {
			mSubreddit = Constants.FRONTPAGE_STRING;
		}

		mAfter = after;
		mBefore = before;
		mCount = count;
		mFilterEngine = new RedditFilterEngine(mContext);
	}

	public Boolean doInBackground(Void... zzz) {
		HttpEntity entity = null;
		boolean isAfter = false;
		boolean isBefore = false;
		try {
			String url;
			StringBuilder sb;
			// If refreshing or something, use the previously used URL to get the threads.
			// Picking a new subreddit will erase the saved URL, getting rid of after= and before=.
			// subreddit.length != 0 means you are going Next or Prev, which creates new URL.
			if (Constants.FRONTPAGE_STRING.equals(mSubreddit)) {
				sb = new StringBuilder(Constants.REDDIT_BASE_URL + "/").append(mSortByUrl)
						.append(".json?").append(mSortByUrlExtra).append("&");
			}
			//prepare a search query
			else if(Constants.REDDIT_SEARCH_STRING.equals(mSubreddit)) {
				sb = new StringBuilder(Constants.REDDIT_BASE_URL + "/search/").append(".json?q=")
						.append(URLEncoder.encode(mSearchQuery, "utf8")).append("&sort=" + mSortSearch);
			} else if(Constants.REDDIT_SAVED_STRING.equals(mSubreddit)) {
				// Appending the ? without query params is still valid.
				sb = new StringBuilder(mDTTSavedURI.toString()).append('?');
			} else {
				sb = new StringBuilder(Constants.REDDIT_BASE_URL + "/r/")
						.append(mSubreddit.trim())
						.append("/").append(mSortByUrl).append(".json?")
						.append(mSortByUrlExtra).append("&");
			}
			// "before" always comes back null unless you provide correct "count"
			if (mAfter != null) {
				// count: 25, 50, ...
				sb = sb.append("count=").append(mCount)
						.append("&after=").append(mAfter).append("&");
				isAfter = true;
			}
			else if (mBefore != null) {
				// count: nothing, 26, 51, ...
				sb = sb.append("count=").append(mCount + 1 - Constants.DEFAULT_THREAD_DOWNLOAD_LIMIT)
						.append("&before=").append(mBefore).append("&");
				isBefore = true;
			}

			url = sb.toString();
            /*CharSequence text = (CharSequence)url;
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(mContext, text, duration);
            toast.show();*/
			//https://pay.reddit.com/.json?&
			if (Constants.LOGGING) Log.d(TAG, "url=" + url);

			InputStream in = null;
			boolean currentlyUsingCache = false;

			if (Constants.USE_THREADS_CACHE) {
				try {
					if (CacheInfo.checkFreshSubredditCache(mContext)
							&& url.equals(CacheInfo.getCachedSubredditUrl(mContext))) {
						in = mContext.openFileInput(Constants.FILENAME_SUBREDDIT_CACHE);
						mContentLength = mContext.getFileStreamPath(Constants.FILENAME_SUBREDDIT_CACHE).length();
						currentlyUsingCache = true;
						if (Constants.LOGGING) Log.d(TAG, "Using cached subreddit JSON, length=" + mContentLength);
					}
				} catch (Exception cacheEx) {
					if (Constants.LOGGING) Log.w(TAG, "skip cache", cacheEx);
				}
			}

			// If we couldn't use the cache, then do HTTP request
			if (!currentlyUsingCache) {
				HttpGet request;
				try {
					request = new HttpGet(url);
				} catch (IllegalArgumentException e) {
					mUserError = "Invalid subreddit.";
					if (Constants.LOGGING) Log.e(TAG, "IllegalArgumentException", e);
					return false;
				}
				HttpResponse response = mClient.execute(request);

				// Read the header to get Content-Length since entity.getContentLength() returns -1
				Header contentLengthHeader = response.getFirstHeader("Content-Length");

				entity = response.getEntity();
				in = entity.getContent();

				if (contentLengthHeader != null) {
					mContentLength = Long.valueOf(contentLengthHeader.getValue());
					if (Constants.LOGGING) Log.d(TAG, "Content length [sent]: "+mContentLength);
				}
				else {
					mContentLength = -1;
					if (Constants.LOGGING) Log.d(TAG, "Content length not available");
				}

				if (Constants.USE_THREADS_CACHE) {
					in = CacheInfo.writeThenRead(mContext, in, Constants.FILENAME_SUBREDDIT_CACHE);
					try {
						CacheInfo.setCachedSubredditUrl(mContext, url);
					} catch (IOException e) {
						if (Constants.LOGGING) Log.e(TAG, "error on setCachedSubreddit", e);
					}
				}
			}

			ProgressInputStream pin = new ProgressInputStream(in, mContentLength);
			pin.addPropertyChangeListener(this);

			try {
				parseSubredditJSON(pin);

				mLastCount = mCount;
				if (isAfter)
					mCount += Constants.DEFAULT_THREAD_DOWNLOAD_LIMIT;
				else if (isBefore)
					mCount -= Constants.DEFAULT_THREAD_DOWNLOAD_LIMIT;

				saveState();

				return true;

			} catch (IllegalStateException e) {
				mUserError = "Invalid subreddit.";
				if (Constants.LOGGING) Log.e(TAG, "IllegalStateException", e);
			} catch (Exception e) {
				if (Constants.LOGGING) Log.e(TAG, "Exception", e);
			} finally {
				pin.close();
				in.close();
			}
		} catch (Exception e) {
			if (Constants.LOGGING) Log.e(TAG, "DownloadThreadsTask", e);
		} finally {
			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (Exception e2) {
					if (Constants.LOGGING) Log.e(TAG, "entity.consumeContent()", e2);
				}
			}
		}
		return false;
	}

	protected void parseSubredditJSON(InputStream in)
			throws IOException, JsonParseException, IllegalStateException {

		String genericListingError = "Not a subreddit listing";
		try {
			Listing listing = mOm.readValue(in, Listing.class);

			if (!Constants.JSON_LISTING.equals(listing.getKind()))
				throw new IllegalStateException(genericListingError);
			// Save the modhash, after, and before
			ListingData data = listing.getData();
			if (StringUtils.isEmpty(data.getModhash()))
				mModhash = null;
			else
				mModhash = data.getModhash();

			mLastAfter = mAfter;
			mLastBefore = mBefore;
			mAfter = data.getAfter();
			mBefore = data.getBefore();

			// Go through the children and get the ThingInfos
			for (ThingListing tiContainer : data.getChildren()) {
				// Only add entries that are threads. kind="t3"
				if (Constants.THREAD_KIND.equals(tiContainer.getKind())) {
					ThingInfo ti = tiContainer.getData();
					ti.setClicked(Common.isClicked(mContext, ti.getUrl()));
					if((mSettings.getShowNSFW() || !ti.isOver_18()) && !mFilterEngine.isFiltered(ti)) {
						mThingInfos.add(ti);
					}
				}
			}
		} catch (Exception ex) {
			if (Constants.LOGGING) Log.e(TAG, "parseSubredditJSON", ex);
		}
	}

	abstract protected void saveState();
}