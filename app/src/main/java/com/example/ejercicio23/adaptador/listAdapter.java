package com.example.ejercicio23.adaptador;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ejercicio23.R;
import com.example.ejercicio23.modelo.Fotografía;

import java.util.List;

public class listAdapter extends ArrayAdapter<Fotografía> {

    public listAdapter(@NonNull Context context, int resource, List<Fotografía> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_foto, parent, false);

        Fotografía foto = getItem(position);

        ImageView img = convertView.findViewById(R.id.imgFoto);
        TextView desc = convertView.findViewById(R.id.txtDescripcion);


            Bitmap bitmap = BitmapFactory.decodeByteArray(foto.getImage(), 0, foto.getImage().length);
            img.setImageBitmap(bitmap);
            desc.setText(foto.getDescripcion());


        return convertView;
    }
}
