//package com.example.cookdi.login;
//
//
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.widget.Toast;
//import com.example.cookdi.R;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class OtpMailActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_otp);
//
//        final PinEntryEditText txtPinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
//        txtPinEntry.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.toString().equals("1234")) {
//                    Toast.makeText(OtpMailActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                } else if (s.length() == "1234".length()) {
//                    Toast.makeText(OtpMailActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
//                    txtPinEntry.setText(null);
//                }
//            }
//        });
//    }
//}
