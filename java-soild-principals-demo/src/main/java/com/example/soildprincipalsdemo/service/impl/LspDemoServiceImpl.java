package com.example.soildprincipalsdemo.service.impl;

import com.example.soildprincipalsdemo.model.LspRequest;
import com.example.soildprincipalsdemo.model.PrincipleDemoResponse;
import com.example.soildprincipalsdemo.service.LspDemoService;
import com.example.soildprincipalsdemo.solid.lsp.Rectangle;
import com.example.soildprincipalsdemo.solid.lsp.Shape;
import com.example.soildprincipalsdemo.solid.lsp.Square;
import org.springframework.stereotype.Service;

/**
 * LSP demonstration service implementation.
 *
 * The caller uses Shape abstraction and can substitute Rectangle or Square
 * implementations safely.
 */
@Service
public class LspDemoServiceImpl implements LspDemoService {

    @Override
    public PrincipleDemoResponse demonstrate(LspRequest request) {
        // Step 1: Normalize requested shape type.
        String shapeType = request.getShape().toUpperCase();

        // Step 2: Build concrete shape while exposing it as Shape abstraction.
        Shape shape;
        if ("RECTANGLE".equals(shapeType)) {
            shape = new Rectangle(request.getWidth(), request.getHeight());
        } else if ("SQUARE".equals(shapeType)) {
            // For square, we use width as side for a clear demo input contract.
            shape = new Square(request.getWidth());
        } else {
            throw new IllegalArgumentException("Unsupported shape: " + request.getShape());
        }

        // Step 3: Use the abstraction call without caring about concrete type.
        double area = shape.area();

        // Step 4: Return details that explain substitution behavior.
        return new PrincipleDemoResponse("LSP", "PASS", "Concrete shapes are substitutable through Shape abstraction.")
                .addDetail("requestedShape", shapeType)
                .addDetail("resolvedType", shape.type())
                .addDetail("computedArea", area);
    }
}
