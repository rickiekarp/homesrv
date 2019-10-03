package net.rickiekarp.reddit.common.util;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import net.rickiekarp.reddit.R;
import net.rickiekarp.reddit.common.Common;
import net.rickiekarp.reddit.common.Constants;
import net.rickiekarp.reddit.things.ThingInfo;

public class Util {

	private static final String TAG = "Util";

	// Spans for marking an author as the OP (submitter)
	private static ForegroundColorSpan LIGHT_OP_FGCS = null;
	private static ForegroundColorSpan DARK_OP_FGCS = null;

	// Spans for marking an author as a moderator
	// Determined that dark_green looks fine in both light and dark themes.
	private static ForegroundColorSpan MODERATOR_FGCS = null;

	// Spans for marking an author as an admin
	// Determined that red looks fine in both light and dark themes.
	private static ForegroundColorSpan ADMIN_FGCS = null;

	public static ForegroundColorSpan getAdminSpan(Context context) {
		if (ADMIN_FGCS == null) {
			ADMIN_FGCS = new ForegroundColorSpan(context.getResources().getColor(R.color.red));
		}
		return ADMIN_FGCS;
	}

	public static ForegroundColorSpan getModeratorSpan(Context context) {
		if (MODERATOR_FGCS == null) {
			MODERATOR_FGCS = new ForegroundColorSpan(context.getResources().getColor(R.color.dark_green));
		}
		return MODERATOR_FGCS;
	}

	public static ForegroundColorSpan getOPSpan(Context context, int theme) {
		if (Util.isLightTheme(theme)) {
			if (LIGHT_OP_FGCS == null) {
				LIGHT_OP_FGCS = new ForegroundColorSpan(context.getResources().getColor(R.color.blue));
			}
			return LIGHT_OP_FGCS;
		} else {
			if (DARK_OP_FGCS == null) {
				DARK_OP_FGCS = new ForegroundColorSpan(context.getResources().getColor(R.color.pale_blue));
			}
			return DARK_OP_FGCS;
		}
	}

	/**
	 * Convert HTML tags so they will be properly recognized by
	 * android.text.Html.fromHtml()
	 * @param html unescaped HTML
	 * @return converted HTML
	 */
	public static String convertHtmlTags(String html) {
		// Handle <code>
		html = html.replaceAll("<code>", "<tt>").replaceAll("</code>", "</tt>");

		// Handle <pre>
		int preIndex = html.indexOf("<pre>");
		int preEndIndex = -6;  // -"</pre>".length()
		StringBuilder bodyConverted = new StringBuilder();
		while (preIndex != -1) {
			// get the text between previous </pre> and next <pre>.
			bodyConverted = bodyConverted.append(html.substring(preEndIndex + 6, preIndex));
			preEndIndex = html.indexOf("</pre>", preIndex);
			// Replace newlines with <br> inside the <pre></pre>
			// Retain <pre> tags since android.text.Html.fromHtml() will ignore them anyway.
			bodyConverted = bodyConverted.append(html.substring(preIndex, preEndIndex).replaceAll("\n", "<br>"))
					.append("</pre>");
			preIndex = html.indexOf("<pre>", preEndIndex);
		}
		html = bodyConverted.append(html.substring(preEndIndex + 6)).toString();

		// Handle <li>
		html = html.replaceAll("<li>(<p>)?", "&#8226; ")
				.replaceAll("(</p>)?</li>", "<br>");

		// Handle <strong> and <em>, which are normally <b> and <i> respectively, but reversed in Android.
		// ANDROID BUG: http://code.google.com/p/android/issues/detail?id=3473
		html = html.replaceAll("<strong>", "<b>").replaceAll("</strong>", "</b>")
				.replaceAll("<em>", "<i>").replaceAll("</em>", "</i>");

		return html;
	}

