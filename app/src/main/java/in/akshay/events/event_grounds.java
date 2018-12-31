package in.akshay.events;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import me.grantland.widget.AutofitTextView;

import static com.mikepenz.iconics.Iconics.TAG;


public class event_grounds extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerview;
    public static final String ARG_PAGE = "ARG_PAGE";
    protected ImageView imgViewCamera, share;
    protected int LOAD_IMAGE_CAMERA = 0, CROP_IMAGE = 1, LOAD_IMAGE_GALLARY = 2;
    private Uri picUri, crop;
    private File pic;
    private boolean mlike = false, checklike;
    private DatabaseReference mdatabaselike;
    long likeno;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    ArrayList<posts> data = new ArrayList<>();
    Bitmap photo;


    //FIREBASE DATABASE FIELDS
    DatabaseReference mUserDatabse;
    DatabaseReference post;
    EditText tag;
    FirebaseUser user;
    SwipeRefreshLayout swipeLayout;


    StorageReference mStorageRef;
    ProgressDialog mProgress;


    private int mPage;

    public static event_grounds newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        event_grounds fragment = new event_grounds();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_grounds, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUserDatabse = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        mdatabaselike = FirebaseDatabase.getInstance().getReference().child("Likes");

        mUserDatabse.keepSynced(true);
        mdatabaselike.keepSynced(true);


        mProgress = new ProgressDialog(getContext());


        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);


        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerview.setHasFixedSize(true);


        post = mUserDatabse.child("Posts");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        tag = (EditText) view.findViewById(R.id.tag);
        imgViewCamera = (ImageView) view.findViewById(R.id.userProfileImageView);
        share = (ImageView) view.findViewById(R.id.sharebtn);
        getData();


        imgViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_GALLARY);


            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserProfile();
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_IMAGE_GALLARY) {
            if (data != null) {

                picUri = data.getData();
                imgViewCamera.setImageURI(picUri);

                //CropImage();
            }
        } else if (requestCode == CROP_IMAGE) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();


                if (extras.getParcelable("data") != null) {
                    photo = extras.getParcelable("data");

                    imgViewCamera.setImageBitmap(photo);

                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_GALLARY);

                }


            }
        }

    }





    @Override
    public void onPause() {
        super.onPause();
    }

    private void saveUserProfile() {

        if (picUri != null) {

           /* mProgress.setTitle("Uploading Post");
            mProgress.setMessage("Please wait....");
            mProgress.show();

            Calendar c = new GregorianCalendar();
            String mytime = java.text.DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());


           /* StorageReference mChildStorage = mStorageRef.child("User_Profile").child(picUri.getLastPathSegment());


            mChildStorage.putFile(picUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {


                    mStorageRef.child("User_Profile").child(picUri.getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                           final String url=String.valueOf(uri);
                            //String imageUrl = String.valueOf(taskSnapshot.getMetadata().getReference().getDownloadUrl());
                            addInitialDataToFirebase(url);
                            Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });*/
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = mStorageRef.child("images/" + UUID.randomUUID().toString());


            ref.putFile(picUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                 ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                     @Override
                                     public void onSuccess(Uri uri) {
                                         //Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_SHORT).show();
                                         progressDialog.dismiss();
                                         addInitialDataToFirebase(uri.toString());
                                     }
                                 });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });



        }

    }


    public List<upload_post> setdata(String imageurl) {

        List<upload_post> mock = new ArrayList<>();
        upload_post test = new upload_post();

        test.setStatus(tag.getText().toString().trim());
        test.setUserid(mAuth.getUid());
        test.setUsername(user.getDisplayName());
        test.setImagurl(imageurl);
        test.setProfilepic(String.valueOf(user.getPhotoUrl()));


        mock.add(test);
        return mock;


    }

    private void addInitialDataToFirebase(String imageurl) {


        List<upload_post> sampleEntries = setdata(imageurl);


        for (upload_post Entry : sampleEntries) {
            String key = post.push().getKey();
            Entry.setPostId(key);
            post.child(key).setValue(Entry);

            new CDialog(getActivity()).createAlert("Post Added",
                    CDConstants.SUCCESS,   // Type of dialog
                    CDConstants.LARGE)    //  size of dialog
                    .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                    .setDuration(2000)   // in milliseconds
                    .setTextSize(CDConstants.LARGE_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                    .show();

            tag.setText("");
            imgViewCamera.setImageDrawable(getResources().getDrawable(R.drawable.baseline_add_a_photo_black_48));


        }


    }

    public void getData() {

        mUserDatabse.child("Posts").orderByChild("userid").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();

                        data.clear();
                        while (items.hasNext()) {

                            DataSnapshot item = items.next();

                            String imageUrl, profilepic, status, username, userid, postid;
                            imageUrl = String.valueOf(item.child("imagurl").getValue());
                            profilepic = String.valueOf(item.child("profilepic").getValue());
                            status = String.valueOf(item.child("status").getValue());
                            username = String.valueOf(item.child("username").getValue());
                            userid = String.valueOf(item.child("userid").getValue());
                            postid = String.valueOf(item.child("postId").getValue());

                            posts entry = new posts(imageUrl, status, userid, username, profilepic, postid);
                            data.add(entry);


                        }
                        recyclerview.setAdapter(new event_grounds.RecUsersAdapter(getActivity(), data));
                        recyclerview.getAdapter().notifyDataSetChanged();
                        mUserDatabse.child("Posts").removeEventListener(this);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
                swipeLayout.setRefreshing(false);
            }
        }, 3000);
    }


    public class RecUsersAdapter extends RecyclerView.Adapter<event_grounds.RecUsersAdapter.RecViewHolder> {


        private Context context;
        private List<posts> data;

        public RecUsersAdapter(Context context, List<posts> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public event_grounds.RecUsersAdapter.RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.organ_no, parent, false);
            return new event_grounds.RecUsersAdapter.RecViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final event_grounds.RecUsersAdapter.RecViewHolder holder, final int position) {

            final posts event = data.get(position);
            //RequestOptions options = new RequestOptions();
            //options.centerCrop();

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            holder.pbar.setBackgroundColor(color);


            holder.username.setText(event.getUsername());
            holder.status.setText("# " + event.getStatus());
            Picasso.get()
                    .load(event.getProfilepic())
                    .into(holder.profilepic);
            Glide.with(context).load(event.getImagurl())
                    //.apply(options)
                    .into(holder.img);

            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(getActivity(),fullscreen.class);
                    i.putExtra("url",event.getImagurl());
                    startActivity(i);
                }
            });


            mdatabaselike.child(event.getPostId()).addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user.getUid())) {
                        holder.like.setLiked(true);
                        if (dataSnapshot.getChildrenCount() != 1) {
                            holder.nooflikes.setText("You and " + (dataSnapshot.getChildrenCount() - 1) + " other people like this");
                        } else {
                            holder.nooflikes.setText("You like this");

                        }

                    } else {
                        holder.nooflikes.setText((dataSnapshot.getChildrenCount()) + " People like this");
                    }


                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });


            holder.like.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {

                    mdatabaselike.child(event.getPostId()).child(user.getUid()).setValue(user.getEmail());


                }

                @Override
                public void unLiked(LikeButton likeButton) {

                    mdatabaselike.child(event.getPostId()).child(user.getUid()).removeValue();

                }
            });















            /*holder.img.setOnClickListener(new View.OnClickListener() {
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
            });*/


        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        public class RecViewHolder extends RecyclerView.ViewHolder {

            public TextView username;
            public ImageView img;
            public CircularImageView profilepic;
            public AutofitTextView status;
            public LinearLayout pbar;
            public TextView nooflikes;
            public LikeButton like;

            public RecViewHolder(View itemView) {
                super(itemView);


                username = (TextView) itemView.findViewById(R.id.username);
                status = (AutofitTextView) itemView.findViewById(R.id.status);
                img = (ImageView) itemView.findViewById(R.id.imgpost);
                profilepic = (CircularImageView) itemView.findViewById(R.id.profilepic);
                pbar = (LinearLayout) itemView.findViewById(R.id.pbar);
                like = (LikeButton) itemView.findViewById(R.id.star_button);
                nooflikes = (TextView) itemView.findViewById(R.id.likes);


            }
        }


    }


}






