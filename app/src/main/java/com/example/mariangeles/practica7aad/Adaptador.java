package com.example.mariangeles.practica7aad;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador extends ArrayAdapter<Palabra> {

    private Context contexto;
    private ArrayList<Palabra> lista;
    private int recurso;

    private static LayoutInflater i;//Para que no lo cree cada vez que llama al GetView()

    public static class ViewHolder{
        public TextView tv1;
    }

    //Al constructor se le pasa el contexto, el recurso sobre el que se va a actuar y la lista de valores.
    public Adaptador(Context context, int resource, ArrayList<Palabra> palabras) {
        super(context, resource, palabras);
        this.contexto = context;
        this.lista = palabras;
        this.recurso = resource;

        this.i = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //CREAMOS INFLADOR
    }

    @Override
    //Este método es llamado cada vez que se necesita dibujar un item.
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null){
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tv1 = (TextView)convertView.findViewById(R.id.tvNombre);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        String muestra=lista.get(position).mostrar();
        vh.tv1.setText(muestra);

        return convertView;
    }


}
