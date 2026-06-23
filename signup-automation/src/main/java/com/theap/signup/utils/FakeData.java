package com.theap.signup.utils;

import com.github.javafaker.Faker;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FakeData {
    private static final Faker faker = new Faker();

    private FakeData() {}

    private static String stamp() {
        return new SimpleDateFormat("MMddHHmmss").format(new Date());
    }

    // ── Random unique email prefix + full email ──────────────────────────
    public static String emailPrefix() {
        return "testuser." + stamp();
    }

    public static String randomEmail(String prefix) {
        return prefix + "@mailinator.com";
    }

    // ── Fixed valid website — avoids "invalid domain" validation errors ──
    public static String website() {
        return "www.google.com";
    }

    // ── Existing methods ─────────────────────────────────────────────────
    public static String agencyName()   { return faker.company().name().replaceAll("[^a-zA-Z0-9 ]", "") + " " + stamp(); }
    public static String address()      { return faker.address().streetAddress(); }
    public static String city()         { return faker.address().city(); }
    public static String zipCode()      { return faker.number().digits(5); }
    public static String phone()        { return "98" + faker.number().digits(8); }
    public static String agencyEmail()  { return "agency." + stamp() + "@mailinator.com"; }
    public static String firstName()    { return faker.name().firstName(); }
    public static String lastName()     { return faker.name().lastName(); }
    public static String jobTitle()     { return faker.job().title(); }
    public static String contactEmail() { return "contact." + stamp() + "@mailinator.com"; }
    public static String password()     { return "Test@" + faker.number().numberBetween(1000, 9999) + "!"; }
}