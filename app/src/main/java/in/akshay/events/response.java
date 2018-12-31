package in.akshay.events;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.grantland.widget.AutofitTextView;

public class response extends AppCompatActivity {

    RecyclerView recyclerview;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    DatabaseReference list;
    Bundle bundle;
    ArrayList<getInterDB> data = new ArrayList<>();
    ColorGenerator generator;
    TextDrawable.IBuilder builder;
    LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        mAuth = FirebaseAuth.getInstance();
        bundle=getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String tittle = "Interested Players ";
        setTitle(tittle);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorFormMasterElementBackground));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(layoutManager);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        list = mDatabase.child("INTERESTED");
        generator = ColorGenerator.MATERIAL;
        builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .roundRect(3);
        Toast.makeText(response.this, String.valueOf(bundle.get("eventid")), Toast.LENGTH_SHORT).show();
        getUsers();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void getUsers() {


        mDatabase.child("INTERESTED").orderByChild("eventid").equalTo(String.valueOf(bundle.get("eventid"))).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                        Toast.makeText(response.this, "Total Responses : " + dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                        data.clear();
                        while (items.hasNext()) {

                            DataSnapshot item = items.next();

                            String name,email,mobile,query,pquerykey,eventid,team;

                            name = String.valueOf(item.child("name").getValue());
                            pquerykey = String.valueOf(item.child("pquerykey").getValue());
                            query = String.valueOf(item.child("query").getValue());
                            email = String.valueOf(item.child("email").getValue());
                            mobile = String.valueOf(item.child("mobile").getValue());
                            eventid = String.valueOf(item.child("eventid").getValue());
                            team=String.valueOf(item.child("teamname").getValue());

                            getInterDB entry = new getInterDB(eventid,name,email,mobile,query,pquerykey,team);
                            data.add(entry);
                        }


                        recyclerview.setAdapter(new response.RecUsersAdapter(response.this, data));
                        recyclerview.getAdapter().notifyDataSetChanged();
                        mDatabase.child("EVENTS").removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }

        );
    }
    private class RecUsersAdapter extends RecyclerView.Adapter<response.RecUsersAdapter.RecViewHolder> {


        private Context context;
        private List<getInterDB> data;

        public RecUsersAdapter(Context context, List<getInterDB> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public response.RecUsersAdapter.RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.inter_player, parent, false);
            return new response.RecUsersAdapter.RecViewHolder(v);
        }

        @Override
        public void onBindViewHolder(response.RecUsersAdapter.RecViewHolder holder, final int position) {

            final getInterDB event = data.get(position);

            int color2 = generator.getColor(event.getEmail());
            TextDrawable ic1 = builder.build(String.valueOf(event.getName().charAt(0)), color2);




            holder.name.setText(event.getName());
            holder.email.setText(event.getEmail());
            holder.phone.setText(event.getMobile());
            holder.img.setImageDrawable(ic1);

            if(event.getNameteam()!=null) {
                holder.team.setText("Team "+event.getNameteam());
            }
            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder;

                    builder = new AlertDialog.Builder(context);

                    builder.setTitle("Call Participant")
                            .setMessage("Are you sure you want to call "+ event.getName()+ "?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", event.getMobile(), null));
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
            holder.phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder;

                    builder = new AlertDialog.Builder(context);

                    builder.setTitle("Call Participant")
                            .setMessage("Are you sure you want to call "+ event.getName()+ "?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", event.getMobile(), null));
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });








        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        public class RecViewHolder extends RecyclerView.ViewHolder {

            public TextView email;
            public TextView phone;
            public ImageView img;
            public ImageView call;
            public ImageView mail;
            public TextView team;
            public AutofitTextView name;


            public RecViewHolder(View itemView) {
                super(itemView);


                phone=(TextView)itemView.findViewById(R.id.phone);
                email = (TextView) itemView.findViewById(R.id.email);
                img = (ImageView) itemView.findViewById(R.id.imgview);
                name = (AutofitTextView) itemView.findViewById(R.id.name);
                call = (ImageView) itemView.findViewById(R.id.call);
                mail= (ImageView) itemView.findViewById(R.id.mail);
                team=(TextView)itemView.findViewById(R.id.teamname);

            }
        }


    }












}
