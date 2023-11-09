package com.example.capstone_seefood;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageProxy;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class PredictActivity  extends AppCompatActivity {
    private Bitmap bitmap = null;

    List<String> imagenet_classes;
    Module module;

    public static String assetFilePath(Context context, String assetName)throws IOException{
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try(InputStream is = context.getAssets().open(assetName)){
            try(OutputStream os = new FileOutputStream(file)){
                byte[] buffer = new byte[4*1024];
                int read;
                while ((read=is.read(buffer))!=-1){
                    os.write(buffer,0,read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);
        try {
            bitmap = BitmapFactory.decodeStream(getAssets().open("PATH SAVED FOTO"));
            module = module.load(assetFilePath(this, "best.pt"));
        }
        catch(IOException e) {
            Log.e("PTRT", "error no module");
            finish();
        }
        var previewView = findViewById(R.id.previewscan);

        imagenet_classes = LoadClasses("");
        LoadTorchModule("best.pt");



        final Button button = findViewById(R.id.btnprediksi);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                @SuppressLint("UnsafeOptInUsageError") Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(bitmap, TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB);
                Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
                float[] scores = outputTensor.getDataAsFloatArray();
                float maxScore = -Float.MAX_VALUE;
                int maxScoreIdx = -1;
                for (int i = 0; i < scores.length; i++) {
                    if (scores[i] > maxScore) {
                        maxScore = scores[i];
                        maxScoreIdx = i;
                    }
                }

            }
        });
    }



    void LoadTorchModule(String fileName) {
        File modelFile = new File(this.getFilesDir(), fileName);
        try {
            if (!modelFile.exists()) {
                InputStream inputStream = getAssets().open(fileName);
                FileOutputStream outputStream = new FileOutputStream(modelFile);
                byte[] buffer = new byte[2048];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();

            }
            module = LiteModuleLoader.load(modelFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }






    List<String> LoadClasses (String fileName){
        List<String> classes = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));
            String line;
            while ((line=br.readLine())!=null){
                classes.add(line);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return classes;

    }
}
