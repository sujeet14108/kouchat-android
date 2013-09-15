
/***************************************************************************
 *   Copyright 2006-2013 by Christian Ihle                                 *
 *   kontakt@usikkert.net                                                  *
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

package net.usikkert.kouchat.android.filetransfer;

import java.io.File;

import net.usikkert.kouchat.util.Validate;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Utility methods for handling files on Android.
 *
 * @author Christian Ihle
 */
public class AndroidFileUtils {

    /**
     * Gets a {@link File} reference to the file represented by the {@link Uri}.
     *
     * <p>The uri is expected to be in the following format: <code>content://media/external/images/media/22</code></p>
     *
     * <p>The <code>content</code> protocol is the only protocol supported, and is used to find files
     * in the Android media database, using a {@link ContentResolver}.</p>
     *
     * @param uri Content uri to the file to return. Can be <code>null</code>.
     * @param contentResolver The content resolver, from a context.
     * @return The file, if it's found, or <code>null</code> if not found.
     */
    public File getFileFromContentUri(final Uri uri, final ContentResolver contentResolver) {
        Validate.notNull(contentResolver, "ContentResolver can not be null");

        if (uri == null || !uri.getScheme().equals("content")) {
            return null;
        }

        final String[] columns = new String[] {MediaStore.MediaColumns.DATA};
        final Cursor cursor = contentResolver.query(uri, columns, null, null, null);

        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();
        final String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));

        return new File(path);
    }
}
