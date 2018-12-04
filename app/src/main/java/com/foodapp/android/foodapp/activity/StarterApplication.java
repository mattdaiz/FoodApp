/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.foodapp.android.foodapp.activity;

import android.app.Application;
import android.util.Log;

import com.foodapp.android.foodapp.model.Messaging.Message;
import com.foodapp.android.foodapp.model.Messaging.MessageInbox;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Log.i("STARTER","SFDSFDF");
    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    //Register Message Class
    ParseObject.registerSubclass(Message.class);
    //Register MessageInbox Class
    ParseObject.registerSubclass(MessageInbox.class);

    // Add your initialization code here
    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId("3c6a5eb3e22bf9d8193b7c1f51b95635da9009a4")
            .clientKey("3966932548330a2a70a2a98f3efb98eb6998b18e")
            .server("http://18.221.202.246:80/parse/")
            .build()
    );

//    ParseObject object = new ParseObject("ExampleObject");
////    object.put("myNumber", "123");
////    object.put("myString", "rob");
//
//    object.saveInBackground(new SaveCallback () {
//      @Override
//      public void done(ParseException ex) {
//        if (ex == null) {
//          Log.i("Parse Result", "Successful!");
//        } else {
//          Log.i("Parse Result", "Failed" + ex.toString());
//        }
//      }
//    });


    ParseUser.enableAutomaticUser();

    ParseACL defaultACL = new ParseACL();
    defaultACL.setPublicReadAccess(true);
    defaultACL.setPublicWriteAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);

  }
}
