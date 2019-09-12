/******************************************************************************
 * Copyright (C) BlueLapiz.net - All Rights Reserved                          *
 * Unauthorized copying of this file, via any medium is strictly prohibited   *
 * Proprietary and confidential                                               *
 * Last edited 11/27/18 2:27 PM                                               *
 * Written by Alexander Sagen <alexmsagen@gmail.com>                          *
 ******************************************************************************/

package app.sagen.killtitle.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class FileGeneralConfiguration extends YamlConfiguration implements GeneralConfiguration {

    private static  SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");

    @Override
    public float getFloat( String path) {
        return (float) this.getDouble(path);
    }

    @Override
    public Location getLocation( String path,  String worldName) {
        if (!this.isSet(path)) return null;
         double x = this.getDouble(path + ".x");
         double y = this.getDouble(path + ".y");
         double z = this.getDouble(path + ".z");
         float pitch = this.getFloat(path + ".pitch");
         float yaw = this.getFloat(path + ".yaw");
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    @Override
    public Location getLocation( String path) {
        if (!this.isSet(path)) return null;
         String world = this.getString(path + ".w");
         double x = this.getDouble(path + ".x");
         double y = this.getDouble(path + ".y");
         double z = this.getDouble(path + ".z");
         float pitch = this.getFloat(path + ".pitch");
         float yaw = this.getFloat(path + ".yaw");
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    @Override
    public void setLocation( String path,  Location value) {
        if (value == null) {
            this.set(path, null);
            return;
        }
        this.set(path + ".w", value.getWorld().getName());
        this.set(path + ".x", value.getX());
        this.set(path + ".y", value.getY());
        this.set(path + ".z", value.getZ());
        this.set(path + ".pitch", value.getPitch());
        this.set(path + ".yaw", value.getYaw());
    }

    @Override
    public void setDate(String path, Date date) {
        this.set(path, DATE_FORMAT.format(date));
    }

    @Override
    public Date getDate(String path) {
        try {
            return DATE_FORMAT.parse(this.getString(path));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
