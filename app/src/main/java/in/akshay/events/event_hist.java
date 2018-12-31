package in.akshay.events;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.grantland.widget.AutofitTextView;

import static com.mikepenz.iconics.Iconics.TAG;


public class event_hist extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";


    RecyclerView recyclerview;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    DatabaseReference list;
    ArrayList<EventDB> data = new ArrayList<>();
    String[] listitems;
    ArrayList<Integer> myImageList;


    public static event_hist newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        event_hist fragment = new event_hist();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_hist, container, false);
        mAuth = FirebaseAuth.getInstance();
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDatabase = FirebaseDatabase.getInstance().getReference();
        list = mDatabase.child("EVENTS");

        listitems = getResources().getStringArray(R.array.cat);
        getUsers();
        myImageList = new ArrayList<>();
        myImageList.add(R.drawable.archery);
        myImageList.add(R.drawable.basketball);
        myImageList.add(R.drawable.bodybuilding);
        myImageList.add(R.drawable.cycling);
        myImageList.add(R.drawable.football);
        myImageList.add(R.drawable.golf);
        myImageList.add(R.drawable.gymnastics);
        myImageList.add(R.drawable.running);
        myImageList.add(R.drawable.shooting);
        myImageList.add(R.drawable.hockey);
        myImageList.add(R.drawable.weightlifting);
        myImageList.add(R.drawable.powerlifting);
        myImageList.add(R.drawable.volleyball);
        myImageList.add(R.drawable.wrestling);
        myImageList.add(R.drawable.kabaddi);
        myImageList.add(R.drawable.boxing);


        return view;

    }

    private void getUsers() {


        mDatabase.child("EVENTS").orderByChild("email").equalTo(Singleton.Instance().getEmail()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                        if (dataSnapshot.getChildrenCount() == 0) {
                            FancyToast.makeText(getActivity(), "You have not submitted any event", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();

                        } else {
                            FancyToast.makeText(getActivity(), "Events submitted by you " + dataSnapshot.getChildrenCount(), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();

                        }
                        data.clear();
                        while (items.hasNext()) {

                            DataSnapshot item = items.next();

                            String eventname, organname, desc, email, phone, city,
                                    venue, category, startdate, enddate, eventid;

                            eventname = String.valueOf(item.child("eventname").getValue());
                            organname = String.valueOf(item.child("organisername").getValue());
                            desc = String.valueOf(item.child("deataileddesc").getValue());
                            email = String.valueOf(item.child("email").getValue());
                            phone = String.valueOf(item.child("organphone").getValue());
                            city = String.valueOf(item.child("city").getValue());
                            category = String.valueOf(item.child("category").getValue());
                            startdate = String.valueOf(item.child("startdate").getValue());
                            enddate = String.valueOf(item.child("enddate").getValue());
                            eventid = String.valueOf(item.child("eventid").getValue());
                            venue = String.valueOf(item.child("venue").getValue());

                            EventDB entry = new EventDB(eventname, organname, desc, email, phone, city, category,
                                    startdate, enddate, venue, eventid);
                            data.add(entry);
                        }


                        recyclerview.setAdapter(new event_hist.RecUsersAdapter(getActivity(), data));
                        recyclerview.getAdapter().notifyDataSetChanged();
                        mDatabase.child("EVENTS").removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }

        );
    }

    private class RecUsersAdapter extends RecyclerView.Adapter<event_hist.RecUsersAdapter.RecViewHolder> {


        private Context context;
        private List<EventDB> data;

        public RecUsersAdapter(Context context, List<EventDB> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public event_hist.RecUsersAdapter.RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.event_hist, parent, false);
            return new event_hist.RecUsersAdapter.RecViewHolder(v);
        }

        @Override
        public void onBindViewHolder(event_hist.RecUsersAdapter.RecViewHolder holder, final int position) {

            final EventDB event = data.get(position);


            holder.category.setText(event.getCategory());
            holder.enddate.setText(event.getEnddate());
            holder.eventname.setText(event.getEventname());
            holder.location.setText("In " + event.getCity());
            holder.startdate.setText(event.getStartdate());
            holder.eventid.setText(event.getEventid());

            for (int i = 0; i < listitems.length; i++) {
                if (event.getCategory().equalsIgnoreCase(listitems[i])) {
                    holder.img1.setImageResource(myImageList.get(i - 1));

                }
            }

           /* if(event.getGender().equalsIgnoreCase("OPEN")){
                holder.gender.setText(event.getGender()+" for All");
            }else {
                holder.gender.setText("Only for "+event.getGender());
            }



            if(event.agereq!=null) {
                holder.agereq.setText("Under "+event.getAgereq());

            }*/


            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent inxt = new Intent(getActivity(), response.class);
                    inxt.putExtra("eventid", event.getEventid());
                    startActivity(inxt);

                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    showDialog(event.getEventname(), event.getDesc(), event.getCity(), event.getPhone(), event.getOrganname(), event.getVenue(), event.getEmail());

                }
            });


            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder;

                    builder = new AlertDialog.Builder(context);

                    builder.setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    remove(event.getEventid());
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
                    data.remove(position);
                    getUsers();
                }
            });


        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        public class RecViewHolder extends RecyclerView.ViewHolder {

            public TextView category;
            public TextView startdate;
            public TextView enddate;
            public TextView location;
            public TextView eventid;
            public ImageView img;
            public AutofitTextView eventname;
            public RaiflatButton btn;
            public ImageView img1;

            public RecViewHolder(View itemView) {
                super(itemView);


                category = (TextView) itemView.findViewById(R.id.category);
                startdate = (TextView) itemView.findViewById(R.id.startDate);
                enddate = (TextView) itemView.findViewById(R.id.endDate);
                location = (TextView) itemView.findViewById(R.id.location);
                eventid = (TextView) itemView.findViewById(R.id.eventid);
                img = (ImageView) itemView.findViewById(R.id.remove);
                eventname = (AutofitTextView) itemView.findViewById(R.id.name);
                btn = (RaiflatButton) itemView.findViewById(R.id.interested);
                img1 = (ImageView) itemView.findViewById(R.id.imgcat);

            }
        }


    }

    public void showDialog(String eventname, String detail, String city, final String phone, String organname, String organaddress) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.event_hist);

        TextView name = (TextView) dialog.findViewById(R.id.name);
        TextView des = (TextView) dialog.findViewById(R.id.description);
        TextView location = (TextView) dialog.findViewById(R.id.city);
        TextView organphone = (TextView) dialog.findViewById(R.id.phone);
        TextView organisername = (TextView) dialog.findViewById(R.id.organname);

        name.setText(eventname);
        des.setText(detail);
        location.setText(city);
        organphone.setText(phone);
        organisername.setText(organname);


        dialog.show();


    }

    public void remove(final String eventid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("EVENTS").orderByChild("eventid").equalTo(eventid);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
                Toast.makeText(getActivity(), eventid + "\n" + " Removed successfull", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });


    }

    public void showDialog(String eventname, String detail, String city, final String phone, String organname, String Venue, String email) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.activity_detail);

        TextView name = (TextView) dialog.findViewById(R.id.name);
        TextView des = (TextView) dialog.findViewById(R.id.description);
        TextView venue = (TextView) dialog.findViewById(R.id.venue);
        TextView location = (TextView) dialog.findViewById(R.id.city);
        TextView organphone = (TextView) dialog.findViewById(R.id.phone);
        TextView organisername = (TextView) dialog.findViewById(R.id.organname);
        TextView emailid = (TextView) dialog.findViewById(R.id.email);


        name.setText(eventname);
        des.setText(detail);
        venue.setText(Venue);
        location.setText(city);
        organphone.setText(phone);
        organisername.setText(organname);
        emailid.setText(email);


        dialog.show();


    }

}


