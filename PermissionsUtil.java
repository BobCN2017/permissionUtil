package com.coomaan.setupnet.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PP on 2017/9/23.
 */

public class PermissionsUtil {
    private static final int REQUEST_CODE = 100;

    private static Activity mContext;

    public static void applyAllNoGrantedPermissions(Activity context) {
        mContext = context;
        String[] allPermissions = getAllPermissions(context);
        if (allPermissions == null) return;

        String[] permissions = getNeedToApplyPermissions(allPermissions);
        if (permissions == null) return;

        ActivityCompat.requestPermissions(context, permissions, REQUEST_CODE);
    }

    public static void toastRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length < 1 || requestCode != REQUEST_CODE) return;
        String granted = "";
        String rejected = "";
        for (int i = 0; i < grantResults.length; i++) {

            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted += " " + permissions[i];
            } else
                rejected += " " + permissions[i];
        }
        String result = (TextUtils.isEmpty(granted) ? granted : "Granted:" + granted)
                + (TextUtils.isEmpty(rejected) ? granted : " Rejected:" + rejected);
        Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
    }

    private static String[] getAllPermissions(Activity context) {
        String[] allPermissions = null;
        try {
            allPermissions = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return allPermissions;
    }

    @Nullable
    private static String[] getNeedToApplyPermissions(String[] allPermissions) {
        List<String> needToApplyPermissions = new ArrayList<>();
        for (String permission : allPermissions) {
            checkPermissionAndAddToList(permission, needToApplyPermissions);
        }
        if (needToApplyPermissions.size() < 1) return null;

        String[] permissions = new String[needToApplyPermissions.size()];
        for (int i = 0; i < needToApplyPermissions.size(); i++) {
            permissions[i] = needToApplyPermissions.get(i);
        }
        return permissions;
    }

    private static void checkPermissionAndAddToList(String permission, List<String> permissions) {
        if (ContextCompat.checkSelfPermission(mContext,
                permission) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(permission);
        }
    }
}
