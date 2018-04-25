package skylist.com.qrcodepresenca;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeActivity extends AppCompatActivity {


    private final String PREFS_KEY = "Prefs_key";
    public SharedPreferences sharedPreferences = null;
    public SharedPreferences.Editor editor = null;
    private String dadosQR[] = new String[4];

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy(HH:mm:ss)");


    private FirebaseAuth mAuth;
    private FirebaseDatabase db = null;
    private DatabaseReference mDatabase;

    Button b_scan = null;
    FirebaseUser user = null;
    TextView textUser = null;
    TextView textTime = null;



    List<Presence> presenceList;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Activity activity = this;
        presenceList = new ArrayList<Presence>();

        //APENAS PARA TESTE
        presenceList.add( new Presence( "teste", "22/04/2018", "preceptor"));
        presenceList.add( new Presence( "teste", "22/04/2018", "preceptor"));
        presenceList.add( new Presence( "teste2", "22/04/2018", "preceptor2"));
        presenceList.add( new Presence( "teste2", "22/04/2018", "preceptor2"));
        presenceList.add( new Presence( "teste3", "22/04/2018", "preceptor3"));
        //FIM DO TESTE

        mRecyclerView       = findViewById(R.id.recyclerView);
        sharedPreferences   = getSharedPreferences( PREFS_KEY, MODE_PRIVATE);
        mDatabase   = FirebaseDatabase.getInstance().getReference();
        editor      = sharedPreferences.edit();
        textUser    = findViewById(R.id.textUser);
        textTime    = findViewById(R.id.currentTime);
        b_scan      = findViewById(R.id.bScan);
        mAuth       = FirebaseAuth.getInstance(); //INSTANCIA DO BANCO DE AUTENTICAÇÃO
        user        = mAuth.getCurrentUser(); //USUARIO ATUALMENTE LOGADO;
        db          = FirebaseDatabase.getInstance();

        mAdapter = new MyAdapter(presenceList);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        textUser.setText( user.getDisplayName() );
        textTime.setText( sdf.format(new Date()) );

        final Query query1 = mDatabase.child("users").child("students").child(mAuth.getCurrentUser().getUid());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currentDate = new SimpleDateFormat("dd/MM/yyyy/HH:mm:ss").format(new Date());
                String date[] = currentDate.split("/");

                Log.i("SAIDA", String.valueOf( dataSnapshot.getValue() ));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        b_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                textTime.setText( sdf.format(new Date()) );

                IntentIntegrator integrator = new IntentIntegrator( activity );
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Camera Scan");
                integrator.setCameraId(0);
                integrator.initiateScan();


               //Toast.makeText(HomeActivity.this, "Salvos", Toast.LENGTH_SHORT).show();
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
                saveNewPresence( dadosQR[0], dadosQR[1], dadosQR[2], dadosQR[3] );
            }else{
                Toast.makeText( getApplicationContext(), "Scan error", Toast.LENGTH_SHORT ).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void saveNewPresence( String materia, String preceptor, String passOfDay , String institution){

        String currentDate = new SimpleDateFormat("dd/MM/yyyy/HH:mm:ss").format(new Date());
        String date[] = currentDate.split("/");

        DatabaseReference myref = db.getReference("users");
        myref.child("students").child(mAuth.getCurrentUser().getUid()).child("name").setValue( mAuth.getCurrentUser().getDisplayName());
        myref.child("students").child(mAuth.getCurrentUser().getUid()).child(materia).child(date[2]).child(date[1]).child(date[0]).child(date[3].split(":")[0]).child("timeCheckin").setValue(date[3]);
        myref.child("students").child(mAuth.getCurrentUser().getUid()).child(materia).child(date[2]).child(date[1]).child(date[0]).child(date[3].split(":")[0]).child("passOfDay").setValue(passOfDay);
        myref.child("students").child(mAuth.getCurrentUser().getUid()).child(materia).child(date[2]).child(date[1]).child(date[0]).child(date[3].split(":")[0]).child("preceptor").setValue(preceptor);
        myref.child("students").child(mAuth.getCurrentUser().getUid()).child(materia).child(date[2]).child(date[1]).child(date[0]).child(date[3].split(":")[0]).child("institution").setValue(institution);

    }
}
