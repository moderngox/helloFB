/**
 * 
 */
package com.example.hellofb.test;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.example.hellofb.MainActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.TestSession;

/**
 * @author moderngox
 * 
 */
public class HelloFBTest extends ActivityUnitTestCase<MainActivity> {

	private static String ACCESS_TOKEN = "CAAEyKSUKgk8BAMouzKIw3lo02NTsxZAzZAuRddtbiACvgK9otRcRBm9lfZAS5a5JrOkuZA4ZB4GCKZCys57RTtBkAmJ5ZA2BWvhem4f4eQn06VAHVnuuqZA3sekVZCjqwLf9A5mt1UCHfJYZCFpYmbPduZAoPmijcgTWPulfSq6M3CXcBETroTALm8LTDktkp8sLsJhYctZAsQYJzeZCOMwSxh8sqn6F2XVzbRekZD";
	private static String APP_ID = "336627273204303";
	private static String SECRET = "b6f2228568690ca67ade64e6ada835f7";
	private TestSession testSession;
	private Intent mLaunchIntent;
	private String resp;

	public HelloFBTest(Class<MainActivity> activityClass) {
		super(activityClass);
	}

	public HelloFBTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mLaunchIntent = new Intent(getInstrumentation().getTargetContext(),
				MainActivity.class);
		startActivity(mLaunchIntent, null, null);
		TestSession.setTestApplicationId(APP_ID);
		TestSession.setTestApplicationSecret(SECRET);
		testSession = TestSession.createSessionWithSharedUser(getActivity(),
				Arrays.asList("user_likes", "user_status"), "testUser");
		AccessToken accessToken = AccessToken.createFromExistingAccessToken(
				ACCESS_TOKEN, null, null, AccessTokenSource.TEST_USER,
				Arrays.asList("user_likes", "user_status"));

		testSession.open(accessToken, new StatusCallback() {

			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
			}
		});
		Session.setActiveSession(testSession);
	}

	@MediumTest
	public void testAuth() {
		assertNotNull(testSession);
	}

	@MediumTest
	public void testSearch() {
		Bundle param = new Bundle();
		param.putString("q", "Los Angeles");
		new Request(testSession, "search", param, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						resp = response.getRawResponse();
					}
				}).executeAsync();
		assertNotNull(resp);
		assertFalse(resp.isEmpty());
	}

	@MediumTest
	public void testAccessToken() {
		assertNotNull(testSession.getAccessToken());
		assertFalse(testSession.getAccessToken().isEmpty());
	}

	@MediumTest
	public void testUserID() {
		assertNotNull(testSession.getTestUserId());
		assertFalse(testSession.getTestUserId().isEmpty());
	}

	@MediumTest
	public void testUserName() {
		assertNotNull(testSession.getTestUserName());
		assertFalse(testSession.getTestUserName().isEmpty());
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
