package com.tea.teahome.User.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.tea.teahome.R;
import com.tea.teahome.Setting.Activity.SettingActivity;
import com.tea.view.Dialog.DialogFragment;
import com.tea.view.View.Toast;
import com.tuya.smart.android.user.api.IBooleanCallback;
import com.tuya.smart.android.user.api.IReNickNameCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tea.teahome.User.Utils.BitmapUtils.getHeadBitmap;
import static com.tea.teahome.User.Utils.DrawableUtils.bitmapToFile;
import static com.tea.teahome.User.Utils.DrawableUtils.drawableToFile;
import static com.tea.teahome.User.Utils.HttpUtils.getNetWorkBitmap;
import static com.tea.teahome.User.Utils.HttpUtils.saveBitmapToFile;
import static com.tea.teahome.User.Utils.UserUtils.getDownloadStatus;
import static com.tea.teahome.User.Utils.UserUtils.getErrorCode;
import static com.tea.teahome.User.Utils.UserUtils.logoutAccount;
import static com.tea.teahome.User.Utils.UserUtils.setDownloadStatus;
import static com.tea.teahome.User.Utils.UserUtils.setHeadIcon;
import static com.tea.teahome.User.Utils.UserUtils.updateUserNickName;
import static com.tea.view.Utils.ViewUtil.addStatusBar;

/**
 * ?????????????????????????????????????????????R.layout.activity_my_information_home???
 *
 * @author jiang yuhang
 * @version 1.0
 * @date 2021-02-7 20:49
 */
public class MyInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ALBUM_OK = 0;
    /**
     * ????????????
     */
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.headImage)
    ImageView headImage;
    /**
     * ?????????
     */
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_userName)
    TextView tv_username;
    /**
     * UID
     */
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_uid)
    TextView tv_uid;
    /**
     * ????????????
     */
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_logout)
    Button bt_logout;
    /**
     * ????????????
     */
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_setting)
    Button bt_setting;
    /**
     * ????????????
     */
    private Activity activity;
    /**
     * ????????????
     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    Bitmap bitmap = BitmapFactory.decodeFile(activity.getDir("icon", Context.MODE_PRIVATE) + "icon.png");
                    headImage.setImageBitmap(bitmap);
                    break;
                case 1:
                    logoutAccount(activity, null);
                    setDefaultUserInf();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * ???????????????
     *
     * @author jiang yuhang
     * @date 2021-02-07 20:50
     **/
    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information_home);
        ButterKnife.bind(this);
        activity = this;
        //?????????????????????
        addStatusBar(this, R.id.ll_myinf, R.color.statusBar_color);
        //????????????
        setHeadIcon(this, headImage, R.drawable.ic_account_default);
    }

    @SuppressLint("SetTextI18n")
    private void setUserInf() {
        User user = TuyaHomeSdk.getUserInstance().getUser();
        if (user == null) {
            setDefaultUserInf();
            return;
        }

        String nickName = user.getNickName();
        String nickNameDefault = "????????????" + user.getMobile().replaceAll("\\d+[-]", "");
        String headIcon = user.getHeadPic();

        //???????????????
        if (nickName.equals("")) {
            //??????????????????
            updateUserNickName(activity, nickNameDefault,
                    new IReNickNameCallback() {
                        @Override
                        public void onSuccess() {
                            tv_username.setText(nickNameDefault);
                        }

                        @Override
                        public void onError(String code, String error) {

                        }
                    });
        } else {
            tv_username.setText(nickName);
        }
        //???????????????
        if (headIcon.equals("")) {
            @SuppressLint({"NewApi", "LocalSuppress"})
            File file = drawableToFile(activity, R.drawable.ic_account_default);
            //??????????????????
            TuyaHomeSdk.getUserInstance().uploadUserAvatar(
                    file,
                    new IBooleanCallback() {
                        @Override
                        public void onSuccess() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                setHeadIcon(activity, headImage, R.drawable.ic_account_default);
                            }
                        }

                        @Override
                        public void onError(String code, String error) {
                            Toast.getToast(activity, getErrorCode(code, error)).show();
                        }
                    });
        } else {
            if (getDownloadStatus(activity)) {
                Message message = handler.obtainMessage(0);
                handler.sendMessage(message);
            } else {
                new Thread(() -> {
                    for (int i = 0; i < 3; i++) {
                        if (getDownloadStatus(activity)) {
                            Message message = handler.obtainMessage(0);
                            handler.sendMessage(message);
                            return;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    setDownloadStatus(activity, false);
                    getNetWorkBitmap(activity, activity.getDir("icon", Context.MODE_PRIVATE) + "icon.png",
                            user.getHeadPic());
                    Message message = handler.obtainMessage(0);
                    handler.sendMessage(message);
                }).start();
            }
        }
        //??????????????????
        tv_uid.setText("?????????:" + user.getUsername());
        //??????ImageView??????????????????
        tv_username.setTag("userInf");
        bt_logout.setVisibility(View.VISIBLE);
        headImage.setOnClickListener(this);
    }

    /**
     * ???????????????????????????
     */
    @SuppressLint({"NewApi", "UseCompatLoadingForDrawables"})
    private void setDefaultUserInf() {
        tv_username.setText("????????????/??????");
        tv_username.setTag("loginAccount");
        headImage.setImageDrawable(getDrawable(R.drawable.ic_account_default));
        tv_uid.setText("");
        headImage.setOnClickListener(null);
        bt_logout.setVisibility(View.INVISIBLE);
    }

    /**
     * ??????????????????
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ALBUM_OK) {
            if (data != null) {
                ContentResolver cr = activity.getContentResolver();
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = getHeadBitmap(cr.openInputStream(uri));
                    @SuppressLint({"NewApi", "LocalSuppress"})
                    File file = bitmapToFile(activity, bitmap);
                    //????????????
                    TuyaHomeSdk.getUserInstance().uploadUserAvatar(file,
                            new IBooleanCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.getToast(activity, "????????????").show();
                                    if (bitmap != null) {
                                        headImage.setImageBitmap(bitmap);
                                        try {
                                            saveBitmapToFile(
                                                    activity.getDir("icon", Context.MODE_PRIVATE) + "icon.png",
                                                    bitmap);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onError(String code, String error) {
                                    Toast.getToast(activity, getErrorCode(code, error)).show();
                                }
                            });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.getToast(activity, "???????????????").show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * ???????????????????????????????????????
     *
     * @author jiang yuhang
     * @date 2021-04-09 23:00
     **/
    @Override
    protected void onResume() {
        setUserInf();
        super.onResume();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    @OnClick({R.id.headImage, R.id.tv_userName, R.id.bt_logout, R.id.bt_setting})
    public void onClick(View v) {
        Intent intent;
        if (v.getTag() != null) {
            switch (v.getTag().toString()) {
                //?????????????????????
                case "logout":
                    androidx.fragment.app.DialogFragment dialogFragment = new DialogFragment(DialogFragment.LOGOUT_MODE, handler);
                    dialogFragment.show(getSupportFragmentManager(), "DialogFragment");
                    break;
                //??????????????????
                case "loginAccount":
                    intent = new Intent(this, LoginAccountActivity.class);
                    startActivity(intent);
                    break;
                //????????????????????????
                case "userInf":
                    intent = new Intent(this, UserInfActivity.class);
                    startActivity(intent);
                    break;
                //????????????
                case "headImage":
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, ALBUM_OK);
                    break;
                //????????????
                case "setting":
                    intent = new Intent(this, SettingActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
}

