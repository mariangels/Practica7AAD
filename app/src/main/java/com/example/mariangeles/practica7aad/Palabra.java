package com.example.mariangeles.practica7aad;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Palabra implements Serializable,Comparable,Parcelable {
    private String nombre, idioma, traduccion, significado;

    public Palabra(){}

    public Palabra(String nombre, String idioma, String traduccion, String significado) {
        this.nombre = nombre;
        this.idioma = idioma;
        this.traduccion = traduccion;
        this.significado = significado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdioma() {
        return idioma;
    }

    public String getTraduccion() {
        return traduccion;
    }

    public String getSignificado() {
        return significado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public void setTraduccion(String traduccion) {
        this.traduccion = traduccion;
    }

    public void setSignificado(String significado) {
        this.significado = significado;
    }

    public String mostrar(){
        return  traduccion + "  Idioma: "+idioma;
    }

    @Override
    public boolean equals(Object o) {

        Palabra palabra = (Palabra) o;
        if(nombre.equals(palabra.nombre) && traduccion.equals(palabra.traduccion) && idioma.equals(palabra.idioma)){
            //palabras iguales
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return nombre != null ? nombre.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Palabra{" +
                "nombre='" + nombre + '\'' +
                ", idioma='" + idioma + '\'' +
                ", traduccion='" + traduccion + '\'' +
                ", significado='" + significado + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }


        /***PARCEL***/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //el orden en el que escriba en la parcela es importante!!!!!!!!!
        parcel.writeString(this.nombre);
        parcel.writeString(this.idioma);
        parcel.writeString(this.traduccion);
        parcel.writeString(this.significado);
    }

    //una forma para el constructor Parcel
    /*
    public Palabra(Parcel p){
        //this(p.readString(),p.readInt(),p.readString(),p.readString());
        //  apellido        edad        nombre          telefono
        //no se puede hacer xq no se leen asi los valores, apellido y nombre estan cambiados
        this.nombre=p.readString();
        this.idioma=p.readString();
        this.traduccion=p.readString();
        this.significado=p.readString();
    }*/
    //se tiene que llamar CREATOR!!!
    public static final Parcelable.Creator<Palabra> CREATOR=
            new Parcelable.Creator<Palabra>(){

        //otra forma para el constructor Parcel
        @Override
        public Palabra createFromParcel(Parcel p) {
            //static no podemos acceder a las los this.
            String nombre=p.readString();
            String idioma=p.readString();
            String traduccion=p.readString();
            String significado=p.readString();
            return new Palabra(nombre, idioma, traduccion, significado);
            //si lo implemento asi, no necesitamos el constructor de la parcela
        }

        @Override
        public Palabra[] newArray(int i) {
            return new Palabra[i];
        }
    };
}
