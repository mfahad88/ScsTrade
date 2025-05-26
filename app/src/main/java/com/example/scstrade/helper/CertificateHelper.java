package com.example.scstrade.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CertificateHelper {

    public static void printSHA1Fingerprint(Context context) {
        try {
            PackageInfo info;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info = context.getPackageManager().getPackageInfo(
                        context.getPackageName(),
                        PackageManager.GET_SIGNING_CERTIFICATES
                );
                Signature[] signatures = info.signingInfo.getApkContentsSigners();
                for (Signature signature : signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA-1");
                    md.update(signature.toByteArray());
                    String sha1 = Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                    Log.d("SHA1-Fingerprint", sha1);
                }
            } else {
                info = context.getPackageManager().getPackageInfo(
                        context.getPackageName(),
                        PackageManager.GET_SIGNATURES
                );
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA-1");
                    md.update(signature.toByteArray());
                    String sha1 = Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                    Log.d("SHA1-Fingerprint", sha1);
                }
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            Log.e("SHA1-Fingerprint", "Exception", e);
        }
    }
}
