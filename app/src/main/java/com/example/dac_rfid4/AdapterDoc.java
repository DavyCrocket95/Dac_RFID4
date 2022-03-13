package com.example.dac_rfid4;

import static android.content.Context.WIFI_SERVICE;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;


import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.dac_rfid4.commons.Constants.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AdapterDoc extends FirestoreRecyclerAdapter<ModelDoc, AdapterDoc.DocViewHolder> {
    private static final String TAG = "Adapter";

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterDoc(@NonNull FirestoreRecyclerOptions<ModelDoc> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DocViewHolder holder, int position, @NonNull ModelDoc model) {
        String titre = model.getTitre();
        Log.d(TAG, "Adapter : " + titre);

        if(titre != null) {
            String auteur = model.getAuteur();
            String formatDoc = model.getFormat();


            holder.tvTitre.setText(titre);
            holder.tvAuteur.setText(auteur);


            int iconeDoc = R.drawable.ic_launcher_background;
            if (formatDoc.equals("Audio")) {
                iconeDoc = R.drawable.audio;
            } else if (formatDoc.equals("Photo")) {
                iconeDoc = R.drawable.photo;
            } else if (formatDoc.equals("Video")) {
                iconeDoc = R.drawable.video;
            }


            RequestOptions opts = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.ic_launcher_background)
                    .placeholder(R.mipmap.ic_launcher);

            Context ctx = holder.ivFormatDoc.getContext();

            holder.ivFormatDoc.setImageResource(iconeDoc);
            Glide.with(ctx)
                    .load(iconeDoc)
                    .apply(opts)
                    .fitCenter()
                    .override(100, 100)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivFormatDoc);
        }

    }

    @NonNull
    @Override
    public DocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder" );
        View v1 = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.row_doc, parent, false);

        return new DocViewHolder(v1);
    }

    public class DocViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFormatDoc;
        private TextView tvTitre, tvAuteur;

        public DocViewHolder(@NonNull View itemView) {
            super(itemView);

            ivFormatDoc = itemView.findViewById(R.id.ivFormatDoc);
            tvTitre = itemView.findViewById(R.id.tvTitre);
            tvAuteur = itemView.findViewById(R.id.tvAuteur);

            Context mContext = ivFormatDoc.getContext();
            ivFormatDoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v2) {

                    int pos = getBindingAdapterPosition();
                    Log.d(TAG, "Adapter position image : " + pos);

                    DocumentSnapshot doc1 = getSnapshots().getSnapshot(pos);
                    String contenuDoc2 = doc1.getString("contenuDoc");
                    String format2 = doc1.getString("format");


                    Log.d(TAG, "Adapter click image" + doc1.get("titre") + " : " + format2);
                    Log.d(TAG, "Adapter contenuDoc : " + contenuDoc2);


                    if (format2.equals("Video")) {

                        LayoutInflater inflater = LayoutInflater.from(mContext);
                        View subView = inflater.inflate(R.layout.activity_detail_video, null);

                        final VideoView iv_Video = (VideoView) subView.findViewById(R.id.vvVideo);

                        Uri uri= Uri.parse( contenuDoc2 );
                        Log.d(TAG, "Adapter uri : " + uri.toString());
                        iv_Video.setVideoURI(uri);

                        iv_Video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                                    @Override
                                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                                        MediaController mc1 = new MediaController(mContext);
                                        iv_Video.setMediaController(mc1);

                                        //mc1.setAnchorView(iv_Video);      //ancre sur la video

                                        ((ViewGroup) mc1.getParent()).removeView(mc1);

                                        ((FrameLayout) subView.findViewById(R.id.videoViewWrapper)).addView(mc1);

                                        mc1.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        });

                        iv_Video.start();


                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                        /** Gestion du bouton fermer **/
                        builder.setNegativeButton("FERMER", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(MainActivity.this, "Opération annulée", Toast.LENGTH_LONG).show();
                            }
                        });


                        final AlertDialog dialog = builder.create();
                        builder.setTitle("VIDEO : " +  doc1.get("titre"));
                        builder.setView(subView);
                        builder.create();
                        builder.show();



                    } else if (format2.equals("Audio")) {
                        Intent i3 = new Intent(mContext, MainActivity.class);
                        i3.putExtra("contenuDoc", contenuDoc2);
                        mContext.startActivity(i3);
                    } else if (format2.equals("Photo")) {
                        LayoutInflater inflater = LayoutInflater.from(mContext);
                        View subView = inflater.inflate(R.layout.activity_detail_photo, null);

                        final ImageView iv_Photo = (ImageView) subView.findViewById(R.id.ivImage2);
                        Glide.with(mContext).load(contenuDoc2).into(iv_Photo);

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                        Log.i(TAG, "Init WIFI_SERVICE");
                        WifiManager wifiManager = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
                        Log.i(TAG, "WIFI_SERVICE");
                        if (wifiManager.isWifiEnabled()) {

                            builder.setPositiveButton("IMPRIMER", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    PrintHelper photoPrinter = new PrintHelper(mContext);
                                    photoPrinter.setColorMode(PrintHelper.COLOR_MODE_MONOCHROME);
                                    photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

                                    Bitmap bitmap = ((BitmapDrawable) iv_Photo.getDrawable()).getBitmap();
                                    photoPrinter.printBitmap("PRINT PHOTO", bitmap);

                                }
                            });
                        }

                        /** Gestion du bouton fermer **/
                        builder.setNegativeButton("FERMER", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(MainActivity.this, "Opération annulée", Toast.LENGTH_LONG).show();
                            }
                        });


                        final AlertDialog dialog = builder.create();
                        builder.setTitle("PHOTO : " + doc1.get("titre"));
                        builder.setView(subView);
                        builder.create();
                        builder.show();
                    }
                    else {
                        Log.d(TAG, "Adapter erreur format : " + format2);
                    }

                }
            });

            //Popup Menu sur une ligne doc
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v1) {
                    PopupMenu popupMenu = new PopupMenu(mContext, v1);
                    popupMenu.getMenu().add("SUPPRIMER");
                    popupMenu.getMenu().add("ARCHIVER");
                    //popupMenu.getMenu().add("MAJ");

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int pos = getBindingAdapterPosition();
                            DocumentSnapshot doc1 = getSnapshots().getSnapshot(pos);
                            String idDoc1 = doc1.getId();
                            String idProd1 = doc1.getString("idProd");
                            String titre = doc1.getString("titre");

                            if (item.getTitle().equals("SUPPRIMER")) {
                                Log.d(TAG, "SUPPRIMER idDoc :" + idDoc1);
                                String contenuDoc2 = doc1.getString("contenuDoc");

                                alertDialogSup(idDoc1, idProd1, titre, contenuDoc2, mContext);
                            }
                            if (item.getTitle().equals("ARCHIVER")) {
                                Log.d(TAG, "ARCHIVER" + idDoc1);

                                alertDialogArch(idDoc1, idProd1, titre, mContext);

                            }
                            /*
                            if(item.getTitle().equals("MAJ")){
                                Log.d(TAG, "MAJ" );
                            }*/
                            return true;
                        }
                    });

                    popupMenu.show();
                    return true;

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v1) {
                    int pos = getBindingAdapterPosition();


                    Log.d(TAG, "Adapter click :" + v1.getId() + " image : " + ivFormatDoc.getId() + " titre : " + tvTitre.getId());
                    Log.d(TAG, "Adapter position : " + pos);

                    DocumentSnapshot doc1 = getSnapshots().getSnapshot(pos);
                    String idDoc = doc1.getId();


                    //Intent i4 = new Intent(mContext, DetailDocActivity.class);
                    //i4.putExtra("idDoc", idDoc);
                    //mContext.startActivity(i4);

                }
            });

        }

        public Bitmap getBitmap(ContentResolver cr, Uri url)
                throws FileNotFoundException, IOException {
            InputStream input = cr.openInputStream(url);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            input.close();
            return bitmap;
        }
    }


    private void deleteDoc(String idDoc, String idProd, String contenu2, Context mContext) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Doc").document(idDoc);

        Log.d(TAG, "deleteDoc Storage" + contenu2);
        Uri uriC = Uri.parse(contenu2);


        String f1 = uriC.getFragment();
        String p1 = uriC.getPath();
        Log.d(TAG, "URI :" + f1 + " : " + p1);
        String[] mp = p1.split("/");
        String name = mp[mp.length - 1];
        String chemein = mp[mp.length - 2];

        Log.d(TAG, " URI :" + chemein + " : " + name);


        docRef.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Succes to delete the document. ");
                            // this method is called when the task is success
                            // after deleting we are starting our MainActivity.
                            /*Toast.makeText(Detail4Activity.this, "Course has been deleted from Database.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Detail4Activity.this, MainActivity.class);
                            startActivity(i);*/

                            /*** delete le contenu  **/
                            //Delete soit Path+"/"+nom.jpg
                            //StorageReference contenuRef = FirebaseStorage.getInstance().getReference()
                            //        .child(chemein + "/" + name);
                            //Log.i(TAG, "deleteDoc: " + contenuRef);

                            //contenuRef.delete();

                        } else {
                            Log.d(TAG, "Fail to delete the document. ");
                        }
                    }
                });

        rechargerView(idProd, mContext);

    }

    private void archiveDoc(String idDoc, String idProd, Context mContext) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("doc").document(idDoc);

        docRef.update("archive", "true")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Succes Archive. ");

                        } else {
                            Log.d(TAG, "Fail to archive the document. ");
                        }
                    }
                });

        rechargerView(idProd, mContext);

    }

    private void rechargerView(String idProd, Context mContext) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference prodRef = db.collection("produits").document(idProd);

        prodRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot ds1) {
                        if (ds1.exists()) {
                            Log.d(TAG, " Bouclage ");

                            //String numero = ds1.getString(NUM_PROD);
                            //String name = ds1.getString(NAME_PROD);

                            //Lancer la page Produit + boutons
                            //Intent i2 = new Intent(mContext, FicheProduitFirstPageActivity.class);
                           // i2.putExtra(KEY_NUMPROD, numero);
                           // i2.putExtra(KEY_IDPROD, idProd);
                            //i2.putExtra(KEY_NAMEPROD, name);
                            //i2.putExtra(KEY_PROVPROD, ds1.getString("provenance"));
                            //mContext.startActivity(i2);

                        } else
                            Log.d(TAG, "onFailure sur Succes");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });

    }


    private void alertDialogSup(String idDoc, String idProd, String nameDoc, String contenu2, Context mContext) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage("Confirmer la suppression de " + nameDoc);
        dialog.setTitle("Dialog Box");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Log.d(TAG, "Yes is clicked");
                        deleteDoc(idDoc, idProd, contenu2, mContext);
                    }
                });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "cancel is clicked");
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }


    private void alertDialogArch(String idDoc, String idProd, String nameDoc, Context mContext) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage("Confirmer l'archivage de " + nameDoc);
        dialog.setTitle("Dialog Box");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Log.d(TAG, "Yes is clicked");
                        archiveDoc(idDoc, idProd, mContext);
                    }
                });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "cancel is clicked");
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

}
