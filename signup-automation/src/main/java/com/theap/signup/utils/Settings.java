package com.theap.signup.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

    private static Settings instance;
    private final Properties props = new Properties();
    private static final String PATH = "src/test/resources/config.properties";

    private Settings() {
        try (FileInputStream file = new FileInputStream(PATH)) {
            props.load(file);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config.properties: " + e.getMessage());
        }
    }

    public static Settings get() {
        if (instance == null) instance = new Settings();
        return instance;
    }

    public String value(String key) {
        String val = props.getProperty(key);
        if (val == null) throw new RuntimeException("Missing key in config: " + key);
        return val.trim();
    }

    public boolean flag(String key)  { return Boolean.parseBoolean(value(key)); }
    public int number(String key)    { return Integer.parseInt(value(key)); }

    public String baseUrl()          { return value("base.url"); }
    public String browser()          { return value("browser"); }
    public boolean headless()        { return flag("headless"); }
    public int implicitWait()        { return number("implicit.wait"); }
    public int explicitWait()        { return number("explicit.wait"); }
    public int pageLoadTimeout()     { return number("page.load.timeout"); }

    public String agencyName()       { return value("agency.name"); }
    public String agencyWebsite()    { return value("agency.website"); }
    public String agencyCountry()    { return value("agency.country"); }
    public String agencyState()      { return value("agency.state"); }
    public String agencyCity()       { return value("agency.city"); }
    public String agencyAddress()    { return value("agency.address"); }
    public String agencyZip()        { return value("agency.zipcode"); }
    public String agencyPhone()      { return value("agency.phone"); }
    public String agencyEmail()      { return value("agency.email"); }

    public String contactFirstName() { return value("contact.firstname"); }
    public String contactLastName()  { return value("contact.lastname"); }
    public String contactDesign()    { return value("contact.designation"); }
    public String contactPhone()     { return value("contact.phone"); }
    public String contactEmail()     { return value("contact.email"); }

    public String password()         { return value("password"); }
}