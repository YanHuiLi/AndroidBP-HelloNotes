package com.example.archer.hellonotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Archer on 2016/5/21.
 */
public class AddContent extends AppCompatActivity implements View.OnClickListener{


    private String val;
    private Button savabtn,cancelbtn;
    private EditText editText;
    private ImageView c_imagview;
    private VideoView c_video;

    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    private File phoneFile,videoFile;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontent);
        val=getIntent().getStringExtra("flag");

        savabtn= (Button) findViewById(R.id.save);
        cancelbtn= (Button) findViewById(R.id.cancel);
        editText= (EditText) findViewById(R.id.ettext);
        c_imagview= (ImageView) findViewById(R.id.c_img);
        c_video= (VideoView) findViewById(R.id.c_video);
        savabtn.setOnClickListener(this);
        cancelbtn.setOnClickListener(this);


        notesDB=new NotesDB(this);
        dbWriter=notesDB.getWritableDatabase();

        initView();


    }
//判断是添加文字还是图片还是video
    public void initView(){
        if (val.equals("1")){
            //证明是添加文字
            c_imagview.setVisibility(View.GONE);
            c_video.setVisibility(View.GONE);
        }

        if (val.equals("2")){
            //图片
            c_imagview.setVisibility(View.VISIBLE);
            c_video.setVisibility(View.GONE);
            Intent intentImg=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            phoneFile=new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile()+"/"+getTime()+".jpg");

            intentImg.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(phoneFile));
             startActivityForResult(intentImg,1);
        }

        if (val.equals("3")){
            c_imagview.setVisibility(View.GONE);
            c_video.setVisibility(View.VISIBLE);

            Intent video=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoFile=new File(Environment.getExternalStorageDirectory()
                    .getAbsoluteFile()+"/"+getTime()+".mp4");

            video.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(videoFile));
            startActivityForResult(video,2);
        }

    }



    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.save:

                 addDB();
                 finish();
                 break;

             case R.id.cancel:

                 finish();
                 break;
         }
    }

    //添加数据到数据库

    public void addDB(){
        ContentValues contentValues=new ContentValues();
        contentValues.put(NotesDB.CONTENT,editText.getText().toString());
        contentValues.put(NotesDB.TIME,getTime());
//        contentValues.put(NotesDB.PATH,"PATH_Test");
        contentValues.put(NotesDB.PATH,phoneFile+ "");
        contentValues.put(NotesDB.VIDEO,videoFile+ "");


        dbWriter.insert(NotesDB.TABLE_NAME,null,contentValues);
    }

    //获取当前设备时间
    public String getTime(){

        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate=new Date();
        return format.format(curDate);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==1){
            Bitmap bitmap= BitmapFactory.decodeFile(phoneFile.getAbsolutePath());
            c_imagview.setImageBitmap(bitmap);
        }

        //视频的显示
        if (requestCode==2){
            //加载视频
            c_video.setVideoURI(Uri.fromFile(videoFile));
            c_video.start();
        }

    }
}
