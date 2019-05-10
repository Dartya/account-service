package ru.sbrf.study.service.layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbrf.study.service.dto.DataInput;
import ru.sbrf.study.service.dto.Record;

import javax.ws.rs.*;
import java.util.List;

@Component
@Path("service")
public class RestService {

    private static final String APPLICATION_JSON = "application/json";

    @Autowired
    private BusinessService businessService;

    @GET
    @Path("hc")
    public String healthCheck() {
        return "OK";
    }

    @POST
    @Path("create")
    @Consumes(APPLICATION_JSON)
    public void createRecord(DataInput dataInput) {
        businessService.registerCall(dataInput.getData());
    }

    @GET
    @Path("getAll")
    @Produces(APPLICATION_JSON)
    public List<Record> getRecords() {
        return businessService.getRecords();
    }
}
