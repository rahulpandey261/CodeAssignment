package romeo.com.quicktest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by RAJA on 07-06-2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyCustomViewHolder>{


    private Context mContext;
    private List<ImageDetails> mImageDetails;

    public CustomAdapter(List<ImageDetails> imageDetails){

        //this.mContext = context;
        Log.d("ILUD CustomAdapter:", "Test" + imageDetails.size());
        this.mImageDetails = imageDetails;
    }

    @Override
    public MyCustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("ILUD CustomAdapter:", "Test1");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        mContext = parent.getContext();
        return new MyCustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCustomViewHolder holder, int position) {
        Log.d("ILUD CustomAdapter:", "Test2");
        holder.tvImageName.setText(mImageDetails.get(position).getImageName());
        Picasso.with(mContext).load(mImageDetails.get(position).getImageUrl()).resize(200, 200).centerCrop()
        .placeholder(R.drawable.default_img)
                .error(R.drawable.default_img)
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return mImageDetails.size();
    }

    public static class MyCustomViewHolder extends RecyclerView.ViewHolder{

         ImageView ivImage;
         TextView tvImageName;

        public MyCustomViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView)itemView.findViewById(R.id.iv_image);
            tvImageName = (TextView)itemView.findViewById(R.id.tv_image_name);
        }


    }
}
