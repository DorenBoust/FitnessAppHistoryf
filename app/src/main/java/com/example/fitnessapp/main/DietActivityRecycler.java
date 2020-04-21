package com.example.fitnessapp.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.user.Diet;
import com.example.fitnessapp.user.Meal;
import com.example.fitnessapp.user.Product;
import com.example.fitnessapp.user.ProductDataBase;
import com.example.fitnessapp.user.User;
import com.squareup.picasso.Picasso;

import java.util.Dictionary;
import java.util.List;

public class DietActivityRecycler extends RecyclerView.Adapter<DietActivityRecycler.ProductHolder> {

    private User user;
    private Meal meal;
    private LayoutInflater inflater;

    public DietActivityRecycler(User user, Meal meal, LayoutInflater inflater) {
        this.user = user;
        this.meal = meal;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //R.layout.diet_activity_layout mean R.layout.diet_activity_layout_new
        View v = inflater.inflate(R.layout.diet_activity_layout_new,parent,false);
        return new ProductHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {

        List<Product> products = meal.getProducts();
        Product product = products.get(position);
        String productName = product.getProductName();

        Dictionary<String, ProductDataBase> productDataBase = user.getProductDataBase();

        ProductDataBase productDetails = productDataBase.get(productName);

        String productNameHEB = productDetails.getProductNameHEB();
        String productImage = productDetails.getProductImage();
        String qty = "" + product.getQty() + " " + product.getUnit();

        holder.qty.setText(qty);
        holder.productTitle.setText(productNameHEB);
        Picasso.get().load(productDetails.getProductImage()).into(holder.productImage);

        List<String> alternatives = product.getAlternative();

        if (alternatives.size() != 0) {

            StringBuilder sb = new StringBuilder();


            for (String alternative : alternatives) {

                String alt = productDataBase.get(alternative).getProductNameHEB() + " • ";

                sb.append(alt);

            }

            //remove lest " • "
            sb.delete(sb.length()-2,sb.length());

            holder.productAlternative.setText(sb);

        } else {

            holder.productAlternative.setText("לא הוכנסו חלופות");

        }



    }

    @Override
    public int getItemCount() {
        return meal.getProducts().size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder{
        TextView productTitle;
        TextView qty;
        ImageView productImage;
        TextView productAlternative;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            productTitle = itemView.findViewById(R.id.tv_product_title);
            qty = itemView.findViewById(R.id.diet_activity_unit);
            productImage = itemView.findViewById(R.id.iv_image_product);
            productAlternative = itemView.findViewById(R.id.product_alternative_tv);
        }
    }
}
