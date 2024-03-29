package com.example.aplikacjakurierska.ActivityCustomer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikacjakurierska.ActivityClient.ChooseProductActivity;
import com.example.aplikacjakurierska.ActivityClient.HistoryOrderActivity;
import com.example.aplikacjakurierska.ActivityClient.LoginActivity;
import com.example.aplikacjakurierska.ActivityClient.MainNewActivity;
import com.example.aplikacjakurierska.ActivityClient.OrderAcitivty;
import com.example.aplikacjakurierska.R;
import com.example.aplikacjakurierska.retrofit.RetrofitServ;
import com.example.aplikacjakurierska.retrofit.iapi.ProductApi;
import com.example.aplikacjakurierska.retrofit.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddingProductsCustomerActivity extends AppCompatActivity
        implements ProductCustomerAdapter.OnStudyListener
{

String id;
int SELECT_PICTURE =200;
ImageView imageGallery;
Button imageButton;
    private static final int REQUEST_CODE_ADD_PRODUCT = 1;


    private List<Product> productList;
    private ProductCustomerAdapter.OnStudyListener monStudylistener;

    FloatingActionButton floatingActionButton;
    ProductCustomerAdapter productCustomerAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_products_customer_main);
        viewListProduct();
        addProductCustomers();
        monStudylistener = this;
        productCustomerAdapter = new ProductCustomerAdapter(new ArrayList<>(), monStudylistener,true);

         Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarBack);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddingProductsCustomerActivity.this, MainNewActivity.class);
                startActivity(intent);
            }
        });
        toolbar.setNavigationOnClickListener(view -> onBackPressed());


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.customer_menu, menu);
        return super.onCreateOptionsMenu(menu);    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.allordermenuCustomer) {
            Intent intent1 = new Intent(this, AllOrderActivity.class);
            this.startActivity(intent1);
            finish();
            return true;
        }
        if (id == R.id.addProductMenuCustomer) {
            Intent intent2 = new Intent(this, AddingProductsCustomerActivity.class);
            this.startActivity(intent2);
            finish();
            return true;
        }
        if (id == R.id.shoppingcartmenu) {
            Intent intent2 = new Intent(this,OrderAcitivty.class);
            this.startActivity(intent2);
            finish();
            return true;
        }
        if (id == R.id.logoutMenuCustomer) {
            SharedPreferences sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent2 = new Intent(this, LoginActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }
    private void addProductCustomers() {
        SharedPreferences sp = getSharedPreferences("main",0);
        String token1 = sp.getString("token", null);
        floatingActionButton = findViewById(R.id.floatingActionButtonAdd);
        floatingActionButton.setOnClickListener(view -> {
            Dialog dialog = new Dialog(AddingProductsCustomerActivity.this);
            dialog.setContentView(R.layout.adding_products_customer);
            imageGallery = dialog.findViewById(R.id.galleryImage);
            imageButton =dialog.findViewById(R.id.galleryButton);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageChooser();
                }});
            AppCompatButton buttonAddProductForSale =dialog.findViewById(R.id.buttonEditProductForSale);
            EditText nameProductAdd =dialog.findViewById(R.id.nameProductAdd);
            ImageView imageView = dialog.findViewById(R.id.galleryImage);
            EditText priceProductAdd  =dialog.findViewById(R.id.priceProductAdd);
            EditText descriptionProductAdd  =dialog.findViewById(R.id.descriptionProductAdd);
            RetrofitServ retrofitServ = new RetrofitServ();
            ProductApi productApi = retrofitServ.getRetrofit().create(ProductApi.class);
            buttonAddProductForSale.setOnClickListener(views -> {
                String nameproduct = String.valueOf(nameProductAdd.getText());
                String priceproduct = String.valueOf(priceProductAdd.getText());
                String descriptionproduct = String.valueOf(descriptionProductAdd.getText());
                Product productadd = new Product();
                productadd.setProductName(nameproduct);
                productadd.setProductPrice(Double.valueOf(priceproduct));
                productadd.setProductDescription(descriptionproduct);
                productApi.add(
                        "Bearer "+token1,
                        productadd).enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        productList.add(response.body());
                        productCustomerAdapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Toast.makeText(AddingProductsCustomerActivity.this, "Nie zapisano produktu", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(AddingProductsCustomerActivity.class.getName() ).log(Level.SEVERE,"Error ");
                    }
                });
                Intent intent = new Intent(getApplicationContext(), AddingProductsCustomerActivity.class);
                startActivity(intent);
            });
            dialog.show();
        });
    }
    public   void viewListProduct() {
        SharedPreferences sp = getSharedPreferences("main",0);
        String token1 = sp.getString("token", null);
        System.out.println(token1);
        RecyclerView recyclertest = findViewById(R.id.recyclerViewCustomerProduct);
        RetrofitServ retrofitServ = new RetrofitServ();
        ProductApi productApi = retrofitServ.getRetrofit().create(ProductApi.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclertest.setLayoutManager(linearLayoutManager);
        productApi.getAll(
                "Bearer "+token1
        ).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                    List<Product> productResponse = response.body();
                    if(productResponse !=null){
                        productList = productResponse;
                        if(!productList.isEmpty()){
                            productCustomerAdapter = new ProductCustomerAdapter(productList,monStudylistener,true);
                            recyclertest.setAdapter(productCustomerAdapter);


                        }else {
                            System.out.println("Pusta lista");
                        }
                    }else {
                        System.out.println("odpowiddz Api jest pusta");
                    }
                }else {
                    System.out.println("błąd odpowiedzi");
                }
                Toast.makeText(AddingProductsCustomerActivity.this, "Pomyślnie zapisano", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(AddingProductsCustomerActivity.this, "Wystąpił błąd", Toast.LENGTH_SHORT).show();
                Logger.getLogger(AddingProductsCustomerActivity.class.getName()).log(Level.SEVERE,"Błąd");
            }

        }) ;

    }
    private void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"SELECT PICTURE"),SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode,int result,Intent data){
        super.onActivityResult(requestCode,result,data);
        if (result== RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri){
                    imageGallery.setImageURI(selectedImageUri);
                }
            }
        }
    }





    @Override
    public void onStudyClick(int position) {
        Product p = productList.get(position);
        Intent intent = new Intent(getApplicationContext(), EditProductActivity.class);
        intent.putExtra("productName",p.getProductName());
        intent.putExtra("productPrice",p.getProductPrice().toString());
        intent.putExtra("productDescription",p.getProductDescription());
        intent.putExtra("id",p.getId());
        startActivity(intent);
    }



    @Override
    public void onStudyLongClick(int position, long id) {
        SharedPreferences sp = getSharedPreferences("main",0);
        String token1 = sp.getString("token", null);

        Product p = productList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(AddingProductsCustomerActivity.this)
                .setTitle("Usuń produkt")
                        .setIcon(R.drawable.delete_icon)
                                .setMessage("Czy na pewno usunąć ten produkt ?")
                                        .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                RetrofitServ retrofitServ = new RetrofitServ();
                                                ProductApi productApi = retrofitServ.getRetrofit().create(ProductApi.class);

                                                    productApi.deleteById(
                                                            "Bearer "+token1,
                                                            Long.valueOf(id)).enqueue(new Callback<Void>() {
                                                        @Override
                                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                                            System.out.println("git");


                                                        }

                                                        @Override
                                                        public void onFailure(Call<Void> call, Throwable t) {

                                                        }
                                                    });
                                                Intent intent = new Intent(getApplicationContext(), AddingProductsCustomerActivity.class);
                                                startActivity(intent);

                                            };
                                        })
                                                .setNegativeButton("NIE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
builder.show();
    }


}