	/**
	 * To the second, not millisecond like reddit
	 * @param timeSeconds
	 * @return
	 */
	public static String getTimeAgo(long utcTimeSeconds, Resources resources) {
		long systime = System.currentTimeMillis() / 1000;
		long diff = systime - utcTimeSeconds;
		if (diff <= 0)
			return resources.getString(R.string.just_now);
		else if (diff < 60) {
			if (diff == 1)
				return resources.getString(R.string.one_second_ago);
			else
				return String.format(resources.getString(R.string.n_seconds_ago), diff);
		}
		else if (diff < 3600) {
			if ((diff / 60) == 1)
				return resources.getString(R.string.one_minute_ago);
			else
				return String.format(resources.getString(R.string.n_minutes_ago), (diff / 60));
		}
		else if (diff < 86400) { // 86400 seconds in a day
			if ((diff / 3600) == 1)
				return resources.getString(R.string.one_hour_ago);
			else
				return String.format(resources.getString(R.string.n_hours_ago), (diff / 3600));
		}
		else if (diff < 604800) { // 86400 * 7
			if ((diff / 86400) == 1)
				return resources.getString(R.string.one_day_ago);
			else
				return String.format(resources.getString(R.string.n_days_ago), (diff / 86400));
		}
		else if (diff < 2592000) { // 86400 * 30
			if ((diff / 604800) == 1)
				return resources.getString(R.string.one_week_ago);
			else
				return String.format(resources.getString(R.string.n_weeks_ago), (diff / 604800));
		}
		else if (diff < 31536000) { // 86400 * 365
			if ((diff / 2592000) == 1)
				return resources.getString(R.string.one_month_ago);
			else
				return String.format(resources.getString(R.string.n_months_ago), (diff / 2592000));
		}
		else {
			if ((diff / 31536000) == 1)
				return resources.getString(R.string.one_year_ago);
			else
				return String.format(resources.getString(R.string.n_years_ago), (diff / 31536000));
		}
	}

	public static String getTimeAgo(double utcTimeSeconds, Resources resources) {
		return getTimeAgo((long)utcTimeSeconds, resources);
	}

	public static String showNumComments(int comments) {
		if (comments == 1) {
			return "1 comment";
		} else {
			return comments + " comments";
		}
	}

	public static String showNumPoints(int score) {
		if (score == 1) {
			return "1 point";
		} else {
			return score + " points";
		}
	}

	public static String absolutePathToURL(String path) {
		if (path.startsWith("/"))
			return Constants.REDDIT_BASE_URL + path;
		else if (path.startsWith("r/"))
			return Constants.REDDIT_BASE_URL + "/" + path;
		return path;
	}

	public static String nameToId(String name) {
		// indexOf('_') == -1 if not found; -1 + 1 == 0
		return name.substring(name.indexOf('_') + 1);
	}

	public static boolean isHttpStatusOK(HttpResponse response) {
		if (response == null || response.getStatusLine() == null) {
			return false;
		}
		return response.getStatusLine().getStatusCode() == 200;
	}

	public static String getResponseErrorMessage(String line) throws Exception {
		String error = null;

		if (StringUtils.isEmpty(line)) {
			error = "Connection error when subscribing. Try again.";
			throw new HttpException("No content returned from subscribe POST");
		}
		if (line.contains("WRONG_PASSWORD")) {
			error = "Wrong password.";
			throw new Exception("Wrong password.");
		}
		if (line.contains("USER_REQUIRED")) {
			// The modhash probably expired
			throw new Exception("User required. Huh?");
		}

		Common.logDLong(TAG, line);
		return error;
	}

