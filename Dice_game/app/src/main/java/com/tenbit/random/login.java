package com.tenbit.random;
import android.app.Activity;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.api.ApiException;
import android.util.Log;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import android.annotation.NonNull;
import com.google.firebase.auth.FirebaseUser;
import com.android.volley.toolbox.StringRequest;
import java.util.Random;
import java.util.Map;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import org.json.JSONObject;
import org.json.JSONException;
import com.android.volley.VolleyError;
import com.android.volley.Request;
import com.android.volley.AuthFailureError;
import java.util.HashMap;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;
import android.widget.TextView;
import android.text.method.LinkMovementMethod;
import android.net.Uri;

public class login extends Activity {
    private Button mSign,coinbase_button;
    public static final int RC_SIGN_IN = 1;
    public FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
	Dialog dialog;
	int start_point = 0;
    int refer_code_Use_orNOt = 1;
	TextView privicy_textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mSign = findViewById(R.id.google_signin_button);
		coinbase_button=findViewById(R.id.coinbase_button);
		dialog = new Dialog(login.this);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
	    privicy_textView=findViewById(R.id.privicy_textView);
        privicy_textView.setMovementMethod(LinkMovementMethod.getInstance());
		privicy_textView.setHighlightColor(Color.TRANSPARENT);
		privicy_textView.setLinkTextColor(getResources().getColor(R.color.privicy_textColor));	
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


        mSign.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View msignin) {

					OnSignInButtonClick();

				}
			});
			
			
		coinbase_button=findViewById(R.id.coinbase_button);
        coinbase_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String coinbase_link="https://www.coinbase.com/signup";
					Intent i=new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(coinbase_link));
					startActivity(i);
				}
			});	
}
	public void OnSignInButtonClick(){
        try{
            Intent choose = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(choose, RC_SIGN_IN);
			dialog.show();
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
		
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {        
                GoogleSignInAccount account = task.getResult(ApiException.class);
                
               firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
				
               dialog.dismiss();
                      
            }
        } else {

            dialog.dismiss();
        }
    }
	private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
			.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					if (task.isSuccessful()) {					
						FirebaseUser user = mAuth.getCurrentUser();
										
					loginlogic(user);					
					} else {					
						dialog.dismiss();
						   Toast.makeText(login .this, task.getException().toString(), Toast.LENGTH_SHORT).show();						
					}
				}
			});
	}
	
	public void loginlogic(final FirebaseUser user) {
/// *** 2ta karone string request fail hoite chilo 1. hasmap e getpram() chilo na 2. data POST Er jagay GET dewa chilo
		
		String uri = "https://10xbit.xyz/existing_checker.php";
		Response.Listener resListener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {


					JSONObject gso=new JSONObject(response);
					String success = gso.getString("success");
					String message = gso.getString("message");

					if (success.equals("1")) {
						
						Intent i = new Intent(login.this, MainActivity.class);
						 startActivity(i);
						 finishAffinity();
					} else if(success.equals("0")) {									
						Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();
						newUser_dataSend(user);
						
					} else{				
						Toast.makeText(login.this, "error 4007", Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(login.this, e.toString(), Toast.LENGTH_SHORT).show();			
				}
			}
		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				Toast.makeText(login.this, error.toString().toString(), Toast.LENGTH_SHORT).show();
				
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.POST, uri, resListener, errListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> pram = new HashMap<String, String>();
				pram.put("email", user.getEmail());
				return pram;  

			}};
		RequestQueue req = Volley.newRequestQueue(login.this);
		req.add(stringrequest);
	}

	public String refer_code_generate(int lenght) {
        char[] chars = ("QWERTYUIOPASDFGHJKLZXCVBNM9876543210").toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < lenght; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
	}
	public void newUser_dataSend(final FirebaseUser user) {

		String uri = "https://10xbit.xyz/newuser_registration.php";
		Response.Listener resListener = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject gso=new JSONObject(response);
					String success = gso.getString("success");
					String message = gso.getString("message");
					if (success.equals("1")) {
								
						Intent i = new Intent(login.this, MainActivity.class);
						startActivity(i);
						finishAffinity();

					} else if(success.equals("0")) {				
						Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();
					} else{					
						Toast.makeText(login.this, "error 4007", Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(login.this, e.toString(), Toast.LENGTH_SHORT).show();				
				}
			}
		};
		Response.ErrorListener errListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				Toast.makeText(login.this, error.toString().toString(), Toast.LENGTH_SHORT).show();			
			}
		};
		StringRequest  stringrequest= new StringRequest(Request.Method.POST, uri, resListener, errListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> pram = new HashMap<String, String>();
				pram.put("name", user.getDisplayName());
				pram.put("email", user.getEmail());
				pram.put("codeuse_ornot", refer_code_Use_orNOt+"");
				pram.put("refer_code", "B"+refer_code_generate(5));
				pram.put("uid", user.getUid());
				pram.put("refer_count", 0+"");
				pram.put("user_point", "0");
				return pram;  

			}};
		RequestQueue req = Volley.newRequestQueue(login.this);
		req.add(stringrequest);	
					
		}	
	         
		}
		
	
	
			
				
				
