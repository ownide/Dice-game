package com.tenbit.random;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.view.View;
import com.unity3d.ads.UnityAds;
import android.widget.Toast;
import com.unity3d.ads.IUnityAdsListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdListener;
import com.android.volley.Response;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import java.util.Map;
import com.android.volley.AuthFailureError;
import java.util.HashMap;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.DialogInterface;
import android.app.AlertDialog;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.reward.RewardItem;
import android.widget.ImageView;
public class ReferActivity extends Activity { 
	
	EditText enter_refer_codeEditText;
	TextView alredy_enter_Textview,refer_codeTextView;
	private Button enter_button,share_button;
	ImageView back_arrow;
	AdRequest adIRequest;
	private InterstitialAd interstitial;
	Dialog p_dialog;
	   
	   int Exist_or_not;
	  AlertDialog.Builder dialog;
	  FirebaseAuth auth;
      String codeuse_ornot,refer_code;
		private String unityGameID = "4087375";
		private Boolean testMode = true;
		private String InterID = "Interstitial_Android";
		//private String RewardID = "Rewarded_Android";
		
		String store_link;
	RewardedVideoAd reward_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reffer);
		
		auth=FirebaseAuth.getInstance();
		
		MobileAds.initialize (getApplicationContext());
		interstitial = new InterstitialAd(getApplicationContext());
		interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
		adIRequest = new AdRequest.Builder().build();
		reward_ad=MobileAds.getRewardedVideoAdInstance(this);
        
  
			inad();
		
		enter_button=findViewById(R.id.referCode_verifyButton);
		enter_refer_codeEditText=findViewById(R.id.edittext_referCode);
		alredy_enter_Textview=findViewById(R.id.alredy_enter_texView);
		refer_codeTextView=findViewById(R.id.refer_code_textView);
		share_button=findViewById(R.id.share_button);
		back_arrow=findViewById(R.id.arrow_refer);
		
		p_dialog = new Dialog(ReferActivity.this);
        p_dialog.setContentView(R.layout.progress_dialog);
        p_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        p_dialog.setCancelable(false);
		
		dialog = new AlertDialog.Builder(ReferActivity.this);
        dialog.setCancelable(false);
        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					enter_refer_codeEditText.setText("");
				}
			});
        dialog.create();
		
	back_arrow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});	
		share_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/plain");
					String body = "Download Bit Btc from play store and earn free bitcoin. My Referral code is : " + refer_code + " " + store_link;
					intent.putExtra(Intent.EXTRA_TEXT, body);
					startActivity(Intent.createChooser(intent, "Share"));
				}
			});	
		enter_button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1) {			
					if(!networkConected()){
						network_dialog();
						enter_refer_codeEditText.clearFocus();
					}else{
						p_dialog.show();
						if(enter_refer_codeEditText.getText().toString().trim().equals("")){
							p_dialog.dismiss();
							dialog.setMessage("Referal code must be not null");
							dialog.show();

						}else if(refer_code.equals(enter_refer_codeEditText.getText().toString().trim())){
							p_dialog.dismiss();
                            dialog.setMessage("Can't use your referral code");
                            dialog.show();							

						}
						else{
							
							refer_code_existing_checker(enter_refer_codeEditText.getText().toString().trim());
						}
											
					}
															               
                }
            });
				
    }
	public void DisplayInterstitialAd() {
        if (UnityAds.isReady(InterID)) {
            UnityAds.show(this, InterID);
        }else{

            Toast.makeText(getApplicationContext(),"ads not loaded",Toast.LENGTH_SHORT).show();
        }
    }
	private void LoadInterstitialAds() {
        IUnityAdsListener InterListener = new IUnityAdsListener() {

            public void onUnityAdsReady(String adUnitId) {        
            
            }

            @Override
            public void onUnityAdsStart(String adUnitId) {
            
            }

            @Override
            public void onUnityAdsFinish(String adUnitId, UnityAds.FinishState finishState) {            
                LoadInterstitialAds();

            }

            @Override
            public void onUnityAdsError(UnityAds.UnityAdsError error, String message) {      
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                LoadInterstitialAds();

            }
        };

        UnityAds.setListener(InterListener);
        UnityAds.load(InterID);
    }
	
	
	
	public void get_user_info() {

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
					p_dialog.dismiss();
						play_store_link();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject rcv = jsonArray.getJSONObject(i);
							codeuse_ornot = rcv.getString("codeuse_ornot").trim();
							refer_code = rcv.getString("refer_code").trim();						
							if(refer_code.equals("")){							
								refer_codeTextView.setText("null");
								
							}
							else{
								refer_codeTextView.setText(refer_code);
								
								
							}
							
							refer_codeTextView.setText(refer_code);
							

							if(codeuse_ornot.equals("1")){

								enter_button.setVisibility(View.VISIBLE);
								enter_refer_codeEditText.setVisibility(View.VISIBLE);
								alredy_enter_Textview.setVisibility(View.GONE);
							}else{

								enter_button.setVisibility(View.GONE);
								enter_refer_codeEditText.setVisibility(View.GONE);
								alredy_enter_Textview.setVisibility(View.VISIBLE);

							}


						}

					} else if(success.equals("0")) {
						p_dialog.dismiss();

						Toast.makeText(ReferActivity.this, message, Toast.LENGTH_SHORT).show();
					} else{

						Toast.makeText(ReferActivity.this, "error 4007", Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();
					p_dialog.dismiss();
					Toast.makeText(ReferActivity.this, e.toString(),Toast.LENGTH_SHORT).show();
					

				}}



		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				p_dialog.dismiss();
				Toast.makeText(ReferActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();		
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.POST, uri, resListener, errListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> pram = new HashMap<>();

				pram.put("uid",auth.getUid());
				return pram;  

			}};

		RequestQueue req = Volley.newRequestQueue(ReferActivity.this);
		req.add(stringrequest);
	
	}

	public void refer_code_existing_checker(final String your_enterReferCode) {
		String uri = "https://10xbit.xyz/search_refercode_existence.php";
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
							String refer_count = rcv.getString("refer_count").trim();
							String uid = rcv.getString("uid").trim();															
							inviteUuser_profileUpdate(uid,refer_count);
						}

					} else if(success.equals("0")) {		
					Rewad_ad(success,message);
					
						
						
						
						
						
						
						
					} else{
					
						Toast.makeText(ReferActivity.this, "error 4007", Toast.LENGTH_SHORT).show();
						p_dialog.dismiss();
						

					}
				} catch (JSONException e) {
					e.printStackTrace();

					Toast.makeText(ReferActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
					
					p_dialog.dismiss();
					
				}
}

			
		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				Toast.makeText(ReferActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();			
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.POST, uri, resListener, errListener){



			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> pram = new HashMap<String, String>();
				pram.put("refer_code", your_enterReferCode);
				return pram;  

			}};
		RequestQueue req = Volley.newRequestQueue(ReferActivity.this);
		req.add(stringrequest);

}
	
	public void inviteUuser_profileUpdate(final String uid, final String refer_count){

		String uri = "https://10xbit.xyz/invite_user_refer_count_update.php";
		Response.Listener resListener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject gso=new JSONObject(response);
					String success = gso.getString("success");
					String message = gso.getString("message");

					if (success.equals("1")) {
							Toast.makeText(ReferActivity.this, message, Toast.LENGTH_SHORT).show();

						currentUser_profileUpdate(uid);
					} else if(success.equals("0")) {				
					
						Toast.makeText(ReferActivity.this, message, Toast.LENGTH_SHORT).show();
						
						p_dialog.dismiss();

					} else{

						Toast.makeText(ReferActivity.this, "error 4007", Toast.LENGTH_SHORT).show();
						p_dialog.dismiss();

					}
				} catch (JSONException e) {
					e.printStackTrace();

					Toast.makeText(ReferActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
					
					p_dialog.dismiss();
				}

			}
		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				Toast.makeText(ReferActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();
			
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.POST, uri, resListener, errListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> pram = new HashMap<String, String>();
				pram.put("uid", uid);
				pram.put("refer_count", Integer.toString(Integer.parseInt(refer_count)+1));
				return pram;  

			}};
		RequestQueue req = Volley.newRequestQueue(ReferActivity.this);
		req.add(stringrequest);
		}
		
	public void currentUser_profileUpdate(final String inviteUser_uid){

		String uri = "https://10xbit.xyz/after_enter_refer_code_update_currentuser.php";
		Response.Listener resListener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject gso=new JSONObject(response);
					String success = gso.getString("success");
					String message = gso.getString("message");

					if (success.equals("1")) {
											
						
						
						Rewad_ad(success,message);

                        
                        												
					
					} else if(success.equals("0")) {					
						Toast.makeText(ReferActivity.this, message, Toast.LENGTH_SHORT).show();
						p_dialog.dismiss();

					} else{

						Toast.makeText(ReferActivity.this, "error 4007", Toast.LENGTH_SHORT).show();
						
						p_dialog.dismiss();
					}
				} catch (JSONException e) {
					e.printStackTrace();

					Toast.makeText(ReferActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

				}

			}
		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(ReferActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();
				
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.POST, uri, resListener, errListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> pram = new HashMap<String, String>();
				pram.put("uid", auth.getUid());		
				pram.put("codeuse_ornot", "0");
				pram.put("invite_user_uid", inviteUser_uid);

				return pram;  

			}};
		RequestQueue req = Volley.newRequestQueue(ReferActivity.this);
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

						Toast.makeText(ReferActivity.this, message, Toast.LENGTH_SHORT).show();
					} else{

						Toast.makeText(ReferActivity.this, "error 4007", Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();

					Toast.makeText(ReferActivity.this, e.toString(), Toast.LENGTH_SHORT).show();			

				}}

		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				Toast.makeText(ReferActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.GET, uri, resListener, errListener);
		RequestQueue req = Volley.newRequestQueue(ReferActivity.this);
		req.add(stringrequest);
		}
		
	public void network_dialog() {

        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ReferActivity.this);

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

        ConnectivityManager connectivityManager = (ConnectivityManager)ReferActivity.this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo_wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfo_data = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (networkInfo_wifi != null && networkInfo_wifi.isConnected() || networkInfo_data != null && networkInfo_data.isConnected()) {

            return true;

        } else {

            return false;
        }

		}

	@Override
	protected void onResume() {
		
	
		super.onResume();
	
		if(!networkConected()){
			
			network_dialog();
		} else{
			p_dialog.show();
			
			get_user_info();
			
			
		}
		

	}
	public void Rewad_ad(final String success,final String message){




		reward_ad.setRewardedVideoAdListener(new RewardedVideoAdListener() {
				@Override
				public void onRewardedVideoAdClosed () {



				}
				@Override
				public void onRewardedVideoAdOpened() {
				}
				@Override
				public void onRewardedVideoStarted() {

				}
				@Override
				public void onRewardedVideoCompleted() {
					
					if(success.equals("0")){
						
						dialog.setMessage(message);
						dialog.show();
						
					} else if(success.equals("1")){
						enter_button.setVisibility(View.GONE);
						enter_refer_codeEditText.setVisibility(View.GONE);
						alredy_enter_Textview.setVisibility(View.VISIBLE);
                        
						
						dialog.setMessage(message);
						dialog.show();
					}
					

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
					Rewad_ad(success,message);
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
	

	
	
	
} 

//develop by mahamudul hasan
