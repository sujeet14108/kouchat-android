
/***************************************************************************
 *   Copyright 2006-2013 by Christian Ihle                                 *
 *   contact@kouchat.net                                                   *
 *                                                                         *
 *   This file is part of KouChat.                                         *
 *                                                                         *
 *   KouChat is free software; you can redistribute it and/or modify       *
 *   it under the terms of the GNU Lesser General Public License as        *
 *   published by the Free Software Foundation, either version 3 of        *
 *   the License, or (at your option) any later version.                   *
 *                                                                         *
 *   KouChat is distributed in the hope that it will be useful,            *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU      *
 *   Lesser General Public License for more details.                       *
 *                                                                         *
 *   You should have received a copy of the GNU Lesser General Public      *
 *   License along with KouChat.                                           *
 *   If not, see <http://www.gnu.org/licenses/>.                           *
 ***************************************************************************/

package net.usikkert.kouchat.android;

import net.usikkert.kouchat.android.chatwindow.AndroidUserInterface;
import net.usikkert.kouchat.android.controller.MainChatController;
import net.usikkert.kouchat.android.service.ChatService;
import net.usikkert.kouchat.android.service.LockHandler;
import net.usikkert.kouchat.android.util.RobotiumTestUtils;
import net.usikkert.kouchat.testclient.TestUtils;

import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Tests the multicast and wake locks.
 *
 * @author Christian Ihle
 */
public class LockTest extends ActivityInstrumentationTestCase2<MainChatController> {

    private Solo solo;
    private LockHandler lockHandler;

    public LockTest() {
        super(MainChatController.class);
    }

    public void setUp() {
        final MainChatController activity = getActivity();

        solo = new Solo(getInstrumentation(), activity);
        lockHandler = getLockHandler(activity);
    }

    public void test01DisableWakeLockIfNecessaryAndQuit() {
        RobotiumTestUtils.openSettings(solo);

        assertTrue(solo.searchText("Enable wake lock"));

        if (wakeLockCheckBoxIsEnabled()) {
            clickOnWakeLockCheckBox();
        }

        assertFalse(wakeLockCheckBoxIsEnabled());

        RobotiumTestUtils.quit(solo);
    }

    public void test02WakeLockShouldBeDisabledNow() {
        checkThatWakeLockIsDisabled();
    }

    // TODO

    public void test99Quit() {
        RobotiumTestUtils.quit(solo);
    }

    public void tearDown() {
        solo.finishOpenedActivities();

        solo = null;
        lockHandler = null;
        setActivity(null);

        System.gc();
    }

    private LockHandler getLockHandler(final MainChatController activity) {
        final AndroidUserInterface androidUserInterface =
                TestUtils.getFieldValue(activity, AndroidUserInterface.class, "androidUserInterface");
        final ChatService chatService = TestUtils.getFieldValue(androidUserInterface, ChatService.class, "context");

        return TestUtils.getFieldValue(chatService, LockHandler.class, "lockHandler");
    }

    private boolean wakeLockCheckBoxIsEnabled() {
        return solo.isCheckBoxChecked(0);
    }

    private void clickOnWakeLockCheckBox() {
        solo.clickOnCheckBox(0);
    }

    private void checkThatWakeLockIsDisabled() {
        solo.sleep(1000);

        assertTrue(lockHandler.multicastLockIsHeld()); // Always on
        assertFalse(lockHandler.wakeLockIsHeld());
    }
}
