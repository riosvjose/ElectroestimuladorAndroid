package com.electro.electroestimulador;

public class BodyParts {
    public int Id;
    public String Name;
    public String Image;
    public BodyParts (int id,String name,String image){
        Id = id;
        Name = name;
        Image = image;
    }

    @Override
    public String toString() {
        return Name;
    }
}
