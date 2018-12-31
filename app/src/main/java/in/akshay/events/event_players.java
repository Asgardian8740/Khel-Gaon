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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;
import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import me.grantland.widget.AutofitTextView;
import me.riddhimanadib.formmaster.FormBuilder;
import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormElementPickerDate;
import me.riddhimanadib.formmaster.model.FormElementPickerSingle;
import me.riddhimanadib.formmaster.model.FormElementTextEmail;
import me.riddhimanadib.formmaster.model.FormElementTextMultiLine;
import me.riddhimanadib.formmaster.model.FormElementTextPhone;
import me.riddhimanadib.formmaster.model.FormElementTextSingleLine;
import me.riddhimanadib.formmaster.model.FormHeader;
import ng.max.slideview.SlideView;
import spencerstudios.com.fab_toast.FabToast;

import static com.mikepenz.iconics.Iconics.TAG;


public class event_players extends Fragment {


    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public DatabaseReference mDatabase;
    DatabaseReference list;
    int x;
    RecyclerView mRecyclerView;
    FormBuilder mFormBuilder;





    public static event_players newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        event_players fragment = new event_players();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);


    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        setupForm(view);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        list = mDatabase.child("EVENTS");
        SlideView slideView = (SlideView) view.findViewById(R.id.slideView);
        slideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                // vibrate the device

                if (mFormBuilder.isValidForm()) {
                    addInitialDataToFirebase();
                } else {
                    FabToast.makeText(getActivity(), "Complete Required Fields", FabToast.LENGTH_SHORT, FabToast.ERROR, FabToast.POSITION_CENTER).show();

                }

                // go to a new activity

            }
        });

       /* MaterialSpinner spinner = (MaterialSpinner)view.findViewById(R.id.gender);
        spinner.setItems("MALE", "FEMALE", "BOTH");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
               gender=item;
            }
        });



        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                enddate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });*/


       /* submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mFormBuilder.isValidForm()){
                 addInitialDataToFirebase();
                }else {
                    FabToast.makeText(getActivity(), "Complete Required Fields", FabToast.LENGTH_SHORT, FabToast.ERROR,  FabToast.POSITION_CENTER).show();

                }



            }
        });*/


        return view;
    }


    public List<list_event> geteventdetails() {

        List<list_event> mock = new ArrayList<>();
        list_event test = new list_event();
        BaseFormElement eventname = mFormBuilder.getFormElement(101);
        BaseFormElement venue = mFormBuilder.getFormElement(102);
        BaseFormElement city = mFormBuilder.getFormElement(103);
        BaseFormElement start = mFormBuilder.getFormElement(104);
        BaseFormElement end = mFormBuilder.getFormElement(105);
        BaseFormElement sport = mFormBuilder.getFormElement(106);
       /* BaseFormElement gender = mFormBuilder.getFormElement(221);
        BaseFormElement age = mFormBuilder.getFormElement(223);
        BaseFormElement fees = mFormBuilder.getFormElement(222);*/
        BaseFormElement detail = mFormBuilder.getFormElement(131);
        BaseFormElement organname = mFormBuilder.getFormElement(301);
        BaseFormElement mobile = mFormBuilder.getFormElement(302);
       /* BaseFormElement address = mFormBuilder.getFormElement(303);*/
        BaseFormElement emaail = mFormBuilder.getFormElement(304);

        test.setOrganiser(organname.getValue());

        test.setEventname(eventname.getValue());
        test.setCategory(sport.getValue());
        test.setCity(city.getValue());
        test.setVenue(venue.getValue());
        test.setStartdate(start.getValue());
        test.setDeataileddesc(detail.getValue());
        test.setEnddate(end.getValue());
        test.setOrganphone(mobile.getValue());
        test.setEmail(emaail.getValue());



        mock.add(test);
        return mock;


    }

    private void addInitialDataToFirebase() {

        List<list_event> sampleEntries = geteventdetails();


        for (list_event Entry : sampleEntries) {
            String key = list.push().getKey();
            Entry.setEventid(key);
            list.child(key).setValue(Entry);
            x = 1;
            new CDialog(getActivity()).createAlert("Event Added",
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

    private void setupForm(View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(getActivity(), mRecyclerView);

        FormHeader header1 = FormHeader.createInstance("EVENT INFORMATION");
        FormElementTextSingleLine EventName = FormElementTextSingleLine.createInstance()
                .setTitle("Event-Name*")
                .setTag(101)
                .setHint("EventName")
                .setRequired(true);

        String[] games;
        games = getResources().getStringArray(R.array.category);

        List<String> game = Arrays.asList(games);
        FormElementPickerSingle sport = FormElementPickerSingle.createInstance()
                .setTitle("Game*")
                .setOptions(game)
                .setHint("Choose")
                .setPickerTitle("Choose")
                .setTag(106)
                .setRequired(true);
        FormElementTextSingleLine venue = FormElementTextSingleLine.createInstance()
                .setTitle("Venue*")
                .setTag(102)
                .setHint("Venue")
                .setRequired(true);
        FormElementTextSingleLine city = FormElementTextSingleLine.createInstance()
                .setTitle("City*")
                .setTag(103)
                .setHint("City")
                .setRequired(true);
        FormElementPickerDate StartDate = FormElementPickerDate.createInstance()
                .setTag(104)
                .setTitle("StartDate*")
                .setHint("Choose")
                .setDateFormat("MMM dd, yyyy")
                .setRequired(true);
        FormElementPickerDate EndDate = FormElementPickerDate.createInstance()
                .setTag(105)
                .setTitle("EndDate*")
                .setHint("Choose")
                .setDateFormat("MMM dd, yyyy")
                .setRequired(true);

        FormHeader header4 = FormHeader.createInstance("FOR ENQUIRY CONTACT");
        FormElementTextSingleLine organname = FormElementTextSingleLine.createInstance()
                .setTitle("Organiser Name*")
                .setTag(301)
                .setValue(Singleton.Instance().getName())
                .setHint("Organiser Name")
                .setRequired(true);
        FormElementTextPhone organphone = FormElementTextPhone.createInstance()
                .setTitle("Mobile No*")
                .setTag(302)
                .setHint("Mobile No")
                .setRequired(true);
       /* FormElementTextSingleLine address = FormElementTextSingleLine.createInstance()
                .setTitle("Address*")
                .setTag(303)
                .setHint("Address")
                .setRequired(true);*/
        FormElementTextEmail email = FormElementTextEmail.createInstance()
                .setTag(304)
                .setTitle("Email")
                .setValue(Singleton.Instance().getEmail());



       /* FormHeader header2 = FormHeader.createInstance("PLAYERS DETAILS");
        List<String> Gender = new ArrayList<>();
        Gender.add("Male");
        Gender.add("Female");
        Gender.add("Open");
        FormElementPickerSingle gender = FormElementPickerSingle.createInstance()
                .setTitle("Gender*")
                .setOptions(Gender)
                .setPickerTitle("Choose")
                .setHint("Choose")
                .setTag(221)
                .setRequired(true);
        FormElementTextNumber fees = FormElementTextNumber.createInstance()
                .setTitle("Registration Fees")
                .setTag(222)
                .setHint("If any")
                .setRequired(false);
        FormElementTextNumber age = FormElementTextNumber.createInstance()
                .setTitle("Age Requirement")
                .setTag(223)
                .setHint("If any")
                .setRequired(false);*/
        FormHeader header3 = FormHeader.createInstance("EVENT DESCRIPTION");
        FormElementTextMultiLine eventdetails = FormElementTextMultiLine.createInstance()
                .setTitle("Event in DESCRIPTION")
                .setTag(131)
                .setHint("Any further Details")
                .setRequired(false);



        List<BaseFormElement> formItems = new ArrayList<>();
        formItems.add(header1);
        formItems.add(EventName);
        formItems.add(sport);
        formItems.add(venue);
        formItems.add(city);
        formItems.add(StartDate);
        formItems.add(EndDate);
        formItems.add(header3);
        formItems.add(eventdetails);
        formItems.add(header4);
        formItems.add(organname);
        formItems.add(organphone);
        formItems.add(email);

        mFormBuilder.addFormElements(formItems);
    }





}


