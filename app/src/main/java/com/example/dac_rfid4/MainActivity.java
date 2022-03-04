package com.example.dac_rfid4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RadioGroup rgCateg1, rgCateg2;
    private EditText etNumProd;
    private String idProd, numProd;

    private FirebaseFirestore db;
    private AdapterDoc adapterDoc;
    private RecyclerView rvDoc;
    private Context ctx2;

    private void init() {
        rgCateg1 = findViewById(R.id.rg1);
        rgCateg2 = findViewById(R.id.rg2);
        etNumProd = findViewById(R.id.etNumProd);

        db = FirebaseFirestore.getInstance();

        rvDoc = findViewById(R.id.rvDoc);
        rvDoc.setHasFixedSize(true);
        rvDoc.setLayoutManager(new LinearLayoutManagerWrapper(ctx2, LinearLayoutManager.VERTICAL, false));

    }

    public class LinearLayoutManagerWrapper extends LinearLayoutManager {

        public LinearLayoutManagerWrapper(Context context) {
            super(context);
        }

        public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            //return super.supportsPredictiveItemAnimations();
            return false;       //Pas d'animation entre les elt dans l'item recycler
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        //numProd = etNumProd.getText().toString().trim();
    }


    public void btnVal(View v1) {
        String numProduit = etNumProd.getText().toString().trim();

        Log.i(TAG, "btnVal");
        //Query q1 = db.collection("Produits")                .whereEqualTo("numero", numProd);

        db.collection("Produits")
                .whereEqualTo("numero", numProduit)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot qds) {
                        if (qds.isEmpty()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY 1" + numProduit);

                        } else {

                            for (DocumentSnapshot documentSnapshot : qds) {
                                if (documentSnapshot.exists()) {
                                    Log.d(TAG, "onSuccess: DOCUMENT" + documentSnapshot.getId() + " ; " + documentSnapshot.getData());
                                    idProd = documentSnapshot.getId();
                                    Log.d(TAG, "onSuccess: name " + documentSnapshot.get("name") );
                                    listDoc();
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void listDoc() {

        /**** Prendre les datas suivant un modelDoc. Attention le modelDoc doit avoir les nommages des champs identiques à celui du firebase ****/
        Query q1 = db.collection("Doc")
                .whereEqualTo("idProd", idProd)
                .whereEqualTo("archive", "false");

        Log.d(TAG, "onSuccess: idProd 2:" + idProd );
        FirestoreRecyclerOptions<ModelDoc> listDoc =
                new FirestoreRecyclerOptions.Builder<ModelDoc>()
                        .setQuery(q1, ModelDoc.class)
                        .build();

        adapterDoc = new AdapterDoc((listDoc));
        rvDoc.setAdapter(adapterDoc);
        adapterDoc.startListening();

    }

    public void btnCategorie(View view) {
        // Clear any checks from both groups:
        rgCateg1.clearCheck();
        rgCateg2.clearCheck();

        // Manually set the check in the newly clicked radio button:
        ((RadioButton) view).setChecked(true);

        // Perform any action desired for the new selection:
        switch (view.getId()) {
            case R.id.rbTout:
                Log.i(TAG, "Tout");
                Query q1 = db.collection("Doc")
                        .whereEqualTo("idProd", idProd)
                        .whereEqualTo("archive", "false");

                FirestoreRecyclerOptions<ModelDoc> lstDoc = new FirestoreRecyclerOptions.Builder<ModelDoc>()
                        .setQuery(q1, ModelDoc.class)
                        .build();

                Log.d(TAG, "onSuccess: idProd 2:" + idProd );
                adapterDoc = new AdapterDoc((lstDoc));
                rvDoc.setAdapter(adapterDoc);
                adapterDoc.startListening();

                break;

            case R.id.rbCert:
                Log.i(TAG, "Certification");

                Query q2 = db.collection("Doc")
                        .whereEqualTo("idProd", idProd)
                        .whereEqualTo("categorie", "Certification")
                        .whereEqualTo("archive", "false");

                FirestoreRecyclerOptions<ModelDoc> lstDoc2 = new FirestoreRecyclerOptions.Builder<ModelDoc>()
                        .setQuery(q2, ModelDoc.class)
                        .build();

                Log.d(TAG, "onSuccess: Certification :" + idProd );
                adapterDoc = new AdapterDoc((lstDoc2));
                rvDoc.setAdapter(adapterDoc);
                adapterDoc.startListening();
                break;

            case R.id.rbTech:
                Log.i(TAG, "Tech");

                Query q3 = db.collection("Doc")
                        .whereEqualTo("idProd", idProd)
                        .whereEqualTo("categorie", "Technique")
                        .whereEqualTo("archive", "false");

                FirestoreRecyclerOptions<ModelDoc> lstDoc3 = new FirestoreRecyclerOptions.Builder<ModelDoc>()
                        .setQuery(q3, ModelDoc.class)
                        .build();

                Log.d(TAG, "onSuccess: Tech :" + idProd );
                adapterDoc = new AdapterDoc((lstDoc3));
                rvDoc.setAdapter(adapterDoc);
                adapterDoc.startListening();

                break;

            case R.id.rbAdmin:
                Log.i(TAG, "Admin");
                break;

            case R.id.rbAutre:
                Log.i(TAG, "Autre");
                break;
        }
    }


}