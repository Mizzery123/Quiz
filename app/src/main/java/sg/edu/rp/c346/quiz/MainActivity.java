package sg.edu.rp.c346.quiz;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnDisplay;
    TextView tvOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDisplay = findViewById(R.id.btnDisplay);
        tvOut = findViewById(R.id.tvDisplay);

        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS);

                if (permissionCheck != PermissionChecker.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, 0);
                    return;
                }

                Uri uri = Uri.parse("content://sms");
                String[] reqCols = new String[]{"address", "body"};

                String filter = "address LIKE ? and body LIKE ?";
                String[] args = {"%66%", "%RP"};
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(uri, reqCols, filter, args, null);
                String smsBody = "";
                if (cursor.moveToFirst()){
                    do{

                        String address = cursor.getString(0);
                        String body = cursor.getString(1);

                        smsBody += address + ": " + body + "\n";
                    }while (cursor.moveToNext());
                }
                tvOut.setText(smsBody);
            }
        });

        int permissionCheck = PermissionChecker.checkSelfPermission
                (MainActivity.this, Manifest.permission.READ_SMS);

        if (permissionCheck != PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_SMS}, 0);
            // stops the action from proceeding further as permission not
            //  granted yet
            return;
        }

    }
}
