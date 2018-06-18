package teamg.hochschulestralsund.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import teamg.hochschulestralsund.ContactItemFragment;
import teamg.hochschulestralsund.ContactItemFragment.OnListFragmentInteractionListener;
import teamg.hochschulestralsund.R;
import teamg.hochschulestralsund.sql.Person;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Meeting} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class ContactMyItemRecyclerViewAdapter extends RecyclerView.Adapter<ContactMyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Person> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final ContactItemFragment itemFragment;

    public ContactMyItemRecyclerViewAdapter(ArrayList<Person> items, OnListFragmentInteractionListener listener, ContactItemFragment itemFragment) {
        mValues = items;
        mListener = listener;
        this.itemFragment = itemFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.person = mValues.get(position);
        holder.textView_contact_name.setText(mValues.get(position).toString());
        holder.textView_contact_email.setText(mValues.get(position).mail);
        holder.textView_contact_phone.setText(mValues.get(position).telephone);

        try {
            File imgFile = new File(holder.person.person_picture_path);

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imageView_contact_1.setImageBitmap(myBitmap);
            }
        } catch (Exception e) {

        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textView_contact_name;
        public final TextView textView_contact_email;
        public final TextView textView_contact_phone;
        public final ImageView imageView_contact_1;

        public Person person;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textView_contact_name = view.findViewById(R.id.textView_contact_name);
            textView_contact_email = view.findViewById(R.id.textView_contact_email);
            textView_contact_phone = view.findViewById(R.id.textView_contact_phone);
            imageView_contact_1 = view.findViewById(R.id.imageView_contact_1);
        }
    }
}
