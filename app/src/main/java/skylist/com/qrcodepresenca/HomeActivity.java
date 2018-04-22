package skylist.com.qrcodepresenca;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeActivity extends AppCompatActivity {


    private final String PREFS_KEY = "Prefs_key";
    public SharedPreferences sharedPreferences = null;
    public SharedPreferences.Editor editor = null;
    private String dadosQR[] = new String[2];

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy(HH:mm:ss)");


    private FirebaseAuth mAuth;
    Button b_scan = null;
    FirebaseUser user = null;
    TextView textUser = null;
    TextView textTime = null;


    RecyclerView mRecyclerView;
    List<Presence> presenceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Activity activity = this;

        presenceList.add( new Presence( "teste", "22/04/2018", "preceptor"));
        presenceList.add( new Presence( "teste", "22/04/2018", "preceptor"));
        presenceList.add( new Presence( "teste2", "22/04/2018", "preceptor2"));
        presenceList.add( new Presence( "teste2", "22/04/2018", "preceptor2"));
        presenceList.add( new Presence( "teste3", "22/04/2018", "preceptor3"));


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(new MyAdapter(presenceList));

        sharedPreferences = getSharedPreferences( PREFS_KEY, MODE_PRIVATE);
        textUser    = findViewById(R.id.textUser);
        textTime    = findViewById(R.id.currentTime);
        b_scan      = findViewById(R.id.bScan);

        mAuth       = FirebaseAuth.getInstance(); //INSTANCIA DO BANCO DE AUTENTICAÇÃO
        user        = mAuth.getCurrentUser(); //USUARIO ATUALMENTE LOGADO;

        textUser.setText( user.getDisplayName() );
        textTime.setText( sdf.format(new Date()) );

        editor      = sharedPreferences.edit();


        b_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*editor.putString("email", "email@email.com");
                editor.putString("password", "123456");
                editor.commit();
                */
                textTime.setText( sdf.format(new Date()) );

                IntentIntegrator integrator = new IntentIntegrator( activity );
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Camera Scan");
                integrator.setCameraId(0);
                integrator.initiateScan();


                Toast.makeText(HomeActivity.this, "Salvos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult( requestCode, resultCode, data );
        if(result != null){
            if( result.getContents() !=null ){
                Toast.makeText( getApplicationContext(), result.getContents(), Toast.LENGTH_SHORT ).show();
                dadosQR = result.getContents().split(";");
            }else{
                Toast.makeText( getApplicationContext(), "Scan error", Toast.LENGTH_SHORT ).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

}
