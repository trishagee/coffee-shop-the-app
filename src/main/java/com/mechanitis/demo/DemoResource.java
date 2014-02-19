package com.mechanitis.demo;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path("/demo")
@Produces(MediaType.APPLICATION_JSON)
public class DemoResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public DemoResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying saySomething(@QueryParam("name") Optional<String> name) {

        return new Saying(counter.incrementAndGet(), name.or(defaultName));
    }





    

    
}