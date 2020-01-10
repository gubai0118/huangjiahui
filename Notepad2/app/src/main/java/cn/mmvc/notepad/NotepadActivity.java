package cn.mmvc.notepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.mmvc.notepad.bean.NotepadBean;
import cn.mmvc.notepad.database.SQLiteHelper;
import cn.mmvc.notepad.notepad.adapter.NotepadAdapter;

public class NotepadActivity extends Activity {
    ListView listview;
    List<NotepadBean> list;
    SQLiteHelper mSQLiteHelper;
    NotepadAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        //用于显示记录的列表
        listview =(ListView) findViewById(R.id.listview);
        ImageView add=(ImageView) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(NotepadActivity.this,RecordActivity.class);
                startActivityForResult(intent,1);
            }
        });
        initData();
    }

    protected  void initData(){
        mSQLiteHelper=new SQLiteHelper(this);//创建数据库
        showQueryData();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotepadBean notepadBean=list.get(position);
                Intent intent=new Intent(NotepadActivity.this,RecordActivity.class);
                intent.putExtra("id",notepadBean.getId());
                intent.putExtra("time",notepadBean.getNotepadTime());
                //记录的内容
                intent.putExtra("content",notepadBean.getNotepadContent());
                //跳转到修改记录界面
                NotepadActivity.this.startActivityForResult(intent,1);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder=new AlertDialog.Builder(NotepadActivity.this)
                        .setMessage("是否删除此记录")
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog,int which){
                                NotepadBean notepadBean =list.get(position);
                                if(mSQLiteHelper.deleteData(notepadBean.getId())){
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();//更新记事本界面
                                    Toast.makeText(NotepadActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();//关闭对话
                            }
                        });
                dialog=builder.create();
                dialog.show();
                return true;
            }
        });
    }

    private void showQueryData() {
        if (list!=null){
            list.clear();
        }
        list =mSQLiteHelper.query();
        adapter =new NotepadAdapter(this,list);
        listview.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==2){
            showQueryData();
        }
    }
}
