package tk.fei0.spacing;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.content.Intent;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button button3;
    private EditText editText;
    private String txt;
    private Matcher m;
    private Pattern p;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.INVISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = (Button) findViewById(R.id.button);
        button3 = (Button) findViewById(R.id.button3);
        editText = (EditText) findViewById(R.id.editText3);

        final ArrayList<Character.UnicodeBlock> japkor = new ArrayList<>();
        japkor.add(Character.UnicodeBlock.HIRAGANA);
        japkor.add(Character.UnicodeBlock.KATAKANA);
        japkor.add(Character.UnicodeBlock.HANGUL_SYLLABLES);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.editText3);
                txt = et.getText().toString();
                for (char currentChar : txt.toCharArray()) {
                    Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(currentChar);
                    if (japkor.contains(unicodeBlock)) {
                        Snackbar.make(view, "文 字 中 包 含 日 文 或 韓 文 ， 請 刪 除 後 再 試 。", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                    } else {
                        progressBar2.setVisibility(View.VISIBLE);
                        StringBuffer sb = new StringBuffer();
                        sb.append(txt);
                        Pattern p = Pattern.compile("\\p{sc=Han}");
                        Matcher m = p.matcher(sb);
                        while (m.find()) {
                            m.appendReplacement(sb, "$0 ");
                        }
                        sb.delete(0, txt.length());
                        sb.deleteCharAt(sb.length()-1);
                        // txt = txt.replaceAll("(?<=\\p{sc=Han})"," ");
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText(null, sb);
                        clipboard.setPrimaryClip(clip);
                        Snackbar.make(view, "已 複 製 至 剪 貼 簿 。", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        editText.setText(sb);
                        progressBar2.setVisibility(View.INVISIBLE);
                    }
                }

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.editText3);
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
