package com.tenbit.random;

import android.app.*;
import android.os.*;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.view.animation.Animation;
import android.content.SharedPreferences;
import java.util.Random;
import android.view.animation.AnimationUtils;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Context;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.AuthFailureError;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONArray;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;
import android.widget.LinearLayout;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdListener;
import android.view.animation.AlphaAnimation;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.reward.RewardItem;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.graphics.Color;
import android.text.Html;



public class MainActivity extends Activity 
{
	
AdRequest adIRequest;
ImageView wallet_img, refarel_image,info_img;
 Button under_button,over_button,d2gretd1,d1gretd2,bounas_button,bounas_button2;
 TextView point_textView,number_picker_textView;
 ImageView dice_one, dice_two;

    CountDownTimer countDownTimer;

   
    int x_picker;
	int random_number_picker;
    int dice_select_number;
    int old_update_points; 
    int random_2;
    int random_1;
	
	
	int last_withdraw_position;
	int newpoint = 0;
	int click_number_count=0;
	String user_point ;
	String work ;
	

    
   // int new_update_points;

    

	

//int last_roll_point;

	//  Adaapter adapter;



	FirebaseAuth auth;


  //  Random random = new Random();

    


	SharedPreferences sharedpreferences;
	int tenx_number_picker;
	RewardedVideoAd reward_ad;
	private InterstitialAd interstitial;
	Animation a;
	Animation anim;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


		wallet_img=findViewById(R.id.wallet);
		refarel_image=findViewById(R.id.ref);
		info_img=findViewById(R.id.qn);
		point_textView = findViewById(R.id.point_textView);
        under_button = findViewById(R.id.under_button);

		over_button=findViewById(R.id.over_button_);
		d1gretd2=findViewById(R.id.trd_button);
		d2gretd1=findViewById(R.id.frth_button);
        dice_one = findViewById(R.id.dice_one);
        dice_two = findViewById(R.id.dice_two);
		bounas_button=findViewById(R.id.bounas_button);
		bounas_button2=findViewById(R.id.bounas_button2);

		number_picker_textView=findViewById(R.id.number_picked);
	
		auth = FirebaseAuth.getInstance();
		MobileAds.initialize (getApplicationContext());
		interstitial = new InterstitialAd(getApplicationContext());
		interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
		adIRequest = new AdRequest.Builder().build();
		reward_ad=MobileAds.getRewardedVideoAdInstance(this);
		
		Rewad_ad();
		sp_load();
		
