package com.example.unittest.integration;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class BeforeAllExtension implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        System.out.println(this.getClass().getName() + " " + context);
    }
}
