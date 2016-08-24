package com.projecttwo;

import com.projecttwo.js.JSFunctions;
import net.java.html.json.Function;
import net.java.html.json.Model;
import net.java.html.json.Property;

@Model(className = "AppModel", targetId="", properties = {

  /* Application's ViewModel properties */
  @Property(name = "FirstName", type = String.class),
  @Property(name = "Message", type = String.class),
})
public class AppCntrl {
    
    static AppModel model = null;
    
    /* Application's ViewModel methods */
    @Function static void sayHello(AppModel m) {
        m.setMessage("Hello, "+m.getFirstName()+"!");
    }
    
    @Function static void sayHi(final AppModel m) {
        // This code shows direct interaction with JavaScript.
        // The implementation of confirmByUser is in JavaScript Libraries project.
        
        JSFunctions.confirmByUser("Say hi?", new Runnable() {
            @Override
            public void run() {
              m.setMessage("Hi, "+m.getFirstName()+"!");
            }
        });
    }

    public static void onPageLoad() throws Exception {
        model = new AppModel();
        
        /* Initialization of application's ViewModel values */
        model.setFirstName("John");

        model.applyBindings();
    }
}