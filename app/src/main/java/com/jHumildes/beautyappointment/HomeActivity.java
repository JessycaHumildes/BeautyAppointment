package com.jHumildes.beautyappointment;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.example.beautyappointment.R;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jHumildes.beautyappointment.Common.Common;
import com.jHumildes.beautyappointment.Fragments.HomeFragment;
import com.jHumildes.beautyappointment.Fragments.MapFragment;
import com.jHumildes.beautyappointment.Fragments.ProceduresFragment;
import com.jHumildes.beautyappointment.Model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class HomeActivity extends AppCompatActivity {

    AlertDialog dialog;


    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    BottomSheetDialog bottomSheetDialog;

    CollectionReference userRef;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);

        //init

        userRef = FirebaseFirestore.getInstance().collection("User");
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        //check intent, if is login = true, enable full access else let user only view
        if (getIntent() !=null){
            boolean isLogin = getIntent().getBooleanExtra(Common.IS_LOGIN, false);
            if (isLogin)
            {
                dialog.show();
                //check is user exists
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        if (account != null){
                            DocumentReference currentUser = userRef.document(account.getId().toString());
                            currentUser.get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful())
                                            {
                                                DocumentSnapshot userSnapShot = task.getResult();
                                                if (!userSnapShot.exists())

                                                    showUpdateDialog(account.getPhoneNumber().toString());


                                            }
                                            if (dialog.isShowing())
                                             dialog.dismiss();
                                        }
                                    });

                        }

                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Toast.makeText(HomeActivity.this, ""+accountKitError.getErrorType().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }

        //View
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView
                .OnNavigationItemSelectedListener() {
            Fragment fragment = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_home)
                    fragment = new HomeFragment();

                else if (menuItem.getItemId() == R.id.action_procedures)
                    fragment = new ProceduresFragment();

                else if (menuItem.getItemId() == R.id.action_map)
                    fragment = new MapFragment();


                return loadFragment(fragment);
            }
        });
       // bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
            .commit();
            return true;
        }
        return false;
    }

    private void showUpdateDialog(String phoneNumber) {

        //if (dialog.isShowing())
           // dialog.dismiss();

        //init dialog
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setTitle("Almost there!");
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_update_information, null);

        Button btn_update = (Button)sheetView.findViewById(R.id.btn_update);
        TextInputEditText edt_name = (TextInputEditText)sheetView.findViewById(R.id.edt_name);
        TextInputEditText edt_email = (TextInputEditText)sheetView.findViewById(R.id.edt_email);
        TextInputEditText edt_phone = (TextInputEditText)sheetView.findViewById(R.id.edt_phone);

        btn_update.setOnClickListener(view -> {

            if (!dialog.isShowing())
                dialog.show();

            User user = new User(edt_name.getText().toString(),
                    edt_email.getText().toString(),
                    phoneNumber);

            userRef.document(phoneNumber)
            .set(user)
                    .addOnSuccessListener(aVoid -> {
                        bottomSheetDialog.dismiss();
                        if (dialog.isShowing())
                            dialog.dismiss();

                        Common.currentUser = user;
                        bottomNavigationView.setSelectedItemId(R.id.action_home);

                        Toast.makeText(HomeActivity.this, "Thank You!", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    bottomSheetDialog.dismiss();
                    if (dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(HomeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }
}