		a=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.Zoom);
		Animation v_a=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.vibrate_animation);
		info_img.setAnimation(v_a);
		bounas_button_logic();
		info_img.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					dialog();
				}
			});	
		wallet_img.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent iw= new Intent(MainActivity.this, WalletActivity.class);
					startActivity(iw);
				}
			});	

		refarel_image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent ic=new Intent(getApplicationContext(),ReferActivity.class);
					startActivity(ic);

				}
			});	
		d2gretd1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                        roll(4);
                 
				}
			});	

		d1gretd2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                        roll(3);
                   		
				}
			});	
		over_button.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
                        roll(2);
                  
				}
			});	

		under_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {						
                       roll(1);      

				}
			});	
		bounas_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                        roll(5);
                 
				}
			});	

		bounas_button2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                        roll(6);

				}
			});	

    }
	public void roll(int click_number) {

		if (reward_ad.isLoaded()){	
			random_1 = new Random().nextInt(6) + 1;// 0 asle jeno sathe +1 add hoy, karon int sob somoy 0 theke count start kore
			random_2 = new Random().nextInt(6) + 1;	
			final int []dice_image = new int[]{R.drawable.ic_dice__1_
				, R.drawable.ic_dice__2_
				, R.drawable.ic_dice__3_
				, R.drawable.ic_dice__4_
				, R.drawable.ic_dice__5_
				, R.drawable.ic_dice__6_};	
			int[]  dice_image2 = new int[]{R.drawable.ic_dice__1_
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

			roll_command(click_number);

			final ValueAnimator animator = ValueAnimator.ofInt(dice_image2);
			countDownTimer = new CountDownTimer(900, 300) {
				public void onTick(long millisUntilFinished) {
					animator.setDuration(300);
					anim=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
					dice_one.startAnimation(anim);
					dice_two.startAnimation(anim);
					animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
							public void onAnimationUpdate(ValueAnimator animation) {				
								dice_one.setImageResource((Integer) animation.getAnimatedValue());//
								dice_two.setImageResource((Integer) animation.getAnimatedValue());
							}
						});    
					animator.start();	
				}
				public void onFinish()
				{
					animator.cancel();
					dice_one.setImageResource(dice_image[random_1 - 1]); // dice image er position 0 theke 5 but random number update korar por er position 1 theke 6  ,, dice image er position jehutu 0 theke 5 tai random number ja asbe -1 add hobe, 1 asle 1-1=0, r 6 asle 6-1=5
					dice_two.setImageResource(dice_image[random_2 - 1]);		
					user_data(0);
					reward_ad.show();
								
				    bounas_button_logic();
					sp_add(random_1,random_2,click_number_count++);
					
					under_button.setEnabled(true);
					over_button.setEnabled(true);
					d2gretd1.setEnabled(true);
					d1gretd2.setEnabled(true);
					under_button.setEnabled(true);
					over_button.setEnabled(true);
					bounas_button.setBackgroundResource(R.drawable.button_shape);
					bounas_button2.setBackgroundResource(R.drawable.button_shape);
					d2gretd1.setBackgroundResource(R.drawable.button_shape);
					d1gretd2.setBackgroundResource(R.drawable.button_shape);
					under_button.setBackgroundResource(R.drawable.button_shape);
					over_button.setBackgroundResource(R.drawable.button_shape);
				}

			};
			countDownTimer.start();	
			
		}else{
			AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
			alert.setCancelable(false);
			alert.setMessage("Wait a while...");
			alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
			alert.show();
		}
      
	}

	public void animateTextView(int oldsavescore,final int finalnewscore,final TextView textview) {
		if(old_update_points!=newpoint){
			addscore(newpoint);	
			
		}
        ValueAnimator valueAnimator = ValueAnimator.ofInt(oldsavescore, finalnewscore);
        valueAnimator.setDuration(900);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					textview.setText(valueAnimator.getAnimatedValue().toString());
						
				}
			});
        valueAnimator.start();
	
	}
	public void dialog(){
        AlertDialog.Builder alart_Dialog;
        alart_Dialog = new AlertDialog.Builder(MainActivity.this);
        alart_Dialog.setCancelable(false);
        alart_Dialog.setMessage(work);
        alart_Dialog.setNegativeButton("ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();

				}
			});	
		alart_Dialog.create();
        alart_Dialog.show();
}
	public void user_data(final int ponintText_setCommand) {
		
		//data get na howar mul karon chilo php code , post method er moddhe
		//direct email othoba uid ba jenokono kicu dia test kora jay na, er karon post method er moddhe data ta sudu ekta key, veluta apps er vitor theke asbe
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
							String name = rcv.getString("name").trim();
							String email = rcv.getString("email").trim();
							String uid = rcv.getString("uid").trim();
							String refer_code = rcv.getString("refer_code").trim();
							String refer_count = rcv.getString("refer_count").trim();
							 user_point = rcv.getString("user_point").trim();
							String codeuse_ornot = rcv.getString("codeuse_ornot").trim();
							
							if(ponintText_setCommand==1){
							point_textView.setText(user_point);
							Rewad_ad();
								
							}
							}			

				} else if(success.equals("0")) {
					
				} else{
					Toast.makeText(MainActivity.this, "error 4007", Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

}}		
		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(MainActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.POST, uri, resListener, errListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> pram = new HashMap<>();
				pram.put("uid",auth.getUid());
				return pram;  

			}};
		RequestQueue req = Volley.newRequestQueue(MainActivity.this);
		req.add(stringrequest);
		
	}
	
	
	public void addscore(final int newscore){
		String uri = "https://10xbit.xyz/newpoint_add.php";
		Response.Listener resListener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {

					JSONObject gso=new JSONObject(response);
					String success = gso.getString("success");
					String message = gso.getString("message");

					if (success.equals("1")) {
						user_data(0);
					} else if(success.equals("0")) {
						addscore(newscore);
						
					} else{
						
					Toast.makeText(MainActivity.this, "error 4007", Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();

				Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
					
				}
			}
		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(MainActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.POST, uri, resListener, errListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> pram = new HashMap<String, String>();
				pram.put("uid", auth.getUid());
				pram.put("user_point", Integer.toString(newscore));
				return pram;  

			}};
		RequestQueue req = Volley.newRequestQueue(MainActivity.this);
		req.add(stringrequest);

	}
	
	public void roll_command(int click_number){
		if(click_number==1){
	 under_button.setEnabled(false);
	 over_button.setEnabled(false);
	 d2gretd1.setEnabled(false);
	 d1gretd2.setEnabled(false);
	 under_button.setEnabled(false);
	 over_button.setEnabled(false);

			bounas_button.setBackgroundResource(R.drawable.invisible_button_shape);
			bounas_button2.setBackgroundResource(R.drawable.invisible_button_shape);
			d2gretd1.setBackgroundResource(R.drawable.invisible_button_shape);
			d1gretd2.setBackgroundResource(R.drawable.invisible_button_shape);
			under_button.setBackgroundResource(R.drawable.invisible_button_shape);
			over_button.setBackgroundResource(R.drawable.invisible_button_shape);
			if(random_number_picker>random_1+random_2 ||  random_number_picker==random_1+random_2){
				newpoint = 9 + Integer.parseInt(user_point);
				old_update_points=Integer.parseInt(user_point);	
			} else{
				newpoint=0+Integer.parseInt(user_point);
				old_update_points=Integer.parseInt(user_point);
			}
		}
		else if(click_number==2){
			under_button.setEnabled(false);
			over_button.setEnabled(false);
			d2gretd1.setEnabled(false);
			d1gretd2.setEnabled(false);
			under_button.setEnabled(false);
			over_button.setEnabled(false);
			bounas_button.setBackgroundResource(R.drawable.invisible_button_shape);
			bounas_button2.setBackgroundResource(R.drawable.invisible_button_shape);
			d2gretd1.setBackgroundResource(R.drawable.invisible_button_shape);
			d1gretd2.setBackgroundResource(R.drawable.invisible_button_shape);
			under_button.setBackgroundResource(R.drawable.invisible_button_shape);
			over_button.setBackgroundResource(R.drawable.invisible_button_shape);
			if(random_number_picker<random_1+random_2){
				newpoint = 9 + Integer.parseInt(user_point);
				old_update_points=Integer.parseInt(user_point);

			} else{
				newpoint=0+Integer.parseInt(user_point);
				old_update_points=Integer.parseInt(user_point);
			}
		} else if
		(click_number==3){
			under_button.setEnabled(false);
			over_button.setEnabled(false);
			d2gretd1.setEnabled(false);
			d1gretd2.setEnabled(false);
			under_button.setEnabled(false);
			over_button.setEnabled(false);	
			bounas_button.setBackgroundResource(R.drawable.invisible_button_shape);
			bounas_button2.setBackgroundResource(R.drawable.invisible_button_shape);
			d2gretd1.setBackgroundResource(R.drawable.invisible_button_shape);
			d1gretd2.setBackgroundResource(R.drawable.invisible_button_shape);
			under_button.setBackgroundResource(R.drawable.invisible_button_shape);
			over_button.setBackgroundResource(R.drawable.invisible_button_shape);
			if(random_1>random_2){
				newpoint = 9 + Integer.parseInt(user_point);
				old_update_points=Integer.parseInt(user_point);
			} else{
				newpoint=0+Integer.parseInt(user_point);
				old_update_points=Integer.parseInt(user_point);
			}
		} 
		else if (click_number==4){	
			under_button.setEnabled(false);
			over_button.setEnabled(false);
			d2gretd1.setEnabled(false);
			d1gretd2.setEnabled(false);
			under_button.setEnabled(false);
			over_button.setEnabled(false);
			bounas_button.setBackgroundResource(R.drawable.invisible_button_shape);
			bounas_button2.setBackgroundResource(R.drawable.invisible_button_shape);
			d2gretd1.setBackgroundResource(R.drawable.invisible_button_shape);
			d1gretd2.setBackgroundResource(R.drawable.invisible_button_shape);
			under_button.setBackgroundResource(R.drawable.invisible_button_shape);
			over_button.setBackgroundResource(R.drawable.invisible_button_shape);
			if(random_1<random_2){
				newpoint = 9 + Integer.parseInt(user_point);

				old_update_points=Integer.parseInt(user_point);
			} else{
				newpoint=0+Integer.parseInt(user_point);

				old_update_points=Integer.parseInt(user_point);
			}
		} else if(click_number==5){
			bounas_button.clearAnimation();
			bounas_button2.clearAnimation();
			under_button.setEnabled(false);
			over_button.setEnabled(false);	
			under_button.setEnabled(false);
			over_button.setEnabled(false);
			d2gretd1.setBackgroundResource(R.drawable.invisible_button_shape);
			d1gretd2.setBackgroundResource(R.drawable.invisible_button_shape);
			under_button.setBackgroundResource(R.drawable.invisible_button_shape);
			over_button.setBackgroundResource(R.drawable.invisible_button_shape);
			bounas_button.setVisibility(View.GONE);
			bounas_button2.setVisibility(View.GONE);
			click_number_count=0;
			
			if(dice_select_number==1){//random 2 mane dice1 r random 1 mane dice2
				if(random_2==tenx_number_picker){
					newpoint = 9*x_picker + Integer.parseInt(user_point);

					old_update_points=Integer.parseInt(user_point);
				} else{
					newpoint = 0 + Integer.parseInt(user_point);

					old_update_points=Integer.parseInt(user_point);
				}
			} else {
				if(random_1==tenx_number_picker){
					newpoint = 9*x_picker + Integer.parseInt(user_point);

					old_update_points=Integer.parseInt(user_point);
				} else{
					newpoint = 0 + Integer.parseInt(user_point);

					old_update_points=Integer.parseInt(user_point);
				}
			} 
		}else if(click_number==6){
			bounas_button.clearAnimation();
			bounas_button2.clearAnimation();
			under_button.setEnabled(false);
			over_button.setEnabled(false);
			under_button.setEnabled(false);
			over_button.setEnabled(false);
			d2gretd1.setBackgroundResource(R.drawable.invisible_button_shape);
			d1gretd2.setBackgroundResource(R.drawable.invisible_button_shape);
			under_button.setBackgroundResource(R.drawable.invisible_button_shape);
			over_button.setBackgroundResource(R.drawable.invisible_button_shape);
			bounas_button.setVisibility(View.GONE);
			bounas_button2.setVisibility(View.GONE);
			click_number_count=0;
			if(random_1==random_2){
				newpoint = 9*10 + Integer.parseInt(user_point);

				old_update_points=Integer.parseInt(user_point);
				
			}else{
				newpoint = 0 + Integer.parseInt(user_point);

				old_update_points=Integer.parseInt(user_point);
				
			}

		}
		
		
	}
	public void Rewad_ad(){
		reward_ad.setRewardedVideoAdListener(new RewardedVideoAdListener() {
				@Override
				public void onRewardedVideoAdClosed () {
					random_number_picker=new Random().nextInt(8)+2;
					number_picker_textView.setText(random_number_picker+"");

					if(old_update_points!=newpoint){
						animateTextView(old_update_points, newpoint,point_textView);

					}
					
					Rewad_ad();
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
					
				}
			}
		);
		reward_ad.loadAd(getString(R.string.reward_id), new AdRequest.Builder().build());
		
		}
	public void network_dialog() {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(MainActivity.this);
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

        ConnectivityManager connectivityManager = (ConnectivityManager)MainActivity.this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo_wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfo_data = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (networkInfo_wifi != null && networkInfo_wifi.isConnected() || networkInfo_data != null && networkInfo_data.isConnected()) {
            return true;

        } else {

            return false;
        }

	}
	public void sp_add(int dice_1, int dice_2,int click_num_count){	
		SharedPreferences sb=getSharedPreferences("sp_add",getApplicationContext().MODE_PRIVATE);	
		SharedPreferences.Editor edit= sb.edit();
		edit.putInt("dice1",dice_1);
		edit.putInt("dice2",dice_2);
		edit.putInt("click_num_count",click_num_count);
		edit.commit();
		
		
	}
	
	public void sp_load(){	
		SharedPreferences sb=getSharedPreferences("sp_add",getApplicationContext().MODE_PRIVATE);
		int sp_random_1 = new Random().nextInt(6)+1;// 0 asle jeno sathe +1 add hoy, karon int sob somoy 0 theke count start kore
		int sp_random_2 = new Random().nextInt(6)+1;	
		int save_dice1_number= sb.getInt("dice1",sp_random_1);
	int save_dice2_number=	sb.getInt("dice2",sp_random_2);
	int save_click_num_count=sb.getInt("click_num_count",0);
		final int []dice_image_sp = new int[]{R.drawable.ic_dice__1_
			, R.drawable.ic_dice__2_
			, R.drawable.ic_dice__3_
			, R.drawable.ic_dice__4_
			, R.drawable.ic_dice__5_
			, R.drawable.ic_dice__6_};
		click_number_count=save_click_num_count;
		dice_one.setImageResource(dice_image_sp[save_dice1_number-1]); // dice image er position 0 theke 5 but random number update korar por er position 1 theke 6  ,, dice image er position jehutu 0 theke 5 tai random number ja asbe -1 add hobe, 1 asle 1-1=0, r 6 asle 6-1=5
		dice_two.setImageResource(dice_image_sp[save_dice2_number-1]);
	}
	public void bounas_button_logic(){
		if(click_number_count>0){
			tenx_number_picker=new Random().nextInt(5)+1;
			dice_select_number=new Random().nextInt(2);
			x_picker=new Random().nextInt(16)+15;
			if(dice_select_number==1){
				bounas_button.setText("d1"+"="+tenx_number_picker+", "+x_picker+"x");
			} else{
				bounas_button.setText("d2"+"="+tenx_number_picker+", "+x_picker+"x");
			}
			bounas_button.setVisibility(View.VISIBLE);
			bounas_button2.setVisibility(View.VISIBLE);
			bounas_button.setAnimation(a);
			bounas_button2.setAnimation(a);

		}else{

		} 
		
	}
	public void how_to_work() {
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
							 work = rcv.getString("work").trim();				

					}
						}

			
				} catch (JSONException e) {
					e.printStackTrace();

				}}

		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				Toast.makeText(MainActivity.this, error.toString().toString(), Toast.LENGTH_SHORT).show();
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.GET, uri, resListener, errListener);
		RequestQueue req = Volley.newRequestQueue(MainActivity.this);
		req.add(stringrequest);

	}
	
	
	
	@Override
	protected void onResume() {
	
		super.onResume();
		if(!networkConected()){
			network_dialog();
		} else{
			user_data(1);
			how_to_work();
			random_number_picker=new Random().nextInt(8)+2;
			number_picker_textView.setText(random_number_picker+"");




		}

	}

	@Override
	public void onBackPressed() {
		
		android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(MainActivity.this);
        alert.setCancelable(false);
        alert.setMessage("You want to exit?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finishAffinity();

				}
			});

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();


				}
			});

        alert.show();
		
		
	}
	

	
			
		
}

// develop by mahamudul hasan 
