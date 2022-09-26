package com.group.ddjjnews.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.Profile;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

class ProfilePhotoAsync extends AsyncTask<String, String, String> {
    public Bitmap bitmap;
    String url;
    String name = null, email = null;
    Profile mFbProfile;
    ImageView mProfileImage;


    public ProfilePhotoAsync(String url) {
        this.url = url;
    }

    @Override
    protected String doInBackground(String... params) {
        // Fetching data from URI and storing in bitmap
        bitmap = DownloadImageBitmap(url);
        return null;
    }

    public static Bitmap DownloadImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("IMAGE", "Error getting bitmap", e);
        }
        return bm;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mProfileImage.setImageBitmap(bitmap);

        saveNewUser();
    }

    private void saveNewUser() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.setUsername(name);
        parseUser.setEmail(email);


        //Saving profile photo as a ParseFile
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) mProfileImage.getDrawable()).getBitmap();
        if (bitmap != null) {
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            byte[] data = stream.toByteArray();
            String thumbName = parseUser.getUsername().replaceAll("\\s+", "");
            final ParseFile parseFile = new ParseFile(thumbName + "_thumb.jpg", data);

            parseFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    parseUser.put("profileThumb", parseFile);

                    //Finally save all the user details
                    parseUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            //Toast.makeText(getContext(), "New user:" + name + " Signed up", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }

    }
}
