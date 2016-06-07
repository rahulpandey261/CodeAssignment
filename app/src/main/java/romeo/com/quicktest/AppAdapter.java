package romeo.com.quicktest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.MyViewHolderClass> {

    private List<ImageDetails> appDatas;
    private Context context;

    public AppAdapter(List<ImageDetails> appDatas)
    {
        this.appDatas = appDatas;
    }

    @Override
    public MyViewHolderClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);
        context = parent.getContext();
        return new MyViewHolderClass(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolderClass holder, int position) {
        ImageDetails appData = appDatas.get(position);
        holder.app_name.setText(appData.getImageName());
        Picasso.with(context).load(appData.getImageUrl()).into(holder.iv_app_image);
    }

    @Override
    public int getItemCount() {
        return appDatas.size();
    }

    public class MyViewHolderClass extends RecyclerView.ViewHolder {
        public ImageView iv_app_image;
        public TextView app_name, app_rating;

        public MyViewHolderClass(View view) {
            super(view);
            iv_app_image = (ImageView) view.findViewById(R.id.iv_image);
            app_name = (TextView) view.findViewById(R.id.tv_image_name);
            //year = (TextView) view.findViewById(R.id.year);
        }
    }
}
