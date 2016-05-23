package com.example.archer.hellonotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button textButton,imgButton,videoButton;
    private ListView listView;
    private Intent intent;
    private MyAdapter adapter;
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
//        NotesDB notesDB = new NotesDB(this);
//        dbWriter= notesDB.getWritableDatabase();
//        addDB();



    }

    //初始化组件
    public void initView(){
        listView= (ListView) findViewById(R.id.list);
        textButton= (Button) findViewById(R.id.text123);
        imgButton= (Button) findViewById(R.id.img);
        videoButton= (Button) findViewById(R.id.video);

        textButton.setOnClickListener(this);
        imgButton.setOnClickListener(this);
        videoButton.setOnClickListener(this);
        notesDB=new NotesDB(this);
        dbReader=notesDB.getReadableDatabase();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                cursor.moveToPosition(position);
                Intent i = new Intent(MainActivity.this, SelectAct.class);
                i.putExtra(NotesDB.ID,
                        cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
                i.putExtra(NotesDB.CONTENT, cursor.getString(cursor
                        .getColumnIndex(NotesDB.CONTENT)));
                i.putExtra(NotesDB.TIME,
                        cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                i.putExtra(NotesDB.PATH,
                        cursor.getString(cursor.getColumnIndex(NotesDB.PATH)));
                i.putExtra(NotesDB.VIDEO,
                        cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO)));
                startActivity(i);
            }
        });


    }

    @Override
    public void onClick(View v) {
        intent=new Intent(this,AddContent.class);
        switch (v.getId()){
            case R.id.text123:
               intent.putExtra("flag","1");
                startActivity(intent);
                break;

            case R.id.img:
                intent.putExtra("flag","2");
                startActivity(intent);
                break;

            case R.id.video:
                intent.putExtra("flag","3");
                startActivity(intent);
                break;

        }
    }

    //测试能否注入数据库
/*
    public void addDB(){
        ContentValues contentValues=new ContentValues();
        contentValues.put(NotesDB.CONTENT,"Hello");
        contentValues.put(NotesDB.TIME,getTime());
        //插入语句 添加数据进数据库
        dbWriter.insert(NotesDB.TABLE_NAME,null,contentValues);

    }

    //获取当前设备时间
    public String getTime(){

        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate=new Date();
        return format.format(curDate);

    }
    */

    //查询数据库的语句。适配
    public void selectDB(){
        cursor=dbReader.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);
        adapter=new MyAdapter(this,cursor);
        listView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }
}
