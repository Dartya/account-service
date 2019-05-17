package ru.sbrf.study.service.layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.sbrf.study.service.dto.*;

import javax.ws.rs.*;
import java.io.IOException;
import java.math.BigDecimal;
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
    public void createAccount(long token, double summ, String currency){
        AccountCreate accountCreate = new AccountCreate(token, new BigDecimal(summ), currency);
        businessService.registerAccount(accountCreate);
    }

    @POST
    @Path("delete")
    @Consumes(APPLICATION_JSON)
    public void deleteAccount(long token, int accountId){
        AccountDelete accountDelete = new AccountDelete(accountId, token);
        businessService.deleteAccount(accountDelete);
    }

    @POST
    @Path("pull")
    @Consumes(APPLICATION_JSON)
    public void pullMoney(long token, int accountId, double summ){
        PullPushMoney pullPushMoney = new PullPushMoney(accountId, token, new BigDecimal(summ));
        businessService.pullMoney(pullPushMoney);
    }

    @POST
    @Path("push")
    @Consumes(APPLICATION_JSON)
    public void pushMoney(long token, int accountId, double summ){
        PullPushMoney pullPushMoney = new PullPushMoney(accountId, token, new BigDecimal(summ));
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
    public List<History> getMyHistory(long token){ return businessService.getMyHistory(new Token(token));}
}
