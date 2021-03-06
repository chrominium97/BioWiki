package org.wordpress.biowiki.mocks;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;

import org.wordpress.biowiki.TestUtils;
import kr.kdev.dg1s.biowiki.util.AppLog;
import kr.kdev.dg1s.biowiki.util.AppLog.T;
import org.xmlrpc.android.XMLRPCCallback;
import org.xmlrpc.android.XMLRPCException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class XMLRPCClientCustomizableJSONMock extends XMLRPCClientCustomizableMockAbstract {
    public XMLRPCClientCustomizableJSONMock(URI uri, String httpUser, String httpPassword) {
    }

    public void addQuickPostHeader(String type) {
    }

    public void setAuthorizationHeader(String authToken) {
    }

    private Object readFile(String method, String prefix) {
        // method example: wp.getUsersBlogs
        // Filename: default-wp.getUsersBlogs.json
        String filename = prefix + "-" + method + ".json";
        try {
            Gson gson = new Gson();
            InputStream is = mContext.getAssets().open(filename);
            String jsonString = TestUtils.convertStreamToString(is);
            AppLog.i(T.TESTS, "loading: " + filename);
            try {
                // Try to load a JSONArray
                return TestUtils.injectDateInArray(gson.fromJson(jsonString, Object[].class));
            } catch (Exception e) {
                // If that fails, try to load a JSONObject
                return TestUtils.injectDateInHashMap(TestUtils.stringMapToHashMap((StringMap) gson.fromJson(jsonString, Object.class)));
            }
        } catch (IOException e) {
            AppLog.e(T.TESTS, "can't read file: " + filename);
        }
        return null;
    }

    public Object call(String method, Object[] params) throws XMLRPCException {
        AppLog.v(T.TESTS, "XMLRPCClientCustomizableJSONMock: call: " + method);
        if ("login-failure".equals(mPrefix)) {
            // Wrong login
            throw new XMLRPCException("code 403");
        }

        Object retValue = readFile(method, mPrefix);
        if (retValue == null) {
            // failback to default
            AppLog.w(T.TESTS, "failback to default");
            retValue = readFile(method, "default");
        }
        return retValue;
    }

    public Object call(String method) throws XMLRPCException {
        return null;
    }

    public Object call(String method, Object[] params, File tempFile) throws XMLRPCException {
        return null;
    }

    public long callAsync(XMLRPCCallback listener, String methodName, Object[] params) {
        return 0;
    }

    public long callAsync(XMLRPCCallback listener, String methodName, Object[] params, File tempFile) {
        return 0;
    }
}
