package smc.minjoon.accompanying.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import smc.minjoon.accompanying.R;

public class DisabledRegisterActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private boolean validate =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_disabled);
        final CheckBox checkService = (CheckBox)findViewById(R.id.checkService);
        final CheckBox checkPrivacy = (CheckBox)findViewById(R.id.checkPrivacy);
        final EditText idText=(EditText)findViewById(R.id.idText);
        final EditText passwordText=(EditText)findViewById(R.id.passwordText);
        final EditText nameText=(EditText)findViewById(R.id.nameText);
        final EditText phoneText=(EditText)findViewById(R.id.phoneText);
        final Button registerBtn=(Button)findViewById(R.id.registerBtn);
        Button service=(Button)findViewById(R.id.service);
        Button privacy=(Button)findViewById(R.id.privacy);
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceIntent=new Intent(DisabledRegisterActivity.this,ServiceActivity.class);
                DisabledRegisterActivity.this.startActivity(serviceIntent);
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent privacyIntent=new Intent(DisabledRegisterActivity.this,PrivacyActivity.class);
                DisabledRegisterActivity.this.startActivity(privacyIntent);
            }
        });

        final Button validateBtn=(Button) findViewById(R.id.validateBtn);
        validateBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                final String userID=idText.getText().toString();
                if(validate)
                {
                    return;
                }
                if(userID.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(DisabledRegisterActivity.this);
                    dialog=builder.setMessage("아이디가 입력되지 않았습니다")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener=new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                if(!Pattern.matches("^(?=.*[a-zA-Z]).{6,20}$", userID))
                                {
                                    Toast.makeText(DisabledRegisterActivity.this,"아이디 형식을 지켜주세요.",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    return;
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(DisabledRegisterActivity.this);
                                dialog=builder.setMessage("사용할 수 있는 아이디입니다")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false);
                                validate=true;
                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                validateBtn.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DisabledRegisterActivity.this);
                                dialog= builder.setMessage("사용할 수 없는 아이디입니다..")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog .show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                };
                DisabledValidateRequest disabledvalidateRequest = new DisabledValidateRequest(userID, responseListener);
                RequestQueue queue= Volley.newRequestQueue(DisabledRegisterActivity.this);
                queue.add(disabledvalidateRequest);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();
                final String userName = nameText.getText().toString();
                final String userPhone = phoneText.getText().toString();
                final String userPhoto = "none";
                final String userToken = FirebaseInstanceId.getInstance().getToken();
                if(!validate){
                    AlertDialog.Builder builder=new AlertDialog.Builder(DisabledRegisterActivity.this);
                    dialog=builder.setMessage("아이디 중복 검사를 해주세요")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                if (checkPrivacy.isChecked() == false || checkService.isChecked() == false || userID.equals("") || userPassword.equals("") || userName.equals("") || userPhone.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder(DisabledRegisterActivity.this);
                    dialog=builder.setMessage("약관에 동의를 하지 않았거나 입력되지 않은 내용이 있습니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                //핸드폰번호 유효성
                if(!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", userPhone))
                {
                    Toast.makeText(DisabledRegisterActivity.this,"올바른 핸드폰 번호가 아닙니다.",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }

                //비밀번호 유효성
                if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", userPassword))
                {
                    Toast.makeText(DisabledRegisterActivity.this,"비밀번호 형식을 지켜주세요.",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DisabledRegisterActivity.this);
                                    builder.setMessage("회원 등록에 성공했습니다.")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(DisabledRegisterActivity.this, DisabledLoginActivity.class);
                                                    DisabledRegisterActivity.this.startActivity(intent);
                                                }
                                            })
                                            .create()
                                            .show();

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DisabledRegisterActivity.this);
                                    builder.setMessage("회원 등록에 실패했습니다.")
                                            .setNegativeButton("다시 시도", null)
                                            .create()
                                            .show();
                                }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                DisabledRegisterRequest disabledregisterRequest = new DisabledRegisterRequest(userID,userPassword, userName, userPhone, userPhoto, userToken, responseListener);
                RequestQueue disabledqueue= Volley.newRequestQueue(DisabledRegisterActivity.this);
                disabledqueue.add(disabledregisterRequest);
            }
        });
    }
//    @Override
//    protected void onStop(){
//        super.onStop();
//        if(dialog !=null);
//        {
//            dialog.dismiss();
//            dialog=null;
//        }
//    }
}
