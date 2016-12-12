package edu.sjsu.cmpe.partyon.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.config.App;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class MyPaymentQRCodeActivity extends CloseableActivity {
    private final static int WIDTH = 200;
    @Override
    int getToolBarID() {
        return R.id.my_pay_toolbar;
    }

    @Override
    int getResourceID() {
        return R.layout.activity_my_payment_qrcode;
    }
    private String generateCodeInfo(){
        JSONObject code = new JSONObject();
        try {
            code.put("app_id","partyon");
            code.put("intent","payment");
            code.put("user_id", App.getUser().getObjectId());
            code.put("balance",100);
            code.put("expiresAt",new Date().getTime()+1000*60*60*5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code.toString();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_payment_qrcode);
        ImageView imageView = (ImageView) findViewById(R.id.my_pay_qr_imageview);
        try {
            Bitmap bitmap = encodeAsBitmap(generateCodeInfo());
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
