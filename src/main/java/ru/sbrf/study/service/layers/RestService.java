package ru.sbrf.study.service.layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.sbrf.study.service.dto.*;

import javax.ws.rs.*;
import java.io.IOException;
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
    public void createAccount(AccountCreate accountCreate){
        businessService.registerAccount(accountCreate);
    }

    @POST
    @Path("delete")
    @Consumes(APPLICATION_JSON)
    public void deleteAccount(AccountDelete accountDelete){
        businessService.deleteAccount(accountDelete);
    }

    @POST
    @Path("pull")
    @Consumes(APPLICATION_JSON)
    public void pullMoney(PullPushMoney pullPushMoney){
        businessService.pullMoney(pullPushMoney);
    }

    @POST
    @Path("push")
    @Consumes(APPLICATION_JSON)
    public void pushMoney(PullPushMoney pullPushMoney){
        businessService.pushMoney(pullPushMoney);
    }

    @GET
    @Path("get-history")
    @Produces(APPLICATION_JSON)
    public List<History> getHistory() {
        return businessService.getHistory();
    }

    @GET
    @Path("get-my-history")
    @Produces(APPLICATION_JSON)
    public List<History> getMyHistory(Token token){ return businessService.getMyHistory(token);}
}
