package com.example.asm_and103_ph44315;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asm_and103_ph44315.Model.Phone;
import com.example.asm_and103_ph44315.service.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Add_Update extends AppCompatActivity {

    Toolbar toolbar;
    EditText edt_ten, edt_hang, edt_gia, edt_soluong;
    Button btn_submit;
    ImageView img_hinhanh;
    APIService apiService;
    Uri imageUri;
    String type;
//    String id, hinhanh, ten, hang, gia, soluong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

        type = getIntent().getStringExtra("type");
//        id = getIntent().getStringExtra("id");
//        hinhanh = getIntent().getStringExtra("hinhanh");
//        ten = getIntent().getStringExtra("ten");
//        gia = getIntent().getStringExtra("gia");
//        soluong = getIntent().getStringExtra("soluong");

        anhxa();
        img_hinhanh.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edt_ten.getText().toString().trim();
                String hang = edt_hang.getText().toString().trim();
                String gia = edt_gia.getText().toString().trim();
                String soluong = edt_soluong.getText().toString().trim();
                String imgUri = imageUri.toString();

                if (ten.isEmpty() || hang.isEmpty() || gia.isEmpty() || soluong.isEmpty() || imageUri == null) {
                    Toast.makeText(Add_Update.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (type.equals("add")) {
                    Phone phone = new Phone( imgUri,ten, hang, Double.parseDouble(gia), Integer.parseInt(soluong));

                    CallApi();

                    Call<Void> call = apiService.post(phone);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(Add_Update.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(Add_Update.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Add_Update.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                Log.e("loi", String.valueOf(response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }
    private void anhxa() {
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (type.equals("add")) {
            getSupportActionBar().setTitle("Add new products");
        } else {
//            getSupportActionBar().setTitle("Update products - ID: " + id);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();;
            }
        });

        edt_ten = findViewById(R.id.edt_ten);
        edt_hang = findViewById(R.id.edt_hang);
        edt_gia = findViewById(R.id.edt_gia);
        edt_soluong = findViewById(R.id.edt_soluong);
        btn_submit = findViewById(R.id.btn_submit);
        edt_ten = findViewById(R.id.edt_ten);
        img_hinhanh = findViewById(R.id.img_hinhanh);
    }
    private void CallApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);
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