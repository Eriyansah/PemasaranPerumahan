package com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.Interface.ItemClickListener;
import com.teknokrat.mobile2019.if117fx15312393.pemasaranperumahan.R;

public class BookingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice, txtProductLocation, date, time;
    private ItemClickListener itemClickListener;

    public BookingViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.booking_product_name);
        txtProductPrice = itemView.findViewById(R.id.booking_product_price);
        txtProductLocation = itemView.findViewById(R.id.booking_product_location);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;
    }
}
