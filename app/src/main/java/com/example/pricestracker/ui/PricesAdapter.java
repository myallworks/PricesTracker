package com.example.pricestracker.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pricestracker.R;
import com.example.pricestracker.models.Price;

import java.util.List;
import java.util.Locale;

public class PricesAdapter extends RecyclerView.Adapter<PricesAdapter.PriceViewHolder> {
    private List<Price> prices;

    public PricesAdapter(List<Price> prices) {
        this.prices = prices;
    }

    @NonNull
    @Override
    public PriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_price, parent, false);
        return new PriceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceViewHolder holder, int position) {
        Price price = prices.get(position);
        holder.bind(price);
    }

    @Override
    public int getItemCount() {
        return prices != null ? prices.size() : 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updatePrices(List<Price> newPrices) {
        prices = newPrices;
        notifyDataSetChanged();
    }

    static class PriceViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvCurrentPrice;
        private final TextView tvChange;

        public PriceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCurrentPrice = itemView.findViewById(R.id.tvCurrentPrice);
            tvChange = itemView.findViewById(R.id.tvChange);
        }

        public void bind(Price price) {
            tvName.setText(price.getName());
            tvCurrentPrice.setText(String.format(Locale.getDefault(), "$%.2f", price.getPrice()));

            double changePercent = (price.getChange() / price.getPrice()) * 100;
            String changeText = String.format(Locale.getDefault(), "%.2f (%.2f%%)",
                    price.getChange(), changePercent);
            tvChange.setText(changeText);

            int color = price.getChange() >= 0 ?
                    itemView.getContext().getColor(R.color.green) :
                    itemView.getContext().getColor(R.color.red);
            tvChange.setTextColor(color);
        }
    }
}