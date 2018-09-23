package ssbahmed.com.quizapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import ssbahmed.com.quizapp.Model.User;

public class MainActivity extends AppCompatActivity {
    MaterialEditText editNewuser, editNewPassword, editNeeEmail;//for sign up
    MaterialEditText editUser, editPassword;//for sign in
    Button buttonSignIn, buttonSignUp;
    FirebaseDatabase mDatabase;
    DatabaseReference mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference("Users");

        editUser = findViewById(R.id.user);
        editPassword = findViewById(R.id.password);
        buttonSignIn = findViewById(R.id.button_sign_in);
        buttonSignUp = findViewById(R.id.button_sign_up);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please fill the information");
        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up, null);

        editNewuser = sign_up_layout.findViewById(R.id.editNewUser);
        editNewPassword = sign_up_layout.findViewById(R.id.editPassword);
        editNeeEmail = sign_up_layout.findViewById(R.id.editEmail);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);
        alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final User user = new User(editNewuser.getText().toString().trim(),
                        editNewPassword.getText().toString().trim(), editNeeEmail.getText().toString().trim());
                mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUserName()).exists()) {
                            Toast.makeText(MainActivity.this, "This User already Exist", Toast.LENGTH_SHORT).show();
                        } else {
                            mUsers.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this, "Usersuuccessfully added", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        alertDialog.show();

    }

    private void signIn(final String name, final String pwd) {
        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(name).exists()) {
                    if (!name.isEmpty()) {
                        User logIn = dataSnapshot.child(name).getValue(User.class);
                        if (logIn.getPassword().equals(pwd)) {
                            Toast.makeText(MainActivity.this, "Loging successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(MainActivity.this, " Enter user Name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "  user Is Not Exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
