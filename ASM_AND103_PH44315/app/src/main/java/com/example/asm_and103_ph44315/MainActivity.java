package com.example.asm_and103_ph44315;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asm_and103_ph44315.Adapter.AdapterPhone;
import com.example.asm_and103_ph44315.Model.Phone;
import com.example.asm_and103_ph44315.service.APIService;
import com.example.asm_and103_ph44315.service.ClickListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ArrayList<Phone> list = new ArrayList<>();
    RecyclerView rcv;
    AdapterPhone adapterPhone;
    APIService apiService;
    Uri imageUri;
    Toolbar toolbar;
    EditText edt_ten, edt_hang, edt_gia, edt_soluong;
    Button btn_submit, btn_cancel;
    ImageView img_hinhanh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcv= findViewById(R.id.rcv);
        adapterPhone = new AdapterPhone(MainActivity.this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        rcv.setLayoutManager(linearLayoutManager);
        rcv.setAdapter(adapterPhone);
        adapterPhone.notifyDataSetChanged();
        //
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setSubtitle("Phone Management");
        //
        CallApi();

        Call<ArrayList<Phone>> call = apiService.getPhone();

        call.enqueue(new Callback<ArrayList<Phone>>() {
            @Override
            public void onResponse(Call<ArrayList<Phone>> call, Response<ArrayList<Phone>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    adapterPhone = new AdapterPhone(MainActivity.this, list);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                    rcv.setLayoutManager(linearLayoutManager);
                    rcv.setAdapter(adapterPhone);
                    adapterPhone.setClickListener(new ClickListener() {
                        @Override
                        public void UpdateItem(Phone phone) {
                            OpenDialogUpdate(phone);
                        }
                    });
                    adapterPhone.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Phone>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_them) {
            Intent intent = new Intent(MainActivity.this, Add_Update.class);
            intent.putExtra("type", "add");
            startActivity(intent);
        } else if (item.getItemId() == R.id.item_dangxuat) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            finishAffinity();
            startActivity(new Intent(MainActivity.this, Welcome.class));
            Toast.makeText(MainActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void CallApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);
    }
    private void OpenDialogUpdate(Phone phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_update, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        edt_ten = view.findViewById(R.id.edt_ten);
        edt_hang = view.findViewById(R.id.edt_hang);
        edt_gia = view.findViewById(R.id.edt_gia);
        edt_soluong = view.findViewById(R.id.edt_soluong);
        btn_submit = view.findViewById(R.id.btn_submit);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        img_hinhanh = view.findViewById(R.id.img_hinhanh);

        edt_ten.setText(phone.getTen());
        edt_hang.setText(phone.getHang());
        edt_gia.setText(String.valueOf(phone.getGia()));
        edt_soluong.setText(String.valueOf(phone.getSoluong()));
        Glide.with(MainActivity.this)
                .load(phone.getHinhanh())
                .centerCrop()
                .into(img_hinhanh);
        img_hinhanh.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten = edt_ten.getText().toString().trim();
                String hang = edt_hang.getText().toString().trim();
                String gia = edt_gia.getText().toString().trim();
                String soluong = edt_soluong.getText().toString().trim();
                String imgUri = imageUri.toString();

                if (ten.isEmpty() || hang.isEmpty() || gia.isEmpty() || soluong.isEmpty() || imageUri == null) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                phone.setTen(ten);
                phone.setHang(hang);
                phone.setGia(Double.valueOf(gia));
                phone.setSoluong(Integer.valueOf(soluong));
                phone.setHinhanh(imgUri);
                CallApi();
                Call<Void> call = apiService.put( phone.get_id(), phone);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            CallApi();

                            Call<ArrayList<Phone>> call1 = apiService.getPhone();

                            call1.enqueue(new Callback<ArrayList<Phone>>() {
                                @Override
                                public void onResponse(Call<ArrayList<Phone>> call, Response<ArrayList<Phone>> response) {
                                    if (response.isSuccessful()) {
                                        list = response.body();
                                        adapterPhone = new AdapterPhone(MainActivity.this, list);
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                                        rcv.setLayoutManager(linearLayoutManager);
                                        rcv.setAdapter(adapterPhone);
                                        adapterPhone.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ArrayList<Phone>> call, Throwable t) {
                                    Log.e("Main", t.getMessage());
                                }
                            });
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    if (imageUri != null) {
                        img_hinhanh.setImageURI(imageUri);
                    }
                }
            }
    );
}