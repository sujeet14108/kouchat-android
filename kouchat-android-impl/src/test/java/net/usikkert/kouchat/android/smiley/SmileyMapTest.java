
/***************************************************************************
 *   Copyright 2006-2014 by Christian Ihle                                 *
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

package net.usikkert.kouchat.android.smiley;

import static org.junit.Assert.*;

import java.util.Set;

import net.usikkert.kouchat.android.R;
import net.usikkert.kouchat.android.controller.MainChatController;
import net.usikkert.kouchat.junit.ExpectedException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Test of {@link SmileyMap}.
 *
 * @author Christian Ihle
 */
@RunWith(RobolectricTestRunner.class)
public class SmileyMapTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private SmileyMap smileyMap;

    @Before
    public void setUp() {
        smileyMap = new SmileyMap(new MainChatController());
    }

    @Test
    public void constructorShouldThrowExceptionIfContextIsNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Context can not be null");

        new SmileyMap(null);
    }

    @Test
    public void getSmileyCodesShouldContainAllElevenSmileys() {
        final Set<String> textSmileys = smileyMap.getSmileyCodes();

        assertNotNull(textSmileys);
        assertEquals(11, textSmileys.size());

        assertTrue(textSmileys.contains(":)"));
        assertTrue(textSmileys.contains(":("));
        assertTrue(textSmileys.contains(":p"));
        assertTrue(textSmileys.contains(":D"));

        assertTrue(textSmileys.contains(";)"));
        assertTrue(textSmileys.contains(":O"));
        assertTrue(textSmileys.contains(":@"));
        assertTrue(textSmileys.contains(":S"));

        assertTrue(textSmileys.contains(";("));
        assertTrue(textSmileys.contains(":$"));
        assertTrue(textSmileys.contains("8)"));
    }

    @Test
    public void getSmileyShouldThrowExceptionIfCodeIsNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Smiley code can not be empty");

        smileyMap.getSmiley(null);
    }

    @Test
    public void getSmileyShouldThrowExceptionIfCodeIsEmpty() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Smiley code can not be empty");

        smileyMap.getSmiley(" ");
    }

    @Test
    public void getSmileyShouldThrowExceptionIfCodeIsNotFound() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Smiley with code ':%' does not exist");

        smileyMap.getSmiley(":%");
    }

    @Test
    public void getSmileyShouldSupportAllElevenSmileys() {
        assertEquals(R.drawable.ic_smiley_smile, smileyId(":)"));
        assertEquals(R.drawable.ic_smiley_sad, smileyId(":("));
        assertEquals(R.drawable.ic_smiley_tongue, smileyId(":p"));
        assertEquals(R.drawable.ic_smiley_teeth, smileyId(":D"));

        assertEquals(R.drawable.ic_smiley_wink, smileyId(";)"));
        assertEquals(R.drawable.ic_smiley_omg, smileyId(":O"));
        assertEquals(R.drawable.ic_smiley_angry, smileyId(":@"));
        assertEquals(R.drawable.ic_smiley_confused, smileyId(":S"));

        assertEquals(R.drawable.ic_smiley_cry, smileyId(";("));
        assertEquals(R.drawable.ic_smiley_embarrassed, smileyId(":$"));
        assertEquals(R.drawable.ic_smiley_shade, smileyId("8)"));
    }

    @Test
    @Config(qualifiers = "xhdpi")
    public void getSmileyShouldReturnDrawableWithCorrectBounds() {
        final Drawable smiley = smileyMap.getSmiley(":)");

        assertEquals(new Rect(0, 0, 40, 40), smiley.getBounds());
    }

    private int smileyId(final String code) {
        return Robolectric.shadowOf(smileyMap.getSmiley(code)).getCreatedFromResId();
    }
}
