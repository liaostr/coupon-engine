package de.delinero.copt;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.delinero.copt.builders.CartBuilder;
import de.delinero.copt.builders.CouponBuilder;
import de.delinero.copt.engines.CouponEngine;
import de.delinero.copt.exceptions.PayloadFileException;
import de.delinero.copt.models.Cart;
import de.delinero.copt.models.Coupon;
import de.delinero.copt.models.Message;
import de.delinero.copt.modules.CouponEngineModule;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {

    private static String cartFilename;
    private static String couponFilename;
    private static Boolean silent;

    public static void main(String[] args) {
        if (! parseArguments(args)) {
            System.out.printf("Usage: java -cp <classpath> de.delinero.copt.App cart.json coupon.json [silent]%n");

            return;
        }

        Injector injector = Guice.createInjector(new CouponEngineModule());

        CartBuilder cartBuilder = injector.getInstance(CartBuilder.class);
        CouponBuilder couponBuilder = injector.getInstance(CouponBuilder.class);
        CouponEngine couponEngine = new CouponEngine(silent);

        Cart cart = cartBuilder.build(getPayloadFile(cartFilename));
        Coupon coupon = couponBuilder.build(getPayloadFile(couponFilename));

        Boolean validationResult = couponEngine.evaluate(cart, coupon.getValidationRules());
        Boolean applicationResult = couponEngine.evaluate(cart, coupon.getApplicationRules());

        System.out.printf(String.format("%s%n", Message.getMessageByResults(validationResult, applicationResult)));
    }

    private static String getPayloadFile(String filename) {
        try {
            return new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new PayloadFileException(exception);
        }
    }

    private static Boolean parseArguments(String[] args) {

        if (args.length == 2) {
            cartFilename = args[0];
            couponFilename = args[1];
            silent = true;
        } else if (args.length == 3) {
            cartFilename = args[0];
            couponFilename = args[1];
            silent = Boolean.valueOf(args[2]);
        } else {
            return false;
        }

        return true;
    }

}
