package com.example.kimsujin.ssg_project;

import android.app.Application;

public class ActivityState extends Application {
    private boolean userInputActivityState;
    private boolean mainActivityState;
    private boolean pumDetailActivityState;

    public boolean isUserInputActivityState() {
        return userInputActivityState;
    }

    public void setUserInputActivityState(boolean userInputActivityState) {
        this.userInputActivityState = userInputActivityState;
    }

    public boolean isMainActivityState() {
        return mainActivityState;
    }

    public void setMainActivityState(boolean mainActivityState) {
        this.mainActivityState = mainActivityState;
    }

    public boolean isPumDetailActivityState() {
        return pumDetailActivityState;
    }

    public void setPumDetailActivityState(boolean pumDetailActivityState) {
        this.pumDetailActivityState = pumDetailActivityState;
    }
}
