package com.jquerymobile.demo.contact.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticationService extends Service {
    
    public IBinder onBind(Intent intent) {
    	return null;
    }
}
