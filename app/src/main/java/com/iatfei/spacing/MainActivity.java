package com.iatfei.spacing;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.content.Intent;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button button3;
    private EditText editText;
    private String txt;
    private Matcher m;
    private Pattern p;
    private ProgressBar progressBar2;
    private int japenable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.INVISIBLE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = findViewById(R.id.button);
        button3 = findViewById(R.id.button3);
        editText = findViewById(R.id.editText3);

        final ArrayList<Character.UnicodeBlock> jap = new ArrayList<>();
        jap.add(Character.UnicodeBlock.HIRAGANA);
        jap.add(Character.UnicodeBlock.KATAKANA);
        jap.add(Character.UnicodeBlock.HANGUL_SYLLABLES);

        final ArrayList<Character.UnicodeBlock> chinese = new ArrayList<>();
        chinese.add(Character.UnicodeBlock.CJK_COMPATIBILITY);
        chinese.add(Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS);
        chinese.add(Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
        chinese.add(Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
        chinese.add(Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT);
        chinese.add(Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION);
        chinese.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
        chinese.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
        chinese.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B);
        chinese.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C);
        chinese.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D);

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        japenable = pref.getInt("jap", 2);

        if (japenable == 2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("日 文 問 題")
                    .setMessage("因 為 系 統 無 法 準 確 偵 測 文 字 是 日 本 漢 字 還 是 中 文 字 ， 所 有 中 文 字 和 漢 字 都 會 隔 格 ， 而 片 假 名 和 平 假 名 不 會 。 但 此 與 她 的 習 慣 （ 所 有 日 文 字 不 隔 格 ） 不 符 。\n" +
                            "因 此 ， 系 統 不 會 轉 換 任 何 包 含 片 假 名 或 平 假 名 的 文 字 。\n" +
                            "你 可 以 設 定 忽 略 本 偵 測 。")
                    .setCancelable(false)
                    .setPositiveButton("確 定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("jap", 0);
                            editor.apply();
                        }
                    })
                    .show();
        }
        if (japenable == 1) {
            Snackbar.make(button, "日 文 偵 測 已 關 閉 。", Snackbar.LENGTH_LONG)
                    .setAction("打 開", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("jap", 0);
                            editor.apply();
                        }
                    })
                    .show();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean containsJap = false;
                japenable = pref.getInt("jap", 2);
                EditText et = findViewById(R.id.editText3);
                txt = et.getText().toString();
                for (char currentChar : txt.toCharArray()) {
                    Character.UnicodeBlock unijap = Character.UnicodeBlock.of(currentChar);
                    if (jap.contains(unijap) && japenable != 1) {
                        Snackbar.make(view, "文 字 中 包 含 日 文 ， 請 刪 除 或 關 閉 日 文 偵 測 後 再 試 。", Snackbar.LENGTH_LONG)
                                .setAction("關 閉 偵 測 功 能", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putInt("jap", 1);
                                        editor.apply();
                                    }
                                })
                                .show();
                        containsJap = true;
                        break;
                    }
                }
                if (!containsJap || japenable == 1) {
                    StringBuilder builder = new StringBuilder();
                    //builder.setLength(txt.length());

                    if (txt.length() > 1) {
                        builder.append(txt.charAt(0));
                        Character.UnicodeBlock uni0 = Character.UnicodeBlock.of(txt.charAt(0));
                        if (chinese.contains(uni0)) {
                            char cand1more = txt.charAt(1);
                            Character.UnicodeBlock uni1more = Character.UnicodeBlock.of(cand1more);
                            if (!chinese.contains(uni1more) && cand1more != ' ' && cand1more != '\n')
                                builder.append(" ");
                        }

                        for (int i = 1; i < txt.length(); i++) {
                            char candidate = txt.charAt(i);
                            Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(candidate);
                            if (chinese.contains(unicodeBlock)) {

                                char candbefore = txt.charAt(i - 1);
                                if (candbefore != '\n')
                                    builder.append(" ").append(candidate);
                                else
                                    builder.append(candidate);

                                if (i < txt.length() - 1) {
                                    char cand1 = txt.charAt(i + 1);
                                    Character.UnicodeBlock uni1 = Character.UnicodeBlock.of(cand1);
                                    if (!chinese.contains(uni1) && cand1 != ' ' && cand1 != '\n')
                                        builder.append(" ");
                                }
                            } else
                                builder.append(candidate);
                            }

                            String sb = builder.toString();
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText(null, sb);
                            clipboard.setPrimaryClip(clip);
                            Snackbar.make(view, "已 複 製 至 剪 貼 簿 。", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            editText.setText(builder.toString());
                    } else {
                        Snackbar.make(view, "文 字 太 短 。 請 檢 查 後 再 試 。", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
