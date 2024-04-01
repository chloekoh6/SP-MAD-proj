package com.sp.android_studio_project;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class RedeemShopItemFragment extends DialogFragment {

    private ImageView imageicon;
    private TextView item;
    private TextView pointsneeded;
    private String imageurl;
    private String itemname;
    private String currentUid;
    private String type;
    private String id;
    private int quantity;
    private int points;
    private Button button;

    FirebaseAuth mAuth;
    Context context = this.getContext();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public RedeemShopItemFragment() {
        // Required empty public constructor
    }

    public RedeemShopItemFragment(String image, String item, int points, int quantity, String type, String id){
        this.imageurl = image;
        this.itemname = item;
        this.points = points;
        this.quantity = quantity;
        this.type = type;
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_redeem_shop_item, container, false);

        //linking components
        imageicon = view.findViewById(R.id.itemimage);
        item = view.findViewById(R.id.item);
        pointsneeded = view.findViewById(R.id.pointsneeded);
        button = view.findViewById(R.id.redeembutton);

        String Spoints = String.valueOf(points);

        item.setText(itemname);
        pointsneeded.setText(Spoints);
        Picasso.with(context).load(imageurl).fit().centerInside().into(imageicon);

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),"works", Toast.LENGTH_SHORT).show();
                getEmail();
            }
        });

        return view;
    }

    public void getEmail(){
        db.collection("Users").document(currentUid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            String email = task.getResult().getString("email");
                            getshopdata(email,itemname);
                        } else {
                            Toast.makeText(getContext(),"Search fail",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void getshopdata(String email, String item){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = calendar.getTime();
        String dateString = simpleDateFormat.format(date);

        db.collection("shop").document("redeem").collection("redeem")
                .whereEqualTo("email", email)
                .whereEqualTo("item", item)
                .whereEqualTo("date", dateString)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot result = task.getResult();
                        if (result.isEmpty()) {
                            getUserPoints();
                            setshopdata(email, itemname, dateString);
                        } else {
                            Toast.makeText(getContext(), "You've already redeemed it today!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        System.out.println("Error getting documents: " + task.getException());
                    }
                });

    }

    public void setshopdata(String email, String item, String date){

        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("item", item);
        data.put("date", date);
        DocumentReference docRef = db.collection("shop").document("redeem").collection("redeem").document();
        docRef.set(data);
    }

    public void getUserPoints(){
        db.collection("Users").document(currentUid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            int pts = Math.toIntExact(task.getResult().getLong("points"));
                            minuspoints(points, quantity, pts);

                        } else {
                            Toast.makeText(getContext(),"Search fail",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void minuspoints(int points, int Hquantity, int userpts){

        int newUpoints;
        int newQuantity;
        int upts;
        upts = userpts;
        if(Hquantity != 0){
            if(upts >= points){
                newQuantity = Hquantity - 1;
                newUpoints = upts-points;

                DocumentReference documentReference = db.collection("Users").document(currentUid);

                documentReference.update("points",newUpoints)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Redeemed!", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error updating user data", Toast.LENGTH_SHORT).show();
                            }
                        });

                if(type == "item"){
                    DocumentReference documentReference1= db.collection("shop").document("reusuableitems")
                            .collection("items").document(id);
                    documentReference1.update("quantity", newQuantity);
                } else{
                    DocumentReference documentReference1= db.collection("shop").document("vouchers")
                            .collection("vouchers").document(id);
                    documentReference1.update("quantity", newQuantity);
                }

            } else {
                Toast.makeText(getContext(), "Not enough points", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Fully Redeemed", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}