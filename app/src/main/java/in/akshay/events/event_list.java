package in.akshay.events;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;
import com.farbod.labelledspinner.LabelledSpinner;
import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rw.loadingdialog.LoadingView;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import me.grantland.widget.AutofitTextView;


public class event_list extends Fragment implements LabelledSpinner.OnItemChosenListener,SwipeRefreshLayout.OnRefreshListener {
    public static final String ARG_PAGE = "ARG_PAGE";


    RecyclerView recyclerview;
    List<String> yourFilterList;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public DatabaseReference indatabase;
    DatabaseReference inlist;
    DatabaseReference list;
    ArrayList<EventDB> data = new ArrayList<>();
    String[] listitems;
    String choice=null;
    String pname = "null";
    String pemail = "null";
    String pmobile = "null";
    String pdesc = "null";
    String peventid = "null";
    String teamname = "null";
    ArrayList<Integer> myImageList;
    int x, y;
    SwipeRefreshLayout swipeLayout;
    FirebaseUser user;




    public static event_list newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        event_list fragment = new event_list();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);


        listitems = getResources().getStringArray(R.array.cat);
        mAuth = FirebaseAuth.getInstance();

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        list = mDatabase.child("EVENTS");
        indatabase = FirebaseDatabase.getInstance().getReference();
        indatabase.keepSynced(true);

        inlist=indatabase.child("INTERESTED");
        yourFilterList= Arrays.asList(listitems);

        LabelledSpinner yourSpinner = (LabelledSpinner)view.findViewById(R.id.your_labelled_spinner);

        yourSpinner.setItemsArray(R.array.category);
        yourSpinner.setOnItemChosenListener(this);

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
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        getdata(choice);
        return view;
    }
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                getUsers();
                swipeLayout.setRefreshing(false);
            }
        }, 3000);
    }
    public void getdata(String Choice){
        if(Choice!=null ){
            getUsers(Choice);
        }else{
            getUsers();
        }

    }
    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        choice=String.valueOf(adapterView.getItemAtPosition(position));
       if(choice.equalsIgnoreCase("ALL")){
           getUsers();
       }else {
           getdata(choice);
       }
    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
         getUsers();
    }


    private void getUsers(String spo) {


        mDatabase.child("EVENTS").orderByChild("category").equalTo(spo).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                        if(dataSnapshot.getChildrenCount()==0){
                            FancyToast.makeText(getActivity(),"Oops no event for your sport", FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();

                        }else {
                            FancyToast.makeText(getActivity(),"Total no of events found "+dataSnapshot.getChildrenCount(), FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

                        }                        data.clear();
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
                            venue=String.valueOf(item.child("venue").getValue());

                            EventDB entry = new EventDB(eventname, organname, desc, email, phone, city,category,
                                    startdate, enddate,venue, eventid);
                            data.add(entry);
                        }


                        recyclerview.setAdapter(new event_list.RecUsersAdapter(getActivity(), data));
                        recyclerview.getAdapter().notifyDataSetChanged();
                        mDatabase.child("EVENTS").removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }

        );
    }
    private void getUsers() {


        mDatabase.child("EVENTS").orderByChild("category").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                        if(dataSnapshot.getChildrenCount()==0){
                            FancyToast.makeText(getActivity(),"Oops no event for your sport", FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();

                        }else {
                            FancyToast.makeText(getActivity(),"Total no of events found "+dataSnapshot.getChildrenCount(), FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

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
                            venue=String.valueOf(item.child("venue").getValue());

                            EventDB entry = new EventDB(eventname, organname, desc, email, phone, city,category,
                                     startdate, enddate,venue, eventid);
                            data.add(entry);
                        }


                        recyclerview.setAdapter(new event_list.RecUsersAdapter(getActivity(), data));
                        recyclerview.getAdapter().notifyDataSetChanged();
                        mDatabase.child("EVENTS").removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }

        );
    }


    public static class EventHolder extends RecyclerView.ViewHolder {

        public TextView category;
        public TextView startdate;
        public TextView enddate;
        public TextView location;
        public TextView eventname;

        public EventHolder(View itemView) {
            super(itemView);

            category = (TextView) itemView.findViewById(R.id.category);
            startdate = (TextView) itemView.findViewById(R.id.startDate);
            enddate = (TextView) itemView.findViewById(R.id.endDate);
            location = (TextView) itemView.findViewById(R.id.location);
            eventname = (TextView) itemView.findViewById(R.id.name);
        }

    }


    public class RecUsersAdapter extends RecyclerView.Adapter<RecUsersAdapter.RecViewHolder> {


        private Context context;
        private List<EventDB> data;

        public RecUsersAdapter(Context context, List<EventDB> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public RecUsersAdapter.RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.custom_event, parent, false);
            return new RecViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final RecViewHolder holder, int position) {

            final EventDB event = data.get(position);


            holder.category.setText(event.getCategory());
            holder.enddate.setText(event.getEnddate());
            holder.eventname.setText(event.getEventname());
            holder.location.setText("In "+event.getCity());
            holder.startdate.setText(event.getStartdate());

            for(int i=0;i<listitems.length;i++){
                if(event.getCategory().equalsIgnoreCase(listitems[i])){
                    holder.img.setImageResource(myImageList.get(i-1));

                }
            }



            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playerdetails(event.getEventid());
                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    showDialog(event.getEventname(), event.getDesc(), event.getCity(), event.getPhone(), event.getOrganname(), event.getVenue(),event.getEmail());

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
            public AutofitTextView eventname;
            public ImageView img;
            public RaiflatButton  btn;

            public RecViewHolder(View itemView) {
                super(itemView);


                category = (TextView) itemView.findViewById(R.id.category);
                startdate = (TextView) itemView.findViewById(R.id.startDate);
                enddate = (TextView) itemView.findViewById(R.id.endDate);
                location = (TextView) itemView.findViewById(R.id.location);
                /*agereq = (TextView) itemView.findViewById(R.id.agereq);*/
                eventname = (AutofitTextView) itemView.findViewById(R.id.name);
                img = (ImageView) itemView.findViewById(R.id.imgcat);
                btn=(RaiflatButton) itemView.findViewById(R.id.participate);
                /*gender=(TextView)itemView.findViewById(R.id.gender);*/


            }
        }


    }

    public void showDialog(String eventname, String detail, String city, final String phone, String organname, String Venue,String email) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.activity_detail);

        TextView name = (TextView) dialog.findViewById(R.id.name);
        TextView des = (TextView) dialog.findViewById(R.id.description);
        TextView venue=(TextView)dialog.findViewById(R.id.venue);
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

   /* public void showCategory() {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Choose your Mode");
        mBuilder.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    getAllUsers();
                    dialogInterface.dismiss();


                } else {
                    getUsers(listitems[i]);

                    dialogInterface.dismiss();

                }
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }*/

    public List<Inters_DB> getplayer(String eventid,String name,String email,String desc,String tmname,String mobile) {

        List<Inters_DB> mock = new ArrayList<>();
        Inters_DB test = new Inters_DB();
        test.setName(name);
        test.setEmail(email);
        test.setMobile(mobile);
        test.setQuery(desc);
        test.setTeamname(tmname);
        test.setEventid(eventid);
        mock.add(test);
        return mock;


    }

    public void playerdetails(final String eventid) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.playerdetails);
        FirebaseUser user = mAuth.getCurrentUser();


        final MaterialEditText name = (MaterialEditText) dialog.findViewById(R.id.name);
        final MaterialEditText email = (MaterialEditText) dialog.findViewById(R.id.email);
        final MaterialEditText mobile = (MaterialEditText) dialog.findViewById(R.id.mobile);
        final MaterialEditText desc = (MaterialEditText) dialog.findViewById(R.id.query);
        final MaterialEditText team = (MaterialEditText) dialog.findViewById(R.id.teamname);
        final RaiflatButton btn = (RaiflatButton) dialog.findViewById(R.id.interested);


        name.setText(user.getDisplayName());
        email.setText(user.getEmail());


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                pname = String.valueOf(name.getText());
                pemail = String.valueOf(email.getText());
                pmobile = String.valueOf(mobile.getText());
                pdesc = String.valueOf(desc.getText());
                teamname=String.valueOf(team.getText());
                peventid = eventid;
                addInitialDataToFirebase(eventid,pname,pemail,pdesc,teamname,pmobile);
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private void addInitialDataToFirebase(String eventid,String pname,String pemail,String pdesc,String teamname,String pmobile) {

        List<Inters_DB> sampleEntries = getplayer(eventid,pname,pemail,pdesc,teamname,pmobile);


        for (Inters_DB Entry : sampleEntries) {
            String key = inlist.push().getKey();
            Entry.setPquerykey(key);
            inlist.child(key).setValue(Entry);
            x = 1;
            new CDialog(getActivity()).createAlert("Thank you for Registering",
                    CDConstants.SUCCESS,   // Type of dialog
                    CDConstants.LARGE)    //  size of dialog
                    .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                    .setDuration(2000)   // in milliseconds
                    .setTextSize(CDConstants.LARGE_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                    .show();


        }
        if (x != 1) {
            new CDialog(getActivity()).createAlert("Error",
                    CDConstants.ERROR,   // Type of dialog
                    CDConstants.LARGE)    //  size of dialog
                    .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                    .setDuration(2000)   // in milliseconds
                    .setTextSize(CDConstants.LARGE_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                    .show();
        }


    }
    @Override
    public void onDestroyView() {
        swipeLayout.removeAllViews();
        super.onDestroyView();
    }


}







