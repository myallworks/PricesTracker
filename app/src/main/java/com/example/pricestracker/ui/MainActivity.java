package com.example.pricestracker.ui;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pricestracker.R;
import com.example.pricestracker.api.ApiClient;
import com.example.pricestracker.api.ApiInterface;
import com.example.pricestracker.auth.TokenManager;
import com.example.pricestracker.models.Price;
import com.example.pricestracker.ui.chart.PriceChartHelper;
import com.example.pricestracker.utils.Constants;
import com.example.pricestracker.utils.NetworkUtils;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvPrices;
    private PricesAdapter pricesAdapter;
    private TokenManager tokenManager;
    private LineChart priceChart;
    private List<Price> allPrices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tokenManager = new TokenManager(this);
        rvPrices = findViewById(R.id.rvPrices);
        rvPrices.setLayoutManager(new LinearLayoutManager(this));

        priceChart = findViewById(R.id.priceChart);
        PriceChartHelper.setupChart(priceChart);

        fetchPrices();
    }

    private void fetchPrices() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = tokenManager.getToken();
        if (token == null) {
            Toast.makeText(this, "Not authenticated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Price>> call = apiInterface.getPrices(token);

        call.enqueue(new Callback<List<Price>>() {
            @Override
            public void onResponse(Call<List<Price>> call, Response<List<Price>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allPrices = response.body();
                    pricesAdapter = new PricesAdapter(allPrices);
                    rvPrices.setAdapter(pricesAdapter);
                    PriceChartHelper.updateChart(priceChart, allPrices);

                    checkForPriceChanges(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Price>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkForPriceChanges(List<Price> newPrices) {
        if (allPrices.isEmpty()) {
            allPrices = newPrices;
            return;
        }

        for (Price newPrice : newPrices) {
            Price oldPrice = findPriceById(newPrice.getId());
            if (oldPrice != null && oldPrice.getPrice() != newPrice.getPrice()) {
                showPriceChangeNotification(oldPrice, newPrice);
            }
        }
        allPrices = newPrices;
    }

    private Price findPriceById(String id) {
        for (Price price : allPrices) {
            if (price.getId().equals(id)) {
                return price;
            }
        }
        return null;
    }

    private void showPriceChangeNotification(Price oldPrice, Price newPrice) {
        String title = "Price Change: " + newPrice.getName();
        String message = String.format(Locale.getDefault(),
                "From $%.2f to $%.2f (%.2f%%)",
                oldPrice.getPrice(),
                newPrice.getPrice(),
                ((newPrice.getPrice() - oldPrice.getPrice()) / oldPrice.getPrice()) * 100
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Constants.FCM_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_price_alert)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(newPrice.getId().hashCode(), builder.build());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterPrices(newText);
                    return true;
                }
            });
        }

        MenuItem darkModeItem = menu.findItem(R.id.menu_dark_mode);
        darkModeItem.setChecked(isDarkModeEnabled());
        return true;
    }

    private void filterPrices(String query) {
        List<Price> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            filteredList.addAll(allPrices);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Price price : allPrices) {
                if (price.getId().toLowerCase().contains(lowerCaseQuery) ||
                        price.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(price);
                }
            }
        }
        pricesAdapter.updatePrices(filteredList);
    }

    private boolean isDarkModeEnabled() {
        return (getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_logout) {
            tokenManager.clearToken();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        } else if (id == R.id.menu_dark_mode) {
            boolean isDarkMode = !item.isChecked();
            item.setChecked(isDarkMode);
            AppCompatDelegate.setDefaultNightMode(
                    isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}