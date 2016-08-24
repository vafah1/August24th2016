package com.projecttwo;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class AppModelTest {
    @Test public void sayHelloTest() {
        AppModel model = new AppModel();
        model.setFirstName("John");
        AppCntrl.sayHello(model);
        assertEquals("Hello, John!", model.getMessage(), "John is pleased");        
    }
}
