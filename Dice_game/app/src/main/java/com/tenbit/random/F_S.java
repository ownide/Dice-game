package com.tenbit.random;

import android.app.Activity;
import android.os.Bundle;
import android.animation.ValueAnimator;
import android.os.CountDownTimer;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.view.View;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.android.volley.Response;
import org.json.JSONObject;
import org.json.JSONException;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import java.util.Map;
import java.util.HashMap;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import android.net.Uri;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

public class F_S extends Activity {
	ValueAnimator animator ;
	ImageView dice;
	String version,
	update_message,
	store_link,
	dialog_code,
	exit_message;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		setContentView(R.layout.f_s);
		dice=findViewById(R.id.dice);
		if(!networkConected()){
			network_dialog();
		} else{

			anim();

		}
	

    }
	
    public void network_dialog() {
			android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(F_S.this);

			alert.setCancelable(false);

			alert.setTitle("Disconnected");

			alert.setMessage("Please chack your internet connection");

			alert.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				});

			alert.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(!networkConected()){

							network_dialog();

						} else{
							anim();


						}
							
					}
				});

			alert.show();
			
	
}
        

    public boolean networkConected() {


        ConnectivityManager connectivityManager = (ConnectivityManager) F_S.this.getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo_wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfo_data = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (networkInfo_wifi != null && networkInfo_wifi.isConnected() || networkInfo_data != null && networkInfo_data.isConnected()) {

            return true;

        } else {

            return false;
        }


    }

   
	private int get_current_v_code() {
    PackageInfo    packageInfo = null;

        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionCode;
}

	public void appInfo_fromDatabase() {

		String uri = "https://10xbit.xyz/info.php";
		Response.Listener resListener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {


				try {


					JSONObject gso=new JSONObject(response);
					String success = gso.getString("success");


					String message = gso.getString("message");
					JSONArray jsonArray = gso.getJSONArray("data");
					if (success.equals("1")) {

						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject rcv = jsonArray.getJSONObject(i);
							version = rcv.getString("version").trim();
						 update_message = rcv.getString("message").trim();
							 store_link = rcv.getString("store_link").trim();
							 dialog_code = rcv.getString("dialog_code").trim();
							 exit_message = rcv.getString("exit_message").trim();
							
							if(get_current_v_code()==Integer.parseInt(version)){
								if(Integer.parseInt(dialog_code)==0){
									FirebaseAuth mAuth;
									mAuth=FirebaseAuth.getInstance();
									FirebaseUser currentUser = mAuth.getCurrentUser();

									if (currentUser!= null) {

										Intent intent = new Intent(F_S.this, MainActivity.class);

										startActivity(intent);
										finishAffinity();
									} else{
										
										Intent intent=new Intent(F_S.this,login.class);
										startActivity(intent);
										finish();
										
									}
									

								} else{

									exit_dialog();			
								}
							} else{

								update_dialog();
							}		
						}

					} else if(success.equals("0")) {
				
						Toast.makeText(F_S.this, message, Toast.LENGTH_SHORT).show();
					} else{
						
						Toast.makeText(F_S.this, "error 4007", Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();

					Toast.makeText(F_S.this, e.toString(), Toast.LENGTH_SHORT).show();			

				}}

		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				Toast.makeText(F_S.this, error.toString().toString(), Toast.LENGTH_SHORT).show();
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.GET, uri, resListener, errListener);
		RequestQueue req = Volley.newRequestQueue(F_S.this);
		req.add(stringrequest);
		
}

public void exit_dialog(){
	
	android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(F_S.this);
	alert.setCancelable(false);
	alert.setTitle("Alert!");
	alert.setMessage(exit_message);
	alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();

			}
		});

	alert.show();
	
}
	public void update_dialog(){
		
		animator.pause();
		animator.end();
		dice.clearAnimation();
		android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(F_S.this);
		alert.setCancelable(false);
		alert.setTitle("update available");
		alert.setMessage(update_message);
		alert.setPositiveButton("update", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(store_link));
					startActivity(i);
					finish();
				}
			});

		alert.show();
	}

	

	@Override
	protected void onRestart() {
		super.onRestart();
		
		if(!networkConected()){


			network_dialog();
		} else{

			anim();

		}
		
	}
	public void anim(){
		
		int[] dice_image2 = new int[]{R.drawable.ic_dice__1_
			, R.drawable.ic_dice__2_
			, R.drawable.ic_dice__3_
			, R.drawable.ic_dice__4_
			, R.drawable.ic_dice__5_
			, R.drawable.ic_dice__6_
			, R.drawable.ic_dice__2_
			, R.drawable.ic_dice__3_
			, R.drawable.ic_dice__4_
			, R.drawable.ic_dice__5_
			, R.drawable.ic_dice__6_
			, R.drawable.ic_dice__2_
			, R.drawable.ic_dice__3_
			, R.drawable.ic_dice__4_
			, R.drawable.ic_dice__5_
			, R.drawable.ic_dice__6_
			, R.drawable.ic_dice__2_
			, R.drawable.ic_dice__3_
			, R.drawable.ic_dice__4_
			, R.drawable.ic_dice__5_
			, R.drawable.ic_dice__6_
			, R.drawable.ic_dice__2_
			, R.drawable.ic_dice__3_
			, R.drawable.ic_dice__4_
			, R.drawable.ic_dice__5_
			, R.drawable.ic_dice__6_

		};
		animator = ValueAnimator.ofInt(dice_image2);
		
		
		CountDownTimer countDownTimer = new CountDownTimer(1600,300) {
			public void onTick(long millisUntilFinished) {
	
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				public void onAnimationUpdate(ValueAnimator animation) {
					dice.setImageResource((Integer) animation.getAnimatedValue());
				}});
		
		animator.start();
		
		}
			public void onFinish()
			{
				if(!networkConected()){
					
					network_dialog();
				}else{
					appInfo_fromDatabase();
					
				}
		
				
				}};
		countDownTimer.start();
	}

	@Override
	public void onBackPressed() {
		
	}
	
		
}

// develop by mahamudul hasan

