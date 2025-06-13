package com.timvero.example.admin.risk.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubUser {
    private String login;
    private String name;
    @JsonProperty("followers")
    private int followersCount;
    @JsonProperty("following")
    private int followingCount;
    @JsonProperty("public_repos")
    private int publicRepos;
    @JsonProperty("avatar_url")
    private String avatarUrl;

    // Constructors
    public GithubUser() {}

    // Getters and Setters
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getPublicRepos() {
        return publicRepos;
    }

    public void setPublicRepos(int publicRepos) {
        this.publicRepos = publicRepos;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
