package com.example.aplikacjakurierska.ActivityClient;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikacjakurierska.ActivityCustomer.AddingProductsCustomerActivity;
import com.example.aplikacjakurierska.R;
import com.example.aplikacjakurierska.retrofit.RetrofitServ;
import com.example.aplikacjakurierska.retrofit.iapi.CustomerOrderApi;
import com.example.aplikacjakurierska.retrofit.iapi.PositionCustomerOrderApi;
import com.example.aplikacjakurierska.retrofit.model.CustomerOrder;
import com.example.aplikacjakurierska.retrofit.model.PositionCustomerOrderWithProductNameDTO;
import com.example.aplikacjakurierska.retrofit.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryOrderActivity extends AppCompatActivity implements HistoryOrderAdapter.OnStudyListener{
    private RecyclerView recyclerView;
    private List<CustomerOrder> historyCustomerOrderLists = new ArrayList<>();
    private List<PositionCustomerOrderWithProductNameDTO> positionCustomerOrderWithProductNameDTOS = new ArrayList<>();
    private HistoryOrderAdapter.OnStudyListener monStudylistenerHistory;
    HistoryOrderAdapter historyOrderAdapter;
    DialoghistoryAdapter dialoghistoryAdapter;
    RecyclerView recyclerCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order_client);
        monStudylistenerHistory = this;
        viewListProduct();

        historyOrderAdapter = new HistoryOrderAdapter(new ArrayList<>(), monStudylistenerHistory);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarBack);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryOrderActivity.this, MainNewActivity.class);
                startActivity(intent);
            }
        });
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.new_menu, menu);
        return super.onCreateOptionsMenu(menu);    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.historyordermenu) {
            Intent intent1 = new Intent(this,HistoryOrderActivity.class);
            this.startActivity(intent1);
            finish();
            return true;
        }
        if (id == R.id.cartShop) {
            Intent intent2 = new Intent(this,OrderAcitivty.class);
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
        if (id == R.id.logoutmenu) {
            SharedPreferences sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent2 = new Intent(this,LoginActivity.class);
            startActivity(intent2);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onStudyClick(int position) {
        SharedPreferences sp = getSharedPreferences("main",0);
        String token1 = sp.getString("token", null);
        Long userId = sp.getLong("id",0);

        if (position >= 0 && position < historyCustomerOrderLists.size()) {
            Long customerOrder = historyCustomerOrderLists.get(position).getId();
            // Reszta kodu...
        } else {
            // Obsłuż przypadek, gdy position jest poza zakresem
            // Możesz wyświetlić komunikat lub podjąć odpowiednie działania.
        }
        Long customerOrder = historyCustomerOrderLists.get(position).getId();
//        CustomerOrder customerOrderChoose = historyCustomerOrderLists.get(position);
//        System.out.println(customerOrderChoose);
        Dialog dialog = new Dialog(HistoryOrderActivity.this);
        dialog.setContentView(R.layout.preview_history_order);
getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
dialog.setCancelable(true);
dialog.setCanceledOnTouchOutside(true);

        Button buttonBack = dialog.findViewById(R.id.buttonHistoryBack);
        RecyclerView recyclerViewHistory = dialog.findViewById(R.id.history_position_list_recycler);
        RetrofitServ retrofitServ = new RetrofitServ();
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        Window window = dialog.getWindow();
        if (window != null) {
            // Ustaw rozmiar i umiejscowienie okna dialogowego
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER); // Możesz dostosować to do swoich potrzeb
        }

        PositionCustomerOrderApi positionCustomerOrderApi = retrofitServ.getRetrofit().create(PositionCustomerOrderApi.class);
        positionCustomerOrderApi.getAllPositionCustomerOrdersWithProductNamesByOrderId("Bearer " +token1,customerOrder).enqueue(new Callback<List<PositionCustomerOrderWithProductNameDTO>>() {
            @Override
            public void onResponse(Call<List<PositionCustomerOrderWithProductNameDTO>> call, Response<List<PositionCustomerOrderWithProductNameDTO>> response) {
                Toast.makeText(HistoryOrderActivity.this, "git", Toast.LENGTH_SHORT).show();
                if(response.isSuccessful()){
                    List<PositionCustomerOrderWithProductNameDTO> productResponse = response.body();
                    if(productResponse !=null){
                        positionCustomerOrderWithProductNameDTOS= productResponse;
                        if(!positionCustomerOrderWithProductNameDTOS.isEmpty()){
                            dialoghistoryAdapter = new DialoghistoryAdapter(positionCustomerOrderWithProductNameDTOS);
                            recyclerViewHistory.setAdapter(dialoghistoryAdapter);

                        }else {
                            System.out.println("Pusta lista");
                        }
                    }else {
                        System.out.println("odpowiddz Api jest pusta");
                    }
                }else {
                    System.out.println("błąd odpowiedzi");
                }
                Toast.makeText(HistoryOrderActivity.this, "pomylsnie zapisano", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<List<PositionCustomerOrderWithProductNameDTO>> call, Throwable t) {

            }
        });
        buttonBack.setOnClickListener(x->{
            dialog.cancel();
        });
dialog.show();

    }

    @Override
    public void onStudyLongClick(int position, long id) {

    }



    public   void viewListProduct() {
        SharedPreferences sp = getSharedPreferences("main",0);
        String token1 = sp.getString("token", null);
        Long userId = sp.getLong("id",0);
        RecyclerView recyclerCard = findViewById(R.id.history_order_list_recycler);
        RetrofitServ retrofitServ = new RetrofitServ();
        CustomerOrderApi customerOrderApi = retrofitServ.getRetrofit().create(CustomerOrderApi.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerCard.setLayoutManager(linearLayoutManager);

        customerOrderApi.findByUserId(
                "Bearer "+token1,
                userId
        ).enqueue(new Callback<List<CustomerOrder>>() {
            @Override
            public void onResponse(Call<List<CustomerOrder>> call,
                                   Response<List<CustomerOrder>> response) {
                if(response.isSuccessful()){
                    List<CustomerOrder> productResponse = response.body();
                    if(productResponse !=null){
                        historyCustomerOrderLists = productResponse;
                        System.out.println(historyCustomerOrderLists);
                        if(!historyCustomerOrderLists.isEmpty()){
                            historyOrderAdapter    = new HistoryOrderAdapter(historyCustomerOrderLists, monStudylistenerHistory);
                            recyclerCard.setAdapter(historyOrderAdapter);
                        }else {
                            System.out.println("Pusta lista");
                        }
                    }else {
                        System.out.println("odpowiddz Api jest pusta");
                    }
                }else {
                    System.out.println("błąd odpowiedzi");
                }
                Toast.makeText(HistoryOrderActivity.this, "pomylsnie zapisano", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<List<CustomerOrder>> call, Throwable t) {
                Toast.makeText(HistoryOrderActivity.this, "Nie zapisano jest bład", Toast.LENGTH_SHORT).show();
                Logger.getLogger(AddingProductsCustomerActivity.class.getName()).log(Level.SEVERE,"Błąd");
            }

        }) ;

    }
}
