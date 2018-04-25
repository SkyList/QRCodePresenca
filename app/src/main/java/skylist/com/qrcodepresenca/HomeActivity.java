package skylist.com.qrcodepresenca;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.List;
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
    List<CheckDoDia> presenceList;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Activity activity = this;
        presenceList = new ArrayList<CheckDoDia>();
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
        textUser.setText( user.getDisplayName() );
        textTime.setText( sdf.format(new Date()) );

        loadListFirebase();

        b_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textTime.setText( sdf.format(new Date()) );
                IntentIntegrator integrator = new IntentIntegrator( activity );
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Camera Scan");
                integrator.setCameraId(0);
                integrator.initiateScan();
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
                updateRecycleView(presenceList);
            }else{
                Toast.makeText( getApplicationContext(), "Scan error", Toast.LENGTH_SHORT ).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void loadListFirebase(){
        final List<CheckDoDia> copyBd = new ArrayList<>();
        Query query1 = mDatabase.child("users").child("students").child(mAuth.getCurrentUser().getUid()).child("MATERIAS");
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot ds : dataSnapshot.getChildren()){
                    CheckDoDia cdia = ds.getValue(CheckDoDia.class);
                    Log.i("SAIDA", cdia.materia+" ,"+cdia.date+" ,"+cdia.preceptor);
                    presenceList.add(cdia);
                }

                updateRecycleView( presenceList );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void updateRecycleView( List<CheckDoDia> list){
        mAdapter = new MyAdapter(list);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void saveNewPresence( String materia, String preceptor, String passOfDay , String institution){

        String currentDate = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss").format(new Date());
        String date[] = currentDate.split("-");

        DatabaseReference myref = db.getReference("users");

        CheckDoDia cd = new CheckDoDia( institution, passOfDay, preceptor, materia,date[0]+"-"+date[1]+"-"+date[2], date[3] );
        myref.child("students").child( mAuth.getCurrentUser().getUid() ).child("MATERIAS").child(currentDate).setValue(cd);
        myref.child("students").child(mAuth.getCurrentUser().getUid()).child("name").setValue( mAuth.getCurrentUser().getDisplayName() );

    }
}
