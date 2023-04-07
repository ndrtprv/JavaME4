package com.example.javame4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<UserInfo> users;

    CustomAdapter(Context context, ArrayList<UserInfo> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Bitmap bmp = BitmapFactory.decodeByteArray(users.get(position).getPhoto(),
                0, users.get(position).getPhoto().length);

        holder.show_name_txt.setText(String.valueOf(users.get(position).getName()));
        holder.show_surname_txt.setText(String.valueOf(users.get(position).getSurname()));
        holder.show_phone_txt.setText(String.valueOf(users.get(position).getPhone()));
        holder.show_email_txt.setText(String.valueOf(users.get(position).getEmail()));
        holder.show_address_txt.setText(String.valueOf(users.get(position).getAddress()));
        holder.show_image.setImageBitmap(Bitmap.createScaledBitmap(
                bmp, holder.show_image.getWidth(), holder.show_image.getHeight(), false));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView show_name_txt, show_surname_txt, show_phone_txt, show_email_txt, show_address_txt;
        ImageView show_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            show_name_txt = itemView.findViewById(R.id.show_name);
            show_surname_txt = itemView.findViewById(R.id.show_surname);
            show_phone_txt = itemView.findViewById(R.id.show_phone);
            show_email_txt = itemView.findViewById(R.id.show_email);
            show_address_txt = itemView.findViewById(R.id.show_address);
            show_image = itemView.findViewById(R.id.show_image);
        }
    }
}
