package com.example.soildprincipalsdemo.service.impl;

import com.example.soildprincipalsdemo.model.IspRequest;
import com.example.soildprincipalsdemo.model.PrincipleDemoResponse;
import com.example.soildprincipalsdemo.service.IspDemoService;
import com.example.soildprincipalsdemo.solid.isp.Eatable;
import com.example.soildprincipalsdemo.solid.isp.HumanWorker;
import com.example.soildprincipalsdemo.solid.isp.RobotWorker;
import com.example.soildprincipalsdemo.solid.isp.Workable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ISP demonstration service implementation.
 *
 * The workflow consumes only the interfaces needed by the selected worker.
 */
@Service
public class IspDemoServiceImpl implements IspDemoService {

    @Override
    public PrincipleDemoResponse demonstrate(IspRequest request) {
        // Step 1: Normalize worker type.
        String workerType = request.getWorkerType().toUpperCase();

        // Step 2: Resolve worker as Workable abstraction.
        Workable workable;
        if ("HUMAN".equals(workerType)) {
            workable = new HumanWorker();
        } else if ("ROBOT".equals(workerType)) {
            workable = new RobotWorker();
        } else {
            throw new IllegalArgumentException("Unsupported workerType: " + request.getWorkerType());
        }

        // Step 3: Execute the common capability.
        List<String> actions = new ArrayList<>();
        actions.add(workable.work(request.getTask()));

        // Step 4: Execute eat capability only when object supports Eatable interface.
        if (workable instanceof Eatable eatable) {
            actions.add(eatable.eat());
        }

        // Step 5: Return details proving clients use only required interfaces.
        return new PrincipleDemoResponse("ISP", "PASS", "Clients depend only on methods they actually use.")
                .addDetail("workerType", workerType)
                .addDetail("actions", actions);
    }
}
