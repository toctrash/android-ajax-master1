package iut.projet_android;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends ArrayAdapter<Image>{
	Context context;
    int layoutResourceId;   
    ArrayList<Image> data = new ArrayList<Image>();
   
    public ImageAdapter(Context context, int layoutResourceId, ArrayList<Image> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new ImageHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
           
            row.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }
       
        Image img = data.get(position);
        holder.txtTitle.setText(img.getTitre());
        Picasso.with(context).load(img.getUrl()).into(holder.imgIcon);
       
        return row;
    }
   
    // Image holder icon and title objects
    static class ImageHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
