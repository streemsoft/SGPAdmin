package app.streem.sgpadmin.DAO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase {

    private static DatabaseReference databaseReference;
    private static FirebaseAuth firebaseAuth;


    public static DatabaseReference getDatabaseReference(){
        if(databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    public static FirebaseAuth getFirebaseAuth(){
        if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static void logOut(){
        if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
        }else{
            firebaseAuth.signOut();
        }
    }

    public static void loginDatabase(){

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
