package skylist.com.qrcodepresenca;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {

    EditText inputName = null;
    EditText inputEmail = null;
    EditText inputPassword = null;
    Button b_signup = null;
    Button b_cancel = null;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance(); //INSTANCIA DO BANCO DE AUTENTICAÇÃO

        b_cancel = findViewById(R.id.bCancel);
        b_signup = findViewById(R.id.bSignup);

        inputEmail = findViewById(R.id.editEmail);
        inputName = findViewById(R.id.editName);
        inputPassword = findViewById(R.id.editPassword);

        b_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser( inputEmail.getText().toString(), inputPassword.getText().toString() );
            }
        });

    }

    void createUser( String email, String password){
        mAuth.createUserWithEmailAndPassword( email, password )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Criado com sucesso", Toast.LENGTH_LONG).show();

                            updateUserProfile( inputName.getText().toString().toUpperCase(), "" );

                            startActivity(new Intent( getApplicationContext(), HomeActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void updateUserProfile( String profileName, String photoURL ){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//INSTANCIA DO USUARIO ATUAL
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(profileName)
                .setPhotoUri(Uri.parse(photoURL))
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("OK", "User profile updated.");
                        }
                    }
                });
    }
}
