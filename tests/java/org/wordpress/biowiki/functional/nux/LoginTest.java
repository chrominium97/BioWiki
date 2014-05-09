package org.wordpress.biowiki.functional.nux;

import org.wordpress.biowiki.ActivityRobotiumTestCase;
import org.wordpress.biowiki.R;
import org.wordpress.biowiki.mocks.RestClientFactoryTest;
import org.wordpress.biowiki.mocks.XMLRPCFactoryTest;
import kr.kdev.dg1s.biowiki.ui.accounts.WelcomeActivity;

public class LoginTest extends ActivityRobotiumTestCase<WelcomeActivity> {
    public LoginTest() {
        super(WelcomeActivity.class);
    }

    public void testGoodCredentials() throws Exception {
        RestClientFactoryTest.setPrefixAllInstances("default");
        XMLRPCFactoryTest.setPrefixAllInstances("default");
        mSolo.enterText(0, "test");
        mSolo.enterText(1, "test");
        mSolo.clickOnText(mSolo.getString(R.string.sign_in));
    }

    public void testBadCredentials() throws Exception {
        RestClientFactoryTest.setPrefixAllInstances("login-failure");
        XMLRPCFactoryTest.setPrefixAllInstances("login-failure");
        mSolo.enterText(0, "test");
        mSolo.enterText(1, "test");
        mSolo.clickOnText(mSolo.getString(R.string.sign_in));
        boolean errorMessageFound = mSolo.searchText(mSolo.getString(R.string.username_or_password_incorrect));
        assertTrue("Error message not found", errorMessageFound);
    }

    public void testCreateAccountInvalidEmail() throws Exception {
        mSolo.clickOnText(mSolo.getString(R.string.nux_welcome_create_account));
        mSolo.waitForText(mSolo.getString(R.string.create_account_wpcom));
        mSolo.clearEditText(0);
        mSolo.enterText(0, "test");
        mSolo.enterText(1, "test");
        mSolo.enterText(2, "test");
        mSolo.clickOnText(mSolo.getString(R.string.nux_welcome_create_account));
        boolean errorMessageFound = mSolo.searchText(mSolo.getString(R.string.invalid_email_message));
        assertTrue("Error message not found", errorMessageFound);
    }

    public void testCreateAccountUsernameExists() throws Exception {
        RestClientFactoryTest.setPrefixAllInstances("username-exists");
        mSolo.clickOnText(mSolo.getString(R.string.nux_welcome_create_account));
        mSolo.waitForText(mSolo.getString(R.string.create_account_wpcom));
        mSolo.clearEditText(0);
        mSolo.enterText(0, "test@test.com");
        mSolo.enterText(1, "test");
        mSolo.enterText(2, "test");
        mSolo.clickOnText(mSolo.getString(R.string.nux_welcome_create_account));
        boolean errorMessageFound = mSolo.searchText(mSolo.getString(R.string.username_exists));
        assertTrue("Error message not found", errorMessageFound);
    }

    public void testCreateAccountPasswordTooShort() throws Exception {
        mSolo.clickOnText(mSolo.getString(R.string.nux_welcome_create_account));
        mSolo.waitForText(mSolo.getString(R.string.create_account_wpcom));
        mSolo.clearEditText(0);
        mSolo.enterText(0, "test@test.com");
        mSolo.enterText(1, "test");
        mSolo.enterText(2, "tes");
        mSolo.clickOnText(mSolo.getString(R.string.nux_welcome_create_account));
        boolean errorMessageFound = mSolo.searchText(mSolo.getString(R.string.invalid_password_message));
        assertTrue("Error message not found", errorMessageFound);
    }

    public void testCreateAccountPasswordTooWeak() throws Exception {
        RestClientFactoryTest.setPrefixAllInstances("password-invalid");
        mSolo.clickOnText(mSolo.getString(R.string.nux_welcome_create_account));
        mSolo.waitForText(mSolo.getString(R.string.create_account_wpcom));
        mSolo.clearEditText(0);
        mSolo.enterText(0, "test@test.com");
        mSolo.enterText(1, "test");
        mSolo.enterText(2, "test");
        mSolo.clickOnText(mSolo.getString(R.string.nux_welcome_create_account));
        boolean errorMessageFound = mSolo.searchText(mSolo.getString(R.string.password_invalid));
        assertTrue("Error message not found", errorMessageFound);
    }

    public void testCreateAccountOk() throws Exception {
        RestClientFactoryTest.setPrefixAllInstances("default");
        XMLRPCFactoryTest.setPrefixAllInstances("default");
        mSolo.clickOnText(mSolo.getString(R.string.nux_welcome_create_account));
        mSolo.waitForText(mSolo.getString(R.string.create_account_wpcom));
        mSolo.clearEditText(0);
        mSolo.enterText(0, "test@test.com");
        mSolo.enterText(1, "test");
        mSolo.enterText(2, "test");
        mSolo.clickOnText(mSolo.getString(R.string.nux_welcome_create_account));
    }

    public void testLoginMalformedGetUsersBlog() throws Exception {
        RestClientFactoryTest.setPrefixAllInstances("default");
        XMLRPCFactoryTest.setPrefixAllInstances("malformed-getusersblog");
        mSolo.enterText(0, "test");
        mSolo.enterText(1, "test");
        mSolo.clickOnText(mSolo.getString(R.string.sign_in));
        mSolo.sleep(100000);
    }
}
