package com.deviyan.crudsqlite.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deviyan.crudsqlite.database.DatabaseHelper;
import com.deviyan.crudsqlite.activities.EditProductActivity;
import com.deviyan.crudsqlite.R;
import com.deviyan.crudsqlite.models.ProductModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{

    private Context context;
    private List <ProductModel> productModels;
    DatabaseHelper databaseHelper;

    public ProductAdapter(Context context, List<ProductModel> productModels) {
        this.context = context;
        this.productModels = productModels;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_item, parent, false);
        return new ProductAdapter.ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {

        //get views
        String id = productModels.get(position).getId();
        String name = productModels.get(position).getName();
        String unit = productModels.get(position).getUnit();
        String price = productModels.get(position).getPrice();
        String date = productModels.get(position).getDataOfExpiry();
        String inv = productModels.get(position).getAvailableInventory();
        String cost = productModels.get(position).getAvailableInventoryCost();
        String image = productModels.get(position).getImagePath();

        double nPrice = Double.parseDouble(price);
        double nCost = Double.parseDouble(cost);

        //set views
        holder.tvName.setText("Product name: " + name);
        holder.tvUnit.setText("Unit: " +unit);
        holder.tvPrice.setText("Price: " + String.format("%.2f", nPrice));
        holder.tvExpiration.setText("Date: " +date);
        holder.tvAvailInv.setText("Available Inventory: " + inv);
        holder.tvAvailCost.setText("Available Inventory Cost: " + String.format("%.2f", nCost));
        holder.ivPhoto.setImageURI(Uri.parse(image));

        holder.mbtnUpdate.setOnClickListener(view -> {
            updateProduct(id, name, unit, price, date, inv, cost, image);
        });

        holder.mbtnDelete.setOnClickListener(view -> {
            deleteProduct(id, position);
        });

    }
    private void deleteProduct(final String id, int position) {

        databaseHelper.deleteProduct(id);

        productModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,productModels.size());

        Toast.makeText(context, "Product  successfully deleted" , Toast.LENGTH_SHORT).show();

    }

    private void updateProduct(String id, String name, String unit, String price, String date, String inv, String cost, String image) {

                Intent intent = new Intent(context, EditProductActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("unit", unit);
                intent.putExtra("price", price);
                intent.putExtra("date", date);
                intent.putExtra("inv", inv);
                intent.putExtra("cost", cost);
                intent.putExtra("imagePath", image);
                intent.putExtra("editMode", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }


    class ProductHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvUnit, tvPrice, tvExpiration, tvAvailInv, tvAvailCost;
        ImageView ivPhoto;
        MaterialButton mbtnUpdate, mbtnDelete;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_product_name);
            tvUnit = itemView.findViewById(R.id.tv_unit);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvExpiration = itemView.findViewById(R.id.tv_date_expiry);
            tvAvailInv = itemView.findViewById(R.id.tv_available_inv);
            tvAvailCost = itemView.findViewById(R.id.tv_available_inv_cost);
            ivPhoto = itemView.findViewById(R.id.iv_display_photo);
            mbtnUpdate = itemView.findViewById(R.id.mbtn_update);
            mbtnDelete = itemView.findViewById(R.id.mbtn_delete);

        }
    }
}
