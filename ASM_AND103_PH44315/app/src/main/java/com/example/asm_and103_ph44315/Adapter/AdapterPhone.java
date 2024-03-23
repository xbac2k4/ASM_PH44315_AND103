package com.example.asm_and103_ph44315.Adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.asm_and103_ph44315.Add_Update;
import com.example.asm_and103_ph44315.MainActivity;
import com.example.asm_and103_ph44315.Model.Phone;
import com.example.asm_and103_ph44315.R;
import com.example.asm_and103_ph44315.service.APIService;
import com.example.asm_and103_ph44315.service.ClickListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AdapterPhone extends RecyclerView.Adapter<AdapterPhone.ViewHolder>{
    private final Context context;
    private final ArrayList<Phone> list;
    Phone phone;
    APIService apiService;

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    ClickListener clickListener;

    public AdapterPhone(Context context, ArrayList<Phone> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_phone, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context)
                .load(list.get(position).getHinhanh())
                .centerCrop()
                .into(holder.image);
        holder.tvID.setText("ID: " + list.get(position).get_id());
        holder.tvTen.setText("Name: " + list.get(position).getTen());
        holder.tvHang.setText("Brand: " + list.get(position).getHang());
        holder.tvGia.setText("Price: $" + list.get(position).getGia());
        holder.tvSoluong.setText("Quantity: " + list.get(position).getSoluong()) ;
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = list.get(position);
                if (clickListener !=null){
                    clickListener.UpdateItem(phone);
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = list.get(position);
                OpenDialogDelete(phone);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID, tvTen, tvHang, tvGia, tvSoluong;
        ImageView image;
        ImageButton edit, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tv_id);
            image = itemView.findViewById(R.id.img_hinhanh);
            tvTen = itemView.findViewById(R.id.tv_ten);
            tvHang = itemView.findViewById(R.id.tv_hang);
            tvGia = itemView.findViewById(R.id.tv_gia);
            tvSoluong = itemView.findViewById(R.id.tv_soluong);
            edit = itemView.findViewById(R.id.btn_edit);
            delete = itemView.findViewById(R.id.btn_delete);
        }
    }
    private void OpenDialogDelete(Phone phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có chắc chắn muốn xóa không ?");
        builder.setIcon(R.drawable.warning).setTitle("Cảnh Báo");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CallApi();
                Call<Void> call = apiService.delete(phone.get_id());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            list.remove(phone);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(context, "Xóa thật bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("onResponse: ", t.getMessage());
                    }
                });
            }
        }).setNegativeButton("Cancel", null);
        builder.show();
    }
    private void CallApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);
    }

}