	public static void copyPlainTextToClipboard(Context context, String textToCopy) {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(textToCopy);
		} else {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			android.content.ClipData clip = android.content.ClipData.newPlainText(textToCopy, textToCopy);
			clipboard.setPrimaryClip(clip);
		}
	}

	// ===============
	//      Theme
	// ===============

	public static boolean isLightTheme(int theme) {
		return theme == R.style.Reddit_Light_Medium || theme == R.style.Reddit_Light_Large || theme == R.style.Reddit_Light_Larger || theme == R.style.Reddit_Light_Huge;
	}

	public static boolean isDarkTheme(int theme) {
		return theme == R.style.Reddit_Dark_Medium || theme == R.style.Reddit_Dark_Large || theme == R.style.Reddit_Dark_Larger || theme == R.style.Reddit_Dark_Huge;
	}

	public static int getInvertedTheme(int theme) {
		switch (theme) {
			case R.style.Reddit_Light_Medium:
				return R.style.Reddit_Dark_Medium;
			case R.style.Reddit_Light_Large:
				return R.style.Reddit_Dark_Large;
			case R.style.Reddit_Light_Larger:
				return R.style.Reddit_Dark_Larger;
			case R.style.Reddit_Light_Huge:
				return R.style.Reddit_Dark_Huge;
			case R.style.Reddit_Dark_Medium:
				return R.style.Reddit_Light_Medium;
			case R.style.Reddit_Dark_Large:
				return R.style.Reddit_Light_Large;
			case R.style.Reddit_Dark_Larger:
				return R.style.Reddit_Light_Larger;
			case R.style.Reddit_Dark_Huge:
				return R.style.Reddit_Light_Huge;
			default:
				return R.style.Reddit_Light_Medium;
		}
	}

	public static int getThemeResourceFromPrefs(String themePref, String textSizePref) {
		if (Constants.PREF_THEME_LIGHT.equals(themePref)) {
			if (Constants.PREF_TEXT_SIZE_MEDIUM.equals(textSizePref))
				return R.style.Reddit_Light_Medium;
			else if (Constants.PREF_TEXT_SIZE_LARGE.equals(textSizePref))
				return R.style.Reddit_Light_Large;
			else if (Constants.PREF_TEXT_SIZE_LARGER.equals(textSizePref))
				return R.style.Reddit_Light_Larger;
			else if (Constants.PREF_TEXT_SIZE_HUGE.equals(textSizePref))
				return R.style.Reddit_Light_Huge;
		} else { /* if (Constants.PREF_THEME_DARK.equals(themePref)) */
			if (Constants.PREF_TEXT_SIZE_MEDIUM.equals(textSizePref))
				return R.style.Reddit_Dark_Medium;
			else if (Constants.PREF_TEXT_SIZE_LARGE.equals(textSizePref))
				return R.style.Reddit_Dark_Large;
			else if (Constants.PREF_TEXT_SIZE_LARGER.equals(textSizePref))
				return R.style.Reddit_Dark_Larger;
			else if (Constants.PREF_TEXT_SIZE_HUGE.equals(textSizePref))
				return R.style.Reddit_Dark_Huge;
		}
		return R.style.Reddit_Light_Medium;
	}

	/**
	 * Return the theme and textSize String prefs
	 */
	public static String[] getPrefsFromThemeResource(int theme) {
		switch (theme) {
			case R.style.Reddit_Light_Medium:
				return new String[] { Constants.PREF_THEME_LIGHT, Constants.PREF_TEXT_SIZE_MEDIUM };
			case R.style.Reddit_Light_Large:
				return new String[] { Constants.PREF_THEME_LIGHT, Constants.PREF_TEXT_SIZE_LARGE };
			case R.style.Reddit_Light_Larger:
				return new String[] { Constants.PREF_THEME_LIGHT, Constants.PREF_TEXT_SIZE_LARGER };
			case R.style.Reddit_Light_Huge:
				return new String[] { Constants.PREF_THEME_LIGHT, Constants.PREF_TEXT_SIZE_HUGE };
			case R.style.Reddit_Dark_Medium:
				return new String[] { Constants.PREF_THEME_DARK, Constants.PREF_TEXT_SIZE_MEDIUM };
			case R.style.Reddit_Dark_Large:
				return new String[] { Constants.PREF_THEME_DARK, Constants.PREF_TEXT_SIZE_LARGE };
			case R.style.Reddit_Dark_Larger:
				return new String[] { Constants.PREF_THEME_DARK, Constants.PREF_TEXT_SIZE_LARGER };
			case R.style.Reddit_Dark_Huge:
				return new String[] { Constants.PREF_THEME_DARK, Constants.PREF_TEXT_SIZE_HUGE };
			default:
				return new String[] { Constants.PREF_THEME_LIGHT, Constants.PREF_TEXT_SIZE_MEDIUM };
		}
	}

	public static int getTextAppearanceResource(int themeResource, int androidTextAppearanceStyle) {
		switch (themeResource) {
			case R.style.Reddit_Light_Medium:
			case R.style.Reddit_Dark_Medium:
				switch (androidTextAppearanceStyle) {
					case android.R.style.TextAppearance_Small:
						return R.style.TextAppearance_Medium_Small;
					case android.R.style.TextAppearance_Medium:
						return R.style.TextAppearance_Medium_Medium;
					case android.R.style.TextAppearance_Large:
						return R.style.TextAppearance_Medium_Large;
					default:
						return R.style.TextAppearance_Medium_Medium;
				}
			case R.style.Reddit_Light_Large:
			case R.style.Reddit_Dark_Large:
				switch (androidTextAppearanceStyle) {
					case android.R.style.TextAppearance_Small:
						return R.style.TextAppearance_Large_Small;
					case android.R.style.TextAppearance_Medium:
						return R.style.TextAppearance_Large_Medium;
					case android.R.style.TextAppearance_Large:
						return R.style.TextAppearance_Large_Large;
					default:
						return R.style.TextAppearance_Large_Medium;
				}
			case R.style.Reddit_Light_Larger:
			case R.style.Reddit_Dark_Larger:
				switch (androidTextAppearanceStyle) {
					case android.R.style.TextAppearance_Small:
						return R.style.TextAppearance_Larger_Small;
					case android.R.style.TextAppearance_Medium:
						return R.style.TextAppearance_Larger_Medium;
					case android.R.style.TextAppearance_Large:
						return R.style.TextAppearance_Larger_Large;
					default:
						return R.style.TextAppearance_Larger_Medium;
				}
			case R.style.Reddit_Light_Huge:
			case R.style.Reddit_Dark_Huge:
				switch (androidTextAppearanceStyle) {
					case android.R.style.TextAppearance_Small:
						return R.style.TextAppearance_Huge_Small;
					case android.R.style.TextAppearance_Medium:
						return R.style.TextAppearance_Huge_Medium;
					case android.R.style.TextAppearance_Large:
						return R.style.TextAppearance_Huge_Large;
					default:
						return R.style.TextAppearance_Huge_Medium;
				}
			default:
				return R.style.TextAppearance_Medium_Medium;
		}
	}


	// =======================
	//    Mail Notification
	// =======================

	public static long getMillisFromMailNotificationPref(String pref) {
		if (Constants.PREF_MAIL_NOTIFICATION_SERVICE_OFF.equals(pref)) {
			return 0;
		} else if (Constants.PREF_MAIL_NOTIFICATION_SERVICE_5MIN.equals(pref)) {
			return 5 * 60 * 1000;
		} else if (Constants.PREF_MAIL_NOTIFICATION_SERVICE_30MIN.equals(pref)) {
			return 30 * 60 * 1000;
		} else if (Constants.PREF_MAIL_NOTIFICATION_SERVICE_1HOUR.equals(pref)) {
			return 1 * 3600 * 1000;
		} else if (Constants.PREF_MAIL_NOTIFICATION_SERVICE_6HOURS.equals(pref)) {
			return 6 * 3600 * 1000;
		} else { /* if (Constants.PREF_MAIL_NOTIFICATION_SERVICE_1DAY.equals(pref)) */
			return 24 * 3600 * 1000;
		}
	}


	// ===============
	//   Transitions
	// ===============

	public static void overridePendingTransition(Method activity_overridePendingTransition, Activity act, int enterAnim, int exitAnim) {
		// only available in Android 2.0 (SDK Level 5) and later
		if (activity_overridePendingTransition != null) {
			try {
				activity_overridePendingTransition.invoke(act, enterAnim, exitAnim);
			} catch (Exception ex) {
				if (Constants.LOGGING) Log.e(TAG, "overridePendingTransition", ex);
			}
		}
	}

	// ===============
	//       Uri
	// ===============
	static Uri createCommentUriNoContext(String linkId, String commentId)
	{
		return Uri.parse(new StringBuilder(Constants.REDDIT_BASE_URL + "/comments/")
				.append(linkId)
				.append("/z/")
				.append(commentId)
				.toString());
	}

	public static Uri createCommentUriNoContext(ThingInfo commentThingInfo)
	{
		if (commentThingInfo.getLink_id() != null)
		{
			return createCommentUriNoContext(nameToId(commentThingInfo.getLink_id()), commentThingInfo.getId());
		}
		return null;
	}


	static Uri createCommentUri(String linkId, String commentId, int commentContext) {
		return Uri.parse(new StringBuilder(Constants.REDDIT_BASE_URL + "/comments/")
				.append(linkId)
				.append("/z/")
				.append(commentId)
				.append("?context=")
				.append(commentContext)
				.toString());
	}

	public static Uri createCommentUri(ThingInfo commentThingInfo, int commentContext) {
		if (commentThingInfo.getContext() != null)
			return Uri.parse(absolutePathToURL(commentThingInfo.getContext()));
		if (commentThingInfo.getLink_id() != null)
			return createCommentUri(nameToId(commentThingInfo.getLink_id()), commentThingInfo.getId(), commentContext);
		return null;
	}

	public static Uri createProfileUri(String username) {
		return Uri.parse(new StringBuilder(Constants.REDDIT_BASE_URL + "/user/")
				.append(username)
				.toString());
	}

	public static Uri createSavedUri(String username) {
		return Uri.parse(new StringBuilder(Constants.REDDIT_BASE_URL + "/user/")
				.append(username)
				.append("/saved.json")
				.toString());
	}

	public static Uri createSubmitUri(String subreddit) {
		if (Constants.FRONTPAGE_STRING.equals(subreddit))
			return Uri.parse(Constants.REDDIT_BASE_URL + "/submit");

		return Uri.parse(new StringBuilder(Constants.REDDIT_BASE_URL + "/r/")
				.append(subreddit)
				.append("/submit")
				.toString());
	}

	static Uri createSubmitUri(ThingInfo thingInfo) {
		return createSubmitUri(thingInfo.getSubreddit());
	}

	public static Uri createSubredditUri(String subreddit) {
		if (Constants.FRONTPAGE_STRING.equals(subreddit))
			return Uri.parse(Constants.REDDIT_BASE_URL + "/");

		return Uri.parse(new StringBuilder(Constants.REDDIT_BASE_URL + "/r/")
				.append(subreddit)
				.toString());
	}

	static Uri createSubredditUri(ThingInfo thingInfo) {
		return createSubredditUri(thingInfo.getSubreddit());
	}

	public static Uri createThreadUri(String subreddit, String threadId) {
		return Uri.parse(new StringBuilder(Constants.REDDIT_BASE_URL + "/r/")
				.append(subreddit)
				.append("/comments/")
				.append(threadId)
				.toString());
	}

	public static Uri createThreadUri(ThingInfo threadThingInfo) {
		return createThreadUri(threadThingInfo.getSubreddit(), threadThingInfo.getId());
	}

	public static boolean isRedditUri(Uri uri) {
		if (uri == null) return false;
		String host = uri.getHost();
		return host != null && (host.equals("reddit.com") || host.endsWith(".reddit.com"));
	}

	public static boolean isRedditShortenedUri(Uri uri) {
		if (uri == null) return false;
		String host = uri.getHost();
		return host != null && host.equals("redd.it");
	}

	/**
	 * Creates mobile version of <code>uri</code> if applicable.
	 *
	 * @return original uri if no mobile version of uri is known
	 */
	public static Uri optimizeMobileUri(Uri uri) {
		if (isNonMobileWikipediaUri(uri)) {
			uri = createMobileWikpediaUri(uri);
		}
		return uri;
	}

	/**
	 * @return if uri points to a non-mobile wikpedia uri.
	 */
	static boolean isNonMobileWikipediaUri(Uri uri) {
		if (uri == null) return false;
		String host = uri.getHost();
		return host != null && host.endsWith(".wikipedia.org") && !host.contains(".m.wikipedia.org");
	}

	/**
	 * @return mobile version of a wikipedia uri
	 */
	static Uri createMobileWikpediaUri(Uri uri) {
		String uriString = uri.toString();
		return Uri.parse(uriString.replace(".wikipedia.org/", ".m.wikipedia.org/"));
	}

	public static boolean isYoutubeUri(Uri uri) {
		if (uri == null) return false;
		String host = uri.getHost();
		return host != null && (host.endsWith(".youtube.com") || host.equals("youtu.be"));
	}

	public static boolean isAndroidMarketUri(Uri uri) {
		if (uri == null) return false;
		String host = uri.getHost();
		return host != null && host.equals("market.android.com");
	}

	// ===============
	//   UI
	// ===============

	public static void setStateOfUpvoteDownvoteButtons(Dialog theDialog,
													   boolean isLoggedIn,
													   ThingInfo theThingInfo,
													   CompoundButton.OnCheckedChangeListener upvoteListener,
													   CompoundButton.OnCheckedChangeListener downvoteListener) {
		final CheckBox voteUpButton = (CheckBox) theDialog.findViewById(R.id.vote_up_button);
		final CheckBox voteDownButton = (CheckBox) theDialog.findViewById(R.id.vote_down_button);
		// Only show upvote/downvote if user is logged in
		if (isLoggedIn) {
			voteUpButton.setVisibility(View.VISIBLE);
			voteDownButton.setVisibility(View.VISIBLE);

			// Remove the OnCheckedChangeListeners because we are about to setChecked(),
			// and I think the Buttons are recycled, so old listeners will fire
			// for the previous vote target ThingInfo.
			voteUpButton.setOnCheckedChangeListener(null);
			voteDownButton.setOnCheckedChangeListener(null);

			// Set initial states of the vote buttons based on user's past actions
			if (theThingInfo.getLikes() == null) {
				// User is currently neutral
				voteUpButton.setChecked(false);
				voteDownButton.setChecked(false);
			} else if (theThingInfo.getLikes()) {
				// User currenty likes it
				voteUpButton.setChecked(true);
				voteDownButton.setChecked(false);
			} else {
				// User currently dislikes it
				voteUpButton.setChecked(false);
				voteDownButton.setChecked(true);
			}
			voteUpButton.setEnabled(!theThingInfo.isArchived());
			voteDownButton.setEnabled(!theThingInfo.isArchived());
			if (!theThingInfo.isArchived()) {
				voteUpButton.setOnCheckedChangeListener(upvoteListener);
				voteDownButton.setOnCheckedChangeListener(downvoteListener);
			}
		} else {
			voteUpButton.setVisibility(View.GONE);
			voteDownButton.setVisibility(View.GONE);
		}
	}

}
