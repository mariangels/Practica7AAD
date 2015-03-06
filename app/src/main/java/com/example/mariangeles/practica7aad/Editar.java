package com.example.mariangeles.practica7aad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

import java.util.ArrayList;


public class Editar extends Activity {

    private Palabra palabra;
    private ObjectContainer bd;
    private boolean editar=false;

    @Override
    protected void onPause() {
        super.onPause();
        conexion().close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        Bundle b = getIntent().getExtras();
        if(b!=null) {
            palabra = b.getParcelable("palabra");
            mostrarPalabra();
            editar=true;
        }
    }

    public void mostrarPalabra(){
        ((EditText) findViewById(R.id.eNombre)).setText(palabra.getNombre());
        ((EditText) findViewById(R.id.eIdioma)).setText(palabra.getIdioma());
        ((EditText) findViewById(R.id.eTraduccion)).setText(palabra.getTraduccion());
        ((EditText) findViewById(R.id.eSignificado)).setText(palabra.getSignificado());
    }

    public void guardar(View view){
        String nombre=((EditText) findViewById(R.id.eNombre)).getText().toString();
        String idioma=((EditText) findViewById(R.id.eIdioma)).getText().toString();
        String traduccion=((EditText) findViewById(R.id.eTraduccion)).getText().toString();
        String significado=((EditText) findViewById(R.id.eSignificado)).getText().toString();
        Palabra p=new Palabra(nombre,idioma,traduccion,significado);
        if(camposNoNulos(p)) {
            if(existe()) {
                if (!editar) {
                    //añadir
                    conexion().store(p);
                    conexion().commit();
                } else {
                    editar(p);
                }
                finish();
            }else{
                tostada("ya existe");
            }
        }else{
            tostada("Campos nulos");
        }
    }

    public boolean existe(){
        ObjectSet<Palabra> palabrasList = conexion().queryByExample(palabra);
        if (palabrasList.hasNext()){
            return true;
        }
        return false;
    }

    public void editar(Palabra p){
        ObjectSet<Palabra> palabrasList = conexion().queryByExample(palabra);
        if (palabrasList.hasNext()){
            Palabra palabraEditar = palabrasList.next();
            palabraEditar.setNombre(p.getNombre());
            palabraEditar.setIdioma(p.getIdioma());
            palabraEditar.setTraduccion(p.getTraduccion());
            palabraEditar.setSignificado(p.getSignificado());

            conexion().store(palabraEditar);
            conexion().commit();
        }
    }

    public ObjectContainer conexion(){
        try {
            if (bd == null) {
                bd = Db4oEmbedded.openFile
                        (Db4oEmbedded.newConfiguration(), getExternalFilesDir(null) +
                                "/bd.db4o");
            }
            return bd;
        }catch(Exception r){
            return null;
        }
    }

    public boolean camposNoNulos(Palabra p){
        if(p.getNombre().isEmpty() || p.getIdioma().isEmpty() || p.getTraduccion().isEmpty() || p.getSignificado().isEmpty())
            return false;
        return true;
    }

    private void tostada(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
