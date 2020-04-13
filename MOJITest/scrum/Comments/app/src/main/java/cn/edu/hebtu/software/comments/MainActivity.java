package cn.edu.hebtu.software.comments;

import android.os.Bundle;
import android.os.Handler;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.am_lv_comments)
    ListView mAmLvComments;
    @Bind(R.id.am_tv_zan)
    TextView mAmTvZan;
    @Bind(R.id.am_tv_comment)
    TextView mAmTvComment;
    @Bind(R.id.am_srl_reflush)
    SwipeRefreshLayout mAmSrlReflush;
    //添加listview头
    View mView;
    //列表数据
    List<Comment> mCommentList;
    //adapter
    BaseAdapter mBaseAdapter;
    @Bind(R.id.am_et_msg)
    EditText mAmEtMsg;
    @Bind(R.id.am_b_save)
    Button mAmBSave;
    @Bind(R.id.am_ll_liuyan)
    LinearLayout mAmLlLiuyan;
    @Bind(R.id.am_ll_info)
    LinearLayout mAmLlInfo;


    //回复的内容
    String info = "";
    //标记位，是评论还是回复。默true认评论
    boolean isComment=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("1","1");
        Log.e("2","2");
        setContentView(R.layout.activity_main);
        Log.e("2","2");
        ButterKnife.bind(this);
        initView();
        initData();
        initAdapter();
        initListener();Log.e("","");
    }

    private void initView() {
        mView = LayoutInflater.from(this).inflate(R.layout.head_topic, null);
    }

    private void initData() {
        mCommentList = new ArrayList<>();
        Comment comment = null;
        for (int i = 0; i < 2; i++) {
            if (i % 2 == 0) {
                comment = new Comment(i + "", "张三" + i, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1581949641615&di=ba8a36100e3826cc8d527d148b095a16&imgtype=0&src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20180803%2F23%2F1533308847-sJINRfclxg.jpeg", 1, "郑州航空工业管理学院", "今天真开心，敲了一天代码。", "2015-03-04 23:02:06", null);
            }
            if (i % 2 == 1) {
                comment = new Comment(i + "", "张三" + i, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1581949696360&di=ec747657497be074e2d324e6bd415c92&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20170724%2Fad28da0d658b43aba84ce91df9cacdad.jpeg", 1, "郑州航空工业管理学院", "今天真开心，敲了一天代码。", "2015-03-04 23:02:06", "王二狗" + i);
            }
            mCommentList.add(comment);
        }
    }

    private void initAdapter() {
        mBaseAdapter = new MyDetailSoicalAdapter(this, mCommentList);
        mAmLvComments.addHeaderView(mView);//添加头
        mAmLvComments.setAdapter(mBaseAdapter);
    }

    private void initListener() {
        mAmLvComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int post=position-1;//除去头
                Toast.makeText(MainActivity.this, "点击了第"+post+"个", Toast.LENGTH_SHORT).show();
                isComment=false;//
                comment(true);//调出评论框
                mAmEtMsg.setText("");//清空
                mAmEtMsg.setHint("回复"+mCommentList.get(post).getNickName());
                Log.e("update", "update");
                Log.e("update", "update");
            }
        });

    }


    @OnClick({R.id.am_tv_zan, R.id.am_tv_comment, R.id.am_et_msg, R.id.am_b_save, R.id.am_ll_liuyan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.am_tv_zan:
                break;
            case R.id.am_tv_comment:
                //点击评论按钮
                comment(true);
                break;
            case R.id.am_et_msg:
                break;
            case R.id.am_b_save:
                saveComment();
                break;
            case R.id.am_ll_liuyan:
                break;
        }
    }

    private void saveComment() {
        if (!TextUtils.isEmpty(mAmEtMsg.getText())) {
            info = mAmEtMsg.getText().toString();
            updateComment();
        } else {
            Toast.makeText(MainActivity.this, "请输入内容后在留言", Toast.LENGTH_SHORT).show();
        }
    }

    //这边应该上传用户留言，然后刷新界面
    private void updateComment() {
        Comment comment = null;
        comment = new Comment(666 + "", "张三" + 666, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1581949696355&di=b929db561e5fb323f6cb13ee9540a46a&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F26%2F20170726102254_J4MH5.jpeg", 1, "郑州航空工业管理学院",info, "2015-03-04 23:02:06", null);
        mCommentList.add(0,comment);
        mBaseAdapter.notifyDataSetChanged();
        //还原
        comment(false);
    }

    private void comment(boolean flag) {
        if(flag){
            mAmLlInfo.setVisibility(View.GONE);
            mAmLlLiuyan.setVisibility(View.VISIBLE);
            onFocusChange(flag);
        }else{
            mAmLlInfo.setVisibility(View.VISIBLE);
            mAmLlLiuyan.setVisibility(View.GONE);
            onFocusChange(flag);
            Log.e("GENGXIN", "ddd");
        }


    }

    /**
     * 显示或隐藏输入法
     */
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        MainActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    mAmEtMsg.setFocusable(true);
                    mAmEtMsg.requestFocus();
                } else {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(mAmEtMsg.getWindowToken(), 0);
                }
            }
        }, 100);
    }
}
