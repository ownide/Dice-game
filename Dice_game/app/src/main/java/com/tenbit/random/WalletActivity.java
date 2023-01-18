package com.tenbit.random;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.android.volley.Response;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.android.volley.AuthFailureError;
import java.util.HashMap;
import java.util.Map;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.widget.ImageView;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;
import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.reward.RewardItem;
public class WalletActivity extends Activity { 
Button withdraw_button;
EditText withd_edittext;
	ImageView history,back_arrow;
	String user_point,name,email,uid;
	String invite_user_uid;
	AdRequest adIRequest;
	private InterstitialAd interstitial;
	Dialog p_dialog;
	String coinbase_email,status,point;
	String store_link;
	FirebaseAuth auth;
	RewardedVideoAd reward_ad;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);
		auth = FirebaseAuth.getInstance();	
		withdraw_button=findViewById(R.id.with_button);
		withd_edittext=findViewById(R.id.with_edit);
		back_arrow=findViewById(R.id.arrow);
		history=findViewById(R.id.history);
		p_dialog = new Dialog(WalletActivity.this);
        p_dialog.setContentView(R.layout.progress_dialog);
        p_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        p_dialog.setCancelable(false);
		MobileAds.initialize (getApplicationContext());
		interstitial = new InterstitialAd(getApplicationContext());
		interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
		
		adIRequest = new AdRequest.Builder().build();	
		reward_ad=MobileAds.getRewardedVideoAdInstance(this);
		inad();
		back_arrow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					finish();
				}
			});	
		history.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1){
					AlertDialog.Builder a_bulder = new AlertDialog.Builder(WalletActivity.this);
					if(status.equals("null") || coinbase_email.equals("null") || point.equals("null")){
						a_bulder.setMessage("payment not yet");

					} else{
						
						String status_msg;
						if(status.equals("0")){
							
							status_msg="waiting";
							
						} else if(status.equals("-1")){
							
							status_msg="rejected";
						} else{
							
							status_msg="send";
						}

						a_bulder.setMessage("email:"+coinbase_email+"\n"+"point:"+point+"\n"+"status:"+status_msg);
					}
					a_bulder.setTitle("Withdraw History");
					a_bulder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});

					a_bulder.create();
					a_bulder.show();
				}


			});
		withdraw_button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1) {								 
	 withdraw_checker(withd_edittext.getText().toString(),Integer.parseInt(user_point)); 
	 
	 }		           
          });

    }
	public void withdraw_checker(final String enter_email ,final int points) {
		
		
		if(!networkConected()){
			network_dialog();
			
		}else{
			
			if(enter_email.trim().equals("")){
				Toast.makeText(getApplicationContext(),"enter your coinbase email",Toast.LENGTH_SHORT).show();

			}  else{
				AlertDialog.Builder alart_Dialog;
				alart_Dialog = new AlertDialog.Builder(WalletActivity.this);
				alart_Dialog.setCancelable(false);
				alart_Dialog.setTitle("Withdraw");
				alart_Dialog.setMessage("Are you sure to withdraw " + points + " POINTS TO \n" + enter_email+ "?" + "\n\nRemember\n - Points ITS DIFFERENT TO SATOSHI ");
				alart_Dialog.setPositiveButton("Withdraw", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (Integer.parseInt(user_point) < 2500) {
								dialog.dismiss();
								AlertDialog.Builder a_bulder = new AlertDialog.Builder(WalletActivity.this);

								a_bulder.setMessage("It will take a minimum of 2500 coins to withdraw");
								a_bulder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
										}
									});

								a_bulder.create();
								a_bulder.show();
							} else if(status.equals("0")){

								dialog.dismiss();
								AlertDialog.Builder a_bulder = new AlertDialog.Builder(WalletActivity.this);
								a_bulder.setMessage("If you have already made a payment request, please wait until it is completed");
								a_bulder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
										}
									});

								a_bulder.create();
								a_bulder.show();	

							}else {
								p_dialog.show();

								withdraw_logic(points,enter_email);
							}

						}
					});
				alart_Dialog.setNeutralButton("close", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
				alart_Dialog.create();
				alart_Dialog.show();


			}
			
			
		}
		
    }
	
	public void withdraw_logic(int points,String enter_email) {
		
		
		
		//1. invite user exist korle invite user er kave comission send kora
		//2. number ta normaly
		
		
				
							user_point="0";
							point_update_request(points,enter_email);
											

}
		
	public void get_user_info(final int editText_command) {
		String uri = "https://10xbit.xyz/user_data.php";
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
							 name = rcv.getString("name").trim();
							email = rcv.getString("email").trim();
							uid = rcv.getString("uid").trim();		
							user_point = rcv.getString("user_point").trim();
							invite_user_uid=rcv.getString("invite_user_uid");
							if(editText_command==1){

								withd_edittext.setText(email);
								p_dialog.dismiss();
																							

						}}

					} else if(success.equals("0")) {
						p_dialog.dismiss();
					
						Toast.makeText(WalletActivity.this, message, Toast.LENGTH_SHORT).show();
					} else{
						p_dialog.dismiss();
				
						Toast.makeText(WalletActivity.this, "error 4007", Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();
					p_dialog.dismiss();
				Toast.makeText(WalletActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

				}}
		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				p_dialog.dismiss();

				Toast.makeText(WalletActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.POST, uri, resListener, errListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> pram = new HashMap<>();
				pram.put("uid",auth.getUid());
				return pram;  

			}};
		RequestQueue req = Volley.newRequestQueue(WalletActivity.this);
		req.add(stringrequest);
}
	public void point_update_request(final int withdraw_point,final String enter_email){
		String uri = "https://10xbit.xyz/newpoint_add.php";
		Response.Listener resListener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject gso=new JSONObject(response);
					String success = gso.getString("success");
					String message = gso.getString("message");
					if (success.equals("1")) {								
							get_user_info(0);	
							
							if(invite_user_uid.trim().equals("")){
								
								
								withdraw_request(withdraw_point,enter_email);
								
							} else{
								
								
								
								invite_user_Balance_get(withdraw_point,enter_email);
								
							}
							
						
					} else if(success.equals("0")) {
						p_dialog.dismiss();
						Toast.makeText(WalletActivity.this, message, Toast.LENGTH_SHORT).show();

					} else{

						Toast.makeText(WalletActivity.this, "error 4007", Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();
					p_dialog.dismiss();

						Toast.makeText(WalletActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

				}
			}
		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				p_dialog.dismiss();

				Toast.makeText(WalletActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();			
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.POST, uri, resListener, errListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> pram = new HashMap<String, String>();
				pram.put("uid", auth.getUid());
				pram.put("user_point", "0");
				return pram;  

			}};
		RequestQueue req = Volley.newRequestQueue(WalletActivity.this);
		req.add(stringrequest);

		}
		
	public void withdraw_request(final int withdraw_point,final String enter_email) {	
		play_store_link();	
		Date date = new Date();
		long time_date = date.getTime();
		SimpleDateFormat time_date_Format = new SimpleDateFormat("dd MMM yyyy hh:mm a");
		final String withdraw_time = time_date_Format.format(new Date(time_date));
		String uri = "https://10xbit.xyz/withdraw_request.php";
		Response.Listener resListener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject gso=new JSONObject(response);
					String success = gso.getString("success");
					String message = gso.getString("message");
					if (success.equals("1")) {			
						
						withdraw_history();
						Rewad_ad();
					} else if(success.equals("0")) {
						p_dialog.dismiss();
						Toast.makeText(WalletActivity.this, message, Toast.LENGTH_SHORT).show();
						
					} else{
						
						p_dialog.dismiss();
						Toast.makeText(WalletActivity.this, "error 4007", Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();
					
					p_dialog.dismiss();

					Toast.makeText(WalletActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
				}

			}
		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				Toast.makeText(WalletActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.POST, uri, resListener, errListener){

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> pram = new HashMap<String, String>();
				pram.put("name", name);
				pram.put("user_email", email);
				pram.put("uid", uid);
				pram.put("withdraw_point", Integer.toString(withdraw_point));
				pram.put("date_time", withdraw_time);
				pram.put("status","0");
				pram.put("coinbase_email", enter_email);
				return pram;  

			}};
		RequestQueue req = Volley.newRequestQueue(WalletActivity.this);
		req.add(stringrequest);
	}
	public void on_withdraw_req_complete() {
        AlertDialog.Builder alart_Dialog;
        alart_Dialog = new AlertDialog.Builder(WalletActivity.this);
        alart_Dialog.setCancelable(false);
		alart_Dialog.setNegativeButton("Later",new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {			  
					dialog.dismiss();

				}
			});
        alart_Dialog.setMessage("Request sent.\n It may take 1 minute to 3 hours to send your coinbase. ");
        alart_Dialog.setPositiveButton("Rate Us", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {			  
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(store_link));
					startActivity(i);

				}
			});

        alart_Dialog.create();
        alart_Dialog.show();
    }
	public void withdraw_history() {
		String uri = "https://10xbit.xyz/withdraw_history.php";
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
							coinbase_email = rcv.getString("coinbase_email").trim();
							point = rcv.getString("withdraw_point").trim();
							status = rcv.getString("status").trim();

						}

					} else if(success.equals("0")) {

						Toast.makeText(WalletActivity.this, message, Toast.LENGTH_SHORT).show();
					} else{

						Toast.makeText(WalletActivity.this, "error 4007", Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();

					Toast.makeText(WalletActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
					
				}}
		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				Toast.makeText(WalletActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.POST, uri, resListener, errListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> pram = new HashMap<>();
				pram.put("uid",auth.getUid());
				return pram;  

			}};
		RequestQueue req = Volley.newRequestQueue(WalletActivity.this);
		req.add(stringrequest);		
	} 
	public void play_store_link() {
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
							store_link = rcv.getString("store_link").trim();																																		

						}
					} else if(success.equals("0")) {
						store_link="null";

						Toast.makeText(WalletActivity.this, message, Toast.LENGTH_SHORT).show();
					} else{

						Toast.makeText(WalletActivity.this, "error 4007", Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();

					Toast.makeText(WalletActivity.this, e.toString(), Toast.LENGTH_SHORT).show();			

				}}

		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				Toast.makeText(WalletActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.GET, uri, resListener, errListener);
		RequestQueue req = Volley.newRequestQueue(WalletActivity.this);
		req.add(stringrequest);
	}
	
	public void inad(){

		AdListener ad_listener=new AdListener(){					
			public void onAdLoaded() {  

				interstitial.show();

			}  

			public void onAdFailedToLoad(){
				interstitial.loadAd(adIRequest);

			}
			public void onAdOpened(){

			}
			public void onAdClicked(){
			}
			public void onAdClosed(){

			}

			public void onAdImpression(){

			}					

		};

		interstitial.setAdListener(ad_listener);  
		interstitial.loadAd(adIRequest);
		

}
	public void network_dialog() {

        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(WalletActivity.this);

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

					if (!networkConected()) {

						network_dialog();
						
						}

					

				}
			});

        alert.show();


    }

    public boolean networkConected() {

        ConnectivityManager connectivityManager = (ConnectivityManager)WalletActivity.this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo_wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfo_data = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (networkInfo_wifi != null && networkInfo_wifi.isConnected() || networkInfo_data != null && networkInfo_data.isConnected()) {

            return true;

        } else {

            return false;
        }}
		


	@Override
	protected void onResume() {
		
		super.onResume();
		
		if(!networkConected()){
			
			network_dialog();
		} else{
			
			p_dialog.show();	
			get_user_info(1);
			withdraw_history();
		}
	}

	public void Rewad_ad(){
		reward_ad.setRewardedVideoAdListener(new RewardedVideoAdListener() {
				@Override
				public void onRewardedVideoAdClosed () {
					
					on_withdraw_req_complete();
					
				}
				@Override
				public void onRewardedVideoAdOpened() {
				}
				@Override
				public void onRewardedVideoStarted() {

				}
				@Override
				public void onRewardedVideoCompleted() {	

				}

				@Override
				public void onRewardedVideoAdLeftApplication() {

				}
				@Override
				public void onRewarded(RewardItem reward) {
					reward.getAmount();			
				}
				@Override
				public void onRewardedVideoAdFailedToLoad(int errorCode) {
					Rewad_ad();	

				}
				@Override
				public void onRewardedVideoAdLoaded() {
					p_dialog.dismiss();
					reward_ad.show();

				}
			}
		);
		reward_ad.loadAd(getString(R.string.reward_id), new AdRequest.Builder().build());

	}
	
