package net.rickiekarp.reddit.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rickiekarp.reddit.R;
import net.rickiekarp.reddit.captcha.CaptchaCheckRequiredTask;
import net.rickiekarp.reddit.captcha.CaptchaDownloadTask;
import net.rickiekarp.reddit.comments.CommentsListActivity;
import net.rickiekarp.reddit.common.CacheInfo;
import net.rickiekarp.reddit.common.Common;
import net.rickiekarp.reddit.common.Constants;
import net.rickiekarp.reddit.common.FormValidation;
import net.rickiekarp.reddit.common.ProgressInputStream;
import net.rickiekarp.reddit.common.RedditIsFunHttpClientFactory;
import net.rickiekarp.reddit.common.tasks.VoteTask;
import net.rickiekarp.reddit.common.util.StringUtils;
import net.rickiekarp.reddit.common.util.Util;
import net.rickiekarp.reddit.login.LoginDialog;
import net.rickiekarp.reddit.login.LoginTask;
import net.rickiekarp.reddit.mail.MessageComposeTask;
import net.rickiekarp.reddit.markdown.Markdown;
import net.rickiekarp.reddit.settings.RedditSettings;
import net.rickiekarp.reddit.things.Listing;
import net.rickiekarp.reddit.things.ListingData;
import net.rickiekarp.reddit.things.ThingInfo;
import net.rickiekarp.reddit.things.ThingListing;
import net.rickiekarp.reddit.threads.ShowThumbnailsTask;
import net.rickiekarp.reddit.threads.ShowThumbnailsTask.ThumbnailLoadAction;
import net.rickiekarp.reddit.threads.ThreadClickDialog;
import net.rickiekarp.reddit.threads.ThreadClickDialogOnClickListenerFactory;
import net.rickiekarp.reddit.threads.ThreadsListActivity;
import net.rickiekarp.reddit.threads.ThumbnailOnClickListenerFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Activity to view user submissions and comments.
 * Also check their link and comment karma.
 * 
 * @author TalkLittle
 *
 */
