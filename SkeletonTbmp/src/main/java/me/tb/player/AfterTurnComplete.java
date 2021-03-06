package me.tb.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Juhess88 on 5/3/2015.
 */
public class AfterTurnComplete extends ActionBarActivity {

    protected ArrayAdapter<String> adapter1;
    protected ArrayAdapter<String> adapter2;
    private Toolbar toolbar;
    //list of buttons
    List<Button> button_list = new ArrayList<Button>();
    TextView s1, s2, n1, n2;

    Button b0, b1, b2, b3, b4, b5, b6, b7, b8;
    Button tb_tiles2;
    ImageView img, img2;
    String tiles = "";

    Bitmap mIcon11 = null;

    //    private RecyclerView recyclerView;
//    private RecyclerShareAdapter recyclerAdapter;
    private Button buttonMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            setContentView(R.layout.activity_after_turn21);
        } else {
            setContentView(R.layout.activity_after_turn_complete);
        }
        toolbar = (Toolbar) findViewById(R.id.app_bar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SkeletonActivity.isViewingBoardAfterTurn = false;

        Button[] bttn_arr = new Button[]{b0, b1, b2, b3, b4, b5, b6, b7, b8};

        button_list.addAll(Arrays.asList(bttn_arr));

        button_list.set(0, (Button) findViewById(R.id.button0));
        button_list.set(1, (Button) findViewById(R.id.button1));
        button_list.set(2, (Button) findViewById(R.id.button2));
        button_list.set(3, (Button) findViewById(R.id.button3));
        button_list.set(4, (Button) findViewById(R.id.button4));
        button_list.set(5, (Button) findViewById(R.id.button5));
        button_list.set(6, (Button) findViewById(R.id.button6));
        button_list.set(7, (Button) findViewById(R.id.button7));
        button_list.set(8, (Button) findViewById(R.id.button8));

        Intent intent = getIntent();

        ArrayList<String> button_letters = new ArrayList<String>();

        button_letters = intent.getStringArrayListExtra("button_letter");

        int i = button_list.size() - 1;
        for (Button b : button_list) {
            b.setText(button_letters.get(i));
            i--;
        }

        adapter1 = new ArrayAdapter<String>(this, R.layout.items);
        adapter2 = new ArrayAdapter<String>(this, R.layout.items);

        //Configure the list view
        ListView list_1 = (ListView) findViewById(R.id.listview1_1);
        list_1.setAdapter(adapter1);

        //Configure the list view
        ListView list_2 = (ListView) findViewById(R.id.listview2_2);
        list_2.setAdapter(adapter2);

        n1 = (TextView) findViewById(R.id.gName_1);
        s1 = (TextView) findViewById(R.id.gScore_1);
        n2 = (TextView) findViewById(R.id.gName_2);
        s2 = (TextView) findViewById(R.id.gScore_2);

        String nextPlayer = intent.getStringExtra("nextPlayerTurn");
        if (nextPlayer != null) {
            if (nextPlayer.equals("p_2")) {
                n1.setTextColor(getResources().getColor(R.color.primaryColor));
                s1.setTextColor(getResources().getColor(R.color.primaryColor));
            } else {
                n2.setTextColor(getResources().getColor(R.color.primaryColor));
                s2.setTextColor(getResources().getColor(R.color.primaryColor));

            }
        }

        img = (ImageView) findViewById(R.id.gProfPic_1);
        img2 = (ImageView) findViewById(R.id.gProfPic_2);
        String name1 = intent.getStringExtra("name1");
        String name2 = intent.getStringExtra("name2");
        String score1 = intent.getStringExtra("score1");
        String score2 = intent.getStringExtra("score2");

        n1.setText(name1);
        s1.setText(score1);
        n2.setText(name2);
        s2.setText(score2);

        String pic1 = intent.getStringExtra("pic1");
        if (pic1 != null) {
            new LoadProfileImage(img).execute(pic1);
        } else {
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.blankprofile);
            img.setImageBitmap(getCircleBitmap((Bitmap.createScaledBitmap(largeIcon, 150, 150, false))));
        }
        String pic2 = intent.getStringExtra("pic2");
        if (pic2 != null) {
            new LoadProfileImage(img2).execute(pic2);
        } else {
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.blankprofile);
            img2.setImageBitmap(getCircleBitmap((Bitmap.createScaledBitmap(largeIcon, 150, 150, false))));
        }
        tiles = intent.getStringExtra("tiles");
        if (tiles.equals("0")) {
            EditText e = (EditText) findViewById(R.id.after_turn_edittext);
            e.setHint("Final Turn Complete");
        }

        tb_tiles2 = (Button) findViewById(R.id.toolbar_tiles2);
        tb_tiles2.setText("Tiles: " + tiles);

        ArrayList<String> list1 = intent.getStringArrayListExtra("list1");
        ArrayList<String> list2 = intent.getStringArrayListExtra("list2");

        for (i = 0; i < list1.size(); i++) {
            adapter1.insert(list1.get(i), 0);
        }

        for (i = 0; i < list2.size(); i++) {
            adapter2.insert(list2.get(i), 0);
        }

        buttonMessage = (Button) findViewById(R.id.button_message2);
        buttonMessage.setText(SkeletonActivity.shareMessageCombo);
        buttonMessage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String type = "image/*";
                            String mediaPath = Environment.getExternalStorageDirectory() + "/game_icon1.png";
