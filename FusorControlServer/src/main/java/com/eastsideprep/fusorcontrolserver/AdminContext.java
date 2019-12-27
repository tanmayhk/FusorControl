/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eastsideprep.fusorcontrolserver;

import static com.eastsideprep.fusorcontrolserver.WebServer.cd;
import static com.eastsideprep.fusorcontrolserver.WebServer.cs;
import static com.eastsideprep.fusorcontrolserver.WebServer.dl;
import static com.eastsideprep.fusorcontrolserver.WebServer.dm;
import java.io.IOException;
import static spark.Spark.halt;
import static spark.Spark.stop;

/**
 *
 * @author gmein
 */
public class AdminContext extends ObserverContext {

    AdminContext(String login, WebServer ws) {
        super(login, ws);
    }

    String killRoute() {
        if (dl != null) {
            dl.shutdown();
        }
        dm.shutdown();

        stop();
        System.out.println("Server ended with /kill");
        System.exit(0);
        return "server ended";
    }

    String startLogRoute() {
        synchronized (ws) {
            if (dl != null) {
                dl.shutdown();
            }
            dl = new DataLogger();
            try {
                dl.init(dm, cs);
            } catch (IOException ex) {
                System.out.println("startLog IO exception: " + ex);
            }
            dm.autoStatusOn();
            System.out.println("New log started");
            return "log started";

        }
    }

    String stopLogRoute() {
        synchronized (ws) {
            if (dl != null) {
                dm.autoStatusOff();
                dl.shutdown();
                dl = null;
                System.out.println("Log stopped");
                return "log stopped";
            } else {
                return "log not running";
            }
        }
    }

    String variacRoute(spark.Request req) {
        int variacValue = Integer.parseInt(req.queryParams("value"));
        System.out.println("Received Variac Set " + variacValue);
        if (cd.variac.setVoltage(variacValue)) {
            return "set value as " + req.queryParams("value");
        }
        throw halt("Variac control failed");
    }

    String tmpOnRoute() {
        if (cd.tmp.setOn()) {
            return "turned on TMP";
        }
        throw halt(500, "TMP control failed");
    }

    String tmpOffRoute() {
        if (cd.tmp.setOff()) {
            return "turned off TMP";
        }
        throw halt(500, "TMP control failed");
    }

    String solenoidOnRoute() {
        if (cd.gas.setOpen()) {
            return "set solenoid to open";
        }
        throw halt(500, "set solenoid failed");
    }

    String solenoidOffRoute() {
        if (cd.gas.setOpen()) {
            return "set solenoid to open";
        }
        throw halt(500, "set solenoid failed");
    }

    String needleValveRoute(spark.Request req) {
        int value = Integer.parseInt(req.queryParams("value"));
        System.out.println("Received needle valve Set " + value);
        if (cd.needle.set("needlevalve", value)) {
            System.out.println("needle valve success");
            return "set needle valve value as " + value;
        }
        System.out.println("needle valve fail");
        throw halt(500, "set needle valve failed");
    }
}