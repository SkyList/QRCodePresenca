package skylist.com.qrcodepresenca;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText emailInput     = null;
    EditText passwordInput  = null;
    Button   signinButton   = null;
    TextView signupText     = null;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance(); //INSTANCIA DO BANCO DE AUTENTICAÇÃO

        emailInput      = findViewById(R.id.emailText);
        passwordInput   = findViewById(R.id.passwordText);
        signinButton    = findViewById(R.id.signinButton);
        signupText      = findViewById(R.id.signupText);

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( getApplicationContext(), SignupActivity.class ));
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinCheck( emailInput.getText().toString(), passwordInput.getText().toString());
            }
        });

    }

    void signinCheck( String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Log.w("sc", "signInWithEmail:failed", task.getException());
                            Toast.makeText(getApplicationContext(), "Sorry, try again", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),  task.isSuccessful()+" ...", Toast.LENGTH_SHORT).show();
                            Log.d("sc", "signInWithEmail:onComplete:" +  task.isSuccessful());
                            startActivity(new Intent( getApplicationContext(), HomeActivity.class));
                        }
                    }
                });
    }


}