//                            createInstagramIntent(AfterTurnComplete.this, type, mediaPath, SkeletonActivity.shareMessageCombo);
                            onShareClick();
//                            initShareIntent("facebook.katana");
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    public static List<RecyclerShareModel> getData() {
        List<RecyclerShareModel> data = new ArrayList<>();
        int icon = R.drawable.shareicon;
        String title = SkeletonActivity.shareMessageCombo;

        RecyclerShareModel current = new RecyclerShareModel();
        current.iconId = icon;
        current.title = title;
        data.add(current);

        return data;
    }

    private void initShareIntent(String type) {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/jpeg");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                Log.d("package name", info.activityInfo.packageName.toLowerCase());
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type)) {
                    share.putExtra(Intent.EXTRA_SUBJECT, "subject");
                    share.putExtra(Intent.EXTRA_TEXT, "your text");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/game_icon1.png"))); // Optional, just if you wanna share an image.
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found)
                return;

            startActivity(Intent.createChooser(share, "Select"));
        }
    }

    public void onShareClick() {
        Resources resources = getResources();
        String type = "image/*";
        String mediaPath = Environment.getExternalStorageDirectory() + "/game_icon1.png";

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Download in Google Play Store\nhttps://play.google.com/store/apps/details?id=me.tb.player");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Play Word Bandit - Multiplayer");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.setType(type);

        PackageManager pm = getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType(type);

        Intent openInChooser = Intent.createChooser(emailIntent, resources.getString(R.string.share_chooser_text));

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if (packageName.contains("com.google.android.gm")) {
                emailIntent.setPackage(packageName);
            } else if (packageName.contains("twitter") || packageName.contains("facebook.katana") || packageName.contains("com.instagram.android")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType(type);
                // Add the URI and the caption to the Intent.
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                if (packageName.contains("twitter") || packageName.contains("instagram")) {
                    intent.putExtra(Intent.EXTRA_TEXT, SkeletonActivity.shareMessageCombo+"\nDownload now https://play.google.com/store/apps/details?id=me.tb.player");
                }
                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        startActivity(openInChooser);
    }

    //This controls all the sharing platform intents
    public static void createInstagramIntent(Context context, String type, String mediaPath, String caption) {

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        // Add the URI and the caption to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, caption);
        share.putExtra(Intent.EXTRA_SUBJECT, "Check this out");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Broadcast the Intent.
        context.startActivity(Intent.createChooser(share, "Share to"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

//        MenuItem tile_item = menu.findItem(R.id.tile_count);
//        MenuItem shuffle = menu.findItem(R.id.shuffle);
//        MenuItem pass = menu.findItem(R.id.pass);
        MenuItem exit = menu.findItem(R.id.exit);
//        MenuItem enter = menu.findItem(R.id.enter);
        MenuItem clear = menu.findItem(R.id.clear);
        MenuItem settings = menu.findItem(R.id.action_settings);

//        tile_item.setTitle("Tiles Remaining: " + tiles);
//        shuffle.setVisible(false);
//        pass.setVisible(false);
//        enter.setVisible(false);
        clear.setVisible(false);
        exit.setVisible(false);
        settings.setVisible(false);

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AfterTurnComplete.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }

    /**
     * Background Async task to load user profile picture from url
     */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];

            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(getCircleBitmap(Bitmap.createScaledBitmap(result, 150, 150, false)));
        }
    }

    public Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}
