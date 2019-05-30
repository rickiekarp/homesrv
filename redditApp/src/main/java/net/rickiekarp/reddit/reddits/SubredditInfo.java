package net.rickiekarp.reddit.reddits;

import android.content.Context;

import net.rickiekarp.reddit.R;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

public class SubredditInfo implements Serializable, Comparable<SubredditInfo>
{
    public static final long SEC_PER_MIN = 60L, SEC_PER_HOUR = SEC_PER_MIN * 60L,
                             SEC_PER_DAY = SEC_PER_HOUR * 24L, SEC_PER_MON = SEC_PER_DAY * 30L,
                             SEC_PER_YR = SEC_PER_DAY * 365L;
    public String name;
    public String description;
    public boolean nsfw;
    public int subscribers;
    public URL url;
    public Date created;

    public String getAgeString(Context context)
    {
        long timeDelta = (System.currentTimeMillis() - created.getTime()) / 1000;
        String output = "";
        String format = context.getString(R.string.second_age_format);
        long divisor = 1;
        if(timeDelta >= SEC_PER_YR)
        {
            format = context.getString(R.string.year_age_format);
            divisor = SEC_PER_YR;
        }
        else if(timeDelta > SEC_PER_MON)
        {
            format = context.getString(R.string.month_age_format);
            divisor = SEC_PER_MON;
        }
        else if(timeDelta > SEC_PER_DAY)
        {
            format = context.getString(R.string.day_age_format);
            divisor = SEC_PER_DAY;
        }
        else if(timeDelta > SEC_PER_HOUR)
        {
            format = context.getString(R.string.hour_age_format);
            divisor = SEC_PER_HOUR;
        }
        else if(timeDelta > SEC_PER_MIN)
        {
            format = context.getString(R.string.minute_age_format);
            divisor = SEC_PER_MIN;
        }

        output += String.format(format, timeDelta / divisor);
        if(timeDelta >= divisor * 2)
        {
            output += "s";
        }
        return output;
    }

    @Override
    public int compareTo(SubredditInfo other)
    {
        return name.compareToIgnoreCase(other.name);
    }

    @Override
    public int hashCode()
    {
        return name.toLowerCase().hashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        return hashCode() == other.hashCode();
    }
}
