package ru.sbrf.study.service.layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import ru.sbrf.study.service.dto.*;
import ru.sbrf.study.service.entities.HistoryEntity;
import ru.sbrf.study.service.service.AccountService;
import ru.sbrf.study.service.service.HistoryRepository;
import ru.sbrf.study.service.service.HistoryService;

import javax.ws.rs.*;
import java.util.List;

@Component
@Path("service")
public class RestService {

    private static final String APPLICATION_JSON = "application/json";

    @Autowired
    private BusinessService businessService;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private AccountService accountService;

    @GET
    @Path("hc")
    public String healthCheck() {
        return "OK";
    }

    @POST
    @Path("create")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public int createAccount(AccountCreate accountCreate){
        return businessService.registerAccount(accountCreate);
    }

    @POST
    @Path("delete")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public int deleteAccount(AccountDelete accountDelete){
        return businessService.deleteAccount(accountDelete);
    }

    @POST
    @Path("pull")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public int pullMoney(PullPushMoney pullPushMoney){
        return businessService.pullMoney(pullPushMoney);
    }

    @POST
    @Path("push")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public int pushMoney(PullPushMoney pullPushMoney){
        return businessService.pushMoney(pullPushMoney);
    }

    @GET
    @Path("get-history")
    @Produces(APPLICATION_JSON)
    public List<HistoryEntity> getHistory() {
        return businessService.getHistory();
    }

    @GET
    @Path("get-my-history")
    @Produces(APPLICATION_JSON)
    public List<HistoryEntity> getMyHistory(Token token){ return businessService.getMyHistory(token);}

    @GET
    @Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Path("getAll")
    public List<HistoryEntity> getAll() {
        return historyRepository.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Path("addAcc")
    public void addAcc(AccountCreate accountCreate){
        accountService.addAccount(accountCreate);
    }
}
