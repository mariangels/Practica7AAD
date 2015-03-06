package com.example.mariangeles.practica7aad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Principal extends Activity {


    ArrayList<Palabra> palabras;
    private Adaptador adaptador;
    private ListView lt=null;
    private ObjectContainer bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diccionario);

        palabras=new ArrayList<Palabra>();
        query();

        lt = (ListView) findViewById(R.id.lista);
        // Para mostrar una lista de valores obtenida a partir de un array de cadenas, se puede
        //utilizar la clase ArrayAdapter.
        adaptador = new Adaptador(this, R.layout.elemento, palabras);
        lt.setAdapter(adaptador);

        //para la mierda del long click que sino no funciona
        registerForContextMenu(lt);

        lt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tostada(palabras.get(i).getNombre());
                //CLICK
            }
        });
    }


    /********************************************************************/
    /**************************    DB4O    ******************************/
    /********************************************************************/

    @Override
    protected void onPause() {
        super.onPause();
        if (bd != null) {
            bd.close();
            bd = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bd == null) {
            bd = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
                    getExternalFilesDir(null) + "/bd.db4o");
        }
    }

    public void query(){
        Palabra palabra= new Palabra();
        List<Palabra> palabraList = conexion().queryByExample(palabra);
        palabras = new ArrayList<Palabra>(palabraList);
    }

    public void eliminar(final Palabra palabra){
        ObjectSet<Palabra> palabrasList = conexion().query(
                new Predicate<Palabra>() {
                    @Override
                    public boolean match(Palabra p) {
                        return p.compareTo(palabra)==0;
                    }
                });
        if (palabrasList.hasNext()){
            Palabra p = palabrasList.next();
            conexion().delete(p);
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

    /********************************************************************/

    //muestra el menu superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.principal, menu);
        return true;
    }

    //trabajamos con el menu superior
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.mnañadir){
            Intent i = new Intent(this,Editar.class);
            startActivityForResult(i, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    //Este método es el que muestra el menú contextual al realizar una pulsación larga sobre el elemento.
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.diccionario, menu);
    }

    //Long click para utilizar el menu contextual
    public boolean onContextItemSelected(MenuItem item) {
        //Obtener el item del listView
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index= info.position;

        //Que hacemos con el item seleccionado:
        int id=item.getItemId();
        switch (id){
            case R.id.mnborrar:
                tostada("Borrado elemento "+palabras.get(index).getNombre());
                eliminar(palabras.get(index));
                palabras.remove(index);
                adaptador = new Adaptador(this, R.layout.elemento, palabras);
                lt.setAdapter(adaptador);
                break;
            case R.id.mneditar:
                Intent i = new Intent(this,Editar.class);
                Bundle b=new Bundle();
                b.putParcelable("palabra", palabras.get(index));
                i.putExtras(b);
                startActivityForResult(i, 1);
                break;
            case R.id.mnmostrar:
                alertMostrar(index);
                break;
        }
        return super.onContextItemSelected(item);
    }

    //A la vuelta del intent
    @Override
    public void onActivityResult(int requestCode,  int resultCode, Intent data){
        if (requestCode == 1) {
            query();
            adaptador = new Adaptador(this, R.layout.elemento, palabras);
            lt.setAdapter(adaptador);
            tostada("añadido");
        }
    }

    public void alertMostrar(int i){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.mostrar, null);
        Palabra pal=palabras.get(i);
        ((TextView) vista.findViewById(R.id.mNombre)).setText("Nombre: "+pal.getNombre());
        ((TextView) vista.findViewById(R.id.mIdioma)).setText("Idioma: "+pal.getIdioma());
        ((TextView) vista.findViewById(R.id.mTraduccion)).setText("Traduccion: "+pal.getTraduccion());
        ((TextView) vista.findViewById(R.id.mSignificado)).setText("Significado: "+pal.getSignificado());

        alert.setView(vista);
        alert.show();
    }

    private void tostada(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}