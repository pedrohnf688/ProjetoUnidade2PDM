package com.example.phnf2.projeto2unidade.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.phnf2.projeto2unidade.R;
import com.example.phnf2.projeto2unidade.adapter.ConversaAdapter;
import com.example.phnf2.projeto2unidade.adapter.RecyclerLinkMapsClickListener;
import com.example.phnf2.projeto2unidade.modelo.Conversa;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentConversa extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private List<Conversa> listaMensagem = new ArrayList<>();
    ImageButton Anexar;
    ImageButton Enviar;
    EditText Conteudo;
    RecyclerView recyclerView;
    private ChildEventListener mChildEventListener;
    private ConversaAdapter conversaAdapter;
    //criando o objeto do auth
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //criando o objeto do storage
    private static final int CODIGO_FOTO = 56;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private static final int CODIGO_LOGAR = 5;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;



    public FragmentConversa() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_fragment_conversa, container, false);

        Anexar = v.findViewById(R.id.anexar);
        Enviar = v.findViewById(R.id.enviar);
        Conteudo = v.findViewById(R.id.conteudo);
        recyclerView = v.findViewById(R.id.recyclerConversa);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUsername = ANONYMOUS;
//listener do login
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    onSignInInitialize(user.getDisplayName());
                }else{
                    onSignOutCleanUp();
                  //chama o fluxo de login
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(
                                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                                                    new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            CODIGO_LOGAR);
                }
            }
        };

        mFirebaseDatabase  = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("chat_photos");

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Locale locale = new Locale("pt", "BR");
                GregorianCalendar calendar = new GregorianCalendar();
                SimpleDateFormat formatador = new SimpleDateFormat("dd' de 'MMMMM' de 'yyyy' - 'HH':'mm'h'", locale);


                Conversa mensagem = new Conversa(Conteudo.getText().toString(), mUsername, formatador.format(calendar.getTime()),null);

                mMessagesDatabaseReference.push().setValue(mensagem);
                Conteudo.setText("");

            }
        });


        Anexar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), CODIGO_FOTO);
            }
        });


        // Tratar clicks do recyclerview do link do maps

        recyclerView.addOnItemTouchListener(new RecyclerLinkMapsClickListener(getContext(), recyclerView, new RecyclerLinkMapsClickListener.OnLinkMapsClickListener() {
            @Override
            public void onLinkMapsLongClick(View view, int posicao) {

                Toast.makeText(getContext(), "Toque Longo", Toast.LENGTH_SHORT).show();

            }


        }));

        return v;
    }


    private void onSignInInitialize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
    }
    //implementando o listener do banco
    private void attachDatabaseReadListener(){
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Conversa conversa = dataSnapshot.getValue(Conversa.class);
                    listaMensagem.add(conversa);
                    recyclerView.setAdapter(new ConversaAdapter(getContext(),listaMensagem));
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void onSignOutCleanUp() {
        mUsername = ANONYMOUS;
        listaMensagem.clear();
        detachDatabaseReadListener();
    }
    //removendo o listener do banco
    private void detachDatabaseReadListener(){
        if(mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
        listaMensagem.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Locale locale = new Locale("pt", "BR");
        final GregorianCalendar calendar = new GregorianCalendar();
        final SimpleDateFormat formatador = new SimpleDateFormat("dd' de 'MMMMM' de 'yyyy' - 'HH':'mm'h'", locale);

        if (requestCode == CODIGO_LOGAR){
            if (resultCode == RESULT_OK){
                Toast.makeText(getContext(), "Bem-vindo", Toast.LENGTH_SHORT).show();
            }else if (resultCode == RESULT_CANCELED){
                //finaliza ação
            }
        }else if (requestCode == CODIGO_FOTO && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            StorageReference photoref = storageRef.child(mUsername + "_" + selectedImageUri.getLastPathSegment());
//para upload da imagem basta photoref.putFile(selectedImageUri);
//addOnSuccessListener para saber quando a imagem foi enviada
            photoref.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
// Get a URL to the uploaded content
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//Log.i("TESTE", uri.toString());
                            Conversa conversa = new Conversa(null, mUsername,formatador.format(calendar.getTime()), uri.toString());
                            mMessagesDatabaseReference.push().setValue(conversa);
                        }
                    });
                }
            });
        }

    }

}
