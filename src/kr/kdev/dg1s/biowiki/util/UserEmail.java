package kr.kdev.dg1s.biowiki.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Patterns;

import java.util.regex.Pattern;

public class UserEmail {
    public static String getPrimaryEmail(Context context) {
        try {
            AccountManager accountManager = AccountManager.get(context);
            if (accountManager == null)
                return "";
            Account[] accounts = accountManager.getAccounts();
            Pattern emailPattern = Patterns.EMAIL_ADDRESS;
            for (Account account : accounts) {
                // make sure account.name is an email address before adding to the list
                if (emailPattern.matcher(account.name).matches()) {
                    return account.name;
                }
            }
            return "";
        } catch (SecurityException e) {
            // exception will occur if app doesn't have GET_ACCOUNTS permission
            return "";
        }
    }
}
