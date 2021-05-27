package com.tea.markdown.Activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tea.ftp.utils.KnowledgeFTPUtils;
import com.tea.markdown.R;
import com.tea.markdown.R2;
import com.tea.sql.Bean.Knowledge;
import com.tea.sql.Dao.KnowledgeDao;
import com.tea.view.Dialog.DialogFragment;
import com.tea.view.View.Toast;
import com.wega.library.loadingDialog.LoadingDialog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tea.markdown.Markdown.Markdown.parse;
import static com.tea.view.Utils.ViewUtil.addStatusBar;

/**
 * @author jiang yuhang
 * @version 1.0
 * @className MarkdownUIActivity
 * @program teaHome
 * @date 2021-04-26 21:05
 */
public class MarkdownUIActivity extends AppCompatActivity
        implements View.OnClickListener {
    public static final String PAGE = "page";
    public static final String DEF_VALUE = "";
    public static final String TITLE = "title";
    public static final String TIME = "time";
    private final static String mimeType = "text/html";
    private final static String enCoding = "utf-8";
    private final static String MARKDOWN = "MARKDOWN";
    private final static int FAIL = 10;
    private final static int SUCCESS = 12;

    LoadingDialog loadingDialog;

    @SuppressLint("NonConstantResourceId")
    @BindView(R2.id.et_title)
    EditText et_title;
    @SuppressLint("NonConstantResourceId")
    @BindView(R2.id.markdown)
    EditText markdown;

    @SuppressLint("DefaultLocale")
    protected final Handler handler = new Handler(msg -> {
        String title = et_title.getText().toString();
        String page = markdown.getText().toString();

        Date date = new Date();

        if (msg.what == DialogFragment.MARKDOWN_SAVE) {
            //保存草稿
            SharedPreferences preferences = getSharedPreferences(MARKDOWN, MODE_PRIVATE);
            preferences.edit()
                    .putLong(TIME, date.getTime())
                    .putString(PAGE, page)
                    .putString(TITLE, title)
                    .apply();
        } else if (msg.what == DialogFragment.MARKDOWN_DROP) {
            //清除草稿
            SharedPreferences preferences = getSharedPreferences(MARKDOWN, MODE_PRIVATE);
            preferences.edit()
                    .putLong(TIME, 0)
                    .putString(PAGE, DEF_VALUE)
                    .putString(TITLE, DEF_VALUE)
                    .apply();
        } else if (msg.what == DialogFragment.MARKDOWN_SHOW) {
            //读取草稿
            SharedPreferences preferences = getSharedPreferences(MARKDOWN, MODE_PRIVATE);

            page = preferences.getString(PAGE, DEF_VALUE);
            title = preferences.getString(TITLE, DEF_VALUE);

            if (page.length() == 0 && title.length() == 0) {
                return true;
            } else {
                long time = ((new Date()).getTime() - preferences.getLong(TIME, 0)) / 1000;
                String tip = "已经恢复为%d%s前的草稿。";

                if (time < 60) {
                    tip = String.format(tip, time, "秒");
                } else {
                    tip = String.format(tip, time / 60, "分");
                }

                markdown.setText(page);
                et_title.setText(title);
                Toast.getToast(this, tip).show();
            }
        } else if (msg.what == SUCCESS) {
            loadingDialog.loadSuccess(msg.obj.toString());
            loadingDialog.cancel();
        } else if (msg.what == FAIL) {
            loadingDialog.loadFail(msg.obj.toString());
            loadingDialog.cancel();
        }

        if (msg.obj != null && msg.obj.equals("Dialog")) {
            super.finish();
        }
        return true;
    });

    @SuppressLint("NonConstantResourceId")
    @BindView(R2.id.markdown_webView)
    WebView markdown_webView;
    @BindViews({R2.id.title, R2.id.bold, R2.id.italics, R2.id.delete, R2.id.inline, R2.id.list})
    List<Button> buttons;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markdown);
        ButterKnife.bind(this);

        addStatusBar(this, R.id.ll_markdown, R.color.topBar_color);
        this.findViewById(R.id.markdown_webView)
                .setBackgroundColor(getResources().getColor(R.color.app_bg_color));
    }

    /**
     * @param isEdit 当前状态
     */
    protected void changeEditMode(View v, boolean isEdit) {
        if (v instanceof Button) {
            Button button = (Button) v;
            markdown_webView.setVisibility(isEdit ? View.VISIBLE : View.GONE);
            markdown.setVisibility(isEdit ? View.GONE : View.VISIBLE);
            button.setTag(isEdit ? "edit" : "see");
            button.setText(isEdit ? "编辑" : "预览");

            for (Button b : buttons) {
                b.setEnabled(!isEdit);
                if (isEdit) {
                    b.setText(DEF_VALUE);
                } else {
                    b.setText(b.getHint());
                }
            }
            if (isEdit) {
                String text = markdown.getText().toString();
                markdown_webView.loadDataWithBaseURL(null,
                        parse(text),
                        mimeType,
                        enCoding,
                        null);
            }
        }
    }

    /**
     * 上传文章
     */
    protected void updatePage() {
        String title = et_title.getText().toString();
        String page = markdown.getText().toString();

        if (title.length() == 0 && page.length() == 0) {
            Toast.getToast(this, "标题、内容不能为空。").show();
        } else if (title.length() == 0) {
            Toast.getToast(this, "标题不能为空。").show();
        } else if (page.length() == 0) {
            Toast.getToast(this, "内容不能为空。").show();
        } else {
            loadingDialog = getLoadingDialog();
            loadingDialog.loading();

            Thread thread = new Thread(() -> {
                if (KnowledgeDao.getKnowledgeByTitle(title) != null) {
                    Message.obtain(handler, FAIL, "标题已重复").sendToTarget();
                } else {
                    Knowledge knowledge = new Knowledge();
                    SharedPreferences preferences = getSharedPreferences("USER", MODE_PRIVATE);

                    knowledge.setAuthorName(preferences.getString("nickname", ""));
                    knowledge.setHavePic(false);
                    knowledge.setTitle(title);
                    knowledge.setCreateTime(new Date());

                    if (Objects.equals(knowledge.getAuthorName(), "")) {
                        Message.obtain(handler, FAIL, "上传失败").sendToTarget();
                    } else {
                        KnowledgeDao.addKnowledge(knowledge);

                        KnowledgeFTPUtils knowledgeFTPUtils = new KnowledgeFTPUtils("");
                        String text = markdown.getText().toString();
                        InputStream inputStream = new ByteArrayInputStream(parse(text).getBytes());

                        knowledgeFTPUtils.getFtp().update(knowledge.getUrl(), inputStream);
                        Message.obtain(handler, SUCCESS, "上传完成").sendToTarget();
                        handler.sendEmptyMessage(DialogFragment.MARKDOWN_DROP);
                        super.finish();
                    }
                }
            });
            thread.start();
        }
    }

    protected LoadingDialog getLoadingDialog() {
        LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
        return builder.setLoading_text("上传中")
                .setFail_text("上传失败")
                .setSuccess_text("上传完成")
                .create();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @OnClick({R2.id.see, R2.id.title, R2.id.bold, R2.id.italics, R2.id.delete,
            R2.id.inline, R2.id.list, R2.id.update})
    public void onClick(View v) {

    }
}
