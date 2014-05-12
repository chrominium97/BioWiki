package kr.kdev.dg1s.biowiki.models;

import android.text.TextUtils;

import org.json.JSONObject;

import kr.kdev.dg1s.biowiki.util.JSONUtil;
import kr.kdev.dg1s.biowiki.util.StringUtils;
import kr.kdev.dg1s.biowiki.util.UrlUtils;

/**
 * Created by nbradbury on 6/22/13.
 */
public class ReaderUser {

    public long userId;
    // isFollowed isn't read from json or stored in db - used by ReaderUserAdapter to mark followed users
    public transient boolean isFollowed;
    private String userName;
    private String displayName;
    private String url;
    private String profileUrl;
    private String avatarUrl;
    /*
     * not stored - used by ReaderUserAdapter for performance
     */
    private transient String urlDomain;

    public static ReaderUser fromJson(JSONObject json) {
        ReaderUser user = new ReaderUser();
        if (json == null)
            return user;

        user.userId = json.optLong("ID");
        user.userName = JSONUtil.getString(json, "username");
        user.url = JSONUtil.getString(json, "URL");
        user.profileUrl = JSONUtil.getString(json, "profile_URL");
        user.avatarUrl = JSONUtil.getString(json, "avatar_URL");

        // "me" api call (current user) has "display_name", others have "name"
        if (json.has("display_name")) {
            user.displayName = JSONUtil.getStringDecoded(json, "display_name");
        } else {
            user.displayName = JSONUtil.getStringDecoded(json, "name");
        }

        return user;
    }

    public String getUserName() {
        return StringUtils.notNullStr(userName);
    }

    public void setUserName(String userName) {
        this.userName = StringUtils.notNullStr(userName);
    }

    public String getDisplayName() {
        return StringUtils.notNullStr(displayName);
    }

    public void setDisplayName(String displayName) {
        this.displayName = StringUtils.notNullStr(displayName);
    }

    public String getUrl() {
        return StringUtils.notNullStr(url);
    }

    public void setUrl(String url) {
        this.url = StringUtils.notNullStr(url);
    }

    public String getProfileUrl() {
        return StringUtils.notNullStr(profileUrl);
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = StringUtils.notNullStr(profileUrl);
    }

    public String getAvatarUrl() {
        return StringUtils.notNullStr(avatarUrl);
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = StringUtils.notNullStr(avatarUrl);
    }

    public boolean hasUrl() {
        return !TextUtils.isEmpty(url);
    }

    public String getUrlDomain() {
        if (urlDomain == null) {
            if (hasUrl()) {
                urlDomain = UrlUtils.getDomainFromUrl(getUrl());
            } else {
                urlDomain = "";
            }
        }
        return urlDomain;
    }

    public boolean isSameUser(ReaderUser user) {
        if (user == null)
            return false;
        if (this.userId != user.userId)
            return false;
        if (!this.getAvatarUrl().equals(user.getAvatarUrl()))
            return false;
        if (!this.getDisplayName().equals(user.getDisplayName()))
            return false;
        if (!this.getUserName().equals(user.getUserName()))
            return false;
        if (!this.getUrl().equals(user.getUrl()))
            return false;
        if (!this.getProfileUrl().equals(user.getProfileUrl()))
            return false;
        return true;
    }

}