public void invite_user_Balance_get(final int withdraw_point,final String enter_email){
	
	String uri = "https://10xbit.xyz/user_data.php";

//testt4
	
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
						String invite_user_point_ = rcv.getString("user_point").trim();
						//** refer point na ,amra point get kore update kore dibo
						invite_user_referBalance_update(invite_user_point_,withdraw_point,enter_email);
					
}
				} else if(success.equals("0")) {

					Toast.makeText(WalletActivity.this, message, Toast.LENGTH_SHORT).show();
				} else{

					Toast.makeText(WalletActivity.this, "error 4007", Toast.LENGTH_SHORT).show();

				}
			} catch (JSONException e) {
				e.printStackTrace();

				Toast.makeText(WalletActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

			}}
	};
	Response.ErrorListener errListener = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {

			Toast.makeText(WalletActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();
		}
	};
	StringRequest  stringrequest= new StringRequest(Request.Method.POST, uri, resListener, errListener){
		@Override
		protected Map<String, String> getParams() throws AuthFailureError {
			Map<String, String> pram = new HashMap<>();
			pram.put("uid",invite_user_uid);
			
			
			
			return pram;  

		}};
	RequestQueue req = Volley.newRequestQueue(WalletActivity.this);
	req.add(stringrequest);		
	
	
	
	
}
	public void invite_user_referBalance_update(final String invite_user_point, final int withdraw_point, final String enter_email){
	
		
		
		
		String uri = "https://10xbit.xyz/newpoint_add.php";
		Response.Listener resListener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject gso=new JSONObject(response);
					String success = gso.getString("success");
					String message = gso.getString("message");
					
					if (success.equals("1")) {
						
						
						withdraw_request(withdraw_point,enter_email);
						
					} else if(success.equals("0")) {

						Toast.makeText(WalletActivity.this, message, Toast.LENGTH_SHORT).show();
					} else{

						Toast.makeText(WalletActivity.this, "error 4007", Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();

					Toast.makeText(WalletActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

				}}
		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				Toast.makeText(WalletActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.POST, uri, resListener, errListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				
				int invite_user_b;
			
					
					invite_user_b=Integer.parseInt(invite_user_point);
					
					
					
				Map<String, String> pram = new HashMap<>();
				pram.put("uid",invite_user_uid);
				pram.put("user_point",Integer.toString(invite_user_b+(withdraw_point*10/100)));
				return pram;  

			}};
		RequestQueue req = Volley.newRequestQueue(WalletActivity.this);
		req.add(stringrequest);		
		
		
		
		
		
		
	}

	
}



