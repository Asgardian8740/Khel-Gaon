package in.akshay.events;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.grantland.widget.AutofitTextView;


public class participate extends Fragment {

    public DatabaseReference events, intereted, meventdata, minterdata;
    ValueEventListener valueEventListener;
    FirebaseAuth mAuth;
    ArrayList<EventDB> data = new ArrayList<>();
    RecyclerView recyclerview;

    String uid;


    public static participate newInstance(int position) {
        participate fragment = new participate();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);


        return fragment;
    }

    public participate() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /* mPosition = getArguments().getInt("position");*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participate, container, false);

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);


        meventdata = FirebaseDatabase.getInstance().getReference();
        events = meventdata.child("EVENTS");
        minterdata = FirebaseDatabase.getInstance().getReference();
        intereted = minterdata.child("INTERESTED");
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();


        return view;
    }
}





