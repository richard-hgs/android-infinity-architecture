package com.infinity.architecture.utils.view;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

public class ViewUtils {
    /**
     * @return "[package]:id/[xml-id]"
     * where [package] is your package and [xml-id] is id of view
     * or "no-id" if there is no id
     */
    public static String getViewIdName(View view) {
        String name = "N/D";
        try {
            if (view.getId() == View.NO_ID) {
                name = "no-id";
            } else {
                name = (view.getResources() != null ? view.getResources().getResourceName(view.getId()) : "N/D");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    @Nullable
    public static <T extends View> T searchViewParentsForViewWithId(View view, @IdRes int viewIdToSearch, int maxDepth) {
        int currentDepth = 0;
        while(view.getParent() != null) {
            View viewFound = view.findViewById(viewIdToSearch);
            if (viewFound != null) {
                //noinspection unchecked
                return (T) viewFound;
            }
            if (maxDepth > 0 && currentDepth >= maxDepth) {
                break;
            }
            view = (View) view.getParent();
            currentDepth++;
        }
        return null;
    }

    public static View debugViewIds(View view, String logtag) {
        Log.v(logtag, "traversing: " + view.getClass().getSimpleName() + ", id: " + view.getId());
        if (view.getParent() != null && (view.getParent() instanceof ViewGroup)) {
            return debugViewIds((View)view.getParent(), logtag);
        }
        else {
            debugChildViewIds(view, logtag, 0);
            return view;
        }
    }

    private static void debugChildViewIds(View view, String logtag, int spaces) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                Log.v(logtag, padString("view: " + child.getClass().getSimpleName() + "(" + child.getId() + ")", spaces));
                debugChildViewIds(child, logtag, spaces + 1);
            }
        }
    }

    private static String padString(String str, int noOfSpaces) {
        if (noOfSpaces <= 0) {
            return str;
        }
        StringBuilder builder = new StringBuilder(str.length() + noOfSpaces);
        for (int i = 0; i < noOfSpaces; i++) {
            builder.append(' ');
        }
        return builder.append(str).toString();
    }
}
