package com.pai.rateit.controller.account;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pai.rateit.R;
import com.pai.rateit.mapper.store.UserMapper;
import com.pai.rateit.model.user.User;

/**
 * Created by kevin on 27/04/2018.
 */

public class AccountController implements FirebaseAuth.AuthStateListener {

    public static String TAG_SIGN_IN = "SIGN_IN";
    public static String TAG_DATABASE = "DATABASE";

    private Activity mActivity;
    private AccountStateListener mAccountStateListener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private User mUser = null;

    public AccountController(Activity activity, AccountStateListener accountStateListener) {
        mActivity = activity;

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        mAccountStateListener = accountStateListener;

        mDb = FirebaseFirestore.getInstance();
    }

    public boolean isAuth() {
        return mAuth.getCurrentUser() != null;
    }

    public void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_SIGN_IN, "signInAnonymously:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_SIGN_IN, "signInAnonymously:failure", task.getException());
                            Toast.makeText(mActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void loadUserData() {
        if (!isAuth())
            return;

        DocumentReference docRef = mDb.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        mUser = document.toObject(User.class);
                        if (mAccountStateListener != null)
                            mAccountStateListener.onUserDataChanged(mUser);
                    } else {
                        mUser = new User();
                        askForNotifyPermission();
                    }
                } else {
                    Log.d(TAG_DATABASE, "get failed with ", task.getException());
                    mUser = null;
                    if (mAccountStateListener != null)
                        mAccountStateListener.onUserDataChanged(null);
                }
            }
        });
    }

    public void askForNotifyPermission() {
        if (!isAuth())
            return;

        new AlertDialog.Builder(mActivity)
                .setTitle(R.string.allow_notifications_alert_dialog_title)
                .setMessage(R.string.allow_notifications_alert_dialog_message)
                .setPositiveButton(R.string.allow_notifications_alert_dialog_positive,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setNotificationPermission(true);
                            }
                        })
                .setNegativeButton(R.string.allow_notifications_alert_dialog_negative,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setNotificationPermission(false);
                            }
                        })
                .show();
    }

    public void pushUserDataToCloud() {
        if (!isAuth() || mUser == null)
            return;

        mDb.collection("users").document(mAuth.getCurrentUser().getUid())
                .set(new UserMapper().serialize(mUser))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (mAccountStateListener != null)
                            mAccountStateListener.onUserDataChanged(mUser);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG_DATABASE, "Error writing document", e);
                    }
                });
    }

    public void setNotificationPermission(boolean granted) {
        if (!isAuth() || mUser == null)
            return;

        mUser.setNotify(granted);
        pushUserDataToCloud();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        mUser = null;
        if (!isAuth())
            signInAnonymously();
        else
            loadUserData();

        if (mAccountStateListener != null)
            mAccountStateListener.onAccountChanged(firebaseAuth.getCurrentUser());
    }

    public interface AccountStateListener {
        public void onAccountChanged(FirebaseUser firebaseUser);
        public void onUserDataChanged(User user);
    }
}
