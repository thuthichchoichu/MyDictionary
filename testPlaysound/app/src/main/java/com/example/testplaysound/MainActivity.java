package com.example.testplaysound;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String SOUND_TEMPORARY ="m.mp3" ;
    Button btn;
    MediaPlayer p;
    SoundPool Pool;
    boolean ready;
    int soundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p = new MediaPlayer();
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileOutputStream out = openFileOutput("FDF", MODE_PRIVATE);
                    Toast.makeText(getApplicationContext(), String.valueOf(out.), Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
//      

            }

        });
    }
    private void loadSound(String url) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                BufferedInputStream in = null;
                FileOutputStream output = null;
                HttpURLConnection http = null;
                try {
                    URL url1 = new URL(url);
                    http = (HttpURLConnection) url1.openConnection();
                    in = new BufferedInputStream(http.getInputStream());
                    long size = http.getContentLengthLong(), downloaded=0;
                    output = openFileOutput(SOUND_TEMPORARY, MODE_PRIVATE);
                    BufferedOutputStream buf = new BufferedOutputStream(output, 1024);
                    byte buffer[] = new byte[1024];
                    int read;
                    while ((read = in.read(buffer, 0, 1024)) >= 0) {
                        buf.write(buffer, 0, read);
                        downloaded+=read;
                        Log.d("process", (downloaded/size)*100+"");
                    }
                    loadSoundToPlayer(SOUND_TEMPORARY);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();


    }

    private void loadSoundToPlayer(String soundName) {

  Pool = new SoundPool.Builder().setMaxStreams(1).build();
          soundId = Pool.load(getFilesDir()+"/m.mp3",1);
            Pool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                   ready=true;
//                   Pool.release();
                }
            });



// soundId for reuse later on


    }
//        }

//    }

//
//    private void play() {
//        MediaPlayer p = new MediaPlayer();
//        try {
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//        try {
//            URL url1 = new URL(url);
//            http = (HttpURLConnection) url1.openConnection();
//            in = new BufferedInputStream(http.getInputStream()) ;
//            output = new FileOutputStream(getFilesDir() + "/file.mp3");
//            BufferedOutputStream buf = new BufferedOutputStream(output,1024);
//            Log.d("test",getFilesDir()+" ");
//
//            byte buffer[] = new byte[1024];
//            int read;
//            while ((read = in.read(buffer,0,1024)) >= 0) {
//                buf.write(buffer, 0, read);
//            }
//        } catch (Exception e) {
//            Log.d("test",e.getMessage());
//        } finally {
//            try {
//                if (output != null)
//                    output.close();
//                if (in != null)
//                    in.close();
//            } catch (IOException ignored) {
//            }
//
//            if (http != null)
//                http.disconnect();
//        }
//    }

//    public void downloadFile(String url) {
//        String oppath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/m.mp3";
//        Toast.makeText(MainActivity.this, oppath, Toast.LENGTH_SHORT).show();
//        ObjectOutputStream
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    URL url1 = new URL(url);
//                    HttpURLConnection http = (HttpURLConnection) url1.openConnection();
//                    BufferedInputStream in = new BufferedInputStream(http.getInputStream());
//                    File file = new File()
//                    FileOutputStream fos = new FileOutputStream();
//
//                    fileOutputStream = activity.openFileOutput(DATA_COLOR, Context.MODE_PRIVATE);
//                    objectOutputStream = new ObjectOutputStream(fileOutputStream);
//                    objectOutputStream.writeObject(list);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//                try {
//                    String fileLink = "https://dictionary.cambridge.org/media/english/uk_pron/u/ukf/ukfea/ukfeast011.mp3";
//
//
//                    URL link = new URL(fileLink);
//                    InputStream ins = link.openStream();
//                    ReadableByteChannel chh = Channels.newChannel(link.openStream());
//                    FileOutputStream fos = new FileOutputStream(new File(oppath));
//                    fos.getChannel().transferFrom(chh, 0, Long.MAX_VALUE);
//                    fos.close();
//                    chh.close();
//                } catch (IOException ex) {
////                    ex.printStackTrace();
////                }
//            }
//        }).start();
//    }
}