public final class ProfileActivity extends ListActivity
		implements View.OnCreateContextMenuListener {

	private static final String TAG = "ProfileActivity";

	static final Pattern USER_PATH_PATTERN = Pattern.compile(Constants.USER_PATH_PATTERN_STRING);

	private final ObjectMapper mObjectMapper = Common.getObjectMapper();

	/** Custom list adapter that fits our threads data into the list. */
	private ThingsListAdapter mThingsAdapter;
	private ArrayList<ThingInfo> mThingsList;
	// Lock used when modifying the mMessagesAdapter
	private static final Object MESSAGE_ADAPTER_LOCK = new Object();


	private final HttpClient mClient = RedditIsFunHttpClientFactory.getGzipHttpClient();


	// Common settings are stored here
	private final RedditSettings mSettings = new RedditSettings();

	// UI State
	private ThingInfo mVoteTargetThingInfo = null;
	// TODO: String mVoteTargetId so when you rotate, you can find the TargetThingInfo again
	private DownloadProfileTask mCurrentDownloadThingsTask = null;
	private final Object mCurrentDownloadThingsTaskLock = new Object();
	private View mNextPreviousView = null;

	private String mUsername = null;

	private String mAfter = null;
	private String mBefore = null;
	private int mCount = 0;
	private String mLastAfter = null;
	private String mLastBefore = null;
	private int mLastCount = 0;
	private int[] mKarma = null;
	private String mSortByUrl = null;
	private String mSortByUrlExtra = null;

	private String mJumpToThreadId = null;

	private volatile String mCaptchaIden = null;
	private volatile String mCaptchaUrl = null;

	// ProgressDialogs with percentage bars
//    private AutoResetProgressDialog mLoadingCommentsProgress;
//    private int mNumVisibleMessages;

	/**
	 * Called when the activity starts up. Do activity initialization
	 * here, not in a constructor.
	 *
	 * @see Activity#onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CookieSyncManager.createInstance(getApplicationContext());

		mSettings.loadRedditPreferences(this, mClient);
		setRequestedOrientation(mSettings.getRotation());
		setTheme(mSettings.getTheme());
		requestWindowFeature(Window.FEATURE_PROGRESS);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.profile_list_content);
		registerForContextMenu(getListView());

		if (savedInstanceState != null) {
			if (Constants.LOGGING) Log.d(TAG, "using savedInstanceState");
			mUsername = savedInstanceState.getString(Constants.USERNAME_KEY);
			mAfter = savedInstanceState.getString(Constants.AFTER_KEY);
			mBefore = savedInstanceState.getString(Constants.BEFORE_KEY);
			mCount = savedInstanceState.getInt(Constants.THREAD_COUNT_KEY);
			mLastAfter = savedInstanceState.getString(Constants.LAST_AFTER_KEY);
			mLastBefore = savedInstanceState.getString(Constants.LAST_BEFORE_KEY);
			mLastCount = savedInstanceState.getInt(Constants.THREAD_LAST_COUNT_KEY);
			mKarma = savedInstanceState.getIntArray(Constants.KARMA_KEY);
			mSortByUrl = savedInstanceState.getString(Constants.CommentsSort.SORT_BY_KEY);
			mJumpToThreadId = savedInstanceState.getString(Constants.JUMP_TO_THREAD_ID_KEY);
			mVoteTargetThingInfo = savedInstanceState.getParcelable(Constants.VOTE_TARGET_THING_INFO_KEY);

			// try to restore mThingsList using getLastNonConfigurationInstance()
			// (separate function to avoid a compiler warning casting ArrayList<ThingInfo>
			restoreLastNonConfigurationInstance();
			if (mThingsList == null) {
				// Load previous page of profile items
				if (mLastAfter != null) {
					new DownloadProfileTask(mUsername, mLastAfter, null, mLastCount).execute();
				} else if (mLastBefore != null) {
					new DownloadProfileTask(mUsername, null, mLastBefore, mLastCount).execute();
				} else {
					new DownloadProfileTask(mUsername).execute();
				}
			} else {
				// Orientation change. Use prior instance.
				resetUI(new ThingsListAdapter(this, mThingsList));
				setTitle(String.format(getResources().getString(R.string.user_profile), mUsername));
			}
			return;
		}
		// Handle subreddit Uri passed via Intent
		else if (getIntent().getData() != null) {
			Matcher userPathMatcher = USER_PATH_PATTERN.matcher(getIntent().getData().getPath());
			if (userPathMatcher.matches()) {
				mUsername = userPathMatcher.group(1);
				new DownloadProfileTask(mUsername).execute();
				return;
			}
		}

		// No username specified by Intent, so load the logged in user's profile
		if (mSettings.isLoggedIn()) {
			mUsername = mSettings.getUsername();
			new DownloadProfileTask(mUsername).execute();
			return;
		}

		// Can't find a username to use. Quit.
		if (Constants.LOGGING) Log.e(TAG, "Could not find a username to use for ProfileActivity");
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		CookieSyncManager.getInstance().startSync();
		int previousTheme = mSettings.getTheme();
		boolean previousLoggedIn = mSettings.isLoggedIn();
		mSettings.loadRedditPreferences(this, mClient);
		setRequestedOrientation(mSettings.getRotation());
		if (mSettings.getTheme() != previousTheme) {
			resetUI(mThingsAdapter);
		}
		updateNextPreviousButtons();
		updateKarma();
		if (mSettings.isLoggedIn() != previousLoggedIn) {
			new DownloadProfileTask(mSettings.getUsername()).execute(Constants.DEFAULT_COMMENT_DOWNLOAD_LIMIT);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		CookieSyncManager.getInstance().stopSync();
		mSettings.saveRedditPreferences(this);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		// Avoid having to re-download and re-parse the messages list
		// when rotating or opening keyboard.
		return mThingsList;
	}

	@SuppressWarnings("unchecked")
	private void restoreLastNonConfigurationInstance() {
		mThingsList = (ArrayList<ThingInfo>) getLastNonConfigurationInstance();
	}



	private final class ThingsListAdapter extends ArrayAdapter<ThingInfo> {
		static final int THREAD_ITEM_VIEW_TYPE = 0;
		static final int COMMENT_ITEM_VIEW_TYPE = 1;

		private static final int VIEW_TYPE_COUNT = 2;

		public boolean mIsLoading = true;

		private LayoutInflater mInflater;

		@Override
		public int getItemViewType(int position) {
			ThingInfo item = getItem(position);
			if (item.getName().startsWith(Constants.THREAD_KIND)) {
				return THREAD_ITEM_VIEW_TYPE;
			}
			if (item.getName().startsWith(Constants.COMMENT_KIND)) {
				return COMMENT_ITEM_VIEW_TYPE;
			}
			return COMMENT_ITEM_VIEW_TYPE;
		}

		@Override
		public int getViewTypeCount() {
			return VIEW_TYPE_COUNT;
		}

		public boolean isEmpty() {
			if (mIsLoading)
				return false;
			return super.isEmpty();
		}

		public ThingsListAdapter(Context context, List<ThingInfo> objects) {
			super(context, 0, objects);
			mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			ThingInfo item = this.getItem(position);

			if (getItemViewType(position) == THREAD_ITEM_VIEW_TYPE) {
				// Here view may be passed in for re-use, or we make a new one.
				if (convertView == null) {
					view = mInflater.inflate(R.layout.threads_list_item, null);
				} else {
					view = convertView;
				}

				ThreadsListActivity.fillThreadsListItemView(
						position, view, item, ProfileActivity.this, mClient, mSettings, mThumbnailOnClickListenerFactory
				);
			} else if (getItemViewType(position) == COMMENT_ITEM_VIEW_TYPE) {
				// Here view may be passed in for re-use, or we make a new one.
				if (convertView == null) {
					view = mInflater.inflate(R.layout.comments_list_item, null);
				} else {
					view = convertView;
				}

				CommentsListActivity.fillCommentsListItemView(view, item, mSettings);
				view.setPadding(15, 5, 0, 5);
			}

			return view;
		}
	} // End of MessagesListAdapter


	/**
	 * Called when user clicks an item in the list. Mark message read.
	 * If item was already focused, open a dialog.
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		ThingInfo item = mThingsAdapter.getItem(position);

		// Mark the message/comment as selected
		mVoteTargetThingInfo = item;

		if (item.getName().startsWith(Constants.THREAD_KIND)) {
			showDialog(Constants.DIALOG_THREAD_CLICK);
		} else {
			openContextMenu(v);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		int rowId = (int) info.id;
		ThingInfo item = mThingsAdapter.getItem(rowId);

		// Mark the message/comment as selected
		mVoteTargetThingInfo = item;

		if (item.isThreadKind()) {
			menu.add(0, Constants.DIALOG_THREAD_CLICK, Menu.NONE, R.string.goto_thread);
		} else {
			menu.add(0, Constants.DIALOG_COMMENT_CLICK, Menu.NONE, R.string.view_context);
			if (mVoteTargetThingInfo.getLink_id() != null) {
				// Don't add the thread link if the link_id is null for some reason.
				menu.add(1, Constants.DIALOG_THREAD_CLICK, Menu.NONE, R.string.goto_thread);
			}
			// Lazy-load the URL list to make the list-loading more 'snappy'.
			if (mVoteTargetThingInfo.getUrls() != null && mVoteTargetThingInfo.getUrls().isEmpty()) {
				Markdown.getURLs(mVoteTargetThingInfo.getBody(), mVoteTargetThingInfo.getUrls());
			}
			if (mVoteTargetThingInfo.getUrls() != null && !mVoteTargetThingInfo.getUrls().isEmpty()) {
				menu.add(0, Constants.DIALOG_LINKS, Menu.NONE, R.string.links_menu_item);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Intent i;

		switch (item.getItemId()) {
			case Constants.DIALOG_COMMENT_CLICK:
				i = new Intent(getApplicationContext(), CommentsListActivity.class);
				i.setData(Util.createCommentUri(mVoteTargetThingInfo, 3));
				i.putExtra(Constants.EXTRA_SUBREDDIT, mVoteTargetThingInfo.getSubreddit());
				i.putExtra(Constants.EXTRA_TITLE, mVoteTargetThingInfo.getTitle());
				startActivity(i);
				return true;
			case Constants.DIALOG_THREAD_CLICK:
				// Launch an Intent for CommentsListActivity
				CacheInfo.invalidateCachedThread(getApplicationContext());
				i = new Intent(getApplicationContext(), CommentsListActivity.class);
				i.putExtra(Constants.EXTRA_SUBREDDIT, mVoteTargetThingInfo.getSubreddit());
				i.putExtra(Constants.EXTRA_TITLE, mVoteTargetThingInfo.getTitle());
				// Thread things are handled differently than comment things.
				if (mVoteTargetThingInfo.isThreadKind()) {
					i.setData(Util.createThreadUri(mVoteTargetThingInfo));
					i.putExtra(Constants.EXTRA_NUM_COMMENTS, Integer.valueOf(mVoteTargetThingInfo.getNum_comments()));
				} else {
					// Should only get to this point if the Thing is a Comment, and link_id is valid.
					i.setData(Util.createThreadUri(mVoteTargetThingInfo.getSubreddit(), Util.nameToId(mVoteTargetThingInfo.getLink_id())));
				}
				startActivity(i);
				return true;
			case Constants.DIALOG_LINKS:
				Common.showLinksDialog(ProfileActivity.this, mSettings, mVoteTargetThingInfo);
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}



	/**
	 * Resets the output UI list contents, retains session state.
	 * @param messagesAdapter A MessagesListAdapter to use. Pass in null if you want a new empty one created.
	 */
	void resetUI(ThingsListAdapter messagesAdapter) {
		findViewById(R.id.loading_light).setVisibility(View.GONE);
		findViewById(R.id.loading_dark).setVisibility(View.GONE);

		if (mSettings.isAlwaysShowNextPrevious()) {
			if (mNextPreviousView != null) {
				getListView().removeFooterView(mNextPreviousView);
				mNextPreviousView = null;
			}
		} else {
			findViewById(R.id.next_previous_layout).setVisibility(View.GONE);
			if (getListView().getFooterViewsCount() == 0) {
				// If we are not using the persistent navbar, then show as ListView footer instead
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				mNextPreviousView = inflater.inflate(R.layout.next_previous_list_item, null);
				getListView().addFooterView(mNextPreviousView);
			}
		}

		synchronized (MESSAGE_ADAPTER_LOCK) {
			if (messagesAdapter == null) {
				// Reset the list to be empty.
				mThingsList = new ArrayList<ThingInfo>();
				mThingsAdapter = new ThingsListAdapter(this, mThingsList);
			} else {
				mThingsAdapter = messagesAdapter;
			}

			setListAdapter(mThingsAdapter);
			mThingsAdapter.mIsLoading = false;
			mThingsAdapter.notifyDataSetChanged();  // Just in case
		}
		getListView().setDivider(null);
		Common.updateListDrawables(this, mSettings.getTheme());
		updateNextPreviousButtons();
	}

	private void enableLoadingScreen() {
		if (Util.isLightTheme(mSettings.getTheme())) {
			findViewById(R.id.loading_light).setVisibility(View.VISIBLE);
			findViewById(R.id.loading_dark).setVisibility(View.GONE);
		} else {
			findViewById(R.id.loading_light).setVisibility(View.GONE);
			findViewById(R.id.loading_dark).setVisibility(View.VISIBLE);
		}
		synchronized (MESSAGE_ADAPTER_LOCK) {
			if (mThingsAdapter != null)
				mThingsAdapter.mIsLoading = true;
		}
		getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_START);
	}

	private void disableLoadingScreen() {
		resetUI(mThingsAdapter);
		getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_END);
	}

	private void updateNextPreviousButtons() {
		Common.updateNextPreviousButtons(this, mNextPreviousView, mAfter, mBefore, mCount, mSettings,
				downloadAfterOnClickListener, downloadBeforeOnClickListener);
	}

	private void updateKarma() {
		if (mKarma == null)
			return;
		View karmaLayout = findViewById(R.id.karma_layout);
		View karmaLayoutBorder = findViewById(R.id.karma_border_bottom);
		if (karmaLayout != null && karmaLayoutBorder != null) {
			karmaLayout.setVisibility(View.VISIBLE);
			if (Util.isLightTheme(mSettings.getTheme())) {
				karmaLayout.setBackgroundResource(android.R.color.background_light);
				karmaLayoutBorder.setBackgroundResource(R.color.black);
			} else {
				karmaLayoutBorder.setBackgroundResource(R.color.white);
			}
			TextView linkKarma = (TextView) findViewById(R.id.link_karma);
			TextView commentKarma = (TextView) findViewById(R.id.comment_karma);
			linkKarma.setText(mKarma[0] + " link karma");
			commentKarma.setText(mKarma[1] + " comment karma");
		}
	}

	/**
	 * Hide or show specific menu items as necessary in a user's profile.
	 */
	private void hideShowMenuItems(Menu theMenu) {
		// Only show the 'View saved posts' menu item when the user is logged in AND we're currently viewing their
		// profile.
		MenuItem savedMenuItem = theMenu.findItem(R.id.saved_menu_id);
		if (savedMenuItem != null) {
			if (mSettings.isLoggedIn() && mSettings.getUsername().equalsIgnoreCase(mUsername)) {
				savedMenuItem.setVisible(true);
			} else {
				savedMenuItem.setVisible(false);
			}
		}
	}



	private class DownloadProfileTask extends AsyncTask<Integer, Long, Void>
			implements PropertyChangeListener {

		private ArrayList<ThingInfo> _mThingInfos = new ArrayList<ThingInfo>();
		private long _mContentLength;

		private String mUsername;
		private String mAfter;
		private String mBefore;
		private int mCount;
		private String mLastAfter = null;
		private String mLastBefore = null;
		private int mLastCount = 0;
		private int[] mKarma;
		private String mSortByUrl;
		private String mSortByUrlExtra;

		public DownloadProfileTask(String username) {
			this(username, null, null, Constants.DEFAULT_THREAD_DOWNLOAD_LIMIT,
					ProfileActivity.this.mSortByUrl, ProfileActivity.this.mSortByUrlExtra);
		}

		public DownloadProfileTask(String username, String after, String before, int count) {
			this(username, after, before, count,
					ProfileActivity.this.mSortByUrl, ProfileActivity.this.mSortByUrlExtra);
		}

		/**
		 * The real constructor
		 * @param username
		 * @param after
		 * @param before
		 * @param count
		 * @param sortByUrl
		 * @param sortByUrlExtra
		 */
		public DownloadProfileTask(String username, String after, String before, int count,
								   String sortByUrl, String sortByUrlExtra) {
			mUsername = username;
			mAfter = after;
			mBefore = before;
			mCount = count;
			mSortByUrl = sortByUrl;
			mSortByUrlExtra = sortByUrlExtra;
		}

		protected void saveState() {
			ProfileActivity.this.mLastAfter = mLastAfter;
			ProfileActivity.this.mLastBefore = mLastBefore;
			ProfileActivity.this.mLastCount = mLastCount;
			ProfileActivity.this.mAfter = mAfter;
			ProfileActivity.this.mBefore = mBefore;
			ProfileActivity.this.mCount = mCount;
			ProfileActivity.this.mKarma = mKarma;
			ProfileActivity.this.mSortByUrl = mSortByUrl;
			ProfileActivity.this.mSortByUrlExtra = mSortByUrlExtra;
		}

		// XXX: maxComments is unused for now
		public Void doInBackground(Integer... maxComments) {
			HttpEntity entity = null;
			boolean isAfter = false;
			boolean isBefore = false;
			InputStream in = null;
			ProgressInputStream pin = null;

			try {

				if (mKarma == null)
					mKarma = getKarma();

				String url;
				StringBuilder sb = new StringBuilder(Constants.REDDIT_BASE_URL).append("/user/").append(mUsername.trim()).append("/.json?");

				if (mSortByUrl != null)
					sb = sb.append(mSortByUrl).append("&");
				if (mSortByUrlExtra != null)
					sb = sb.append(mSortByUrlExtra).append("&");

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
				if (Constants.LOGGING) Log.d(TAG, "url=" + url);

				HttpGet request = new HttpGet(url);
				HttpResponse response = mClient.execute(request);

				// Read the header to get Content-Length since entity.getContentLength() returns -1
				Header contentLengthHeader = response.getFirstHeader("Content-Length");
				if (contentLengthHeader != null) {
					_mContentLength = Long.valueOf(contentLengthHeader.getValue());
					if (Constants.LOGGING) Log.d(TAG, "Content length: "+_mContentLength);
				} else {
					_mContentLength = -1;
					if (Constants.LOGGING) Log.d(TAG, "Content length: UNAVAILABLE");
				}

				entity = response.getEntity();
				in = entity.getContent();

				// setup a special InputStream to report progress
				pin = new ProgressInputStream(in, _mContentLength);
				pin.addPropertyChangeListener(this);

				parseThingsJSON(pin);

				mLastCount = mCount;
				if (isAfter)
					mCount += Constants.DEFAULT_THREAD_DOWNLOAD_LIMIT;
				else if (isBefore)
					mCount -= Constants.DEFAULT_THREAD_DOWNLOAD_LIMIT;

				saveState();

			} catch (Exception e) {
				if (Constants.LOGGING) Log.e(TAG, "failed", e);
			} finally {
				if (pin != null) {
					try {
						pin.close();
					} catch (IOException e2) {
						if (Constants.LOGGING) Log.e(TAG, "pin.close()", e2);
					}
				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e2) {
						if (Constants.LOGGING) Log.e(TAG, "in.close()", e2);
					}
				}
				if (entity != null) {
					try {
						entity.consumeContent();
					} catch (Exception e2) {
						if (Constants.LOGGING) Log.e(TAG, "entity.consumeContent()", e2);
					}
				}
			}
			return null;
		}

		/**
		 * @return [linkKarma, commentKarma]
		 */
		private int[] getKarma() throws IOException {
			String url = new StringBuilder(Constants.REDDIT_BASE_URL).append("/user/").append(mUsername.trim()).append("/about.json").toString();

			if (Constants.LOGGING) Log.d(TAG, "karma url=" + url);

			HttpGet request = new HttpGet(url);
			HttpResponse response = mClient.execute(request);

			HttpEntity entity = null;
			InputStream in = null;
			try {
				entity = response.getEntity();
				in = entity.getContent();

				UserInfo userInfo = UserInfoParser.parseJSON(in);
				if (userInfo != null)
					return new int[] { userInfo.getLink_karma(), userInfo.getComment_karma() };

			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception ignore) {
					}
				}
				if (entity != null) {
					try {
						entity.consumeContent();
					} catch (Exception ignore) {
					}
				}
			}

			return null;
		}

		private void parseThingsJSON(InputStream in) throws IOException,
				IllegalStateException {

			String genericListingError = "Not a user page listing";
			try {
				Listing listing = mObjectMapper.readValue(in, Listing.class);

				if (!Constants.JSON_LISTING.equals(listing.getKind()))
					throw new IllegalStateException(genericListingError);
				// Save the modhash, after, and before
				ListingData data = listing.getData();
				if (StringUtils.isEmpty(data.getModhash()))
					mSettings.setModhash(null);
				else
					mSettings.setModhash(data.getModhash());

				mLastAfter = mAfter;
				mLastBefore = mBefore;
				mAfter = data.getAfter();
				mBefore = data.getBefore();

				// Go through the children and get the ThingInfos
				for (ThingListing tiContainer : data.getChildren()) {
					if (Constants.COMMENT_KIND.equals(tiContainer.getKind())) {
						ThingInfo ti = tiContainer.getData();
						// HTML to Spanned
						String unescapedHtmlBody = Html.fromHtml(ti.getBody_html()).toString();
						Spanned body = Html.fromHtml(Util.convertHtmlTags(unescapedHtmlBody));
						// remove last 2 newline characters
						if (body.length() > 2)
							ti.setSpannedBody(body.subSequence(0, body.length()-2));
						else
							ti.setSpannedBody("");

						// First test for moderator distinguished
						if (Constants.DISTINGUISHED_MODERATOR.equalsIgnoreCase(ti.getDistinguished())) {
							SpannableString distSS = new SpannableString(ti.getAuthor() + " [M]");
							distSS.setSpan(Util.getModeratorSpan(getApplicationContext()), 0, distSS.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							ti.setSSAuthor(distSS);
						} else if (Constants.DISTINGUISHED_ADMIN.equalsIgnoreCase(ti.getDistinguished())) {
							SpannableString distSS = new SpannableString(ti.getAuthor() + " [A]");
							distSS.setSpan(Util.getAdminSpan(getApplicationContext()), 0, distSS.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							ti.setSSAuthor(distSS);
						} else if (ti.getLinkAuthor().equalsIgnoreCase(ti.getAuthor())) {
							SpannableString opSpan = new SpannableString(ti.getLinkAuthor() + " [S]");
							opSpan.setSpan(Util.getOPSpan(getApplicationContext(), mSettings.getTheme()), 0, opSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							ti.setSSAuthor(opSpan);
						}
						_mThingInfos.add(ti);
					} else if (Constants.THREAD_KIND.equals(tiContainer.getKind())) {
						ThingInfo ti = tiContainer.getData();
						ti.setClicked(Common.isClicked(ProfileActivity.this, ti.getUrl()));
						_mThingInfos.add(ti);
					}
				}
			} catch (Exception ex) {
				if (Constants.LOGGING) Log.e(TAG, "parseThingsJSON", ex);
			}
		}

		@Override
		public void onPreExecute() {
			synchronized (mCurrentDownloadThingsTaskLock) {
				if (mCurrentDownloadThingsTask != null)
					mCurrentDownloadThingsTask.cancel(true);
				mCurrentDownloadThingsTask = this;
			}
			resetUI(null);
			enableLoadingScreen();
			if (_mContentLength == -1)
				getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_INDETERMINATE_ON);
		}

		@Override
		public void onPostExecute(Void v) {
			synchronized (mCurrentDownloadThingsTaskLock) {
				mCurrentDownloadThingsTask = null;
			}
			synchronized(MESSAGE_ADAPTER_LOCK) {
				for (ThingInfo mi : _mThingInfos)
					mThingsAdapter.add(mi);
			}

			if (_mContentLength == -1)
				getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_INDETERMINATE_OFF);
			else
				getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_END);

			showThumbnails(_mThingInfos);

			disableLoadingScreen();
			setTitle(String.format(getResources().getString(R.string.user_profile), mUsername));
			updateNextPreviousButtons();
			updateKarma();
		}

		@Override
		public void onProgressUpdate(Long... progress) {
			getWindow().setFeatureInt(Window.FEATURE_PROGRESS, progress[0].intValue() * (Window.PROGRESS_END-1) / (int) _mContentLength);
		}

		public void propertyChange(PropertyChangeEvent event) {
			publishProgress((Long) event.getNewValue());
		}
	}

	private void showThumbnails(List<ThingInfo> thingInfos) {
		int size = thingInfos.size();
		ThumbnailLoadAction[] thumbnailLoadActions = new ThumbnailLoadAction[size];
		for (int i = 0; i < thumbnailLoadActions.length; i++) {
			thumbnailLoadActions[i] = new ThumbnailLoadAction(thingInfos.get(i), null, i);
		}
		new ShowThumbnailsTask(this, mClient, R.drawable.go_arrow).execute(thumbnailLoadActions);
	}


	private class MyLoginTask extends LoginTask {
		public MyLoginTask(String username, String password) {
			super(username, password, mSettings, mClient, getApplicationContext());
		}

		@Override
		protected void onPreExecute() {
			showDialog(Constants.DIALOG_LOGGING_IN);
		}

		@Override
		protected void onPostExecute(Boolean success) {
			removeDialog(Constants.DIALOG_LOGGING_IN);
			if (success) {
				Toast.makeText(ProfileActivity.this, "Logged in as "+mUsername, Toast.LENGTH_SHORT).show();
				showDialog(Constants.DIALOG_COMPOSE);
			} else {
				Common.showErrorToast(mUserError, Toast.LENGTH_LONG, ProfileActivity.this);
			}
		}
	}


	private class MyMessageComposeTask extends MessageComposeTask {
		MyMessageComposeTask(Dialog dialog, ThingInfo targetThingInfo, String captcha) {
			super(dialog, targetThingInfo, captcha, mCaptchaIden, mSettings, mClient, getApplicationContext());
		}

		@Override
		public void onPreExecute() {
			showDialog(Constants.DIALOG_COMPOSING);
		}

		@Override
		public void onPostExecute(Boolean success) {
			removeDialog(Constants.DIALOG_COMPOSING);
			if (success) {
				Toast.makeText(ProfileActivity.this, "Message sent.", Toast.LENGTH_SHORT).show();
				// TODO: add the reply beneath the original, OR redirect to sent messages page
			} else {
				Common.showErrorToast(_mUserError, Toast.LENGTH_LONG, ProfileActivity.this);
				new MyCaptchaDownloadTask(_mDialog).execute();
			}
		}
	}

	private class MyVoteTask extends VoteTask {

		private int _mPreviousScore;
		private Boolean _mPreviousLikes;
		private ThingInfo _mTargetThingInfo;

		public MyVoteTask(ThingInfo thingInfo, int direction, String subreddit) {
			super(thingInfo.getName(), direction, subreddit, getApplicationContext(), mSettings, mClient);
			_mTargetThingInfo = thingInfo;
			_mPreviousScore = thingInfo.getScore();
			_mPreviousLikes = thingInfo.getLikes();
		}

		@Override
		public void onPreExecute() {
			if (!_mSettings.isLoggedIn()) {
				Common.showErrorToast("You must be logged in to vote.", Toast.LENGTH_LONG, _mContext);
				cancel(true);
				return;
			}
			if (_mDirection < -1 || _mDirection > 1) {
				if (Constants.LOGGING) Log.e(TAG, "WTF: _mDirection = " + _mDirection);
				throw new RuntimeException("How the hell did you vote something besides -1, 0, or 1?");
			}
			int newScore;
			Boolean newLikes;
			_mPreviousScore = _mTargetThingInfo.getScore();
			_mPreviousLikes = _mTargetThingInfo.getLikes();
			if (_mPreviousLikes == null) {
				if (_mDirection == 1) {
					newScore = _mPreviousScore + 1;
					newLikes = true;
				} else if (_mDirection == -1) {
					newScore = _mPreviousScore - 1;
					newLikes = false;
				} else {
					cancel(true);
					return;
				}
			} else if (_mPreviousLikes) {
				if (_mDirection == 0) {
					newScore = _mPreviousScore - 1;
					newLikes = null;
				} else if (_mDirection == -1) {
					newScore = _mPreviousScore - 2;
					newLikes = false;
				} else {
					cancel(true);
					return;
				}
			} else {
				if (_mDirection == 1) {
					newScore = _mPreviousScore + 2;
					newLikes = true;
				} else if (_mDirection == 0) {
					newScore = _mPreviousScore + 1;
					newLikes = null;
				} else {
					cancel(true);
					return;
				}
			}
			_mTargetThingInfo.setLikes(newLikes);
			_mTargetThingInfo.setScore(newScore);
			mThingsAdapter.notifyDataSetChanged();
		}

		@Override
		public void onPostExecute(Boolean success) {
			if (success) {
				CacheInfo.invalidateCachedSubreddit(_mContext);
			} else {
				// Vote failed. Undo the score.
				_mTargetThingInfo.setLikes(_mPreviousLikes);
				_mTargetThingInfo.setScore(_mPreviousScore);
				mThingsAdapter.notifyDataSetChanged();

				Common.showErrorToast(_mUserError, Toast.LENGTH_LONG, _mContext);
			}
		}
	}

	private class MyCaptchaCheckRequiredTask extends CaptchaCheckRequiredTask {

		Dialog _mDialog;

		public MyCaptchaCheckRequiredTask(Dialog dialog) {
			super(Constants.REDDIT_BASE_URL + "/message/compose/", mClient);
			_mDialog = dialog;
		}

		@Override
		protected void saveState() {
			ProfileActivity.this.mCaptchaIden = _mCaptchaIden;
			ProfileActivity.this.mCaptchaUrl = _mCaptchaUrl;
		}

		@Override
		public void onPreExecute() {
			// Hide send button so user can't send until we know whether he needs captcha
			final Button sendButton = (Button) _mDialog.findViewById(R.id.compose_send_button);
			sendButton.setVisibility(View.INVISIBLE);
			// Show "loading captcha" label
			final TextView loadingCaptcha = (TextView) _mDialog.findViewById(R.id.compose_captcha_loading);
			loadingCaptcha.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPostExecute(Boolean required) {
			final TextView captchaLabel = (TextView) _mDialog.findViewById(R.id.compose_captcha_textview);
			final ImageView captchaImage = (ImageView) _mDialog.findViewById(R.id.compose_captcha_image);
			final EditText captchaEdit = (EditText) _mDialog.findViewById(R.id.compose_captcha_input);
			final TextView loadingCaptcha = (TextView) _mDialog.findViewById(R.id.compose_captcha_loading);
			final Button sendButton = (Button) _mDialog.findViewById(R.id.compose_send_button);
			if (required == null) {
				Common.showErrorToast("Error retrieving captcha. Use the menu to try again.", Toast.LENGTH_LONG, ProfileActivity.this);
				return;
			}
			if (required) {
				captchaLabel.setVisibility(View.VISIBLE);
				captchaImage.setVisibility(View.VISIBLE);
				captchaEdit.setVisibility(View.VISIBLE);
				// Launch a task to download captcha and display it
				new MyCaptchaDownloadTask(_mDialog).execute();
			} else {
				captchaLabel.setVisibility(View.GONE);
				captchaImage.setVisibility(View.GONE);
				captchaEdit.setVisibility(View.GONE);
			}
			loadingCaptcha.setVisibility(View.GONE);
			sendButton.setVisibility(View.VISIBLE);
		}
	}

	private class MyCaptchaDownloadTask extends CaptchaDownloadTask {

		Dialog _mDialog;

		public MyCaptchaDownloadTask(Dialog dialog) {
			super(mCaptchaUrl, mClient);
			_mDialog = dialog;
		}

		@Override
		public void onPostExecute(Drawable captcha) {
			if (captcha == null) {
				Common.showErrorToast("Error retrieving captcha. Use the menu to try again.", Toast.LENGTH_LONG, ProfileActivity.this);
				return;
			}
			final ImageView composeCaptchaView = (ImageView) _mDialog.findViewById(R.id.compose_captcha_image);
			composeCaptchaView.setVisibility(View.VISIBLE);
			composeCaptchaView.setImageDrawable(captcha);
		}
	}



	/**
	 * Populates the menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.profile, menu);
		hideShowMenuItems(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.compose_message_menu_id:
				if (mSettings.isLoggedIn()) {
					showDialog(Constants.DIALOG_COMPOSE);
				} else {
					showDialog(Constants.DIALOG_LOGIN);
				}
				break;
			case R.id.saved_menu_id:
				if (mSettings.isLoggedIn()) {
					Intent intent = new Intent(getApplicationContext(), ThreadsListActivity.class);
					intent.setData(Util.createSavedUri(mSettings.getUsername()));
					startActivity(intent);
					Util.overridePendingTransition(null, this,
							android.R.anim.slide_in_left, android.R.anim.slide_out_right);
				}
				break;

			case R.id.refresh_menu_id:
				new DownloadProfileTask(mUsername).execute(Constants.DEFAULT_COMMENT_DOWNLOAD_LIMIT);
				break;
			case android.R.id.home:
				Common.goHome(this);
				break;
		}

		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		ProgressDialog pdialog;
		AlertDialog.Builder builder;
		LayoutInflater inflater;
		View layout; // used for inflated views for AlertDialog.Builder.setView()

		switch (id) {
			case Constants.DIALOG_LOGIN:
				dialog = new LoginDialog(this, mSettings, false) {
					@Override
					public void onLoginChosen(String user, String password) {
						removeDialog(Constants.DIALOG_LOGIN);
						new MyLoginTask(user, password).execute();
					}
				};
				break;

			case Constants.DIALOG_COMPOSE:
				inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				builder = new AlertDialog.Builder(new ContextThemeWrapper(this, mSettings.getDialogTheme()));
				layout = inflater.inflate(R.layout.compose_dialog, null);

				Common.setTextColorFromTheme(
						mSettings.getTheme(),
						getResources(),
						(TextView) layout.findViewById(R.id.compose_destination_textview),
						(TextView) layout.findViewById(R.id.compose_subject_textview),
						(TextView) layout.findViewById(R.id.compose_message_textview),
						(TextView) layout.findViewById(R.id.compose_captcha_textview),
						(TextView) layout.findViewById(R.id.compose_captcha_loading)
				);

				final EditText composeDestination = (EditText) layout.findViewById(R.id.compose_destination_input);
				final EditText composeSubject = (EditText) layout.findViewById(R.id.compose_subject_input);
				final EditText composeText = (EditText) layout.findViewById(R.id.compose_text_input);
				final Button composeSendButton = (Button) layout.findViewById(R.id.compose_send_button);
				final Button composeCancelButton = (Button) layout.findViewById(R.id.compose_cancel_button);
				final EditText composeCaptcha = (EditText) layout.findViewById(R.id.compose_captcha_input);
				composeDestination.setText(mUsername);

				dialog = builder.setView(layout).create();
				final Dialog composeDialog = dialog;
				composeSendButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						ThingInfo hi = new ThingInfo();

						if (!FormValidation.validateComposeMessageInputFields(ProfileActivity.this, composeDestination, composeSubject, composeText, composeCaptcha))
							return;

						hi.setDest(composeDestination.getText().toString().trim());
						hi.setSubject(composeSubject.getText().toString().trim());
						new MyMessageComposeTask(composeDialog, hi, composeCaptcha.getText().toString().trim())
								.execute(composeText.getText().toString().trim());
						removeDialog(Constants.DIALOG_COMPOSE);
					}
				});
				composeCancelButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						removeDialog(Constants.DIALOG_COMPOSE);
					}
				});
				break;

			case Constants.DIALOG_THREAD_CLICK:
				dialog = new ThreadClickDialog(this, mSettings);
				break;

			// "Please wait"
			case Constants.DIALOG_LOGGING_IN:
				pdialog = new ProgressDialog(new ContextThemeWrapper(this, mSettings.getDialogTheme()));
				pdialog.setMessage("Logging in...");
				pdialog.setIndeterminate(true);
				pdialog.setCancelable(true);
				dialog = pdialog;
				break;
			case Constants.DIALOG_REPLYING:
				pdialog = new ProgressDialog(new ContextThemeWrapper(this, mSettings.getDialogTheme()));
				pdialog.setMessage("Sending reply...");
				pdialog.setIndeterminate(true);
				pdialog.setCancelable(true);
				dialog = pdialog;
				break;
			case Constants.DIALOG_COMPOSING:
				pdialog = new ProgressDialog(new ContextThemeWrapper(this, mSettings.getDialogTheme()));
				pdialog.setMessage("Composing message...");
				pdialog.setIndeterminate(true);
				pdialog.setCancelable(true);
				dialog = pdialog;
				break;

			default:
				throw new IllegalArgumentException("Unexpected dialog id "+id);
		}
		return dialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);

		switch (id) {
//    	case Constants.DIALOG_LOGIN:
//    		if (mSettings.username != null) {
//	    		final TextView loginUsernameInput = (TextView) dialog.findViewById(R.id.login_username_input);
//	    		loginUsernameInput.setText(mSettings.username);
//    		}
//    		final TextView loginPasswordInput = (TextView) dialog.findViewById(R.id.login_password_input);
//    		loginPasswordInput.setText("");
//    		break;

			case Constants.DIALOG_COMPOSE:
				final EditText composeDestination = (EditText) dialog.findViewById(R.id.compose_destination_input);
				composeDestination.setText(mUsername);
				new MyCaptchaCheckRequiredTask(dialog).execute();
				break;

			case Constants.DIALOG_THREAD_CLICK:
				ThreadsListActivity.fillThreadClickDialog(dialog, mVoteTargetThingInfo, mSettings, mThreadClickDialogOnClickListenerFactory);
				break;

			default:
				// No preparation based on app state is required.
				break;
		}
	}

	private void setLinkClicked(ThingInfo threadThingInfo) {
		threadThingInfo.setClicked(true);
		mThingsAdapter.notifyDataSetChanged();
	}

	private final OnClickListener downloadAfterOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			new DownloadProfileTask(mUsername, mAfter, null, mCount).execute();
		}
	};
	private final OnClickListener downloadBeforeOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			new DownloadProfileTask(mUsername, null, mBefore, mCount).execute();
		}
	};

	private final ThumbnailOnClickListenerFactory mThumbnailOnClickListenerFactory
			= new ThumbnailOnClickListenerFactory() {
		@Override
		public OnClickListener getThumbnailOnClickListener(final ThingInfo threadThingInfo, final Activity activity) {
			return new OnClickListener() {
				public void onClick(View v) {
//					mJumpToThreadId = jumpToId;
					setLinkClicked(threadThingInfo);
					Common.launchBrowser(
							mSettings,
							activity,
							threadThingInfo.getUrl(),
							Util.createThreadUri(threadThingInfo).toString(),
							false,
							false,
							mSettings.isUseExternalBrowser(),
							mSettings.isSaveHistory()
					);
				}
			};
		}
	};

	private final ThreadClickDialogOnClickListenerFactory mThreadClickDialogOnClickListenerFactory
			= new ThreadClickDialogOnClickListenerFactory() {
		@Override
		public OnClickListener getLoginOnClickListener() {
			return new OnClickListener() {
				public void onClick(View v) {
					removeDialog(Constants.DIALOG_THREAD_CLICK);
					showDialog(Constants.DIALOG_LOGIN);
				}
			};
		}
		@Override
		public OnClickListener getLinkOnClickListener(ThingInfo thingInfo, boolean useExternalBrowser) {
			final ThingInfo info = thingInfo;
			final boolean fUseExternalBrowser = useExternalBrowser;
			return new OnClickListener() {
				public void onClick(View v) {
					removeDialog(Constants.DIALOG_THREAD_CLICK);
					// Launch Intent to goto the URL
					Common.launchBrowser(mSettings,ProfileActivity.this, info.getUrl(),
							Util.createThreadUri(info).toString(),
							false, false, fUseExternalBrowser, mSettings.isSaveHistory());
				}
			};
		}
		@Override
		public OnClickListener getCommentsOnClickListener(ThingInfo thingInfo) {
			final ThingInfo info = thingInfo;
			return new OnClickListener() {
				public void onClick(View v) {
					removeDialog(Constants.DIALOG_THREAD_CLICK);
					// Launch an Intent for CommentsListActivity
					CacheInfo.invalidateCachedThread(ProfileActivity.this);
					Intent i = new Intent(ProfileActivity.this, CommentsListActivity.class);
					i.setData(Util.createThreadUri(info));
					i.putExtra(Constants.EXTRA_SUBREDDIT, info.getSubreddit());
					i.putExtra(Constants.EXTRA_TITLE, info.getTitle());
					i.putExtra(Constants.EXTRA_NUM_COMMENTS, Integer.valueOf(info.getNum_comments()));
					startActivity(i);
				}
			};
		}
		@Override
		public CompoundButton.OnCheckedChangeListener getVoteUpOnCheckedChangeListener(ThingInfo thingInfo) {
			final ThingInfo info = thingInfo;
			return new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					removeDialog(Constants.DIALOG_THREAD_CLICK);
					if (isChecked) {
						new MyVoteTask(info, 1, info.getSubreddit()).execute();
					} else {
						new MyVoteTask(info, 0, info.getSubreddit()).execute();
					}
				}
			};
		}
		@Override
		public CompoundButton.OnCheckedChangeListener getVoteDownOnCheckedChangeListener(ThingInfo thingInfo) {
			final ThingInfo info = thingInfo;
			return new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					removeDialog(Constants.DIALOG_THREAD_CLICK);
					if (isChecked) {
						new MyVoteTask(info, -1, info.getSubreddit()).execute();
					} else {
						new MyVoteTask(info, 0, info.getSubreddit()).execute();
					}
				}
			};
		}
	};


	@Override
	protected void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putString(Constants.USERNAME_KEY, mUsername);
		state.putString(Constants.CommentsSort.SORT_BY_KEY, mSortByUrl);
		state.putString(Constants.JUMP_TO_THREAD_ID_KEY, mJumpToThreadId);
		state.putString(Constants.AFTER_KEY, mAfter);
		state.putString(Constants.BEFORE_KEY, mBefore);
		state.putInt(Constants.THREAD_COUNT_KEY, mCount);
		state.putString(Constants.LAST_AFTER_KEY, mLastAfter);
		state.putString(Constants.LAST_BEFORE_KEY, mLastBefore);
		state.putInt(Constants.THREAD_LAST_COUNT_KEY, mLastCount);
		state.putIntArray(Constants.KARMA_KEY, mKarma);
		state.putParcelable(Constants.VOTE_TARGET_THING_INFO_KEY, mVoteTargetThingInfo);
	}

	/**
	 * Called to "thaw" re-animate the app from a previous onSaveInstanceState().
	 *
	 * @see android.app.Activity#onRestoreInstanceState
	 */
	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		final int[] myDialogs = {
				Constants.DIALOG_COMMENT_CLICK,
				Constants.DIALOG_LOGGING_IN,
				Constants.DIALOG_LOGIN,
				Constants.DIALOG_MESSAGE_CLICK,
				Constants.DIALOG_REPLY,
				Constants.DIALOG_REPLYING,
		};
		for (int dialog : myDialogs) {
			try {
				removeDialog(dialog);
			} catch (IllegalArgumentException e) {
				// Ignore.
			}
		}
	}
}
