package com.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import com.bean.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by victor on 2015/3/9 0009.
 */
public class ContactUtil {
    /**
     * 注意在子线程中去查询
     *
     * @param context
     * @return
     */
    public static List<User> loadContacts(Context context) {
        List<User> listUser = new ArrayList<User>();
        String[] phoneNumberProjection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID};

        Cursor phonecursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                phoneNumberProjection, null, null, null);
        if (phonecursor != null) {
            try {
                while (phonecursor.moveToNext()) {
                    int contactId = phonecursor.getInt(0);
                    String phoneNumber = phonecursor.getString(1);
                    String name = phonecursor.getString(2);
                    int photoid = phonecursor.getInt(3);
                    User user = new User();
                    user.setPhoneNumber(phoneNumber);
                    user.setUserName(name);
                    listUser.add(user);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                phonecursor.close();
                phoneNumberProjection = null;
            }
        }
        return listUser;
    }
}
