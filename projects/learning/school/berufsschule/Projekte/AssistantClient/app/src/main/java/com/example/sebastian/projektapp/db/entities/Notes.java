package com.example.sebastian.projektapp.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.sql.Date;


/**
 * Created by sebastian on 17.11.17.
 */

@Entity
public class Notes {
    @PrimaryKey
    @NonNull
    public int id;

    public String content;

    public String title;

}
